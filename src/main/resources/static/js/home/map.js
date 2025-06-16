// 즉시 실행 함수로 스코프 격리
(() => {
  let retryCount = 0;
  const MAX_RETRIES = 50; // 최대 5초 대기 (100ms * 50)

  // — 공통 헤더·푸터 포함 (axios) —
  axios.get('/common/header.html')
      .then(res => {
        const el = document.getElementById('header');
        if (el) el.innerHTML = res.data;
      })
      .catch(err => console.error('header load error:', err));

  axios.get('/common/footer.html')
      .then(res => {
        const el = document.getElementById('footer');
        if (el) el.innerHTML = res.data;
      })
      .catch(err => console.error('footer load error:', err));

  // Kakao Maps API 로드 확인 함수
  function isKakaoMapsReady() {
    return typeof kakao !== 'undefined' &&
        kakao.maps &&
        kakao.maps.Map &&
        kakao.maps.services &&
        kakao.maps.services.Geocoder;
  }

  // Kakao Maps API 로드 대기 후 실행
  function initKakaoMap() {
    if (!isKakaoMapsReady()) {
      console.error('Kakao Maps API가 완전히 로드되지 않았습니다.');
      return false;
    }

    try {
      console.log('initMap called');

      // 2. Kakao 지도 생성 (중심 좌표 및 줌 레벨 설정)
      const mapContainer = document.getElementById('map');
      if (!mapContainer) {
        console.error('지도 컨테이너를 찾을 수 없습니다.');
        return false;
      }

      const mapOption = {
        center: new kakao.maps.LatLng(37.5665, 126.9780),
        level: 6,
        draggable: false,
        zoomable: false
      };
      const map = new kakao.maps.Map(mapContainer, mapOption);

      // 2-1) Geocoder 인스턴스 생성 (주소 → 좌표 변환)
      // 어떤 방식으로든 줌 레벨이 바뀌지 않습니다.
      map.setMinLevel(5);
      map.setMaxLevel(5);
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
            content: '<img src="/images/common/SeongsuBean.png" width="1248" height="500" style="pointer-events: none;">',
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
        axios.get('/api/search/top5')
            .then(res => {
              const list = res.data;
              clearMarkers();
              list.forEach(addr => addMarkerByAddress(addr));
            })
            .catch(console.error);
      }


      const crownBtn = document.getElementById('crownBtn');
      if (crownBtn) {
        crownBtn.addEventListener('click', fetchTop5);
      }

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
        axios.get(`/api/search-by-menu?menuName=${encodeURIComponent(menuName)}`)
            .then(res => {
              const addressList = res.data;
              console.log('[fetchCafeAddresses] 받은 데이터:', addressList);
              if (!Array.isArray(addressList)) return;
              clearMarkers();
              addressList.forEach(addr => {
                const addrs = Array.isArray(addr) ? addr : [addr];
                addrs.forEach(a => addMarkerByAddress(a));
              });
            })
            .catch(err => console.error(err));
      }
      // ───────────────────────────────────────────────────────────────────
      /*
       * 키워드로 통합 검색 → 주소 리스트 반환 → 카카오 지오코딩 → 마커 표시
       */
      function searchByKeyword(keyword) {
        console.log('[searchByKeyword] 요청 키워드:', keyword);
        axios.get(`/api/search?keyword=${encodeURIComponent(keyword)}`)
            .then(res => {
              const addressList = res.data;
              if (!Array.isArray(addressList) || addressList.length === 0) {
                alert('검색 결과가 없습니다.');
                return;
              }
              clearMarkers();
              addressList.forEach(addr => {
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

      if (searchbtn) {
        searchbtn.addEventListener('click', () => {
          const keyword = input.value.trim();
          if (keyword) {
            searchByKeyword(keyword);
          }
        });
      }

      if (input) {
        input.addEventListener('keydown', e => {
          if (e.key === 'Enter') {
            const keyword = input.value.trim();
            if (keyword) {
              searchByKeyword(keyword);
            }
          }
        });
      }

      // 8. 카페 등록 버튼 클릭 시 이동
      const registerBtn = document.getElementById('cafe-registration');
      if (registerBtn) {
        registerBtn.addEventListener('click', () => {
          window.location.href = '/cafe-registration';
        });
      }

      console.log('Kakao Maps 초기화 완료');
      return true;

    } catch (error) {
      console.error('지도 초기화 중 오류:', error);
      return false;
    }
  }

  // ───────────────────────────────────────────────────────────────────
  // 카드 뷰 로직 (Kakao Maps와 독립적으로 실행)
  function initCardView() {
    const ROW_SIZE = 4;      // 한 줄에 보여줄 카드 수
    let currentIndex = 0;    // 다음에 렌더링할 데이터 시작 인덱스
    let cafes = [];          // ← 여기에 API로 받은 데이터가 들어갑니다

    const wrapper = document.getElementById('cards-wrapper');
    const btn = document.getElementById('load-more');

    if (!wrapper || !btn) {
      console.error('카드 뷰 요소를 찾을 수 없습니다.');
      return;
    }

    // ─── 1. API 호출: cafes에 데이터 채우고 첫 줄 렌더링 ───
    axios.get('/api/main/cards')
        .then(res => {
          const data = res.data;
          console.log('API 응답:', data);
          cafes = data;
          btn.style.display = cafes.length > ROW_SIZE ? 'block' : 'none';
          renderRow();
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

    // 더보기 버튼 클릭 이벤트
    btn.addEventListener('click', renderRow);

    // 카페 등록 버튼 클릭 시 이동 (중복 제거됨)
  }

  function initKakaoMapWithRetry() {
    console.log(`Kakao Maps API 로드 확인 중... (시도 ${retryCount + 1}/${MAX_RETRIES})`);
    console.log('kakao 객체 존재:', typeof kakao !== 'undefined');
    console.log('kakao.maps 존재:', typeof kakao !== 'undefined' && kakao.maps);

    if (isKakaoMapsReady()) {
      console.log('Kakao Maps API 로드 완료, 지도 초기화 시작');
      const success = initKakaoMap();
      if (success) {
        console.log('지도 초기화 성공');
        return;
      }
    }

    retryCount++;
    if (retryCount >= MAX_RETRIES) {
      console.error('Kakao Maps API 로드 실패: 최대 재시도 횟수 초과');
      alert('지도를 로드할 수 없습니다. 페이지를 새로고침해주세요.');
      return;
    }

    // 100ms 후 다시 시도
    setTimeout(initKakaoMapWithRetry, 100);
  }

  // DOMContentLoaded 이벤트에서 초기화 시작
  function init() {
    console.log('init() 호출됨 - DOM 상태:', document.readyState);

    // 카드 뷰는 즉시 초기화
    initCardView();

    // Kakao Maps API 초기화 (재시도 로직 포함)
    initKakaoMapWithRetry();
  }

  // DOM 로딩 완료를 확실히 보장
  function waitForDOM() {
    if (document.readyState === 'loading') {
      document.addEventListener('DOMContentLoaded', init);
    } else {
      // 이미 로드된 경우 약간의 지연 후 실행
      setTimeout(init, 50);
    }
  }

  // 초기화 시작
  waitForDOM();

})(); // IIFE 끝