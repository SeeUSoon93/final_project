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

// 게시글번호 역순


// 페이징
