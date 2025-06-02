document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById("passwordVerificationForm");
  const error = document.getElementById("passwordError");

  form.addEventListener("submit", async (e) =>{
    e.preventDefault();

    const password = document.getElementById("currentPassword").value;

    const response = await fetch("/api/account/checkPw",{
      method : "POST",
      headers : {
        "Content-Type" : "application/json"
      },
      body : JSON.stringify({password})
    });

    const result = await response.json();

    if(result.success){
      window.location.href = "/account/editProfile";
    }else{
      error.textContent = "비밀번호가 일치하지 않습니다";
      error.style.display = "block";
    }
  });
});