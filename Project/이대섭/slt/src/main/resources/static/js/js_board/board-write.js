const writeForm = document.querySelector("#writeForm");

class Board {
    constructor(indexNum, subjectStr, writerStr, contentStr, categoryStr) {
        this.index = indexNum;
        this.Subject = subjectStr;
        this.Writer = writerStr;
        this.Content = contentStr;
        this.Category = categoryStr;
        this.date = recordDate();
        this.views = -1;
        this.refresh = false; 
    }

    set Subject(value) {
        if (value.length === 0) throw new Error("제목을 입력해주세요.");
        this.subject = value;
    }

    set Writer(value) {
        if (value.length === 0) throw new Error("작성자를 입력해주세요.");
        this.writer = value;
    }

    set Content(value) {
        if (value.length === 0) throw new Error("내용을 입력해주세요.");
        this.content = value;
    }

    set Category(value) {
        if (value === undefined) throw new Error("카테고리를 선택해주세요.");
        this.category = value;
    }
}

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

// 글작성 버튼
const submitHandler = (e) => {
    e.preventDefault();
    const subject = e.target.subject.value;
    const writer = e.target.writer.value;
    const content = e.target.content.value;
    const category = document.querySelector('input[name="select"]:checked');

    try {
        if (!subject || !writer || !content) {
            throw new Error("제목, 작성자, 내용을 모두 입력해주세요.");
        }

        if (!category) {
            throw new Error("카테고리를 선택해주세요.");
        }

        // boards 가져오기
        let boardsObj = JSON.parse(localStorage.getItem("boards"));

        // 만약 boardsObj가 배열이 아니라면 빈 배열로 초기화
        if (!Array.isArray(boardsObj)) {
            boardsObj = [];
        }

        // 객체 추가
        const index = boardsObj.length;

        const instance = new Board(index, subject, writer, content, category.value);

        // 조회수 초기화
        instance.views = 0;

        boardsObj.push(instance);

        // boards 저장
        const boardsStr = JSON.stringify(boardsObj);
        localStorage.setItem("boards", boardsStr);
        
        // location.href = "./view.html?index=" + index;
        // 작성 완료 후 목록으로 이동
        location.href = "./list.html";
    } catch (e) {
        // 예외 발생시 메시지 출력
        alert(e.message);
        console.error(e);
    }
};

// 초기화 버튼
const resetHandler = (e) => {
    e.preventDefault();
    writeForm.reset();
};

writeForm.addEventListener("submit", submitHandler);
document.querySelector("#reset").addEventListener("click", resetHandler);