let currentPage = 1;
const pageSize = 7;
let totalPages = 1;
let fullReportList = [];

document.addEventListener('DOMContentLoaded', () => {
  fetch('/api/report/list')
  .then(response => {
    if (!response.ok) throw new Error(`서버 오류: ${response.status}`);
    return response.json();
  })
  .then(reportList => {
    fullReportList = reportList;
    totalPages = Math.ceil(reportList.length / pageSize);
    renderReportList();
    renderPagination();
  })
  .catch(error => {
    console.error('게시글 목록 불러오기 실패:', error);
    alert('제보글을 불러오는 중 문제가 발생했습니다.');
  });
});

function renderReportList() {
  const container = document.querySelector('.article-list');
  container.innerHTML = '';

  const start = (currentPage - 1) * pageSize;
  const end = start + pageSize;
  const pageItems = fullReportList.slice(start, end);

  if (pageItems.length === 0) {
    container.innerHTML = '<p>등록된 제보가 없습니다.</p>';
    return;
  }

  pageItems.forEach((item, index) => {
    const article = document.createElement('article');
    article.className = 'article-item';
    article.style.cursor = 'pointer';
    article.onclick = () => {
      window.location.href = `/report/detail/${item.reportBoardId}`;
    };

    article.innerHTML = `
      <div class="article-thumbnail">
        <img src="${item.thumbnail || `/images/board/Cafe${(index % 7) + 1}.png`}" alt="카페 이미지">
      </div>
      <div class="article-content">
        <h2 class="article-title">${escapeHtml(item.title)}</h2>
        <p class="article-description">
          ${escapeHtml(item.content).substring(0, 100)}...
        </p>
      </div>
      <div class="article-meta">
        <span class="article-author">${item.nickname || '익명제보자'}</span>
        <span class="article-date">${formatDate(item.createdDate)}</span>
      </div>
    `;
    container.appendChild(article);
  });
}

function renderPagination() {
  const pagination = document.querySelector('.pagination');
  pagination.innerHTML = '';

  // 이전 버튼
  pagination.appendChild(createPageLink('«', 1, currentPage === 1));
  pagination.appendChild(createPageLink('‹', currentPage - 1, currentPage === 1));

  // 번호
  for (let i = 1; i <= totalPages; i++) {
    const pageLink = createPageLink(i, i, i === currentPage);
    pagination.appendChild(pageLink);
  }

  // 다음 버튼
  pagination.appendChild(createPageLink('›', currentPage + 1, currentPage === totalPages));
  pagination.appendChild(createPageLink('»', totalPages, currentPage === totalPages));
}

function createPageLink(text, page, disabled) {
  const link = document.createElement('a');
  link.href = '#';
  link.textContent = text;
  if (disabled) {
    link.classList.add('disabled');
    link.style.pointerEvents = 'none';
    link.style.opacity = '0.5';
  } else if (page === currentPage) {
    link.classList.add('current');
  }

  link.addEventListener('click', (e) => {
    e.preventDefault();
    if (page !== currentPage && !disabled) {
      currentPage = page;
      renderReportList();
      renderPagination();
    }
  });

  return link;
}

// 날짜 포맷
function formatDate(dateStr) {
  const date = new Date(dateStr);
  return date.toISOString().split('T')[0];
}

// XSS 방지
function escapeHtml(str) {
  return str?.replace(/</g, "&lt;").replace(/>/g, "&gt;") || '';
}
