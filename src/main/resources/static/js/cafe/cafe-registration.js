document.addEventListener('DOMContentLoaded', function () {

  // searchAddress 함수 정의
  function searchAddress() {
    new daum.Postcode({
      oncomplete: function(data) {
        // data.address   : 도로명 주소 또는 지번
        // data.zonecode  : 우편번호(5자리)
        document.getElementById('address').value = data.address;
        document.getElementById('zipCode').value = data.zonecode;
        document.getElementById('detailAddress').focus();
      }
    }).open();
  }

  // "주소검색" 버튼에 클릭 이벤트 바인딩
  const btnAddressSearch = document.getElementById('btnAddressSearch');
  if (btnAddressSearch) {
    btnAddressSearch.addEventListener('click', function(event) {
      event.preventDefault(); // 혹시 폼 내 버튼이라면 form 제출 방지
      searchAddress();
    });
  }

  // 폼 요소들 가져오기
  const form = document.getElementById('cafeForm');
  const descriptionTextarea = document.getElementById('description');
  const charCount = document.getElementById('charCount');
  const imageUpload = document.getElementById('imageUpload');
  const imageInput = document.getElementById('imageInput');
  const imagePreview = document.getElementById('imagePreview');
  const hiddenInput = document.getElementById('businessHoursJson');
  // 글자 수 카운터
  descriptionTextarea.addEventListener('input', function () {
    charCount.textContent = this.value.length;
  });
  // ─────────── 동적 그룹 추가/삭제 로직 ───────────
  const tpl = document.getElementById('tpl-business-hours');
  const container = document.getElementById('business-hours-container');
  const addBtn = document.getElementById('add-business-hours-btn');

  function addGroup() {
    const clone = tpl.content.firstElementChild.cloneNode(true);
    // 삭제 버튼 바인딩 (최소 1개는 남겨둡니다)
    clone.querySelector('.btn-remove-hours')
    .addEventListener('click', () => {
      if (container.children.length > 1) {
        clone.remove();
        reorderNames(); // 삭제 후 인덱스 재정렬
      }
    });
    container.appendChild(clone);
    reorderNames(); // 추가 후 인덱스 재정렬
  }

  // 각 그룹(.business-hours-group) 안의 select · input에
  // 'operationTimes[idx].xxx' 형태로 name 속성을 다시 부여하는 함수
  function reorderNames() {
    container.querySelectorAll('.business-hours-group').forEach((group, idx) => {
      // ① 요일 select
      const weekdaySel = group.querySelector('.weekday-select');
      weekdaySel.setAttribute('name', `operationTimes[${idx}].weekday`);

      // ② openTime input
      const openInput = group.querySelector('.open-time-input');
      openInput.setAttribute('name', `operationTimes[${idx}].openTime`);

      // ③ closeTime input
      const closeInput = group.querySelector('.close-time-input');
      closeInput.setAttribute('name', `operationTimes[${idx}].closeTime`);
    });
  }



  addGroup();                  // 초기 1개 그룹 자동 생성
  addBtn.addEventListener('click', addGroup);
  // ────────────────────────────────────────────────
  // 이미지 업로드 영역 클릭 이벤트
  imageUpload.addEventListener('click', function () {
    imageInput.click();
  });

  // 파일 선택 이벤트
  imageInput.addEventListener('change', function (e) {
    handleFiles(e.target.files);
  });

  // 파일 처리 함수
  function handleFiles(files) {
    Array.from(files).forEach(file => {
      if (file.type.startsWith('image/')) {
        const reader = new FileReader();
        reader.onload = function (e) {
          addImagePreview(e.target.result, file.name);
        };
        reader.readAsDataURL(file);
      }
    });
  }

  // 이미지 미리보기 추가
  function addImagePreview(src, fileName) {
    const previewItem = document.createElement('div');
    previewItem.className = 'preview-item';

    previewItem.innerHTML = `
            <img src="${src}" alt="${fileName}">
            <button type="button" class="remove-image" onclick="removeImage(this)">×</button>
        `;

    imagePreview.appendChild(previewItem);
  }

  // 이미지 제거 함수
  window.removeImage = function (button) {
    button.parentElement.remove();
  };

  // 폼 제출 이벤트 (현재는 화면 테스트용)
  form.addEventListener('submit', function (e) {
    // 동적 그룹별로 businessHours 배열 생성
    // ─── 바뀐 부분: 영업시간 수집 로직 ───
    const businessHours = Array.from(container.children).map(group => {
      const weekday = group.querySelector('.weekday-select').value;
      const openTime = group.querySelector('.open-time-input').value;
      const closeTime = group.querySelector('.close-time-input').value;
      return { weekday, openTime, closeTime };
    });

    // addGroup();

    // ─────────────────────────────────────

    const formData = {
      cafeName: document.getElementById('cafeName').value,
      address: document.getElementById('address').value,
      zipCode: document.getElementById('zipCode').value,
      detailAddress: document.getElementById('detailAddress').value,
      phone: document.getElementById('phone').value,
      description: document.getElementById('description').value,
      businessHours: businessHours,
      imageCount: imagePreview.children.length
    };
    console.log('카페 등록 데이터:', formData);
    alert('카페가 성공적으로 등록되었습니다! (콘솔에서 데이터 확인 가능)');


    hiddenInput.value = JSON.stringify(businessHours);

    // 실제 서버 연동 시 사용할 코드 (현재 주석처리)
    /*
    // FormData 생성
    const serverFormData = new FormData(form);

    // 서버로 전송
    fetch('/cafe/register', {
        method: 'POST',
        body: serverFormData
    })
    .then(response => response.json())
    .then(data => {
        alert('카페가 성공적으로 등록되었습니다!');
        window.location.href = '/cafe/list';
    })
    .catch(error => {
        console.error('Error:', error);
        alert('오류가 발생했습니다.');
    });
    */
  });

  // 취소 버튼 이벤트
  cancelBtn.addEventListener('click', function () {
    if (confirm('작성 중인 내용이 모두 삭제됩니다. 계속하시겠습니까?')) {
      form.reset();
      charCount.textContent = '0';
      imagePreview.innerHTML = '';
      // 영업시간 그룹 리셋: 첫 그룹만 남기고 나머지 제거
      const container = document.getElementById('business-hours-container');
      container.innerHTML = '';
      addGroup();  // 초기 그룹 1개 복원
    }
  })
});

// 데이터베이스 연동 시 사용할 Spring Boot Controller 예시 (현재 주석처리)
/*
@Controller
@RequestMapping("/cafe")
public class CafeController {

    // @Autowired
    // private CafeService cafeService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        // model.addAttribute("cafeForm", new CafeForm());
        return "cafe-form";
    }

    @PostMapping("/register")
    public String registerCafe(@ModelAttribute CafeForm cafeForm,
                              @RequestParam("images") MultipartFile[] images,
                              RedirectAttributes redirectAttributes) {
        try {
            // cafeService.saveCafe(cafeForm, images);
            // redirectAttributes.addFlashAttribute("successMessage", "카페가 성공적으로 등록되었습니다.");
            return "redirect:/cafe/list";
        } catch (Exception e) {
            // redirectAttributes.addFlashAttribute("errorMessage", "오류가 발생했습니다.");
            return "redirect:/cafe/register";
        }
    }
}

// DTO 클래스 예시
public class CafeForm {
    private String cafeName;
    private String address;
    private String phone;
    private String description;
    private String startHour;
    private String startMinute;
    private String startPeriod;
    private String endHour;
    private String endMinute;
    private String endPeriod;

    // getters and setters...
}
*/