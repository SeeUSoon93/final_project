let video = document.getElementById('webcam');
let mediaRecorder;
let recordedBlobs;
let canvas = document.getElementById('canvas');
let context = canvas.getContext('2d');
let recordingStatus = document.getElementById('recordingStatus');
let intervalId;

// 웹캠 접근 및 비디오 스트림 설정
navigator.mediaDevices.getUserMedia({ video: true, audio: false })
  .then((stream) => {
    console.log('Stream obtained:', stream);
    video.srcObject = stream;
  })
  .catch((err) => {
    console.error('좆같은 에ㄹ러:', err);
  });

// 녹화 시작 버튼 이벤트
document.getElementById('startRecord').addEventListener('click', () => {
  recordedBlobs = [];
  try{
    mediaRecorder = new MediaRecorder(video.srcObject);
  }catch(e){
    console.error("개씨발 에러",e);
    return;
  }

  mediaRecorder.ondataavailable = (event) => {
    if (event.data && event.data.size > 0) {
      recordedBlobs.push(event.data);
    }
  };
  mediaRecorder.start();
  console.log("녹화 시작");

  recordingStatus.style.display = 'block'; // 녹화중 문구 표시

  // 2초마다 이미지 캡쳐 및 전송
  intervalId = setInterval(() => {
    context.drawImage(video, 0, 0, canvas.width, canvas.height);
    canvas.toBlob(blob => {
      sendImageToServer(blob); // 이미지 전송 함수 호출
    }, 'image/jpeg');
  }, 2500);
  console.log(recordedBlobs);
});

// 녹화 중지 버튼 이벤트
document.getElementById('stopRecord').addEventListener('click', () => {
  mediaRecorder.stop();
  clearInterval(intervalId); // 이미지 캡쳐 중지
  recordingStatus.style.display = 'none'; // 녹화중 문구 숨김
  console.log("녹화 중지");

  fetch('http://localhost:9091/stop')
  .then(response => response.json())
  .then(data => {
    console.log("받은 문자열:", data);
  })
  .catch(error => console.log('니미 에러:', error));

});

// 이미지 전송 함수
function sendImageToServer(blob) {
  let formData = new FormData();
  formData.append('image', blob);

  fetch('http://localhost:9091/upload', { //FastAPI url 적기
    method: 'POST',
    body: formData
  })
  .then(response => response.json())
  .then(data => {
    console.log("받은 데이터:", data);
    speakText(data.text); // 서버로부터 받은 텍스트 읽기
  })
  .catch(error => console.log('씨밸럼의 에러:', error));
}

// 텍스트를 음성으로 읽는 함수
function speakText(text) {
  let speech = new SpeechSynthesisUtterance(text);
  window.speechSynthesis.speak(speech);
}