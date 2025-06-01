let currentPage = 1;
const pageSize = 12;

document.addEventListener("DOMContentLoaded", function () {
  fetch(`/api/free/list?page=${currentPage}&size=${pageSize}`)
      .then(response => response.json())
      .then(data => {
        const row = document.getElementById("card-row");
        row.innerHTML = ""; // 기존 내용 제거

        if (data.length === 0) {
          row.innerHTML = "<p>등록된 게시글이 없습니다.</p>";
          return;
        }

        data.forEach(item => {
          const card = document.createElement("div");
          card.className = "col-md-4";

          const imagePath = item.thumbnailImage
              ? (item.thumbnailImage.startsWith("/")
                  ? item.thumbnailImage
                  : `/images/board/${item.thumbnailImage}`)
              : '/images/board/default.png';

          const authorName = item.nickName || '익명';

          card.innerHTML = `
          <div class="card" style="cursor: pointer;" onclick="location.href='/free/detail/${item.freeBoardId}'">
            <img src="${imagePath}" class="card-img-top" alt="게시글 이미지">
            <div class="card-body px-0">
              <h5 class="card-title">${item.title}</h5>
              <div class="d-flex justify-content-between">
                <span class="card-text">${authorName}</span>
                <span class="card-text">${formatDate(item.createdDate)}</span>
              </div>
            </div>
          </div>
        `;

          row.appendChild(card);
        });
      })
      .catch(err => {
        console.error("게시글 불러오기 실패", err);
        alert("게시글을 불러오지 못했습니다.");
      });
});

function formatDate(dateStr) {
  const date = new Date(dateStr);
  return date.toISOString().split("T")[0];
}
