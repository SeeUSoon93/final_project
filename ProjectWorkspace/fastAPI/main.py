from fastapi import FastAPI, File, UploadFile
from fastapi.responses import JSONResponse
from typing import List
# from some_model_library import SomeModel # 가정: 모델을 불러오는 라이브러리

app = FastAPI()
model = SomeModel.load("model_path") # 모델 로드
predictions = [] # 예측값을 저장할 리스트

@app.post("/upload")
async def predict(image: UploadFile = File(...)):
    # 이미지를 모델에 입력하여 예측
    prediction = model.predict(image.file)
    predictions.append(prediction)

    return {"message": "예측 수행됨"}

@app.get("/stop")
def get_predictions():
    # 리스트에 저장된 예측값을 문장으로 변환
    sentence = ' '.join(predictions)
    predictions.clear() # 다음 요청을 위해 리스트 초기화

    return JSONResponse(content={"text": sentence})
