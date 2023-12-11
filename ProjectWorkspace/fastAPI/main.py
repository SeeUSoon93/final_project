from fastapi import FastAPI, File, UploadFile, WebSocket
from fastapi.responses import JSONResponse
from typing import List
from predict import predict_method
import numpy as np
import cv2
import mediapipe as mp
from fastapi.middleware.cors import CORSMiddleware
import openai
from fastapi.responses import FileResponse
from fastapi.staticfiles import StaticFiles
from fastapi import FastAPI, WebSocket
from starlette.websockets import WebSocketDisconnect
from collections import Counter
import asyncio
import catboost as cb
from catboost import CatBoostClassifier
# source venv/bin/activate
# uvicorn main:app --reload --host 0.0.0.0 --port 9091
# uvicorn main:app --reload --host 0.0.0.0 --port 9090 --ssl-keyfile=./localhost-key.pem --ssl-certfile=./localhost.pem

app = FastAPI()
app.mount("/static", StaticFiles(directory="static"), name="static")
# 모든 출처와 모든 헤더, 메소드를 허용하도록 CORS 설정
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

predictions = [] # 예측값을 저장할 리스트
temporary_predictions = [] #임시
most_common_prediction = None
predicted_mapping = {'child' : '아이', 'down' : '쓰러지다', 'lost' : '잃어버리다', 'report' : '신고하다', 'sick' : '아프다', 'toilet' : '화장실', 'wallet' : '지갑', 'where' : '어디'}

# 스트리밍 테스트
mp_hands = mp.solutions.hands
mp_drawing = mp.solutions.drawing_utils
hands = mp_hands.Hands(max_num_hands=2, min_detection_confidence=0.5, min_tracking_confidence=0.5)
mp_pose = mp.solutions.pose
pose = mp_pose.Pose()


# 손가락 각도 계산 함수
def calculate_angles(hand_landmarks, image_shape):
    joint = np.zeros((21, 3))
    for j, lm in enumerate(hand_landmarks.landmark):
        joint[j] = [lm.x * image_shape[1], lm.y * image_shape[0], lm.z]
        
    # 벡터 계산
    v1 = joint[[0,1,2,3,0,5,6,7,0,9,10,11,0,13,14,15,0,17,18,19],:]
    v2 = joint[[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20],:]
    v = v2 - v1
    v = v / np.linalg.norm(v, axis=1)[:, np.newaxis]

    # 각도 계산
    angle = np.arccos(np.einsum('nt,nt->n', v[[0,1,2,4,5,6,8,9,10,12,13,14,16,17,18],:], v[[1,2,3,5,6,7,9,10,11,13,14,15,17,18,19],:]))
    angle = np.degrees(angle)
    
    # NaN 값이 있을 경우 0으로 대체
    angle = np.nan_to_num(angle)
    
    return angle, joint.flatten()


def calculate_pose_angles(pose_landmarks, image_shape):
    joint = np.zeros((33, 3))
    for j, lm in enumerate(pose_landmarks.landmark):
        joint[j] = [lm.x * image_shape[1], lm.y * image_shape[0], lm.z]

    # 팔 각도 계산: 어깨(11, 12), 팔꿈치(13, 14), 손목(15, 16) 랜드마크 사용
    v1 = joint[[11, 13, 12, 14], :]  # 어깨와 팔꿈치
    v2 = joint[[13, 15, 14, 16], :]  # 팔꿈치와 손목
    v = v2 - v1
    v = v / np.linalg.norm(v, axis=1)[:, np.newaxis]

    # 양팔의 각도 계산
    arm_angles = np.arccos(np.einsum('nt,nt->n', v[[0, 2], :], v[[1, 3], :]))
    arm_angles = np.degrees(arm_angles)

    return arm_angles

