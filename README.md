# SeongsuBean
성수동 카페 웹
<br>
## 목차
- [커밋 타입](#커밋-타입)
- [브랜치 전략](#브랜치-전략)
- [add와 implement의 차이](#add와-implemnt의-차이점)
- [modify와 fix와 correct의 차이점](#modify와-fix와-correct의-차이점)

---
<br>

## 커밋 타입
| 타입       | 설명                                      | 예시                                               |
|------------|-------------------------------------------|----------------------------------------------------|
| `feat`     | 새로운 기능 추가                          | `feat: add user registration form`                |
| `fix`      | 버그 수정                                 | `fix: resolve crash on login page`                |
| `docs`     | 문서 수정 (README 등)                     | `docs: update README with setup instructions`     |
| `style`    | 코드 포맷팅 (기능 변화 없음)              | `style: reformat code with Prettier`              |
| `refactor` | 코드 리팩토링 (기능 변화 없음)            | `refactor: simplify conditional logic in auth flow` |
| `perf`     | 성능 개선                                 | `perf: optimize image loading time on homepage`   |
| `test`     | 테스트 코드 추가/수정                     | `test: add unit tests for utils.js`               |
| `build`    | 빌드 시스템 관련 변경 (예: 의존성, 도구) | `build: update dependencies to latest versions`   |
| `chore`    | 기타 변경사항 (빌드 제외 설정 등)         | `chore: update .gitignore to exclude .env files`  |
<br>

## 브랜치 전략
- 현재 프로젝트의 경우 main과 feature 브랜치만 사용

| 브랜치     | 설명                                                                 |
|------------|----------------------------------------------------------------------|
| main       | 제품으로 출시할 수 있는 최종 버전의 코드가 있는 브랜치 (배포용)        |
| develop    | 다음 출시 버전을 개발하는 브랜치 (기능 통합 및 테스트용)             |
| feature/기능 | 새로운 기능 개발을 위한 브랜치 (보통 develop에서 분기)               |
| hotfix     | main에서 분기하여 긴급하게 수정하는 브랜치 (배포 중 버그 등)          |
<br>

## add와 implemnt의 차이점
| 항목        | `add`                                      | `implement`                                      |
|-------------|--------------------------------------------|--------------------------------------------------|
| 의미  | "새로 추가했다"                             | "구현했다" 또는 "작동하도록 만들었다"              |
| 초점    | 무엇을 추가했는가에 초점                  | 기능이나 로직을 구현한 것에 초점                  |
| 사용 시점| 파일, 폴더, 버튼, 설정 등 물리적 요소 추가 | 함수, 알고리즘, 로직, 동작 기능 구현              |
| 예시 상황| 새로운 라우터 파일 추가                    | 로그인 기능의 로직을 실제로 작동하게 구현         |
<br>

## modify와 fix와 correct의 차이점
| 단어      | 의미                           | 커밋 타입과의 관계        | 주로 쓰이는 상황                         |
|-----------|--------------------------------|---------------------------|------------------------------------------|
| `modify`  | 기존 코드를 변경               | 보통 `chore` 또는 `refactor` | 기능에 변화 없이 코드 수정할 때         |
| `fix`     | 버그 또는 문제를 수정          | `fix` (표준 커밋 타입)     | 오작동하는 기능이나 버그를 고칠 때      |
| `correct` | 잘못된 부분을 바로잡음         | `fix`나 `chore`로 처리 가능 | 문법 오류, 오타, 논리 실수 등 정정      |


