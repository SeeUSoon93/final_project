// 예제: 로그인 상태를 나타내는 변수
var isLoggedIn = true; // 또는 false로 설정

// 문서가 로드될 때 실행
document.addEventListener("DOMContentLoaded", function () {
    // 아이콘 표시 여부 업데이트
    updateIcons();
});

// 로그인 상태에 따라 아이콘 표시 여부 업데이트
function updateIcons() {
    var btnProfile = document.getElementById("btnProfile");
    var btnLogin = document.getElementById("btnLogin");
    var btnLogout = document.getElementById("btnLogout");
    var btnMyPage = document.getElementById("btnMyPage");

    if (isLoggedIn) {
        // 로그인 상태일 때
        btnProfile.style.display = "none";
        btnLogin.style.display = "none";
        btnLogout.style.display = "inline-block";
        btnMyPage.style.display = "inline-block";
    } else {
        // 로그아웃 상태일 때
        btnProfile.style.display = "inline-block";
        btnLogin.style.display = "inline-block";
        btnLogout.style.display = "none";
        btnMyPage.style.display = "none";
    }
}