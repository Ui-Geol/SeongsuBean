const validationState={
  passwordMatch: false,
  nicknameValid: false
};

function checkNickname(){
  const feedback = document.getElementById("nickname-feedback");
  const nickname = document.getElementById("nickname").value.trim();
  if(!nickname){
    validationState.nicknameValid = false;
    feedback.textContent = "";
    return;
  }

  fetch(`/api/account/checkNickname?nickname=${encodeURIComponent(nickname)}`)
  .then(res => {
    if (!res.ok) throw new Error("서버 응답 오류");
    return res.json();
  })
  .then(data =>{
    if(data.exists){
      feedback.textContent= "이미 사용 중인 닉네임입니다.";
      validationState.nicknameValid = false;
    } else{
      feedback.textContent = "";
      validationState.nicknameValid = true;
    }
  })
  .catch(() =>{
    feedback.textContent = "오류가 발생했습니다.";
    validationState.nicknameValid = false;
  });
}

function checkPassword(){
  const password = document.getElementById("password").value;
  const confirm = document.getElementById("passwordConfirm").value;
  const feedback = document.getElementById("password-feedback");

  if(password && confirm && password !== confirm){
    feedback.textContent = "비밀번호가 일치하지 않습니다.";
    validationState.passwordMatch = false;
  }else{
    feedback.textContent = "";
    validationState.passwordMatch = true;
  }
}

function checkEmpty(event){
  const password = document.getElementById("password").value.trim();
  const confirm = document.getElementById("passwordConfirm").value.trim();
  const nickname = document.getElementById("nickname").value.trim();

  if(!password && !confirm && !nickname){
    alert("수정을 원하는 정보를 입력해주세요.");
    event.preventDefault();
    return;
  }

  if (nickname && !validationState.nicknameValid) {
    alert("닉네임이 중복입니다.");
    event.preventDefault();
    return;
  }

  if ((password || confirm) && !validationState.passwordMatch) {
    alert("비밀번호가 일치하지 않습니다.");
    event.preventDefault();
    return;
  }
}

document.addEventListener("DOMContentLoaded", () => {
  // 기존 nickname, password 체크 등등...

  const deleteBtn = document.querySelector(".delete-btn");
  deleteBtn.addEventListener("click", () => {
    if (!confirm("정말로 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.")) return;

    const email = document.getElementById("email").value;

    fetch(`/api/account/deleteAccount?${encodeURIComponent(email)}`, {
      method: "DELETE",
    })
    .then(res => {
      if (res.ok) {
        alert("회원 탈퇴가 완료되었습니다.");
        window.location.href = "/map";  // 홈 또는 로그인 페이지로 이동
      } else {
        return res.text().then(msg => { throw new Error(msg); });
      }
    })
    .catch(err => {
      console.error(err);
      alert("회원 탈퇴 중 오류가 발생했습니다.");
    });
  });
});

document.addEventListener("DOMContentLoaded", () =>{
  document.getElementById("nickname").addEventListener("blur",checkNickname);
  document.getElementById("password").addEventListener("blur",checkPassword);
  document.getElementById("passwordConfirm").addEventListener("blur",checkPassword);
  document.querySelector("form").addEventListener("submit", checkEmpty);
});
