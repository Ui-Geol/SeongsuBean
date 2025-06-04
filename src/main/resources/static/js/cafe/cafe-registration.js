document.addEventListener('DOMContentLoaded', async function () {
  // 경로에서 카페 ID 추출 (수정 모드 판단)
  const pathParts = window.location.pathname.split('/');
  const last = pathParts[pathParts.length - 1];

  // 숫자인 경우에만 수정 모드로 간주
  const isEdit = /^\d+$/.test(last);
  const cafeId = isEdit ? parseInt(last, 10) : null;

  // DOM 요소들
  const form = document.getElementById('cafeForm');
  const cafeNameInput = document.getElementById('cafeName');
  const addressInput = document.getElementById('address');
  const zipCodeInput = document.getElementById('zipCode');
  const detailAddressInput = document.getElementById('detailAddress');
  const callNumberInput = document.getElementById('phone');
  const introductionInput = document.getElementById('description');
  const businessHoursInput = document.getElementById('businessHoursJson');
  const imageInput = document.getElementById('imageInput');
  const cancelBtn = document.getElementById('cancelBtn');
  const charCountSpan = document.getElementById('charCount');

  // 글자 카운터 업데이트
  function updateCharCount() {
    const length = introductionInput.value.length;
    charCountSpan.textContent = length;
    charCountSpan.style.color = (length > 300) ? '#dc3545' : '#666';
  }

  // 소개 글자 수 카운터
  if (introductionInput && charCountSpan) {
    introductionInput.addEventListener('input', updateCharCount);
    updateCharCount(); // 초기 카운트 설정
  }

  // 취소 버튼 이벤트
  if (cancelBtn) {
    cancelBtn.addEventListener('click', () => {
      window.location.href = '/';
    });
  }

  // 주소 검색 버튼
  const btnAddressSearch = document.getElementById('btnAddressSearch');
  if (btnAddressSearch) {
    btnAddressSearch.addEventListener('click', function () {
      new daum.Postcode({
        oncomplete: function (data) {
          addressInput.value = data.address;
          zipCodeInput.value = data.zonecode;
          detailAddressInput.focus();
        }
      }).open();
    });
  }

  // 영업시간 관리 로직
  const businessHoursContainer = document.getElementById(
      'business-hours-container');
  const addBusinessHoursBtn = document.getElementById('add-business-hours-btn');
  const template = document.getElementById('tpl-business-hours');

  // 시간 옵션 생성
  function populateTimeOptions(container) {
    const hourSelects = container.querySelectorAll('.hour-select');
    const minuteSelects = container.querySelectorAll('.minute-select');
    const periodSelects = container.querySelectorAll('.period-select');

    // 시 옵션 (1-12)
    hourSelects.forEach(select => {
      if (select.children.length <= 1) { // placeholder만 있는 경우
        for (let i = 1; i <= 12; i++) {
          const option = document.createElement('option');
          option.value = i;
          option.textContent = i;
          select.appendChild(option);
        }
      }
    });

    // 분 옵션 (00, 30)
    minuteSelects.forEach(select => {
      if (select.children.length <= 1) {
        ['00', '30'].forEach(minute => {
          const option = document.createElement('option');
          option.value = minute;
          option.textContent = minute;
          select.appendChild(option);
        });
      }
    });

    // AM/PM 옵션
    periodSelects.forEach(select => {
      if (select.children.length <= 1) {
        ['AM', 'PM'].forEach(period => {
          const option = document.createElement('option');
          option.value = period;
          option.textContent = period;
          select.appendChild(option);
        });
      }
    });
  }

  // 영업시간 그룹 추가
  function addBusinessHoursGroup() {
    const clone = template.content.cloneNode(true);
    const group = clone.querySelector('.business-hours-group');

    // 시간 옵션 추가
    populateTimeOptions(group);

    // 삭제 버튼 이벤트
    const removeBtn = group.querySelector('.btn-remove-hours');
    removeBtn.addEventListener('click', function () {
      group.remove();
    });

    businessHoursContainer.appendChild(group);
  }

  // 영업시간 추가 버튼 이벤트
  if (addBusinessHoursBtn) {
    addBusinessHoursBtn.addEventListener('click', addBusinessHoursGroup);

    // 초기 그룹 하나 추가
    addBusinessHoursGroup();
  }

  // 영업시간 데이터 수집 (LocalTime 형식에 맞게 수정)
  function collectBusinessHours() {
    const groups = businessHoursContainer.querySelectorAll(
        '.business-hours-group');
    const businessHours = [];

    groups.forEach(group => {
      const weekday = group.querySelector('.weekday-select').value;
      const startHour = group.querySelector('.hour-select').value;
      const startMinute = group.querySelector('.minute-select').value;
      const startPeriod = group.querySelector('.period-select').value;
      const endHour = group.querySelectorAll('.hour-select')[1].value;
      const endMinute = group.querySelectorAll('.minute-select')[1].value;
      const endPeriod = group.querySelectorAll('.period-select')[1].value;

      if (weekday && startHour && startMinute && startPeriod &&
          endHour && endMinute && endPeriod) {

        // AM/PM을 24시간 형식으로 변환
        const convertTo24Hour = (hour, minute, period) => {
          let h = parseInt(hour);
          if (period === 'AM' && h === 12) {
            h = 0;
          }
          if (period === 'PM' && h !== 12) {
            h += 12;
          }
          return `${h.toString().padStart(2, '0')}:${minute}`;
        };

        businessHours.push({
          weekday: weekday,
          openTime: convertTo24Hour(startHour, startMinute, startPeriod),   // "HH:mm" 형식
          closeTime: convertTo24Hour(endHour, endMinute, endPeriod)        // "HH:mm" 형식
        });
      }
    });

    return businessHours;
  }

  // 이미지 업로드 및 미리보기 로직 (1개만)
  const imageUpload = document.getElementById('imageUpload');
  const imagePreview = document.getElementById('imagePreview');

  if (imageUpload && imageInput) {
    imageUpload.addEventListener('click', () => {
      imageInput.click();
    });

    imageInput.addEventListener('change', function (e) {
      const file = e.target.files[0]; // 첫 번째 파일만 사용
      imagePreview.innerHTML = '';

      if (file && file.type.startsWith('image/')) {
        const reader = new FileReader();
        reader.onload = function (e) {
          const div = document.createElement('div');
          div.className = 'preview-item';
          div.innerHTML = `
                        <img src="${e.target.result}" alt="미리보기">
                        <button type="button" class="remove-image">×</button>
                    `;
          imagePreview.appendChild(div);
        };
        reader.readAsDataURL(file);
      }
    });

    // 이미지 제거 기능 (단순화)
    imagePreview.addEventListener('click', function (e) {
      if (e.target.classList.contains('remove-image')) {
        imageInput.value = ''; // 파일 입력 초기화
        e.target.closest('.preview-item').remove();
      }
    });
  }

  // 수정인 경우 데이터 불러오기
  if (isEdit) {
    try {
      const res = await fetch(`/api/cafe/${cafeId}`);
      const data = await res.json();

      cafeNameInput.value = data.cafeName || '';
      addressInput.value = data.address || '';
      zipCodeInput.value = data.zipCode || '';
      detailAddressInput.value = data.detailAddress || '';
      callNumberInput.value = data.callNumber || '';
      introductionInput.value = data.introduction || '';

      // 영업시간 데이터 복원
      if (data.operationTimes && Array.isArray(data.operationTimes)) {
        // 기존 그룹 제거
        businessHoursContainer.innerHTML = '';

        // 데이터에 따라 그룹 생성
        data.operationTimes.forEach(hours => {
          addBusinessHoursGroup();
          const lastGroup = businessHoursContainer.lastElementChild;

          // 값 설정 (OperationTimeDTO 구조에 맞게)
          if (hours.weekday) {
            lastGroup.querySelector('.weekday-select').value = hours.weekday;
          }
          // 시간 파싱 후 설정 로직 (openTime, closeTime 사용)
          if (hours.openTime) {
            // "08:00 AM" 형식을 파싱해서 select에 설정
            // 추후 구현
          }
          if (hours.closeTime) {
            // "22:00 PM" 형식을 파싱해서 select에 설정
            // 추후 구현
          }
        });
      }

      updateCharCount();
    } catch (err) {
      console.error('수정 데이터 불러오기 실패', err);
      alert('카페 정보를 불러올 수 없습니다.');
    }
  }

  // form submit 이벤트 (상세한 디버깅 포함)
  form.addEventListener('submit', async function (e) {
    e.preventDefault();

    const cafeName = cafeNameInput.value.trim();
    const address = addressInput.value.trim();
    const zipCode = zipCodeInput.value.trim();
    const detailAddress = detailAddressInput.value.trim();
    const callNumber = callNumberInput.value.trim();
    const introduction = introductionInput.value.trim();
    const businessHours = collectBusinessHours();
    const image = imageInput.files[0];

    // 디버깅: 수집된 데이터 확인
    console.log('=== 폼 데이터 확인 ===');
    console.log('cafeName:', cafeName);
    console.log('address:', address);
    console.log('zipCode:', zipCode);
    console.log('detailAddress:', detailAddress);
    console.log('callNumber:', callNumber);
    console.log('introduction:', introduction);
    console.log('businessHours:', businessHours);

    // 유효성 검사
    if (!cafeName) {
      alert('카페 이름을 입력해주세요.');
      return;
    }
    if (!address || !zipCode) {
      alert('주소를 검색해주세요.');
      return;
    }
    if (!detailAddress) {
      alert('상세주소를 입력해주세요.');
      return;
    }
    if (!callNumber) {
      alert('전화번호를 입력해주세요.');
      return;
    }
    if (introduction.length > 300) {
      alert('소개는 300자 이하로 작성해주세요.');
      return;
    }
    if (businessHours.length === 0) {
      alert('영업시간을 하나 이상 설정해주세요.');
      return;
    }

    const files = imageInput.files;
    if (files.length != 1) {
      alert('이미지는 1개만 넣는것이 필수 입니다');
      return;
    }

    // 영업시간 JSON 설정
    businessHoursInput.value = JSON.stringify(businessHours);

    const formData = new FormData(form);

    // 디버깅: FormData 내용 확인
    console.log('=== FormData 내용 확인 ===');
    for (let [key, value] of formData.entries()) {
      console.log(key + ':', value);
    }

    const url = cafeId ? `/api/cafe/${cafeId}` : '/api/cafe';
    const method = cafeId ? 'PUT' : 'POST';

    console.log('=== 요청 정보 ===');
    console.log('URL:', url);
    console.log('Method:', method);

    try {
      console.log('=== 요청 전송 시작 ===');

      const response = await fetch(url, {
        method,
        body: method === 'POST' ? formData : JSON.stringify({
          cafeName,
          address,
          zipCode,
          detailAddress,
          image,
          callNumber,
          introduction,
          businessHours: JSON.stringify(businessHours)
        }),
        headers: method === 'PUT' ? {
          'Content-Type': 'application/json'
        } : undefined
      });

      console.log('=== 응답 정보 ===');
      console.log('Status:', response.status);
      console.log('Status Text:', response.statusText);

      // 응답이 JSON이 아닐 수도 있으므로 먼저 텍스트로 받기
      const responseText = await response.text();
      console.log('Response Text:', responseText);

      let result;
      try {
        result = JSON.parse(responseText);
      } catch (parseError) {
        console.error('JSON 파싱 실패:', parseError);
        console.error('Response Text:', responseText);
        alert('서버 응답을 처리할 수 없습니다: ' + responseText);
        return;
      }

      console.log('Parsed Result:', result);

      if (!response.ok) {
        // 400, 500 등 에러 응답
        console.error('HTTP Error:', response.status, response.statusText);
        const errorMessage = result.message || result.error
            || `HTTP ${response.status} 에러가 발생했습니다.`;
        alert('서버 에러: ' + errorMessage);
        return;
      }

      // 성공 처리
      if (cafeId && result.updated) {
        alert('카페 정보가 수정되었습니다!');
        location.href = `/cafe/${cafeId}`;
      } else if (!cafeId && result.success) {
        alert('카페가 등록되었습니다!');
        location.href = `/cafe/${result.id}`;
      } else {
        console.warn('예상치 못한 응답:', result);
        alert('처리에 실패했습니다: ' + (result.message || '알 수 없는 오류'));
      }
    } catch (err) {
      console.error('=== 네트워크 또는 기타 오류 ===');
      console.error('Error:', err);
      console.error('Error message:', err.message);
      console.error('Error stack:', err.stack);
      alert('요청 중 문제가 발생했습니다: ' + err.message);
    }
  });
});