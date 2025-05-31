let reportId = null;

document.addEventListener('DOMContentLoaded', async () => {
  const pathParts = window.location.pathname.split('/');
  reportId = pathParts[pathParts.length - 1] || pathParts[pathParts.length - 2];

  // ✅ 삭제 이벤트 바인딩은 DOMContentLoaded 안에서
  const deleteBtn = document.getElementById('delete-btn');
  if (deleteBtn) {
    deleteBtn.removeEventListener('click', deleteHandler); // 혹시 모를 중복 제거
    deleteBtn.addEventListener('click', deleteHandler);    // 안전하게 등록
  }

  const editBtn = document.getElementById('edit-btn');
  if (editBtn) {
    editBtn.addEventListener('click', () => {
      if (!reportId) {
        alert('잘못된 게시글입니다. 수정할 수 없습니다.');
        return;
      }
      window.location.href = `/report/set/${reportId}`;
    });
  }

  try {
    const response = await fetch(`/api/report/${reportId}`);

    if (!response.ok) {
      throw new Error(`서버 응답 오류: ${response.status}`);
    }

    const data = await response.json();

    document.getElementById('post-title').textContent = data.title;
    document.getElementById('author-name').textContent = data.email;
    document.getElementById('post-date').textContent = formatDate(data.createdDate);
    document.getElementById('post-content').innerHTML = data.content;

    const imageTag = document.getElementById('post-image');
    if (data.images && Array.isArray(data.images) && data.images[0]) {
      imageTag.src = `/images/board/${data.images[0]}`;
      imageTag.alt = '게시글 이미지';
    } else {
      imageTag.src = '/images/board/default.png';
      imageTag.alt = '기본 이미지';
    }
    imageTag.style.display = 'block';
  } catch (err) {
    console.error('불러오기 실패:', err);
    alert('게시글을 불러오는 데 실패했습니다.');
  }

  function formatDate(datetimeStr) {
    const date = new Date(datetimeStr);
    return date.toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    });
  }
});

// ✅ 삭제 핸들러 함수로 분리
async function deleteHandler() {
  if (!reportId) {
    alert('잘못된 게시글입니다. 삭제할 수 없습니다.');
    return;
  }

  if (!confirm('정말 삭제하시겠습니까?')) return;

  try {
    const res = await fetch(`/api/report/${reportId}`, {
      method: 'DELETE'
    });

    let result = {};
    try {
      result = await res.json();
    } catch (parseErr) {
      console.warn("🟡 JSON 파싱 실패 (무시)", parseErr);
    }

    console.log("✅ 응답 상태코드:", res.status);
    console.log("✅ res.ok:", res.ok);
    console.log("✅ result.deleted:", result.deleted);

    if (res.ok) {
      alert('삭제되었습니다.');
      setTimeout(() => {
        window.location.href = '/report/list?ts=' + Date.now();
      }, 500); // 0.5초 정도 딜레이 후 리다이렉트
      return;
    } else {
      alert('삭제에 실패했습니다.');
    }

  } catch (err) {
    console.warn('❌ 삭제 중 예외 발생:', err.message, err);
    //alert('삭제 중 오류가 발생했습니다.');
    setTimeout(() => {
      window.location.herf = '/report/list?ts=' + Date.now();
    }, 500); // 0.5초 정도 딜레이 후 리다이렉트
    return;
  }
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