// // 댓글
// document.addEventListener('DOMContentLoaded', function () {
//     const commentsContainer = document.getElementById('comments-container');
//     const commentForm = document.getElementById('comment-form');
//     const attachFileBtn = document.getElementById('attach-file');
//     const fileInput = document.getElementById('file-input');

//     attachFileBtn.addEventListener('click', function () {
//         fileInput.click();
//     });

//     commentForm.addEventListener('submit', function (e) {
//         e.preventDefault();

//         const commentText = document.getElementById('comment-text').value;
//         const file = fileInput.files[0];

//         // 댓글 생성
//         const comment = createCommentElement(commentText, file);

//         // 생성한 댓글을 목록의 맨 위에 추가
//         commentsContainer.insertBefore(comment, commentsContainer.firstChild);

//         // 입력 필드 초기화
//         document.getElementById('comment-text').value = '';
//         fileInput.value = '';
//     });

//     // 댓글 삭제와 수정 이벤트는 여기에 추가할 수 있습니다.
// });

// function createCommentElement(text, file) {
//     const commentDiv = document.createElement('div');
//     commentDiv.classList.add('comment');

//     const textDiv = document.createElement('div');
//     textDiv.textContent = text;
//     commentDiv.appendChild(textDiv);

//     if (file) {
//         const img = document.createElement('img');
//         img.src = URL.createObjectURL(file);
//         commentDiv.appendChild(img);
//     }

//     // 삭제 버튼 추가
//     const deleteBtn = document.createElement('button');
//     deleteBtn.textContent = '삭제';
//     deleteBtn.style.backgroundColor = '#477489';
//     deleteBtn.style.color = 'white';
//     deleteBtn.style.border = 'none';
//     deleteBtn.style.marginRight = '10px';
//     deleteBtn.style.width = '5%';
    
//     deleteBtn.addEventListener('click', function () {
//         commentDiv.remove();
//     });
//     commentDiv.appendChild(deleteBtn);

//     // 수정 버튼 추가
//     const editBtn = document.createElement('button');
//     editBtn.textContent = '수정';
//     editBtn.style.backgroundColor = '#477489';
//     editBtn.style.color = 'white';
//     editBtn.style.border = 'none';
//     editBtn.style.width = '5%';

//     editBtn.addEventListener('click', function () {
//         textDiv.contentEditable = true;
//         textDiv.focus();

//         textDiv.addEventListener('blur', function (){
//             textDiv.contentEditable = false;
//         });
//         // const newText = prompt('댓글을 수정하세요:', text);
//         // if (newText !== null) {
//         //     textDiv.textContent = newText;
//         // }
//     });
//     commentDiv.appendChild(editBtn);

//     return commentDiv;

// }

