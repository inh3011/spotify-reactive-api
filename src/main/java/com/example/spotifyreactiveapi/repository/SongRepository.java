package com.example.spotifyreactiveapi.repository;

import com.example.spotifyreactiveapi.domain.Song;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SongRepository extends ReactiveCrudRepository<Song, Long> {

    @Query("""
            INSERT INTO song (artist_name, album_name, song_title, release_date, release_year, like_count)
            VALUES (:#{#song.artistName}, :#{#song.albumName}, :#{#song.songTitle}, :#{#song.releaseDate}, :#{#song.releaseYear}, :#{#song.likeCount})
            ON CONFLICT (artist_name, album_name, song_title)
            DO UPDATE SET
                release_date = EXCLUDED.release_date,
                release_year = EXCLUDED.release_year,
                like_count = EXCLUDED.like_count,
                updated_at = NOW()
            RETURNING *
            """)
    Mono<Song> saveOrUpdate(Song song);
}
