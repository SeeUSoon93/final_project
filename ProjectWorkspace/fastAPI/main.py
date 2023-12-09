from fastapi import FastAPI, File, UploadFile
from fastapi.responses import JSONResponse
from typing import List
from predict import predict_method
import numpy as np
import cv2
from fastapi.middleware.cors import CORSMiddleware
# from some_model_library import SomeModel # 가정: 모델을 불러오는 라이브러리
# uvicorn main:app --reload --host 0.0.0.0 --port 9090 --ssl-keyfile=경로/키_파일.key --ssl-certfile=경로/인증서_파일.crt
# uvicorn main:app --reload --host 0.0.0.0 --port 9090 --ssl-keyfile=./localhost-key.pem --ssl-certfile=./localhost.pem

app = FastAPI()

# 모든 출처와 모든 헤더, 메소드를 허용하도록 CORS 설정
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
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
