let isOpen = true;

function showTab(tabName) {
// Hide all tab contents
  const tabContents = document.querySelectorAll('.tab-content');
  tabContents.forEach(content => {
    content.classList.remove('active');
  });

// Remove active class from all tabs
  const tabs = document.querySelectorAll('.nav-tab');
  tabs.forEach(tab => {
    tab.classList.remove('active');
  });

// Show selected tab content
  document.getElementById(tabName).classList.add('active');

// Add active class to clicked tab
  event.target.classList.add('active');

// Hide hours dropdown when switching tabs
  document.getElementById('hoursDetail').classList.remove('show');
}

function toggleStatus() {
  const statusBadge = document.getElementById('statusBadge');
  isOpen = !isOpen;

  if (isOpen) {
    statusBadge.textContent = '영업중';
    statusBadge.classList.remove('closed');
  } else {
    statusBadge.textContent = '영업종료';
    statusBadge.classList.add('closed');
  }
}

function toggleHours() {
  const hoursDetail = document.getElementById('hoursDetail');
  hoursDetail.classList.toggle('show');
}

function toggleTodayClosed() {
  const btn = document.getElementById('todayClosedBtn');
  const isCancelled = btn.classList.toggle('cancelled');
  btn.textContent = isCancelled ? '휴무 취소' : '오늘 휴무';
}

// 현재 요일과 시간에 맞게 영업상태를 업데이트하는 함수
function updateTodayHours() {
  // operationTimes가 정의되지 않은 경우 함수 종료
  if (typeof operationTimes === 'undefined' || !operationTimes) {
    console.error('operationTimes 데이터가 정의되지 않았습니다.');
    return;
  }

  const today = new Date().getDay(); // 0: 일요일, 1: 월요일, ..., 6: 토요일
  const weekdays = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];

  // operationTimes 배열에서 오늘 요일에 해당하는 시간을 찾기
  const todaySchedule = operationTimes.find(time =>
      time.weekday === weekdays[today]
  );

  // HTML 요소들 가져오기
  const todayHoursSpan = document.querySelector('.hours-dropdown span');
  const statusBadge = document.getElementById('statusBadge');
  const todayClosedBtn = document.getElementById('todayClosedBtn');

  if (todaySchedule && todaySchedule.openTime && todaySchedule.closeTime) {
    // 시간에서 초 제거 (HH:MM:SS -> HH:MM)
    const formatTime = (time) => {
      if (time.includes(':')) {
        const parts = time.split(':');
        return `${parts[0]}:${parts[1]}`; // HH:MM만 반환
      }
      return time;
    };

    const openTime = formatTime(todaySchedule.openTime);
    const closeTime = formatTime(todaySchedule.closeTime);

    // 오늘의 영업시간으로 텍스트 업데이트 (전체 시간 표시)
    todayHoursSpan.textContent = `${openTime} - ${closeTime}`;

    // 현재 시간과 비교하여 영업중/영업종료 상태 업데이트
    const now = new Date();
    const currentTime = now.getHours() * 100 + now.getMinutes(); // HHMM 형식

    // 시간을 숫자로 변환 (HH:MM -> HHMM)
    const parseTimeToNumber = (timeStr) => {
      const cleanTime = timeStr.replace(':', '');
      return parseInt(cleanTime);
    };

    const openTimeNum = parseTimeToNumber(openTime);
    const closeTimeNum = parseTimeToNumber(closeTime);

    if (currentTime >= openTimeNum && currentTime < closeTimeNum) {
      // 영업중인 경우
      statusBadge.textContent = '영업중';
      statusBadge.classList.remove('closed');
      statusBadge.style.display = 'inline-block';
      todayClosedBtn.style.display = 'none';
      isOpen = true;
    } else {
      // 영업 종료 (영업 시작 전이거나 영업 종료 후)
      statusBadge.textContent = '영업종료';
      statusBadge.classList.add('closed');
      statusBadge.style.display = 'inline-block';
      todayClosedBtn.style.display = 'none';
      isOpen = false;
    }
  } else {
    // 오늘 휴무인 경우
    todayHoursSpan.textContent = '오늘 휴무';
    statusBadge.style.display = 'none';
    todayClosedBtn.style.display = 'inline-block';
    isOpen = false;
  }
}

// Close hours dropdown when clicking outside
document.addEventListener('click', function (event) {
  const hoursContainer = document.querySelector('.hours-container');
  const hoursDetail = document.getElementById('hoursDetail');

  if (!hoursContainer.contains(event.target)) {
    hoursDetail.classList.remove('show');
  }
});

// Initialize page
document.addEventListener('DOMContentLoaded', function () {
  console.log('Cafe detail page loaded');

  // 영업시간 업데이트 함수 실행
  updateTodayHours();

  document.getElementById('editCafe').addEventListener('click', () => {
    window.location.href = '/cafe-registration';
  });

  document.getElementById('deleteCafe').addEventListener('click', () => {
    window.location.href = '/';
  });
});
