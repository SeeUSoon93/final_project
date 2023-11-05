# 필요한 라이브러리를 가져옵니다.
import cv2
import mediapipe as mp

# 미디어 파이프에서 제공하는 드로잉 유틸리티와 손 모델을 사용하기 위한 인스턴스를 생성합니다.
mp_drawing = mp.solutions.drawing_utils
mp_hands = mp.solutions.hands

# 웹캠을 사용하기 위해 cv2.VideoCapture 객체를 초기화합니다.
try:
    cap = cv2.VideoCapture(0)
    print("프로그램 시작함")

    # 손을 감지하기 위한 설정으로 'Hands' 객체를 생성합니다.
    with mp_hands.Hands(
        max_num_hands=1,  # 최대 감지할 손의 개수
        min_detection_confidence=0.5,  # 감지를 위한 최소 신뢰도
        min_tracking_confidence=0.5) as hands:  # 추적을 위한 최소 신뢰도

        # 웹캠이 열려 있는 동안 무한 루프를 돌면서 프레임을 읽습니다.
        while cap.isOpened():
            success, image = cap.read()
            if not success:
                continue  # 읽기에 실패하면 다음 프레임으로 건너뜁니다.

            # 이미지를 좌우반전시키고 RGB로 변환합니다. (미디어 파이프가 RGB 이미지를 사용)
            image = cv2.cvtColor(cv2.flip(image, 1), cv2.COLOR_BGR2RGB)

            # 변환된 이미지로 손을 감지합니다.
            results = hands.process(image)

            # 다시 BGR로 이미지를 변환하여 OpenCV에서 사용할 수 있게 합니다.
            image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)

            # 감지된 손의 랜드마크가 있으면 정보를 화면에 표시합니다.
            if results.multi_hand_landmarks:
                for hand_landmarks in results.multi_hand_landmarks:
                    # 랜드마크 좌표를 가져와서 특정 손가락의 x 좌표를 계산합니다.
                    finger1 = int(hand_landmarks.landmark[4].x * 100)
                    finger2 = int(hand_landmarks.landmark[8].x * 100)
                    # 손가락들 사이의 거리를 계산합니다.
                    dist = abs(finger1 - finger2)
                    # 계산된 정보를 이미지 위에 텍스트로 표시합니다.
                    cv2.putText(
                        image, text='f1=%d f2=%d dist=%d ' % (finger1, finger2, dist), org=(10, 30),
                        fontFace=cv2.FONT_HERSHEY_SIMPLEX, fontScale=1,
                        color=(255, 255, 255), thickness=3)

                    # 손의 랜드마크와 연결선을 이미지 위에 그립니다.
                    mp_drawing.draw_landmarks(
                        image, hand_landmarks, mp_hands.HAND_CONNECTIONS)

            # 처리된 이미지를 'image'라는 창에 표시합니다.
            cv2.imshow('image', image)
            # 'q' 키를 누르면 루프에서 빠져나와 프로그램을 종료합니다.
            if cv2.waitKey(1) == ord('q'):
                break

    # 사용이 끝난 후, 웹캠을 해제합니다.
    cap.release()

except Exception as e:
    print(f"에러 발생: {e}")

print("프로그램 종료")