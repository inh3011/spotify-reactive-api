CREATE TABLE artist
(
    id         BIGSERIAL PRIMARY KEY,                           -- 고유 아티스트 식별자
    name       VARCHAR(255) NOT NULL UNIQUE,                    -- 아티스트 이름
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 아티스트 정보 생성 시간
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 아티스트 정보 마지막 수정 시간
);

CREATE TABLE album
(
    id           BIGSERIAL PRIMARY KEY,                           -- 고유 앨범 식별자
    name         VARCHAR(255) NOT NULL,                           -- 앨범 이름
    release_date DATE,                                            -- 앨범 발매일
    release_year INT,                                             -- 앨범 발매년도
    artist_id    BIGINT       NOT NULL REFERENCES artist (id),    -- 앨범을 발매한 아티스트 ID
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 앨범 정보 생성 시간
    updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 앨범 정보 마지막 수정 시간
);
CREATE INDEX idx_album_year_artist ON album (release_year, artist_id);

CREATE TABLE song
(
    id               BIGSERIAL PRIMARY KEY,                           -- 고유 곡 식별자
    title            VARCHAR(255) NOT NULL,                           -- 곡 제목
    artist_id        BIGINT       NOT NULL REFERENCES artist (id),    -- 곡을 부른 아티스트 ID
    album_id         BIGINT REFERENCES album (id),                    -- 곡이 속한 앨범 ID
    length_ms        INT,                                             -- 곡 길이 (밀리초)
    explicit         BOOLEAN,                                         -- 성인 콘텐츠 여부
    popularity       INT,                                             -- 곡 인기도 (0-100)
    like_count       BIGINT       NOT NULL DEFAULT 0,                 -- 곡 좋아요 수
    key_text         VARCHAR(32),                                     -- 곡의 음계
    tempo            DOUBLE PRECISION,                                -- 곡의 템포 (BPM)
    loudness_db      DOUBLE PRECISION,                                -- 곡의 음량 (dB)
    time_signature   VARCHAR(16),                                     -- 곡의 박자
    genre            VARCHAR(64),                                     -- 곡의 장르
    emotion          VARCHAR(64),                                     -- 곡의 감정
    lyrics_text      TEXT,                                            -- 곡의 가사
    energy           INT,                                             -- 곡의 에너지 (0-100)
    danceability     INT,                                             -- 곡의 댄스 가능성 (0-100)
    positiveness     INT,                                             -- 곡의 긍정성 (0-100)
    speechiness      INT,                                             -- 곡의 말하기 정도 (0-100)
    liveness         INT,                                             -- 곡의 라이브 느낌 (0-100)
    acousticness     INT,                                             -- 곡의 어쿠스틱 정도 (0-100)
    instrumentalness INT,                                             -- 곡의 기악 정도 (0-100)
    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 곡 정보 생성 시간
    updated_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 곡 정보 마지막 수정 시간
);
CREATE INDEX idx_song_artist ON song (artist_id);
CREATE INDEX idx_song_album ON song (album_id);

CREATE TABLE song_like
(
    id         BIGSERIAL PRIMARY KEY,                        -- 고유 좋아요 식별자
    song_id    BIGINT    NOT NULL REFERENCES song (id),      -- 좋아요가 눌린 곡 ID
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 좋아요 생성 시간
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 좋아요 마지막 수정 시간
);
CREATE INDEX idx_like_song_time ON song_like (song_id, created_at DESC);
CREATE INDEX idx_like_time ON song_like (created_at DESC);


