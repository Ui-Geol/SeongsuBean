// 즉시 실행 함수로 스코프 격리
(() => {

  // 2. Kakao 지도 생성 (중심 좌표 및 줌 레벨 설정)
  const mapContainer = document.getElementById('map');
  const mapOption = {
    center: new kakao.maps.LatLng(37.5665, 126.9780),
    level: 6,
    draggable: false,
    zoomable: false
  };
  const map = new kakao.maps.Map(mapContainer, mapOption);

  // 2-1) Geocoder 인스턴스 생성 (주소 → 좌표 변환)
  //    어떤 방식으로든 줌 레벨이 바뀌지 않습니다.
  map.setMinLevel(5);    // :contentReference[oaicite:2]{index=2}
  map.setMaxLevel(5);    // :contentReference[oaicite:3]{index=3}
  const geocoder = new kakao.maps.services.Geocoder();

  // 3. SeongsuBean 일러스트 및 커스텀 마커 삽입 (map.js 통합 부분)
  geocoder.addressSearch('서울특별시 성동구 성수일로 56', (result, status) => {
    if (status === kakao.maps.services.Status.OK) {
      const coords = new kakao.maps.LatLng(result[0].y, result[0].x);

      // 커스텀 오버레이 (빈 오버레이)
      const beanOverlay = new kakao.maps.CustomOverlay({
        position: coords,
        yAnchor: 1,
        zIndex: 2
      });
      beanOverlay.setMap(map);

      // 지도 중심 이동
      map.setCenter(coords);

      // 일러스트 오버레이
      const illustration = new kakao.maps.CustomOverlay({
        position: coords,
        content: '<img src="/images/common/SeongsuBean.png" width="1200" height="500" style="pointer-events: none;">',
        xAnchor: 0.5,
        yAnchor: 0.5,
        zIndex: 0
      });
      illustration.setMap(map);
    } else {
      console.warn('일러스트 실패:', status);
    }
  });

  // ───────────────────────────────────────────────────────────────────
  // ① Top5 가져오기 + 마커 추가
  function fetchTop5() {
    fetch('/api/search/top5')
    .then(res => res.json())
    .then(list => {
      clearMarkers();
      list.forEach(addr => addMarkerByAddress(addr));
    })
    .catch(console.error);
  }

  document.getElementById('crownBtn')
  .addEventListener('click', fetchTop5);

  // 아이콘 클릭 리스너
  document.querySelectorAll('#filterIcons .filter-icon-img')
  .forEach(img => {
    img.addEventListener('click', () => {
      const menuName = img.dataset.menu;
      clearMarkers();
      fetchCafeAddresses(menuName);
    });
  });

  // 4. 지도에 표시된 마커 객체들을 저장할 배열
  let markers = [];

  // 마커 전체 삭제
  function clearMarkers() {
    markers.forEach(m => m.setMap(null));
    markers = [];
  }

  // 3) 주소 → 위경도 변환 → 마커 생성
  function addMarkerByAddress(address) {
    console.log('[addMarkerByAddress] 검색:', address);
    geocoder.addressSearch(address, (result, status) => {
      if (status === kakao.maps.services.Status.OK && result[0]) {
        const {y: lat, x: lng} = result[0];
        const pos = new kakao.maps.LatLng(lat, lng);

        const marker = new kakao.maps.Marker({
          map: map,
          position: pos
        });
        markers.push(marker);
      }
    });
  }

  function fetchCafeAddresses(menuName) {
    console.log('[fetchCafeAddresses] 요청하는 메뉴:', menuName);
    fetch(`/api/search-by-menu?menuName=${encodeURIComponent(menuName)}`)
    .then(res => res.json())
    .then(addressList => {
      console.log('[fetchCafeAddresses] 받은 데이터:', addressList);
      if (!Array.isArray(addressList)) {
        return;
      }
      clearMarkers();
      addressList.forEach(addr => {
        const addrs = Array.isArray(addr) ? addr : [addr];
        addrs.forEach(a => addMarkerByAddress(a));
      });
    })
    .catch(err => console.error(err));
  }

  // ───────────────────────────────────────────────────────────────────
  // 카드 뷰 로직
  const ROW_SIZE = 4;      // 한 줄에 보여줄 카드 수
  let currentIndex = 0;    // 다음에 렌더링할 데이터 시작 인덱스
  let cafes = [];          // ← 여기에 API로 받은 데이터가 들어갑니다

  const wrapper = document.getElementById('cards-wrapper');
  const btn = document.getElementById('load-more');

  // ─── 1. API 호출: cafes에 데이터 채우고 첫 줄 렌더링 ───
  fetch('/api/main/cards')
  .then(res => res.json())
  .then(data => {
    console.log('API 응답:', data);
    cafes = data;
    btn.style.display = cafes.length > ROW_SIZE ? 'block' : 'none';
    renderRow();               // 첫 ROW_SIZE개 렌더링
    // 더 로드 버튼 이벤트 바인딩 (한 번만)
    btn.addEventListener('click', renderRow);
  })
  .catch(err => console.error('메인 카드 로딩 실패', err));

  // 카드 한 줄(row) 렌더링 함수
  function renderRow() {
    if (currentIndex >= cafes.length) {
      btn.style.display = 'none';
      return;
    }
    const row = document.createElement('div');
    row.className = 'card-row';

    // ROW_SIZE 개씩 자르고 남으면 남은 개수만큼
    const slice = cafes.slice(currentIndex, currentIndex + ROW_SIZE);

    slice.forEach(cafe => {
      const card = document.createElement('div');
      card.className = 'card';

      card.dataset.cafeId = cafe.cafeId;
      card.innerHTML = `
                <img src="${cafe.mainImage}" alt="${cafe.cafeName}">
                <div class="info">
                    <h4>${cafe.cafeName}</h4>
                    <p>${cafe.introduction || ''}</p>
                </div>
            `;

      card.addEventListener('click', () => {
        const cafeId = card.dataset.cafeId;
        // /cafes/{cafeId} 로 이동해서 서버 측에서 Thymeleaf 페이지(카페 상세) 렌더링하게 함
        window.location.href = `/cafe/${cafeId}`;
      });

      row.appendChild(card);
    });

    wrapper.appendChild(row);
    currentIndex += ROW_SIZE;

    // 더 이상 남는 데이터 없으면 버튼 숨김
    if (currentIndex >= cafes.length) {
      btn.style.display = 'none';
    }
  }

  // 초기 한 줄 렌더링
  renderRow();

  // 버튼 클릭 시 다음 줄 렌더링 (중복 방지 차원에서 두 번 바인딩되어도 무방)
  btn.addEventListener('click', renderRow);

  // ───────────────────────────────────────────────────────────────────
  // ▶ [추가] 받은 카페 상세 정보를 화면에 그리는 함수
  function renderCafeDetail(cafe) {
    const detailSection = document.getElementById('detail-section');
    // 1) 기존 내용을 모두 지운다
    detailSection.innerHTML = '';
    // 2) 새로운 DOM 생성
    const container = document.createElement('div');
    container.className = 'cafe-detail-card';
    detailSection.appendChild(container);
  }

  // ───────────────────────────────────────────────────────────────────
  /*
   * 키워드로 통합 검색 → 주소 리스트 반환 → 카카오 지오코딩 → 마커 표시
   */
  function searchByKeyword(keyword) {
    console.log('[searchByKeyword] 요청 키워드:', keyword);
    fetch(`/api/search?keyword=${encodeURIComponent(keyword)}`)
    .then(res => res.json())
    .then(addressList => {
      if (!Array.isArray(addressList) || addressList.length === 0) {
        alert('검색 결과가 없습니다.');
        return;
      }
      clearMarkers();
      addressList.forEach(addr => {
        // addr 은 "주소 + 상세주소" 형태의 문자열
        addMarkerByAddress(addr);
      });
    })
    .catch(err => {
      console.error('[searchByKeyword] 에러:', err);
      alert('검색 중 오류가 발생했습니다.');
    });
  }

  // 7. 검색창 이벤트 핸들러
  const input = document.getElementById('searchInput');
  const searchbtn = document.getElementById('searchBtn');

  searchbtn.addEventListener('click', () => {
    const keyword = input.value.trim();
    if (keyword) {
      searchByKeyword(keyword);
    }
  });
  input.addEventListener('keydown', e => {
    if (e.key === 'Enter') {
      const keyword = input.value.trim();
      if (keyword) {
        searchByKeyword(keyword);
      }
    }
  });

  // 8. 카페 등록 버튼 클릭 시 이동
  const registerBtn = document.getElementById('cafe-registration');
  if (registerBtn) {
    registerBtn.addEventListener('click', () => {
      window.location.href = '/cafe-registration';
    });
  }

  // ... 이하 place 리스트에 대한 showMarkers, filterByKeyword, filterByCategory, filterSpecial 등 기존 map1.js 함수 유지 ...

})(); // IIFE 끝

