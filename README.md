# Trading - 주식 거래 시스템

Spring Boot WebFlux 기반의 Reactive 주식 거래 시스템

## 📋 프로젝트 개요

간단한 주식 거래 시스템
- **Reactive Programming**: Spring WebFlux + R2DBC
- **Multi-Module**: Customer, Aggregator 모듈로 구성
- **Real-time**: Server-Sent Events(SSE)로 실시간 주가 스트리밍
- **Web UI**: Bootstrap 기반 거래 인터페이스

## 🏗️ 프로젝트 구조

```
Trading/
├── build.gradle              # 루트 빌드 설정
├── settings.gradle           # 멀티모듈 설정
├── customer/                 # 고객 거래 모듈 (Port: 8081)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/customer/
│   │   │   │   ├── application/      # 애플리케이션 계층
│   │   │   │   ├── domain/            # 도메인 계층
│   │   │   │   ├── dto/               # DTO
│   │   │   │   ├── presentation/      # 컨트롤러
│   │   │   │   ├── infra/             # Repository
│   │   │   │   └── exception/         # 예외
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── sql/data.sql
│   │   └── test/
│   └── build.gradle
└── aggregator/               # 집계/프록시 모듈 (Port: 8080)
    ├── src/
    │   └── main/
    │       ├── java/com/example/aggregator/
    │       └── resources/
    │           ├── application.yml
    │           └── static/
    │               └── index.html    # 거래 웹 UI
    └── build.gradle
```

## 🎯 주요 컴포넌트

### Customer 모듈
- **CustomerFinder**: 고객 조회 및 예외 처리
- **PortfolioFinder**: 포트폴리오 조회
- **TradeExecutor**: 거래 실행 (잔액/수량 업데이트 조합)
- **BalanceUpdater**: 잔액 업데이트
- **PortfolioUpdater**: 포트폴리오 수량 업데이트
- **TradeService**: 거래 비즈니스 로직 오케스트레이션
- **CustomerService**: 고객 정보 조회 서비스

### Aggregator 모듈
- **Proxy/Gateway**: Customer 서비스로 요청 프록시
- **Stock Price Stream**: SSE를 통한 실시간 주가 스트리밍
- **Web UI**: 거래 인터페이스 제공

## 🚀 주요 기능

### 1. 고객 정보 조회
```
GET /customers/{customerId}
```

**응답 예시:**
```json
{
  "id": 1,
  "name": "Sam",
  "balance": 10000,
  "holdings": [
    {"ticker": "GOOGLE", "quantity": 5},
    {"ticker": "APPLE", "quantity": 3}
  ]
}
```

### 2. 주식 매수
```
POST /customers/{customerId}/buy
```

**요청:**
```json
{
  "ticker": "GOOGLE",
  "quantity": 1,
  "action": "BUY"
}
```

### 3. 주식 매도
```
POST /customers/{customerId}/sell
```

**요청:**
```json
{
  "ticker": "GOOGLE",
  "quantity": 1,
  "action": "SELL"
}
```

### 4. 실시간 주가 스트리밍
```
GET /stock/price-stream
```

**SSE 응답:**
```
data: {"ticker":"GOOGLE","price":150}
data: {"ticker":"APPLE","price":200}
```

## 🖥️ 웹 UI 사용법

### 접속 방법
```
http://localhost:8080/?customer=1
http://localhost:8080/?customer=2
```

쿼리 파라미터 `?customer=1` 또는 `?customer=2`로 고객 ID를 지정합니다.

### 기능
- **실시간 주가**: SSE로 실시간 주가 업데이트
- **매수/매도**: 각 종목별 Buy/Sell 버튼 클릭
- **포트폴리오**: 보유 종목 및 수량 표시
- **잔액**: 사용 가능한 잔액 표시
- **포트폴리오 가치**: 총 자산 가치 (잔액 + 보유 주식)

### 지원 종목
- APPLE
- AMAZON
- GOOGLE
- MICROSOFT

## 🛠️ 기술 스택

- **Language**: Java 24
- **Framework**: Spring Boot 3.5.6
- **Reactive**: Spring WebFlux, Project Reactor
- **Database**: H2 (R2DBC)
- **Build Tool**: Gradle
- **Test**: JUnit 5, WebTestClient
- **Real-time**: Server-Sent Events (SSE)

