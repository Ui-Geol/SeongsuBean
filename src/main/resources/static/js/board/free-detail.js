document.querySelector(".comment-submit").addEventListener("click", function () {
  const content = document.querySelector(".comment-input").value;
  const email = sessionStorage.getItem("email");
  const freeBoardId = new URLSearchParams(location.search).get("id");

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
  });
});
