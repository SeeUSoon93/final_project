import os
import tempfile
import numpy as np
import pandas as pd
import cv2
import mediapipe as mp
import matplotlib.pyplot as plt
import pickle
import math
from catboost import CatBoostClassifier


# 손가락 각도 계산 함수
def calculate_angles(hand_landmarks, image_shape):
    joint = np.zeros((21, 3))
    for j, lm in enumerate(hand_landmarks.landmark):
        joint[j] = [lm.x * image_shape[1], lm.y * image_shape[0], lm.z]
    
    # 벡터 계산
    v1 = joint[[0,1,2,3,0,5,6,7,0,9,10,11,0,13,14,15,0,17,18,19],:]
    v2 = joint[[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20],:]
    v = v2 - v1
    # 정규화
    v = v / np.linalg.norm(v, axis=1)[:, np.newaxis]

    # 각도 계산
    angle = np.arccos(np.einsum('nt,nt->n', v[[0,1,2,4,5,6,8,9,10,12,13,14,16,17,18],:], v[[1,2,3,5,6,7,9,10,11,13,14,15,17,18,19],:]))
    angle = np.degrees(angle)
    
    return angle

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


def predict_method(img):
    cb_model = CatBoostClassifier()
    cb_model.load_model('cb_model.cbm')

    # 필요한 Mediapipe 솔루션을 초기화합니다.
    mp_hands = mp.solutions.hands
    mp_drawing = mp.solutions.drawing_utils
    hands = mp_hands.Hands(max_num_hands=2, min_detection_confidence=0.5, min_tracking_confidence=0.5)
    mp_pose = mp.solutions.pose
    pose = mp_pose.Pose()

    # 이미지 처
    img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    result = hands.process(img)
    pose_result = pose.process(img)
    img = cv2.cvtColor(img, cv2.COLOR_RGB2BGR)

    if pose_result.pose_landmarks:
        arm_angles = calculate_pose_angles(pose_result.pose_landmarks, img.shape)
        
    if result.multi_hand_landmarks:
        for res in result.multi_hand_landmarks:
            joint = np.zeros((21, 3))
            for j, lm in enumerate(res.landmark):
                joint[j] = [lm.x, lm.y, lm.z]

            # 벡터 계산
            v1 = joint[[0,1,2,3,0,5,6,7,0,9,10,11,0,13,14,15,0,17,18,19],:]
            v2 = joint[[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20],:]
            v = v2 - v1
            v = v / np.linalg.norm(v, axis=1)[:, np.newaxis]

            # 각도 계산
            angle = np.arccos(np.einsum('nt,nt->n',
                v[[0,1,2,4,5,6,8,9,10,12,13,14,16,17,18],:],
                v[[1,2,3,5,6,7,9,10,11,13,14,15,17,18,19],:]))
            angle = np.degrees(angle)

            # NaN 값이 있을 경우 0으로 대체
            angle = np.nan_to_num(angle)

            # 데이터 차원 맞추기
            data = np.concatenate((angle, [0]*(30-len(angle)))) if len(angle) < 30 else angle
            data = np.concatenate((data, arm_angles)) if len(arm_angles) > 0 else data
            data = data.reshape(1, -1)  # 예측을 위해 데이터를 적절한 형태로 변환
            predicted_label = cb_model.predict(data)
            print(predicted_label)
            # 예측된 라벨을 화면에 표시
            org = (int(res.landmark[0].x * img.shape[1]), int(res.landmark[0].y * img.shape[0]))
            cv2.putText(img, text=str(predicted_label[0]), org=(org[0], org[1] + 20), 
                        fontFace=cv2.FONT_HERSHEY_SIMPLEX, fontScale=1, color=(255, 255, 255), thickness=2)

    # 사용 종료 후 자원 해제
    cv2.destroyAllWindows()

    return predicted_label