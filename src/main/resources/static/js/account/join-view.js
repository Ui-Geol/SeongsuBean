const validationState = {
  emailValid: false,
  nicknameValid: false,
  passwordMatch: false
};
// 이메일 중복 체크
function checkEmail() {
  const emailInput = document.getElementById('email');
  const feedback = document.getElementById('email-feedback');
  const email = emailInput.value.trim();
  if (!email) {
    feedback.textContent = '';
    validationState.emailValid = false;
    return;
  }

  fetch(`/api/account/checkEmail?email=${encodeURIComponent(email)}`)
  .then(res => {
    if (!res.ok) throw new Error("서버 응답 오류");
    return res.json();
  })
  .then(data => {
    if (data.exists) {
      feedback.textContent = "이미 가입된 이메일입니다.";
      validationState.emailValid = false;
    } else {
      feedback.textContent = "";
      validationState.emailValid = true;
    }
  })
  .catch(() => {
    feedback.textContent = "오류가 발생했습니다.";
    validationState.emailValid = false;
  });
}

// 닉네임 중복 체크
function checkNickname() {
  const nicknameInput = document.getElementById('nickname');
  const feedback = document.getElementById('nickname-feedback');
  const nickname = nicknameInput.value.trim();

  if (!nickname) {
    feedback.textContent = '';
    validationState.nicknameValid = false;
    return;
  }

  fetch(`/api/account/checkNickname?nickname=${encodeURIComponent(nickname)}`)
  .then(res => {
    if (!res.ok) throw new Error("서버 응답 오류");
    return res.json();
  })
  .then(data => {
    if (data.exists) {
      feedback.textContent = "이미 사용 중인 닉네임입니다.";
      validationState.nicknameValid = false;
    } else {
      feedback.textContent = "";
      validationState.nicknameValid = true;
    }
  })
  .catch(() => {
    feedback.textContent = "오류가 발생했습니다.";
    validationState.nicknameValid = false;
  });
}

// 비밀번호 일치 확인
function checkPasswordMatch() {
  const password = document.getElementById('password').value;
  const confirm = document.getElementById('password-confirm').value;
  const feedback = document.getElementById('password-feedback');

  if (password && confirm && password !== confirm) {
    feedback.textContent = "비밀번호가 일치하지 않습니다.";
    validationState.passwordMatch = false;
  } else {
    feedback.textContent = "";
    validationState.passwordMatch = true;
  }
}

function checkJoin(event) {
    const email = document.getElementById('email').value.trim();
    const nickname = document.getElementById('nickname').value.trim();
    const password = document.getElementById('password').value.trim();
    const passwordConfirm = document.getElementById('password-confirm').value.trim();
    const birthdate = document.getElementById('birthdate').value.trim();
    const phone = document.getElementById('phone').value.trim();
    const serviceTerms = document.getElementById('service-terms').checked;
    const privacyTerms = document.getElementById('privacy-terms').checked;

  if (!email || !nickname || !password || !passwordConfirm || !birthdate || !phone) {
    alert('모든 필수 정보를 입력해주세요.');
    event.preventDefault();
    return;
  }

  if (!validationState.emailValid) {
    alert('이메일 중복 확인을 통과하지 못했습니다.');
    event.preventDefault();
    return;
  }

  if (!validationState.nicknameValid) {
    alert('닉네임 중복 확인을 통과하지 못했습니다.');
    event.preventDefault();
    return;
  }

  if (!validationState.passwordMatch) {
    alert('비밀번호 확인이 일치하지 않습니다.');
    event.preventDefault();
    return;
  }

  if (!serviceTerms || !privacyTerms) {
    alert('모든 약관에 동의해야 가입할 수 있습니다.');
    event.preventDefault();
    return;
  }
    // 통과 시 submit 허용
}

// 이벤트 연결
document.addEventListener('DOMContentLoaded', () => {
  document.getElementById('email').addEventListener('blur', checkEmail);
  document.getElementById('nickname').addEventListener('blur', checkNickname);
  document.getElementById('password-confirm').addEventListener('blur', checkPasswordMatch);
  document.getElementById('password').addEventListener('blur', checkPasswordMatch);
  document.querySelector('form').addEventListener('submit', checkJoin);
});