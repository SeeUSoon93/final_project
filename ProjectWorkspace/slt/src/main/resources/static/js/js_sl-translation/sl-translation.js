let video = document.getElementById('webcam');
let mediaRecorder;
let recordedBlobs;
let canvas = document.getElementById('canvas');
let context = canvas.getContext('2d');
let recordingStatus = document.getElementById('recordingStatus');
let intervalId; // 2초마다 캡쳐를 위한 인터벌 ID

// 웹캠 접근 및 비디오 스트림 설정
navigator.mediaDevices.getUserMedia({ video: true, audio: false })
  .then((stream) => {
    video.srcObject = stream;
  })
  .catch((err) => {
    console.error('Error accessing media devices:', err);
  });

// 녹화 시작 버튼 이벤트
document.getElementById('startRecord').addEventListener('click', () => {
  recordedBlobs = [];
  mediaRecorder = new MediaRecorder(video.srcObject);

  mediaRecorder.ondataavailable = (event) => {
    if (event.data && event.data.size > 0) {
      recordedBlobs.push(event.data);
    }
  };
  mediaRecorder.start();
  console.log("녹화가 시작되었습니다.");

  recordingStatus.style.display = 'block'; // 녹화중 문구 표시

  // 2초마다 이미지 캡쳐 및 전송
  intervalId = setInterval(() => {
    context.drawImage(video, 0, 0, canvas.width, canvas.height);
    canvas.toBlob(blob => {
      sendImageToServer(blob); // 이미지 전송 함수 호출
    }, 'image/jpeg');
  }, 2000);
  console.log(recordedBlobs);
});

// 녹화 중지 버튼 이벤트
document.getElementById('stopRecord').addEventListener('click', () => {
  mediaRecorder.stop();
  clearInterval(intervalId); // 이미지 캡쳐 중지
  recordingStatus.style.display = 'none'; // 녹화중 문구 숨김
  console.log("녹화가 중지되었습니다.");
});

// 이미지 전송 함수
function sendImageToServer(blob) {
  let formData = new FormData();
  formData.append('image', blob);

  fetch('http://localhost:9090/upload', { //FastAPI url 적기
    method: 'POST',
    body: formData
  })
  .then(response => response.json())
  .then(data => {
    console.log("서버로부터 응답 받음:", data);
    speakText(data.text); // 서버로부터 받은 텍스트 읽기
  })
  .catch(error => console.log('이미지 전송 에러:', error));
}

// 텍스트를 음성으로 읽는 함수
function speakText(text) {
  let speech = new SpeechSynthesisUtterance(text);
  window.speechSynthesis.speak(speech);
}