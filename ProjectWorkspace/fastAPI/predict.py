import os
import tempfile
import numpy as np
import pandas as pd
import cv2
import mediapipe as mp
import matplotlib.pyplot as plt
import pickle
import math
import catboost as cb
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


def predict_method(img):
    cb_model = CatBoostClassifier()
    cb_model.load_model('cb_model.cbm')

    mp_hands = mp.solutions.hands
    mp_drawing = mp.solutions.drawing_utils
    hands = mp_hands.Hands(max_num_hands=2, min_detection_confidence=0.5, min_tracking_confidence=0.5)
    mp_pose = mp.solutions.pose
    pose = mp_pose.Pose()

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
        arm_angles = calculate_pose_angles(pose_result.pose_landmarks, img.shape)
        landmark_data = []
        for idx in [11, 13, 15, 12, 14, 16]:
            landmark = pose_result.pose_landmarks.landmark[idx]
            landmark_data.extend([landmark.x, landmark.y, landmark.z])
        if len(landmark_data) == 0:
            landmark_data = np.zeros(18)

    if result.multi_hand_landmarks:
        for i, hand_landmarks in enumerate(result.multi_hand_landmarks):
            if i>= 2:
                for j, lm in enumerate(res.landmark):
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
    predicted_label = cb_model.predict(data)

    cv2.destroyAllWindows()

    return predicted_label[0][0]