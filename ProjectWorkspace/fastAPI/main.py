from fastapi import FastAPI, File, UploadFile
from fastapi.responses import JSONResponse
from typing import List
from predict import predict_method
import numpy as np
import cv2
from fastapi.middleware.cors import CORSMiddleware
import openai
# source venv/bin/activate
# uvicorn main:app --reload --host 0.0.0.0 --port 9091
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
predicted_mapping = {'belly' : '배', 'child' : '아이', 'down' : '쓰러지다', 'lost' : '잃어버리다', 'plz' : '부탁하다', 'report' : '신고하다', 'sick' : '아프다', 'toilet' : '화장실', 'wallet' : '지갑', 'where' : '어디'}

@app.post("/upload")
async def predict(image: UploadFile = File(...)):
    # 이미지 파일을 읽어 NumPy 배열로 변환
    image_data = await image.read()
    nparr = np.frombuffer(image_data, np.uint8)
    img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

    # 이미지를 모델에 입력하여 예측
    prediction = predict_method(img)
    predictions.append(prediction)

    return {"message": prediction}

@app.get("/stop")
def get_predictions():
    
    predicted_text = []
    for word in predictions:
        predicted_text.append(predicted_mapping.get(word))

    api_key = "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"
    openai.api_key = api_key

    input_text = " ".join(predicted_text)

    # response = openai.Completion.create(
    #   model="text-davinci-003",  # GPT-3.5 모델 선택
    #   prompt="수화단어를 참고해서 다음 문장을 한국어로 자연스럽게 바꿔주세요: " + input_text,
    #   max_tokens=60,
    #   temperature=0.7,
    #   n=1,
    #   )
    # corrected_text = response.choices[0].text.strip()

    predictions.clear() # 다음 요청을 위해 리스트 초기화    
    
    return JSONResponse(content={"text": input_text})
