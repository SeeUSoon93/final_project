import cv2
import numpy as np
import mediapipe as mp
from catboost import CatBoostClassifier

# 손가락 각도 계산 함수
def calculate_angles(hand_landmarks, image_shape):
    joint = np.zeros((21, 3))
    for j, lm in enumerate(hand_landmarks.landmark):
        joint[j] = [lm.x * image_shape[1], lm.y * image_shape[0], lm.z]
        
    v1 = joint[[0,1,2,3,0,5,6,7,0,9,10,11,0,13,14,15,0,17,18,19],:]
    v2 = joint[[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20],:]
    v = v2 - v1
    v = v / np.linalg.norm(v, axis=1)[:, np.newaxis]

    angle = np.arccos(np.einsum('nt,nt->n', v[[0,1,2,4,5,6,8,9,10,12,13,14,16,17,18],:], v[[1,2,3,5,6,7,9,10,11,13,14,15,17,18,19],:]))
    angle = np.degrees(angle)
    
    angle = np.nan_to_num(angle)
    
    return angle, joint.flatten()

def calculate_pose_angles(pose_landmarks, image_shape):
    joint = np.zeros((33, 3))
    for j, lm in enumerate(pose_landmarks.landmark):
        joint[j] = [lm.x * image_shape[1], lm.y * image_shape[0], lm.z]

    # 팔 각도 계산: 어깨(11, 12), 팔꿈치(13, 14), 손목(15, 16) 랜드마크 사용
    v1 = joint[[11, 13, 12, 14], :]
    v2 = joint[[13, 15, 14, 16], :]
    v = v2 - v1
    v = v / np.linalg.norm(v, axis=1)[:, np.newaxis]

    # 양팔의 각도 계산
    arm_angles = np.arccos(np.einsum('nt,nt->n', v[[0, 2], :], v[[1, 3], :]))
    arm_angles = np.degrees(arm_angles)

    return arm_angles

mp_hands = mp.solutions.hands
mp_drawing = mp.solutions.drawing_utils
hands = mp_hands.Hands(max_num_hands=2, min_detection_confidence=0.5, min_tracking_confidence=0.5)
mp_pose = mp.solutions.pose
pose = mp_pose.Pose()

pre_model = CatBoostClassifier()
pre_model.load_model('cb_model.cbm')

def predict_method(img):
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

    if pose_result.pose_landmarks:
        mp.solutions.drawing_utils.draw_landmarks(
            img, pose_result.pose_landmarks, mp_pose.POSE_CONNECTIONS)
        arm_angles = calculate_pose_angles(pose_result.pose_landmarks, img.shape)
        landmark_data = []
        for idx in [11, 13, 15, 12, 14, 16]:
            landmark = pose_result.pose_landmarks.landmark[idx]
            landmark_data.extend([landmark.x, landmark.y, landmark.z])
        if len(landmark_data) == 0:
            landmark_data = np.zeros(18)

    if result.multi_hand_landmarks:
         for res in result.multi_hand_landmarks:
            mp_drawing.draw_landmarks(img, res, mp_hands.HAND_CONNECTIONS)
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

    data = np.concatenate((right_hand_angles, right_hand_coords, left_hand_angles, left_hand_coords, arm_angles, landmark_data))
    data = data.reshape(1,-1)      
    predicted_label = pre_model.predict(data)
    cv2.putText(img, text=str(predicted_label[0]), org=(50, 50),
                    fontFace=cv2.FONT_HERSHEY_SIMPLEX, fontScale=1, color=(0, 0,0), thickness=2)
    
    cv2.destroyAllWindows()

    return predicted_label[0][0], img

def create_sentence(key_list):
    if '아이' in key_list and '쓰러지다' in key_list:
        return "아이가 쓰러졌어요. 신고해주세요"
    elif '화장실' in key_list and '어디'in key_list :
        return "화장실이 어디에요?"
    elif '지갑' in key_list and '잃어버리다' in key_list:
        return "지갑을 잃어버렸어요."
    elif '아이' in key_list and '잃어버리다' in key_list:
        return "아이가 사라졌어요. 신고해주세요"
    elif '아이' in key_list and '아프다' in key_list:
        return "아이가 아파요."
    else:
        return "다시 한번 시도해주세요."