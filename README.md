# hhplus-tdd-1
항해 플러스 1주차

### ❓ [과제] `point` 패키지의 TODO 와 테스트코드를 작성해주세요.

**요구 사항**

- PATCH  `/point/{id}/charge` : 포인트를 충전한다.
- PATCH `/point/{id}/use` : 포인트를 사용한다.
- GET `/point/{id}` : 포인트를 조회한다.
- GET `/point/{id}/histories` : 포인트 내역을 조회한다.
- 잔고가 부족할 경우, 포인트 사용은 실패하여야 합니다.
- 동시에 여러 건의 포인트 충전, 이용 요청이 들어올 경우 순차적으로 처리되어야 합니다. (동시성)


**제공 구조**
- UserPoint
  - id
  - point : 포인트 정보
  - updateMillis : 변경 시간
- PointHistory
  - id
  - userId : UserPoint Id
  - amount : 포인트 차감 값(충전시 +, 사용시 -)
  - type : 포인트 충전/사용 유무(CHARGE/USE)
  - updateMillis : 변경시간

![image](https://github.com/faulty337/hhplus-tdd-1/assets/37091532/ee58359a-cbe2-426e-8c3f-fa6d5d8769b2)
- API
  - PATCH  `/point/{id}/charge` : 포인트를 충전한다.
  - PATCH `/point/{id}/use` : 포인트를 사용한다.
    - userId, isSuccess
  - GET `/point/{id}` : 포인트를 조회한다.
    - userId, point
  - GET `/point/{id}/histories` : 포인트 내역을 조회한다.
    - userId, list\<pointhistory\>, list.size(), pageable(시간 나면 추가)

**예상 문제 사항**
1. 공통
   - 존재 X userId
3. 충전 상황
   - 0 or 음수 값 충전 시도
   - integer 이외 값
4. 사용 상황
   - 0 or 음수 or 보유 포인트 초과 사용 시도
   - integer 이외 값
5. 포인트 조회
6. 포인트 내역 조회
   - 내역 없을 때
