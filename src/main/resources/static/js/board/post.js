document.addEventListener('DOMContentLoaded', function () {
    // 1. Toast UI Editor 초기화
    const editor = new toastui.Editor({
        el: document.querySelector('#editor'),
        height: '500px',
        initialEditType: 'wysiwyg',
        previewStyle: 'vertical',
        language: 'ko'
    });

    // 2. 글자 수 카운터 요소 생성
    const counter = document.createElement('div');
    counter.className = 'char-count';
    counter.style.marginTop = '8px';
    counter.style.fontSize = '14px';
    counter.style.color = '#666';
    counter.textContent = '0 / 2000자';
    document.querySelector('#editor').after(counter);

    // 3. 에디터 내용 변경 시 글자 수 업데이트
    editor.on('change', function () {
        const html = editor.getHTML();
        const tempDiv = document.createElement('div');
        tempDiv.innerHTML = html;
        const plainText = tempDiv.textContent || tempDiv.innerText || '';
        const length = plainText.length;

        counter.textContent = `${length} / 2000자`;
        counter.style.color = (length > 2000) ? '#dc3545' : '#666';
    });

    // 4. form submit 시 본문 유효성 검사 및 hidden input 설정
    const form = document.querySelector('form');
    form.addEventListener('submit', function (e) {
        const html = editor.getHTML();
        const tempElement = document.createElement('div');
        tempElement.innerHTML = html;
        const plainText = tempElement.textContent || tempElement.innerText || "";

        if (plainText.length > 2000) {
            e.preventDefault();
            alert("본문은 2,000자 이하로 작성해주세요.");
            return;
        }

        document.querySelector('#editor-contents').value = html;
    });
});
