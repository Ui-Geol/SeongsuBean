// 즉시 실행 함수로 스코프 격리
(() => {
  // 2) 실제 DB 연동 시 활성화할 fetch 예시
  /*
  fetch('/api/places/from-db')
    .then(res => res.json())
    .then(data => {
      dbPlaces.splice(0, dbPlaces.length, ...data);
    })
    .catch(console.error);
  */

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
      // 커스텀 마커
      const beanContent = '<img src="/images/beanMarker.png" width="50">';
      const beanOverlay = new kakao.maps.CustomOverlay({
        position: coords,
        content: beanContent,
        yAnchor: 1,
        zIndex: 2
      });
      beanOverlay.setMap(map);
      // 지도 중심 이동
      map.setCenter(coords);
      // 일러스트 오버레이
      const illustration = new kakao.maps.CustomOverlay({
        position: coords,
        content: '<img src="/images/mapUI.png" width="1248" height="758" style="pointer-events: none;">',
        xAnchor: 0.5,
        yAnchor: 0.5,
        zIndex: 1
      });
      illustration.setMap(map);
    } else {
      console.warn('일러스트 주소 검색 실패:', status);
    }
  });

  // 4. 지도에 표시된 마커 객체들을 저장할 배열
  let markers = [];

  // 마커 전체 삭제
  function clearMarkers() {
    markers.forEach(m => m.setMap(null));
    markers = [];
  }

  const cafes = [
    {
      name: '아쿠아산타 성수카페',
      desc: '분위기 맛집',
      img: 'images/Cafe1.png'
    },
    {
      name: '블루보틀 커피',
      desc: '성수 카페',
      img: 'https://picsum.photos/seed/b/200/120'
    },
    {
      name: '미디엄 스톤',
      desc: '아늑한 공간',
      img: 'https://picsum.photos/seed/c/200/120'
    },
    {
      name: '파케파케 성수',
      desc: '전통 커피',
      img: 'https://picsum.photos/seed/d/200/120'
    },
    {
      name: '카페 에이',
      desc: '신선한 원두',
      img: 'https://picsum.photos/seed/e/200/120'
    },
    {name: '카페 비', desc: '넓은 테라스', img: 'https://picsum.photos/seed/f/200/120'},
    {
      name: '카페 씨',
      desc: '달콤한 디저트',
      img: 'https://picsum.photos/seed/g/200/120'
    },
    {
      name: '카페 디',
      desc: '모던 인테리어',
      img: 'https://picsum.photos/seed/h/200/120'
    },
    // … 필요만큼 늘리세요
  ];

  const ROW_SIZE = 4;      // 한 줄에 보여줄 카드 수
  let currentIndex = 0;    // 다음에 렌더링할 데이터 시작 인덱스

  const wrapper = document.getElementById('cards-wrapper');
  const btn = document.getElementById('load-more');

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
      card.innerHTML = `
          <img src="${cafe.img}" alt="${cafe.name}">
          <div class="info">
            <h4>${cafe.name}</h4>
            <p>${cafe.desc}</p>
          </div>
        `;
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

  // 버튼 클릭 시 다음 줄 렌더링
  btn.addEventListener('click', renderRow);

  // 6. 이름으로 검색 및 표시
  function searchPlaceByName(name) {
    const place = dbPlaces.find(p => p.name === name);
    if (!place) {
      alert('검색 결과가 없습니다.');
      return;
    }
    clearMarkers();
    geocoder.addressSearch(place.address, (result, status) => {
      if (status === kakao.maps.services.Status.OK) {
        const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
        const imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png';
        const markerImage = new kakao.maps.MarkerImage(imageSrc,
            new kakao.maps.Size(24, 35));
        const marker = new kakao.maps.Marker(
            {map, position: coords, image: markerImage, title: place.name});
        markers.push(marker);
        map.setCenter(coords);
      } else {
        console.warn('주소 검색 실패:', place.address, status);
      }
    });
  }

  // 7. 검색창 이벤트 핸들러
  const input = document.getElementById('searchInput');
  const searchbtn = document.getElementById('searchBtn');
  searchbtn.addEventListener('click', () => {
    const keyword = input.value.trim();
    if (keyword) {
      searchPlaceByName(keyword);
    }
  });
  input.addEventListener('keydown', e => {
    if (e.key === 'Enter') {
      const keyword = input.value.trim();
      if (keyword) {
        searchPlaceByName(keyword);
      }
    }
  });

  // ... 이하 place 리스트에 대한 showMarkers, filterByKeyword, filterByCategory, filterSpecial 등 기존 map1.js 함수 유지 ...
})();
