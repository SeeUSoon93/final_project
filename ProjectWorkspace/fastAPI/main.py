from fastapi import FastAPI, WebSocket
from fastapi.responses import JSONResponse
from fastapi.middleware.cors import CORSMiddleware
from fastapi.staticfiles import StaticFiles

from collections import Counter

from predict import predict_method, create_sentence

import cv2
import asyncio

# python -m uvicorn main:app --reload --host 0.0.0.0 --port 9091

# source venv/bin/activate
# uvicorn main:app --reload --host 0.0.0.0 --port 9091

app = FastAPI()

origins = ["http://localhost:9090",
           "http://localhost:9091"]
app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

predictions = []
predicted_mapping = {'child' : '아이',
                     'down' : '쓰러지다',
                     'lost' : '잃어버리다',
                     'report' : '신고하다',
                     'sick' : '아프다',
                     'toilet' : '화장실',
                     'wallet' : '지갑',
                     'where' : '어디'}
        
@app.websocket("/stream")
async def video_stream(websocket: WebSocket):
    await websocket.accept()

    cap = cv2.VideoCapture(0)
    temporary_predictions = [] 
    
    if not cap.isOpened():
        print("Cannot open camera")
        await websocket.close()
        return

    while True:
        ret, img = cap.read()
        if not ret:
            print("Ignoring empty camera frame.")
            continue

        pre_word, img = predict_method(img)
        temporary_predictions.append(pre_word)

        if len(temporary_predictions) >= 11:  # 2초동안 처리하는 이미지 수
            frequency = Counter(temporary_predictions)
            most_common_string = frequency.most_common(1)[0][0]
            temporary_predictions = []
            predictions.append(most_common_string)
      
        _, buffer = cv2.imencode('.jpg', img)
        await websocket.send_bytes(buffer.tobytes())       
        await asyncio.sleep(0.1)
  
@app.get("/stop")
def get_predictions():
    
    predicted_text = []
    for word in predictions:
        predicted_text.append(predicted_mapping.get(word))

    trans_sentence = create_sentence(predicted_text)
    predictions.clear()
    
    return JSONResponse(content={"text":trans_sentence})
