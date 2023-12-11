let webcamCanvas = document.getElementById('webcam');
let webcamContext = webcamCanvas.getContext('2d');
let mediaRecorder;
let recordedBlobs;
let canvas = document.getElementById('canvas');
let intervalId;
let socket;
let streaming = false;

function filterPosts(selId) {
  var videoBox = document.getElementsByClassName('video-box');
  for (var i = 0; i < videoBox.length; i++) {
    videoBox[i].style.display = 'none';
  }
  var selElement = document.getElementById(selId);
  if (selElement) {
    selElement.style.display = 'block';
  }
}

window.onload = function () {
  filterPosts('predict-video');
}

// 녹화 시작 버튼 이벤트
document.getElementById('startRecord').addEventListener('click', () => {
  if (!streaming) {
    socket = new WebSocket("ws://localhost:9091/stream");
    socket.onmessage = function (e) {
      var arrayBuffer = e.data;
      var blob = new Blob([arrayBuffer], { type: "image/jpeg" });
      var img = new Image();
      img.onload = function () {
        webcamContext.drawImage(img, 0, 0, webcamCanvas.width, webcamCanvas.height);
      }
      img.src = URL.createObjectURL(blob);
    };
    streaming = true;
  }
});

document.getElementById('stopRecord').addEventListener('click', async () => {
  if (streaming) {
    socket.close();
    streaming = false;
    document.getElementById('recordingStatus').style.display = 'none';
  
  try {
    const response = await fetch('http://localhost:9091/stop');
    const data = await response.json();
    console.log(data); // 콘솔에 응답 데이터 출력
    document.getElementById('output').textContent = data.text; // 번역된 텍스트를 표시
    document.getElementById('original').textContent = data.text2; // 원본 텍스트를 표시
  } catch (error) {
    console.error('Error fetching the predictions:', error);
  }
}
});