CREATE INDEX IF NOT EXISTS idx_song_year_artist_album
    ON song (release_year, artist_name, album_name);
