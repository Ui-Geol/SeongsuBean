document.addEventListener('DOMContentLoaded', function () {
  // 폼 요소들 가져오기
  const form = document.getElementById('menuForm');
  const menuDescriptionTextarea = document.getElementById('menuDescription');
  const charCount = document.getElementById('charCount');
  const imageUpload = document.getElementById('imageUpload');
  const imageInput = document.getElementById('imageInput');
  const imagePreview = document.getElementById('imagePreview');
  const cancelBtn = document.getElementById('cancelBtn');
  const priceInput = document.getElementById('price');
  const cafeIdInput = document.getElementById('cafeId')

  // 글자 수 카운터
  menuDescriptionTextarea.addEventListener('input', function () {
    charCount.textContent = this.value.length;
  });

  // 가격 입력 포맷팅 (천 단위 콤마)
  priceInput.addEventListener('input', function () {
    let value = this.value.replace(/[^0-9]/g, '');
    if (value) {
      // 천 단위 콤마 추가는 표시용으로만 사용 (실제 값은 숫자로 유지)
      this.setAttribute('data-formatted', Number(value).toLocaleString() + '원');
    } else {
      this.removeAttribute('data-formatted');
    }
  });

  // 가격 입력 필드 포커스 아웃 시 포맷팅 표시
  priceInput.addEventListener('blur', function () {
    if (this.value) {
      const formatted = Number(this.value).toLocaleString() + '원';
      this.setAttribute('placeholder', formatted);
    }
  });

  // 가격 입력 필드 포커스 시 원래 placeholder 복원
  priceInput.addEventListener('focus', function () {
    this.setAttribute('placeholder', '가격을 입력해주세요');
  });

  // 이미지 업로드 영역 클릭 이벤트
  imageUpload.addEventListener('click', function () {
    imageInput.click();
  });

  // 파일 선택 이벤트
  imageInput.addEventListener('change', function (e) {
    handleFiles(e.target.files);
  });

  // 파일 처리 함수
  function handleFiles(files) {
    Array.from(files).forEach(file => {
      if (file.type.startsWith('image/')) {
        const reader = new FileReader();
        reader.onload = function (e) {
          addImagePreview(e.target.result, file.name);
        };
        reader.readAsDataURL(file);
      }
    });
  }

  // 이미지 미리보기 추가
  function addImagePreview(src, fileName) {
    const previewItem = document.createElement('div');
    previewItem.className = 'preview-item';

    previewItem.innerHTML = `
            <img src="${src}" alt="${fileName}">
            <button type="button" class="remove-image" onclick="removeImage(this)">×</button>
        `;

    imagePreview.appendChild(previewItem);
  }

  // 이미지 제거 함수
  window.removeImage = function (button) {
    button.parentElement.remove();
  };

  // 폼 유효성 검사
  function validateForm() {
    let isValid = true;
    const errors = [];

    // 필수 필드 검사
    const menuCategory = document.getElementById('menuCategory').value;
    const menuName = document.getElementById('menuName').value.trim();
    const price = document.getElementById('price').value;

    // 카테고리 검사
    if (!menuCategory) {
      errors.push('메뉴 카테고리를 선택해주세요.');
      document.getElementById('menuCategory').classList.add('error');
      isValid = false;
    } else {
      document.getElementById('menuCategory').classList.remove('error');
    }

    // 메뉴명 검사
    if (!menuName) {
      errors.push('메뉴명을 입력해주세요.');
      document.getElementById('menuName').classList.add('error');
      isValid = false;
    } else {
      document.getElementById('menuName').classList.remove('error');
    }

    // 가격 검사
    if (!price || price <= 0) {
      errors.push('올바른 가격을 입력해주세요.');
      document.getElementById('price').classList.add('error');
      isValid = false;
    } else {
      document.getElementById('price').classList.remove('error');
    }

    // 에러 메시지 표시
    if (!isValid) {
      alert(errors.join('\n'));
    }

    return isValid;
  }

  form.addEventListener('submit', function (e) {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    // 방법 1: FormData를 수동으로 구성 (권장)
    const serverFormData = new FormData();

    // 각 필드 값을 수동으로 추가
    serverFormData.append('menuCategory',
        document.getElementById('menuCategory').value);
    serverFormData.append('menuName',
        document.getElementById('menuName').value);
    serverFormData.append('menuDescription',
        document.getElementById('menuDescription').value);
    serverFormData.append('price', document.getElementById('price').value);
    serverFormData.append('cafeId', document.getElementById('cafeId').value);
    const selectedFile = document.getElementById('imageInput').files[0];
    if (!selectedFile) {
      alert('이미지를 선택해주세요.');
      return;
    }

    serverFormData.append('image', selectedFile);

    // 서버로 전송
    fetch('/api/cafe/menu', {
      method: 'POST',
      body: serverFormData
    })
    .then(response => response.json())
    .then(data => {
      alert('메뉴가 성공적으로 등록되었습니다!');
      window.location.href = '/cafe/' + data.id;
    })
    .catch(error => {
      console.error('Error:', error);
      alert('오류가 발생했습니다.');
    });
  });

  // 취소 버튼 이벤트
  cancelBtn.addEventListener('click', function () {
    if (confirm('작성 중인 내용이 모두 삭제됩니다. 계속하시겠습니까?')) {
      form.reset();
      charCount.textContent = '0';
      imagePreview.innerHTML = '';
      // 에러 스타일 제거
      const errorElements = form.querySelectorAll('.error');
      errorElements.forEach(element => {
        element.classList.remove('error');
      });

      window.location.href = '/cafe/' + cafeIdInput.value;
    }
  });

  // 실시간 입력 피드백
  const inputs = form.querySelectorAll('input, textarea, select');
  inputs.forEach(input => {
    input.addEventListener('blur', function () {
      if (this.hasAttribute('required') && !this.value.trim()) {
        this.classList.add('error');
      } else {
        this.classList.remove('error');
      }
    });

    input.addEventListener('focus', function () {
      this.classList.remove('error');
    });
  });
});
