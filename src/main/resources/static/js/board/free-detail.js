let freeBoardId = null;

document.addEventListener('DOMContentLoaded', async () => {
    const pathParts = window.location.pathname.split('/');
    freeBoardId = pathParts[pathParts.length - 1] || pathParts[pathParts.length - 2];

    const deleteBtn = document.getElementById('delete-btn');
    if (deleteBtn) {
        deleteBtn.removeEventListener('click', deleteHandler);
        deleteBtn.addEventListener('click', deleteHandler);
    }

    const editBtn = document.getElementById('edit-btn');
    if (editBtn) {
        editBtn.addEventListener('click', () => {
            if (!freeBoardId) {
                alert('잘못된 게시글입니다. 수정할 수 없습니다.');
                return;
            }
            window.location.href = `/free/set/${freeBoardId}`;
        });
    }

    try {
        const res = await fetch(`/api/free/${freeBoardId}`);
        if (!res.ok) throw new Error(`서버 오류: ${res.status}`);

        const data = await res.json();

        document.getElementById('post-title').textContent = data.title;
        document.getElementById('author-name').textContent = data.nickName;
        document.getElementById('post-date').textContent = formatDate(data.createdDate);
        document.getElementById('post-content').innerHTML = data.content;

        const imageContainer = document.getElementById('image-container');
        imageContainer.innerHTML = '';
        if (data.images && Array.isArray(data.images) && data.images.length > 0) {
            data.images .forEach((img) => {
                const imgTag = document.createElement("img");
                imgTag.src = img;
                imgTag.alt = "게시글 이미지";
                imgTag.classList.add("post-image");
                imageContainer.appendChild(imgTag);
            });
        }

        const avatarEl = document.getElementById('author-avatar');
        avatarEl.innerHTML = '';
        if (data.profileImage) {
            const img = document.createElement("img");
            img.src = data.profileImage;
            img.alt = "프로필 이미지";
            img.classList.add("avatar-img");
            avatarEl.appendChild(img);
        } else {
            avatarEl.classList.add("default");
            avatarEl.innerHTML = `<img src="/images/board/SampleProfile.png" class="avatar-img">`;
        }
    } catch (err) {
        console.error("게시글 불러오기 실패:", err);
        alert("게시글을 불러오는 데 실패했습니다.");
    }

    loadComments();

    const cancelBtn = document.getElementById("cancel-btn");
    if (cancelBtn) {
        cancelBtn.addEventListener("click", () => {
            window.location.href = "/free/list";
        });
    }
});

async function deleteHandler() {
    if (!freeBoardId) {
        alert("잘못된 게시글입니다.");
        return;
    }

    if (!confirm("정말 삭제하시겠습니까?")) return;

    try {
        const res = await fetch(`/api/free/${freeBoardId}`, {
            method: "DELETE"
        });

        let result = {};
        try {
            result = await res.json();
        } catch (e) {
            console.warn("JSON 파싱 실패", e);
        }

        if (res.ok && result.deleted) {
            alert("삭제되었습니다.");
            setTimeout(() => {
                window.location.href = "/free/list?ts=" + Date.now();
            }, 500);
        } else {
            alert("삭제에 실패했습니다.");
        }
    } catch (err) {
        console.warn("삭제 중 오류:", err);
        setTimeout(() => {
            window.location.href = "/free/list?ts=" + Date.now();
        }, 500);
    }
}

function formatDate(dateStr) {
    const date = new Date(dateStr);
    return date.toLocaleDateString("ko-KR", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit"
    });
}

function escapeHtml(str) {
    return str?.replace(/</g, "&lt;").replace(/>/g, "&gt;") || '';
}

// 💬 댓글 처리 함수 분리
function loadComments() {
    fetch(`/api/free/comment/${freeBoardId}`)
        .then(res => res.json())
        .then(comments => {
            const commentList = document.querySelector(".comment-list");
            commentList.innerHTML = "";

            comments.forEach(comment => {
                const avatarUrl = comment.profileImage || "/images/board/SampleProfile.png";
                const createdDate = new Date(comment.createdDate).toLocaleString("ko-KR");

                const item = document.createElement("div");
                item.className = "comment-item";
                item.innerHTML = `
          <div class="comment-avatar">
            <img src="${avatarUrl}" alt="프로필 이미지" class="avatar-img">
          </div>
          <div class="comment-content">
            <div class="comment-header">
              <span class="comment-author">${comment.nickName}</span>
              <span class="comment-date">${createdDate}</span>
            </div>
            <div class="comment-text">${comment.content}</div>
            <div class="comment-actions">
              <button class="comment-delete-btn" data-id="${comment.commentId}">삭제하기</button>
            </div>
          </div>
        `;
                commentList.appendChild(item);
            });

            document.querySelectorAll(".comment-delete-btn").forEach(btn => {
                btn.addEventListener("click", function () {
                    const commentId = this.dataset.id;
                    if (confirm("댓글을 삭제하시겠습니까?")) {
                        fetch(`/api/free/comment/${commentId}`, {
                            method: "DELETE"
                        })
                            .then(res => res.json())
                            .then(data => {
                                if (data.success) {
                                    alert("댓글이 삭제되었습니다.");
                                    location.reload();
                                } else {
                                    alert("삭제 실패");
                                }
                            })
                            .catch(err => {
                                console.error("삭제 오류:", err);
                                alert("삭제 중 오류 발생");
                            });
                    }
                });
            });
        });
}

// 💬 댓글 등록
document.querySelector(".comment-submit").addEventListener("click", () => {
    const content = document.querySelector(".comment-input").value.trim();
    const email = sessionStorage.getItem("email");

    if (!content) {
        alert("댓글을 입력해주세요.");
        return;
    }

    fetch("/api/free/comment", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ content, email, freeBoardId })
    })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                alert("댓글이 등록되었습니다.");
                location.reload();
            } else {
                alert("등록 실패");
            }
        })
        .catch(err => {
            console.error("댓글 등록 실패:", err);
            alert("댓글 등록 중 오류 발생");
        });
});
