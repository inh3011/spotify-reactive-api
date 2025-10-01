# Spotify Reactive API

## 기술 스택

- **Language**: Java 21
- **Framework**: Spring Boot 3.5.6
- **Reactive Stack**: Spring WebFlux + R2DBC
- **Database**: PostgreSQL 16
- **Container**: Docker & Docker Compose

## 실행 방법

### 실행

```bash
# 1. 환경변수 파일 생성
cp .env.example .env

# 2. 빌드 및 실행
docker-compose up --build -d
```

### 종료

```bash
docker-compose down
```

## 과제 설명

### 주요 기능

- **대용량 데이터 처리**: 900K Spotify Dataset을 메모리 효율적으로 DB에 저장
- **RESTful API**:
  - 연도&가수별 발매 앨범 수 조회 (페이징 지원)
  - 노래 좋아요 추가
  - 최근 1시간 좋아요 Top 10 조회
