package com.example.spotifyreactiveapi.repository.impl;

import com.example.spotifyreactiveapi.model.SongLikeTopModel;
import com.example.spotifyreactiveapi.repository.SongLikeTopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class SongLikeTopRepositoryImpl implements SongLikeTopRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Flux<SongLikeTopModel> findTopLikes(Integer hour, Integer limit) {
        return databaseClient.sql(
                """
                        SELECT t.song_id,
                               t.like_count,
                               ROW_NUMBER() OVER (ORDER BY t.like_count DESC) AS rank,
                               s.song_title,
                               s.album_name,
                               s.artist_name
                        FROM (
                            SELECT song_id, COUNT(*) AS like_count
                            FROM song_like
                            WHERE created_at >= NOW() - (:hour || ' hours')::interval
                            GROUP BY song_id
                        ) t
                        JOIN song s ON s.id = t.song_id
                        ORDER BY rank
                        LIMIT :limit
                        """)
                .bind("hour", hour)
                .bind("limit", limit)
                .map((row, meta) -> SongLikeTopModel.builder()
                        .songId(row.get("song_id", Long.class))
                        .likeCount(row.get("like_count", Long.class))
                        .rank(row.get("rank", Integer.class))
                        .songTitle(row.get("song_title", String.class))
                        .albumName(row.get("album_name", String.class))
                        .artistName(row.get("artist_name", String.class))
                        .build())
                .all();
    }
}
