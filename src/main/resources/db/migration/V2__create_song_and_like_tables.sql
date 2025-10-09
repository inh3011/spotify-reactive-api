-- 기존 테이블들 삭제 (외래키 제약조건 때문에 순서 중요)
DROP TABLE IF EXISTS song_like CASCADE;
DROP TABLE IF EXISTS song CASCADE;
DROP TABLE IF EXISTS album CASCADE;
DROP TABLE IF EXISTS artist CASCADE;

CREATE TABLE artist
(
    id         BIGSERIAL PRIMARY KEY,                           -- 고유 아티스트 식별자
    name       VARCHAR(255) NOT NULL UNIQUE,                    -- 아티스트 이름
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 아티스트 정보 생성 시간
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 아티스트 정보 마지막 수정 시간
);

CREATE TABLE album
(
    id         BIGSERIAL PRIMARY KEY,                           -- 고유 앨범 식별자
    name       VARCHAR(255) NOT NULL UNIQUE,                    -- 앨범 이름
    artist_id  BIGINT       NOT NULL REFERENCES artist (id),    -- 앨범을 발매한 아티스트 ID
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 앨범 정보 생성 시간
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 앨범 정보 마지막 수정 시간
    UNIQUE (name, artist_id)

);

CREATE TABLE song
(
    id           BIGSERIAL PRIMARY KEY,                           -- 고유 곡 식별자
    title        VARCHAR(255) NOT NULL,                           -- 곡 제목
    artist_id    BIGINT       NOT NULL REFERENCES artist (id),    -- 곡을 부른 아티스트 ID
    album_id     BIGINT REFERENCES album (id),                    -- 곡이 속한 앨범 ID
    release_date DATE,                                            -- 앨범 발매일
    release_year INT,                                             -- 앨범 발매년도
    like_count   BIGINT       NOT NULL DEFAULT 0,                 -- 곡 좋아요 수
    created_at   TIMESTAMP             DEFAULT CURRENT_TIMESTAMP, -- 곡 정보 생성 시간
    updated_at   TIMESTAMP             DEFAULT CURRENT_TIMESTAMP, -- 곡 정보 마지막 수정 시간
    UNIQUE (title, artist_id, album_id)

);

CREATE TABLE song_like
(
    id         BIGSERIAL PRIMARY KEY,                -- 고유 좋아요 식별자
    song_id    BIGINT NOT NULL REFERENCES song (id), -- 좋아요가 눌린 곡 ID
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 좋아요 생성 시간
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP   -- 좋아요 마지막 수정 시간
);



