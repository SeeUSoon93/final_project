// 아코디언 형태
$(".que").click(function () {
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