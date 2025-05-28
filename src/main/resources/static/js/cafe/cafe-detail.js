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

  document.getElementById('editCafe').addEventListener('click', () => {
    window.location.href = '/cafe-registration';
  });

  document.getElementById('deleteCafe').addEventListener('click', () => {
    window.location.href = '/map';
  });
});