## 🏃 실행 방법

### 1. 외부 서비스 실행 (필수)
```bash
java -jar external-services.jar
```
> 실시간 주가 스트리밍을 위한 외부 서비스입니다. 반드시 먼저 실행해야 합니다.

### 2. 전체 빌드
```bash
./gradlew clean build
```

### 3. Customer 모듈 실행 (Port: 8081)
```bash
./gradlew :customer:bootRun
```

### 4. Aggregator 모듈 실행 (Port: 8080)
```bash
./gradlew :aggregator:bootRun
```

### 5. 웹 브라우저 접속
```
http://localhost:8080/?customer=1
```

### 6. 테스트 실행
```bash
./gradlew :customer:test
```

## 🧪 테스트

### 테스트 케이스

| 테스트 | 설명 |
|--------|------|
| `customerInformationTest` | 고객 정보 조회 |
| `buyTest` | 정상 구매 |
| `buyWithInsufficientBalanceTest` | 잔액 부족 시 에러 |
| `sellTest` | 정상 판매 |
| `sellWithInsufficientStockTest` | 수량 부족 시 에러 |
| `multipleTrades` | 연속 거래 (복합 시나리오) |
| `buySameStockMultipleTimes` | 같은 종목 여러 번 구매 |
| `getNonExistentCustomer` | 존재하지 않는 고객 조회 |

## 📝 API 문서

### Customer Service (Port: 6060)

#### 고객 정보 조회
- **Endpoint**: `GET /customers/{customerId}`
- **Success**: 200 OK
- **Error**: 404 Not Found (고객 없음)

#### 주식 매수
- **Endpoint**: `POST /customers/{customerId}/buy`
- **Success**: 200 OK
- **Error**: 400 Bad Request (잔액 부족)

#### 주식 매도
- **Endpoint**: `POST /customers/{customerId}/sell`
- **Success**: 200 OK
- **Error**: 400 Bad Request (수량 부족)

### Aggregator Service (Port: 8080)

#### 웹 UI
- **Endpoint**: `GET /?customer={customerId}`
- **Description**: 거래 웹 인터페이스

#### 주가 스트리밍
- **Endpoint**: `GET /stock/price-stream`
- **Type**: Server-Sent Events (SSE)
- **Description**: 실시간 주가 업데이트

#### 프록시 엔드포인트
- **Pattern**: `/customers/**`
- **Description**: Customer 서비스로 프록시

## 🔧 설정

### Customer Module (application.yml)
```yaml
spring:
  application:
    name: customer
  sql:
    init:
      data-locations: classpath:sql/data.sql

server:
  port: 6060
```

### Aggregator Module (application.yml)
```yaml
spring:
  application:
    name: aggregator

server:
  port: 8080
```

## 💡 아키텍처 특징

### Reactive Programming
- **Mono.zip**: 병렬 실행으로 성능 최적화
- **WebFlux**: Non-blocking I/O
- **R2DBC**: Reactive 데이터베이스 액세스

### Server-Sent Events
- **실시간 주가**: 서버 → 클라이언트 단방향 스트리밍
- **자동 재연결**: 연결 끊김 시 자동 복구
- **효율성**: WebSocket보다 간단하고 가벼움

### Multi-Module Architecture
- **Customer**: 핵심 비즈니스 로직
- **Aggregator**: API Gateway + UI 제공
- **독립 배포**: 각 모듈 독립적으로 실행 가능

## 🚧 향후 개선 사항

- [ ] 주가 변동 알고리즘 개선
- [ ] 거래 히스토리 기능
- [ ] 차트 및 그래프 시각화
- [ ] 사용자 인증/인가
- [ ] Docker 컨테이너화

## 📖 사용 예시

1. **외부 서비스 실행**: `java -jar external-services.jar`(Port: 7070)
2. **Customer 실행** (Port: 6060)
3. **Aggregator 실행** (Port: 8080)
4. **브라우저 접속**: `http://localhost:8080/?customer=1`
5. **실시간 주가 확인**: 자동으로 주가 업데이트
6. **매수**: GOOGLE 종목의 Buy 버튼 클릭
7. **포트폴리오 확인**: 보유 수량 및 가치 확인
8. **매도**: GOOGLE 종목의 Sell 버튼 클릭

이 프로젝트는 학습 목적으로 작성되었습니다.
