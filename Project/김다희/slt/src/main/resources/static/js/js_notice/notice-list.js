// 아코디언 형태
$(".que").click(function() {
	$(this).next(".anw").stop().slideToggle(300);
	$(this).toggleClass('on').siblings().removeClass('on');
	$(this).next(".anw").siblings(".anw").slideUp(300); // 1개씩 펼치기
});

// 관리자 유무에 따른 버튼 표시
// 사용자 정보를 가져오는 함수
function getUserInfo() {
	// 실제로는 서버에서 사용자 정보를 가져와야 합니다.
	// 여기에서는 간단히 닉네임이 'Admin'인 경우를 가정합니다.
	return { isLoggedIn: true, nickname: 'Admin' };
}

// 버튼 표시 함수
function showButtons() {
	const userInfo = getUserInfo();
	const noticeButton = document.querySelector('.notice-button');

	// 사용자가 로그인한 경우에만 버튼을 표시합니다.
	if (userInfo.isLoggedIn) {
		// 사용자가 'Admin'인 경우에만 버튼을 표시합니다.
		if (userInfo.nickname === 'Admin') {
			noticeButton.style.display = 'block';
		}
	}
}

// 페이지 로드 시 버튼 표시 여부 확인
showButtons();


// 공지사항 삭제 체크 버튼
document.addEventListener("DOMContentLoaded", function() {
	// .checkBtn 클래스를 가진 요소들을 가져와서 반복
	var checkBtnElements = document.querySelectorAll('.checkBtn');
	checkBtnElements.forEach(function(checkBtnElement) {
		// 클릭 이벤트 리스너 추가
		checkBtnElement.addEventListener('click', function(event) {
			// input type=radio 요소를 찾아서 클릭 처리
			var radioInput = checkBtnElement.querySelector('input[type=radio]');

			// 라디오 버튼 상태를 토글
			radioInput.checked = !radioInput.checked;

			// 현재 .que 요소에 'on' 클래스 추가 및 제거하여 스타일 변경
			var queElement = checkBtnElement.closest('.que');
			queElement.classList.toggle('on', radioInput.checked);

			// 만약 라디오 버튼이 체크된 상태에서 클릭된 경우, 선택된 .que 요소 이외의 모든 .que 요소에 'on' 클래스 제거
			if (radioInput.checked) {
				var queElements = document.querySelectorAll('.que');
				queElements.forEach(function(otherQueElement) {
					if (otherQueElement !== queElement) {
						otherQueElement.classList.remove('on');
					}
				});
			}

			// 이벤트 전파(stopPropagation)를 막아 부모 요소에 대한 클릭 이벤트 전파 방지
			event.stopPropagation();
		});
	});
});