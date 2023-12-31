const modifyForm = document.querySelector("#writeForm"); // 수정 폼의 ID를 writeForm으로 수정
const modifyFormList = document.querySelectorAll("#writeForm > div"); // 수정 폼의 ID를 writeForm으로 수정
const idx = location.search;
const index = location.search.split("=")[1];
const boardsObj = JSON.parse(localStorage.getItem("boards"));
const board = boardsObj[index];

// 게시글의 데이터 값 출력
for (let i = 0; i < modifyFormList.length; i++) {
    const element = modifyFormList[i].childNodes[1];
    const id = element.name;
    element.value = board[id];
}

// 작성한 입력 값이 빈 값인지 검사
const isEmpty = (subject, writer, content) => {
    if (subject.length === 0) throw new Error("제목을 입력해주세요.");
    if (writer.length === 0) throw new Error("작성자를 입력해주세요.");
    if (content.length === 0) throw new Error("내용을 입력해주세요.");
};

// 현재 날짜 반환 함수
const recordDate = () => {
    const date = new Date();
    const yyyy = date.getFullYear();
    let mm = date.getMonth() + 1;
    let dd = date.getDate();

    mm = (mm > 9 ? "" : 0) + mm;
    dd = (dd > 9 ? "" : 0) + dd;

    const arr = [yyyy, mm, dd];

    return arr.join("-");
};

// 수정완료 버튼
const modifyHandler = (e) => {
    e.preventDefault();
    const subject = e.target.subject.value;
    const writer = e.target.writer.value;
    const content = e.target.content.value;

    try {
        isEmpty(subject, writer, content);
        board.subject = subject;
        board.writer = writer;
        board.content = content;
        board.date = recordDate();

        const boardStr = JSON.stringify(boardsObj);
        localStorage.setItem("boards", boardStr);
        location.href = "./view.html" + idx; // 상세 페이지로 이동
    } catch (e) {
        alert(e.message);
        console.error(e);
    }
};

modifyForm.addEventListener("submit", modifyHandler);
