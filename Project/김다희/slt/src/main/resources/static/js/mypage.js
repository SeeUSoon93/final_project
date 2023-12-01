function openCity(evt, menu) {
    // Declare all variables
    var i, tabcontent, tablinks;

    // Get all elements with class="tabcontent" and hide them
    // 해당 클래스를 가진 모든 요소(탭 내용)를 가져와서 숨김
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    // Get all elements with class="tablink" and remove the class "active"
    // 해당 클래스를 가진 모든 요소(탭 링크)에서 "active" 클래스를 제거
    tablinks = document.getElementsByClassName("tablink");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    // Show the current tab, and add an "active" class to the link that opened the tab
    // 함수의 인자로 전달된 menu 값(탭의 id)을 사용하여 해당 탭의 내용을 보이도록 설정
    document.getElementById(menu).style.display = "block";
    //탭을 클릭한 버튼(링크)에 "active" 클래스를 추가하여 활성화된 상태를 시각적으로 나타냄
    evt.currentTarget.className += " active";
}