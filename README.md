# Trading - 주식 거래 시스템

Spring Boot WebFlux 기반의 Reactive 주식 거래 시스템

## 📋 프로젝트 개요

간단한 주식 거래 시스템
- **Reactive Programming**: Spring WebFlux + R2DBC
- **Multi-Module**: Customer, Aggregator 모듈로 구성

## 🏗️ 프로젝트 구조

```
Trading/
├── build.gradle              # 루트 빌드 설정
├── settings.gradle           # 멀티모듈 설정
├── customer/                 # 고객 거래 모듈
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/customer/
│   │   │   │   ├── application/      # 애플리케이션 계층
│   │   │   │   │   ├── CustomerFinder.java
│   │   │   │   │   ├── CustomerService.java
│   │   │   │   │   ├── PortfolioFinder.java
│   │   │   │   │   ├── PortfolioUpdater.java
│   │   │   │   │   ├── BalanceUpdater.java
│   │   │   │   │   ├── TradeExecutor.java
│   │   │   │   │   └── TradeService.java
│   │   │   │   ├── domain/            # 도메인 계층
│   │   │   │   │   ├── Customer.java
│   │   │   │   │   ├── PortfolioItem.java
│   │   │   │   │   ├── Ticker.java
│   │   │   │   │   ├── TradeAction.java
│   │   │   │   │   └── TradeResult.java
│   │   │   │   ├── dto/               # DTO
│   │   │   │   │   ├── CustomerInformation.java
│   │   │   │   │   ├── Holding.java
│   │   │   │   │   └── TradeResult.java
│   │   │   │   ├── presentation/      # 프레젠테이션 계층
│   │   │   │   │   └── CustomerController.java
│   │   │   │   ├── infra/             # 인프라 계층
│   │   │   │   │   ├── CustomerRepository.java
│   │   │   │   │   └── PortfolioItemRepository.java
│   │   │   │   └── exception/         # 예외
│   │   │   │       ├── CustomerNotFoundException.java
│   │   │   │       ├── InsufficientBalanceException.java
│   │   │   │       └── InsufficientStockException.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── sql/data.sql
│   │   └── test/
│   │       └── java/com/example/customer/
│   │           └── CustomerServiceApplicationTest.java
│   └── build.gradle
└── aggregator/               # 집계 모듈
    ├── src/
    └── build.gradle
```

## 🎯 주요 컴포넌트

- **CustomerFinder**: 고객 조회 및 예외 처리
- **PortfolioFinder**: 포트폴리오 조회
- **TradeExecutor**: 거래 실행 (잔액/수량 업데이트 조합)
- **BalanceUpdater**: 잔액 업데이트
- **PortfolioUpdater**: 포트폴리오 수량 업데이트
- **TradeService**: 거래 비즈니스 로직 오케스트레이션
- **CustomerService**: 고객 정보 조회 서비스

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
  "holding": [
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
  "price": 100,
  "quantity": 5,
  "action": "BUY"
}
```

**동작:**
1. 고객 잔액 확인 (canBuy 검증)
2. 포트폴리오 조회 또는 생성
3. 잔액 차감 (병렬)
4. 수량 증가 (병렬)
5. 결과 반환

### 3. 주식 매도
```
POST /customers/{customerId}/sell
```

**요청:**
```json
{
  "ticker": "GOOGLE",
  "price": 100,
  "quantity": 3,
  "action": "SELL"
}
```

**동작:**
1. 고객 조회
2. 포트폴리오 조회
3. 보유 수량 확인 (canSell 검증)
4. 잔액 증가 (병렬)
5. 수량 감소 (병렬)
6. 결과 반환


## 🛠️ 기술 스택

- **Language**: Java 24
- **Framework**: Spring Boot 3.5.6
- **Reactive**: Spring WebFlux, Project Reactor
- **Database**: H2 (R2DBC)
- **Build Tool**: Gradle
- **Test**: JUnit 5, WebTestClient



## 🏃 실행 방법

### 1. 전체 빌드
```bash
./gradlew clean build
```

### 2. Customer 모듈 실행
```bash
./gradlew :customer:bootRun
```

### 3. 테스트 실행
```bash
./gradlew :customer:test
```

### 4. 특정 테스트 실행
```bash
./gradlew :customer:test --tests "CustomerServiceApplicationTest.buyTest"
```


## 📝 API 문서

### 고객 정보 조회
- **Endpoint**: `GET /customers/{customerId}`
- **Success**: 200 OK
- **Error**: 404 Not Found (고객 없음)

### 주식 매수
- **Endpoint**: `POST /customers/{customerId}/buy`
- **Success**: 200 OK
- **Error**: 400 Bad Request (잔액 부족)

### 주식 매도
- **Endpoint**: `POST /customers/{customerId}/sell`
- **Success**: 200 OK
- **Error**: 400 Bad Request (수량 부족)

## 🔧 설정

### application.yml
```yaml
spring:
  application:
    name: customer
  sql:
    init:
      data-locations: classpath:sql/data.sql

server:
  port: 8081
```


## 🚧 향후 개선 사항

- [ ] Aggregator 모듈 구현

이 프로젝트는 학습 목적으로 작성되었습니다.
