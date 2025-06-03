document.addEventListener('DOMContentLoaded', function () {

  // ─────────────────────────────────────────────────────────────────
  // ① 24시간 문자열 → 12시간+AM/PM 객체 변환 함수
  function to12Hour(time24) {
    const parts = time24.split(':');
    if (parts.length !== 2) {
      // "HH:MM" 형식이 아닐 경우 기본값 반환 (혹은 에러 처리)
      return { hour: '00', minute: '00', period: 'AM' };
    }

    let h = parseInt(parts[0], 10);
    const m = parts[1];
    const period = h >= 12 ? 'PM' : 'AM';
    if (h === 0) {
      h = 12;
    } else if (h > 12) {
      h -= 12;
    }
    // 두 자리 문자열로 포맷
    const hh = String(h).padStart(2, '0');

    // ↓ 축약표현 대신 명시적 키:값 사용
    return {
      hour:   hh,
      minute: m,
      period: period
    };
  }
  // ─────────────────────────────────────────────────────────────────


  // ─────────────────────────────────────────────────────────────────
  // ② 필수 DOM 요소 참조 (ID/클래스 이름이 HTML과 일치해야 합니다)
  const form                   = document.getElementById('cafeForm');
  const businessContainer      = document.getElementById('business-hours-container');
  const addHoursBtn            = document.getElementById('add-business-hours-btn');
  const hiddenInput            = document.getElementById('businessHoursJson');

  const imagePreview           = document.getElementById('imagePreview');
  const cancelBtn              = document.getElementById('cancelBtn');
  const imageUpload            = document.getElementById('imageUpload');
  const imageInput             = document.getElementById('imageInput');
  const btnAddressSearch       = document.getElementById('btnAddressSearch');
  // ─────────────────────────────────────────────────────────────────


  // ─────────────────────────────────────────────────────────────────
  // ③ 옵션 배열 정의 (HTML의 <select>에 JS에서 채울 예정)
  const HOURS   = Array.from({ length: 12 }, (_, i) => String(i + 1).padStart(2, '0'));
  const MINUTES = Array.from({ length: 60 }, (_, i) => String(i).padStart(2, '0'));
  const PERIODS = ['AM', 'PM'];
  // ─────────────────────────────────────────────────────────────────


  // ─────────────────────────────────────────────────────────────────
  // ④ select에 옵션을 채워주는 헬퍼 함수
  function fillSelectOptions(selectEl, optionsArray) {
    optionsArray.forEach(val => {
      const opt = document.createElement('option');
      opt.value = val;
      opt.textContent = val;
      selectEl.appendChild(opt);
    });
  }
  // ─────────────────────────────────────────────────────────────────


  // ─────────────────────────────────────────────────────────────────
  // ⑤ 현재 로컬 시·분·AMPM 반환
  function getLocalTimeParts() {
    const now = new Date();
    let h24 = now.getHours();
    const m = String(now.getMinutes()).padStart(2, '0');
    const period = h24 >= 12 ? 'PM' : 'AM';
    if (h24 === 0) {
      h24 = 12;
    } else if (h24 > 12) {
      h24 -= 12;
    }
    const h12 = String(h24).padStart(2, '0');
    return { hour: h12, minute: m, period: period };
  }
  // ─────────────────────────────────────────────────────────────────


  // ─────────────────────────────────────────────────────────────────
  // ⑥ “영업시간 그룹” 하나를 생성하여 컨테이너에 추가하는 함수
  function addBusinessHoursGroup() {
    const tpl = document.getElementById('tpl-business-hours');
    if (!tpl) {
      console.error('템플릿(id="tpl-business-hours")를 찾을 수 없습니다.');
      return;
    }
    const clone = tpl.content.firstElementChild.cloneNode(true);

    // (1) 복제된 그룹 내의 select 요소들 가져오기
    const hourSelects   = clone.querySelectorAll('.hour-select');
    const minuteSelects = clone.querySelectorAll('.minute-select');
    const periodSelects = clone.querySelectorAll('.period-select');

    // (2) 옵션 채우기: 시, 분, AM/PM
    hourSelects.forEach(el => fillSelectOptions(el, HOURS));
    minuteSelects.forEach(el => fillSelectOptions(el, MINUTES));
    periodSelects.forEach(el => fillSelectOptions(el, PERIODS));

    // (3) 현재 로컬 시간으로 기본 선택값 설정
    const { hour, minute, period } = getLocalTimeParts();
    hourSelects.forEach(el => { el.value = hour; });
    minuteSelects.forEach(el => { el.value = minute; });
    periodSelects.forEach(el => { el.value = period; });

    // (4) “삭제” 버튼 로직: 그룹이 1개 남으면 삭제 불가
    const removeBtn = clone.querySelector('.btn-remove-hours');
    removeBtn.addEventListener('click', function () {
      if (businessContainer.children.length > 1) {
        clone.remove();
      } else {
        alert('영업시간 그룹은 최소 하나가 있어야 합니다.');
      }
    });

    // (5) 컨테이너에 붙이기
    businessContainer.appendChild(clone);
  }

  // 최초 1개 그룹 생성 + “추가” 버튼 이벤트 연결
  addBusinessHoursGroup();
  addHoursBtn.addEventListener('click', addBusinessHoursGroup);
  // ─────────────────────────────────────────────────────────────────


  // ─────────────────────────────────────────────────────────────────
  // ⑦ 주소 검색 (기존 코드 유지)
  function searchAddress() {
    new daum.Postcode({
      oncomplete: function(data) {
        document.getElementById('address').value     = data.address;
        document.getElementById('zipCode').value     = data.zonecode;
        document.getElementById('detailAddress').focus();
      }
    }).open();
  }
  if (btnAddressSearch) {
    btnAddressSearch.addEventListener('click', function(event) {
      event.preventDefault();
      searchAddress();
    });
  }
  // ─────────────────────────────────────────────────────────────────


  // ─────────────────────────────────────────────────────────────────
  // ⑧ 이미지 업로드 / 미리보기 / 삭제 (기존 코드 그대로 유지)
  imageUpload.addEventListener('click', function () {
    imageInput.click();
  });
  imageInput.addEventListener('change', function (e) {
    handleFiles(e.target.files);
  });
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
  function addImagePreview(src, fileName) {
    const previewItem = document.createElement('div');
    previewItem.className = 'preview-item';
    previewItem.innerHTML = `
      <img src="${src}" alt="${fileName}">
      <button type="button" class="remove-image" onclick="removeImage(this)">×</button>
    `;
    imagePreview.appendChild(previewItem);
  }
  window.removeImage = function (button) {
    button.parentElement.remove();
  };
  // ─────────────────────────────────────────────────────────────────


  // ─────────────────────────────────────────────────────────────────
  // ⑨ 폼 제출 이벤트: 모든 그룹을 순회하여 JSON 생성 → 숨겨진 input에 채워넣기
  form.addEventListener('submit', function (e) {
    const businessHoursArray = Array.from(businessContainer.children).map(group => {
      const weekdayEl    = group.querySelector('.weekday-select');
      const hourEls      = group.querySelectorAll('.hour-select');
      const minuteEls    = group.querySelectorAll('.minute-select');
      const periodEls    = group.querySelectorAll('.period-select');

      return {
        weekday:     weekdayEl.value,
        startHour:   hourEls[0].value,
        startMinute: minuteEls[0].value,
        startPeriod: periodEls[0].value,
        endHour:     hourEls[1].value,
        endMinute:   minuteEls[1].value,
        endPeriod:   periodEls[1].value
      };
    });

    // 필수 입력값 검사
    for (let obj of businessHoursArray) {
      if (!obj.weekday ||
          !obj.startHour || !obj.startMinute || !obj.startPeriod ||
          !obj.endHour   || !obj.endMinute   || !obj.endPeriod) {
        alert('모든 영업시간 그룹에서 요일·시·분·AM/PM을 모두 선택해주세요.');
        e.preventDefault();
        return;
      }
    }

    hiddenInput.value = JSON.stringify(businessHoursArray);

    console.log({
      cafeName:       document.getElementById('cafeName').value,
      address:        document.getElementById('address').value,
      zipCode:        document.getElementById('zipCode').value,
      detailAddress:  document.getElementById('detailAddress').value,
      phone:          document.getElementById('phone').value,
      description:    document.getElementById('description').value,
      businessHours:  businessHoursArray,
      imageCount:     imagePreview.children.length
    });
  });
  // ─────────────────────────────────────────────────────────────────


  // ─────────────────────────────────────────────────────────────────
  // ⑩ 취소 버튼 클릭 시 초기화
  cancelBtn.addEventListener('click', function () {
    if (confirm('작성 중인 내용이 모두 삭제됩니다. 계속하시겠습니까?')) {
      form.reset();
      imagePreview.innerHTML = '';
      hiddenInput.value = '';

      while (businessContainer.children.length > 1) {
        businessContainer.removeChild(businessContainer.lastElementChild);
      }
    }
  });
  // ─────────────────────────────────────────────────────────────────

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