document.addEventListener('DOMContentLoaded', async function () {
    const editor = new toastui.Editor({
        el: document.querySelector('#editor'),
        height: '500px',
        initialEditType: 'wysiwyg',
        previewStyle: 'vertical',
        language: 'ko'
    });

    const counter = document.createElement('div');
    counter.className = 'char-count';
    counter.style.marginTop = '8px';
    counter.style.fontSize = '14px';
    counter.style.color = '#666';
    counter.textContent = '0 / 2000자';
    document.querySelector('#editor').after(counter);

    editor.on('change', function () {
        const html = editor.getHTML();
        const tempDiv = document.createElement('div');
        tempDiv.innerHTML = html;
        const plainText = tempDiv.textContent || tempDiv.innerText || '';
        const length = plainText.length;
        counter.textContent = `${length} / 2000자`;
        counter.style.color = (length > 2000) ? '#dc3545' : '#666';
    });

    const pathParts = window.location.pathname.split('/');
    const last = pathParts[pathParts.length - 1];

// 숫자인 경우에만 수정 모드로 간주
    const isEdit = /^\d+$/.test(last);
    const freeBoardId = isEdit ? parseInt(last, 10) : null;


    const form = document.getElementById('free-form');
    const titleInput = document.getElementById('title');
    const contentInput = document.getElementById('editor-contents');
    const cancelBtn = document.getElementById('cancel-btn');
    const headWordSelect = document.getElementById('head-word');

    if (cancelBtn) {
        cancelBtn.addEventListener('click', () => {
            window.location.href = '/free/list';
        });
    }
    // 수정인 경우 데이터 불러오기
    if (isEdit) {
        try {
            const res = await fetch(`/api/free/${freeBoardId}`);
            const data = await res.json();
            titleInput.value = data.title;
            editor.setHTML(data.content);
            //emailInput.value = data.email;
        } catch (err) {
            console.error('수정 데이터 불러오기 실패', err);
            alert('게시글 정보를 불러올 수 없습니다.');
        }
    }

    // form submit 이벤트
    form.addEventListener('submit', async function (e) {
        e.preventDefault();

        const title = titleInput.value.trim();
        const contentHtml = editor.getHTML();
        const tempElement = document.createElement('div');
        tempElement.innerHTML = contentHtml;
        const plainText = tempElement.textContent || tempElement.innerText || '';
        //const email = emailInput.value;
        const headWord = headWordSelect.value;

        if (!headWord || headWord === '머릿말 선택') {
            alert('머릿말을 선택해주세요.');
            return;
        }
        if (!title) {
            alert('제목을 입력해주세요.');
            return;
        }
        if (plainText.length > 2000) {
            alert('본문은 2000자 이하로 작성해주세요.');
            return;
        }

        // content hidden input에 실제 값 저장
        contentInput.value = contentHtml;

        const imageInput = document.getElementById('images');
        const files = imageInput.files;

        if (files.length > 5) {
            alert('이미지는 최대 5개까지만 업로드할 수 있습니다.');
            return;
        }
        const formData = new FormData(form);
        const url = freeBoardId ? `/api/free/post/${freeBoardId}` : '/api/free';
        const method = freeBoardId ? 'PUT' : 'POST';

        try {
            const response = await fetch(url, {
                method,
                body: method === 'POST' ? formData : JSON.stringify({
                    title,
                    content: contentHtml
                    //email
                }),
                headers: method === 'PUT' ? {
                    'Content-Type': 'application/json'
                } : undefined
            });

            const result = await response.json();

            if (freeBoardId && result.updated) {
                alert('게시글이 수정되었습니다!');
                location.href = `/free/detail/${freeBoardId}`;
            } else if (!freeBoardId && result.success) {
                alert('게시글이 등록되었습니다!');
                location.href = `/free/detail/${result.id}`;
            } else {
                alert('처리에 실패했습니다.');
            }
        } catch (err) {
            console.error('전송 오류', err);
            alert('요청 중 문제가 발생했습니다.');
        }
    });
});
