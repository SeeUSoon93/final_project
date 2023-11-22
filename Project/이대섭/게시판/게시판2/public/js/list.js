let boardStr = localStorage.getItem("boards");

// localStorage 초기값 지정
if (boardStr === null){
    const listStr = JSON.stringify([]);
    localStorage.setItem("boards", listStr);
    boardStr = listStr;
}

const boardsObj = JSON.parse(boardStr);

// 템플릿 생성
const template = (index, objValue) => {
    return `
    <tr>
    <td>${index + 1}</td>
    <td><a href="./board/view.html?index=${objValue.index}">${objValue.subject}</a></td>
    <td>${objValue.writer}</td>
    <td>${objValue.date}</td>
    <td>${objValue.views}</td>
    </tr>
    `;
};

// 템플릿 반영
const tbody = document.querySelector("tbody");

for(let i = 0; i < boardsObj.length; i++) {
    tbody.innerHTML += template(i, boardsObj[i]);
    boardsObj[i].refresh = false;
    const refreshStr = JSON.stringify(boardsObj);
    localStorage.setItem("boards", refreshStr);
}