@app.websocket("/stream")
async def video_stream(websocket: WebSocket):
    await websocket.accept()
    cb_model = CatBoostClassifier()
    cb_model.load_model('cb_model.cbm')
    cap = cv2.VideoCapture(0)

    if not cap.isOpened():
        print("Cannot open camera")
        await websocket.close()
        return

    while True:
        ret, img = cap.read()
        if not ret:
            print("Ignoring empty camera frame.")
            continue

        # 이미지 처리
        img = cv2.flip(img, 1)
        img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
        result = hands.process(img)
        pose_result = pose.process(img)
        img = cv2.cvtColor(img, cv2.COLOR_RGB2BGR)

        arm_angles = np.zeros(2)
        landmark_data = np.zeros(18)
        right_hand_angles = np.zeros(15)
        right_hand_coords = np.zeros(63)
        left_hand_angles = np.zeros(15)
        left_hand_coords = np.zeros(63)   
        
        if result.multi_hand_landmarks:
            for i, hand_landmarks in enumerate(result.multi_hand_landmarks):
                if i>= 2:
                    break
                
                hand_type = result.multi_handedness[i].classification[0].label
                angles, joint_coords = calculate_angles(hand_landmarks, img.shape)

                if hand_type == 'Right':
                    right_hand_angles = angles
                    right_hand_coords = joint_coords
                elif hand_type == 'Left':
                    left_hand_angles = angles
                    left_hand_coords = joint_coords   
        
        if pose_result.pose_landmarks:
            arm_angles = calculate_pose_angles(pose_result.pose_landmarks, img.shape)
            landmark_data = []
            for idx in [11, 13, 15, 12, 14, 16]:
                landmark = pose_result.pose_landmarks.landmark[idx]
                landmark_data.extend([landmark.x, landmark.y, landmark.z])
            if len(landmark_data) == 0:
                landmark_data = np.zeros(18)
                
        data = np.concatenate((right_hand_angles, right_hand_coords, left_hand_angles, left_hand_coords, arm_angles, landmark_data))
            
        data = data.reshape(1,-1)
        predicted_label = cb_model.predict(data)
        
        print(predicted_label[0][0])
        # 예측된 라벨을 화면에 표시
        cv2.putText(img, text=str(predicted_label[0]), org=(50, 50),
                    fontFace=cv2.FONT_HERSHEY_SIMPLEX, fontScale=1, color=(0, 0,0), thickness=2)
        
        # 손 랜드마크 그리기
        if result.multi_hand_landmarks:
            for res in result.multi_hand_landmarks:
                mp_drawing.draw_landmarks(img, res, mp_hands.HAND_CONNECTIONS)
        
        if cv2.waitKey(30) == 49:  # 1번 키를 누르면 종료
            break
        _, buffer = cv2.imencode('.jpg', img)
        await websocket.send_bytes(buffer.tobytes())

        # 프레임 속도 조절을 위한 대기
        await asyncio.sleep(0.1)












@app.post("/upload")
async def predict(image: UploadFile = File(...)):
    # 이미지 파일을 읽어 NumPy 배열로 변환
    image_data = await image.read()
    nparr = np.frombuffer(image_data, np.uint8)
    img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
    
    # 이미지를 모델에 입력하여 예측
    prediction = predict_method(img)
    temporary_predictions.append(prediction)

    if len(temporary_predictions) == 3:
        most_common_prediction = Counter(temporary_predictions).most_common(1)[0][0]
        predictions.append(most_common_prediction)
        temporary_predictions.clear()
        
    else:
        most_common_prediction = len(temporary_predictions)
    
    return {"message": most_common_prediction}

@app.get("/stop")
def get_predictions():
    
    predicted_text = []
    for word in predictions:
        predicted_text.append(predicted_mapping.get(word))

    api_key = "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"
    openai.api_key = api_key

    input_text = ", ".join(predicted_text)

    response = openai.Completion.create(
        model="text-davinci-003",  # GPT-4 모델 선택
        prompt="자 너는 수어 동작을 번역한 한글 단어들을 조합해서 올바른 문장으로 만들어주는 통역사야. 여기 주어진 ', '로 구분된 단어들은 수어 동작을 번역한 단어야. 이걸 적절하게 바꿔야 하는데 예를 들면, '아이, 쓰러지다, 신고'가 들어왔다면 '아이가 쓰러졌어요. 신고 해주세요'라고 바꿔야 해. 근데 번역이 순간 잘못되서 전달될 수도 있어. 그럼 그 단어는 제거하거나 바꿔야해. 예를 들어서 '아이, 화장실, 신고' 이런식으로 들어온다면 화장실은 문맥에 맞지 않잖아. 그러니까 잘못 번역된거라고 인지하고 적당한 단어로 바꿔야 해. 예측값은 총 8개 단어이기 때문에 '아이, 쓰러지다, 잃어버리다, 신고, 아프다, 화장실, 지갑, 어디' 이중에서 문맥에 맞는 단어로 바꾼다음에 문장으로 변형해주면 좋겠어: " + input_text,
        max_tokens=60,
        temperature=0.7,
        n=1,
    )
    corrected_text = response.choices[0].text.strip()

    predictions.clear() # 다음 요청을 위해 리스트 초기화    
    
    return JSONResponse(content={"text": response})
