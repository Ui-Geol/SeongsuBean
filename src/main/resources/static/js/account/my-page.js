document.getElementById('profile-upload').addEventListener('change', function () {
  const file = this.files[0];
  const formData = new FormData();
  formData.append("file", file);

  fetch("/api/account/uploadImage", {
    method: "PUT",
    body: formData
  }).then(response => {
    if (response.ok) {
      // 미리보기 이미지 변경
      const reader = new FileReader();
      reader.onload = function (e) {
        document.getElementById('profile-img').src = e.target.result;
      };
      reader.readAsDataURL(file);
    } else {
      alert("업로드 실패");
    }
  });
});