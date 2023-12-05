from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

from fastapi.responses import FileResponse

@app.get("/translate")
def goTranlate():
    return "수어번역 화면"