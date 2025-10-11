package com.example.spotifyreactiveapi.repository.impl;

import com.example.spotifyreactiveapi.model.AlbumCountModel;
import com.example.spotifyreactiveapi.repository.AlbumCountRetrieveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AlbumCountRetrieveRepositoryImpl implements AlbumCountRetrieveRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Flux<AlbumCountModel> findAlbumCountByReleaseYearAndArtist(
            Pageable pageable,
            String artistKeyword,
            Integer yearKeyword) {
        int limit = pageable.getPageSize() > 0 ? pageable.getPageSize() : 20;
        int offset = Math.max(pageable.getPageNumber(), 0) * limit;

        String primaryOrderBy = buildOrderByClause(pageable.getSort());
        String whereClause = buildWhereClause(artistKeyword, yearKeyword);

        DatabaseClient.GenericExecuteSpec query = databaseClient
                .sql("""
                        SELECT release_year, artist_name, COUNT(DISTINCT album_name) AS album_count
                        FROM song
                        WHERE release_year IS NOT NULL AND artist_name IS NOT NULL
                        %s
                        GROUP BY release_year, artist_name
                        ORDER BY %s, artist_name ASC
                        LIMIT :limit OFFSET :offset
                        """.formatted(whereClause, primaryOrderBy))
                .bind("limit", limit)
                .bind("offset", offset);

        if (artistKeyword != null && !artistKeyword.trim().isEmpty()) {
            query = query.bind("artistKeyword", "%" + artistKeyword.trim() + "%");
        }
        if (yearKeyword != null) {
            query = query.bind("yearKeyword", yearKeyword);
        }

        return query
                .map((row, metadata) -> AlbumCountModel.builder()
                        .releaseYear(row.get("release_year", Integer.class))
                        .artistName(row.get("artist_name", String.class))
                        .albumCount(row.get("album_count", Long.class))
                        .build())
                .all();
    }

    @Override
    public Mono<Long> countAlbumCounts(String artistKeyword, Integer yearKeyword) {
        String whereClause = buildWhereClause(artistKeyword, yearKeyword);

        DatabaseClient.GenericExecuteSpec query = databaseClient
                .sql("""
                        SELECT COUNT(*) AS total
                        FROM (
                            SELECT release_year, artist_name
                            FROM song
                            WHERE release_year IS NOT NULL AND artist_name IS NOT NULL
                            %s
                            GROUP BY release_year, artist_name
                        ) t
                        """.formatted(whereClause));

        if (artistKeyword != null && !artistKeyword.trim().isEmpty()) {
            query = query.bind("artistKeyword", "%" + artistKeyword.trim() + "%");
        }
        if (yearKeyword != null) {
            query = query.bind("yearKeyword", yearKeyword);
        }

        return query
                .map((row, metadata) -> row.get("total", Long.class))
                .one();
    }

    private String buildOrderByClause(Sort sort) {
        if (!sort.isSorted()) {
            return "release_year DESC";
        }

        Sort.Order order = sort.iterator().next();
        String property = order.getProperty();
        String direction = order.getDirection().isDescending() ? "DESC" : "ASC";

        return property + " " + direction;
    }

    private String buildWhereClause(String artistKeyword, Integer yearKeyword) {
        StringBuilder whereClause = new StringBuilder();
        if (artistKeyword != null && !artistKeyword.trim().isEmpty()) {
            whereClause.append(" AND artist_name ILIKE :artistKeyword");
        }
        if (yearKeyword != null) {
            whereClause.append(" AND release_year = :yearKeyword");
        }
        return whereClause.toString();
    }

}
