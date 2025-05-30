let reportId = null;

document.addEventListener('DOMContentLoaded', async () => {
  reportId = window.location.pathname.split('/').pop();

  try {
    const response = await fetch(`/api/report/${reportId}`);
    const data = await response.json();

    document.getElementById('post-title').textContent = data.title;
    document.getElementById('author-name').textContent = data.email;
    document.getElementById('post-date').textContent = formatDate(data.createdDate);
    document.getElementById('post-content').innerHTML = data.content;

    if (data.images && data.images.length > 0) {
      const imageTag = document.getElementById('post-image');
      imageTag.src = `/images/board/${data.images[0]}`;
      imageTag.alt = '게시글 이미지';
    }
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

// 아래 이벤트 핸들러에서도 정상 사용 가능
document.getElementById('delete-btn').addEventListener('click', async () => {
  if (!reportId) {
    alert('잘못된 게시글입니다. 삭제할 수 없습니다.');
    return;
  }

  if (confirm('정말 삭제하시겠습니까?')) {
    try {
      const res = await fetch(`/api/report/${reportId}`, {
        method: 'DELETE'
      });

      const result = await res.json();
      if (result.deleted) {
        alert('삭제되었습니다.');
        window.location.href = '/board/report-list';
      } else {
        alert('삭제에 실패했습니다.');
      }
    } catch (err) {
      console.error('삭제 오류:', err);
      alert('삭제 중 오류가 발생했습니다.');
    }
  }
});

document.getElementById('edit-btn').addEventListener('click', () => {
  if (!reportId) {
    alert('잘못된 게시글입니다. 수정할 수 없습니다.');
    return;
  }

  window.location.href = `/report/post/${reportId}`;
});
