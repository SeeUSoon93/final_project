document.addEventListener('DOMContentLoaded', function () {
    let boardStr = localStorage.getItem("boards");

    // localStorage 초기값 지정
    if (boardStr === null) {
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
            <td>${objValue.category}</td>
            <td class="text-left tableset tableset-tit-ellipsis tableset-tit">
                <a href="./view.html?index=${objValue.index}">
                    <span>${objValue.subject}</span>
                </a>
            </td>
            <td class="text-left tableset tableset-tit-ellipsis tableset-tit">
                <a href="./view.html?index=${index}">
                    <span>${objValue.writer}</span>
                </a>
            </td>
            <td>${objValue.views}</td>
            <td>${objValue.date}</td>
            <td>${objValue.likes}</td>
        </tr>
        `;
    };

    // 템플릿 반영
    const tbody = document.querySelector("tbody");

    // 템플릿 초기화
    tbody.innerHTML = "";

    // 모든 게시글을 표시
    for (let i = 0; i < boardsObj.length; i++) {
        tbody.innerHTML += template(i, boardsObj[i]);
    }
});

// 카테고리 버튼 클릭시 카테고리별로 목록 보이게
function filterPosts(category) {
    const radios = document.querySelectorAll('.radio');
    radios.forEach(radio => {
        radio.classList.remove('active');
    });

    const posts = document.querySelectorAll('.post-item');
    posts.forEach(post => {
        const postCategory = post.getAttribute('data-category');
        if (category === 'all' || postCategory === category) {
            post.style.display = 'table-row';
        } else {
            post.style.display = 'none';
        }
    });

    const activeRadio = document.querySelector(`.radio[name="radio"][onclick*="${category}"]`);
    activeRadio.classList.add('active');
}