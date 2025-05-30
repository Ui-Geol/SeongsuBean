document.addEventListener('DOMContentLoaded', function () {
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

    const form = document.querySelector('form');
    form.addEventListener('submit', async function (e) {
        e.preventDefault();

        const title = document.querySelector('input[placeholder="제목을 입력하세요"]').value.trim();
        const contentHtml = editor.getHTML();
        const tempElement = document.createElement('div');
        tempElement.innerHTML = contentHtml;
        const plainText = tempElement.textContent || tempElement.innerText || '';
        const email = 'test@example.com'; // 실제 로그인 유저로 교체

        if (!title) {
            alert('제목을 입력해주세요.');
            return;
        }
        if (plainText.length > 2000) {
            alert('본문은 2000자 이하로 작성해주세요.');
            return;
        }

        const body = {
            title: title,
            content: contentHtml,
            email: email,
            headWord: "잡담"
        };


        try {
            const response = await fetch('/api/free', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(body)
            });

            if (response.ok) {
                const result = await response.json();
                alert('게시글이 등록되었습니다!');
                location.href = `/free/detail/${result.id}`;
            } else {
                alert('저장 실패: 서버 오류');
            }
        } catch (err) {
            console.error('전송 실패', err);
            alert('네트워크 오류로 저장에 실패했습니다.');
        }
    });
});
