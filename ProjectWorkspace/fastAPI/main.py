from fastapi import FastAPI, File, UploadFile
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

@app.get("/main")
def gomain():
    return FileResponse('static/test.html')

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
    
    return JSONResponse(content={"text": input_text})
