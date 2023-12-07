from fastapi import FastAPI, File, UploadFile
from fastapi.responses import JSONResponse
from typing import List
from predict import predict_method
import numpy as np
import cv2
from fastapi.middleware.cors import CORSMiddleware
# from some_model_library import SomeModel # 가정: 모델을 불러오는 라이브러리
# 서버 실행하기 uvicorn main:app --reload --port 9090
app = FastAPI()

# 모든 출처와 모든 헤더, 메소드를 허용하도록 CORS 설정
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # 모든 출처 허용, 특정 출처로 제한하려면 ['http://localhost:3000', 'http://localhost:8080'] 등으로 지정
    allow_credentials=True,
    allow_methods=["*"],  # 모든 HTTP 메소드 허용
    allow_headers=["*"],  # 모든 HTTP 헤더 허용
)

predictions = [] # 예측값을 저장할 리스트

@app.post("/upload")
async def predict(image: UploadFile = File(...)):
    # 이미지 파일을 읽어 NumPy 배열로 변환
    image_data = await image.read()
    nparr = np.frombuffer(image_data, np.uint8)
    img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

    # 이미지를 모델에 입력하여 예측
    prediction = predict_method(img)
    predictions.append(prediction)

    return {"message": "예측 수행됨"}

@app.get("/stop")
def get_predictions():
    # 리스트에 저장된 예측값을 문장으로 변환
    sentence = ' '.join(prediction for sublist in predictions for prediction in sublist)
    predictions.clear() # 다음 요청을 위해 리스트 초기화

    return JSONResponse(content={"text": sentence})
