# Spotify Reactive API

## 기술 스택

- **Language**: Java 21
- **Framework**: Spring Boot 3.5.6
- **Reactive Stack**: Spring WebFlux + R2DBC
- **Database**: PostgreSQL 16
- **Container**: Docker & Docker Compose
- **Monitoring**: Prometheus + Grafana
- **API Documentation**: Swagger/OpenAPI

## 사전 준비

### Spotify Dataset 다운로드

기초 데이터 파일을 다운로드하여 지정된 위치에 배치해야 합니다.

```bash
# 1. Kaggle에서 데이터셋 다운로드
# URL: https://www.kaggle.com/datasets/devdope/900k-spotify?select=900k+Definitive+Spotify+Dataset.json
# 파일명: 900k Definitive Spotify Dataset.json

# 2. 다운로드한 파일을 프로젝트 경로에 복사
cp ~/Downloads/"900k Definitive Spotify Dataset.json" ./src/main/resources/data/
```

## 실행 방법

### Docker로 실행

```bash
# 1. Spotify 데이터 파일 배치
cp ~/Downloads/"900k Definitive Spotify Dataset.json" ./src/main/resources/data/

# 2. application-docker.yml에서 파일 경로 확인/수정
# Docker 환경 (`application-docker.yml`)

spotify:
  data:
    file-path: /app/resources/data/900k Definitive Spotify Dataset.json # 기본값

# 3. Docker Compose로 실행
docker-compose up --build -d

# 4. 로그 확인
docker-compose logs -f app
```

### 로컬 환경에서 실행

```bash
# 1. Spotify 데이터 파일 배치
cp ~/Downloads/"900k Definitive Spotify Dataset.json" ./src/main/resources/data/

# 2. application-local.yml에서 파일 경로 확인/수정
# 로컬 환경 (`application-local.yml`)

spotify:
  data:
    file-path: ./src/main/resources/data/900k Definitive Spotify Dataset.json # 기본값

# 3. PostgreSQL 실행 (Docker)
docker-compose up -d postgres

# 4. 애플리케이션 실행
./gradlew bootRun --args='--spring.profiles.active=local'
```

### 종료

```bash
docker-compose down
```

## 서비스 접근

- Swagger UI: http://localhost:8080/swagger-ui.html
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000

## 주요 기능

### 1. 대용량 데이터 처리

- **메모리 사용량을 최소화**
- **인덱싱 전략**:
  - 복합 인덱스: `(release_year, artist_name, album_name)`
  - 시간 인덱스: `created_at` (좋아요 테이블)

### 2. RESTful API

#### 앨범 통계 API

- **연도 & 가수별 발매 앨범 수 조회**
  - 페이징 지원
  - 정렬 기능 (발매연도, 아티스트)
  - 필터링 (발매연도, 아티스트)

#### 좋아요 API

- **노래 좋아요 추가**
- **최근 N시간 좋아요 Top 조회**
  - 최근 1시간 Top 10
  - 커스터마이징 가능 (hour, top 파라미터)
