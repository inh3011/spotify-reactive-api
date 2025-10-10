package com.example.spotifyreactiveapi.batch.processor;

import com.example.spotifyreactiveapi.controller.dto.SpotifyData;
import com.example.spotifyreactiveapi.model.SongModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class SpotifyDataProcessor implements ItemProcessor<SpotifyData, SongModel> {

    @Override
    public SongModel process(SpotifyData spotifyData) throws Exception {
        log.debug("Processing: {} by {}", spotifyData.getSongTitle(), spotifyData.getArtistName());

        LocalDate releaseDate = parseReleaseDate(spotifyData.getReleaseDate());
        Integer releaseYear = extractYear(spotifyData.getReleaseDate());

        return SongModel.create(
                spotifyData.getSongTitle(),
                spotifyData.getArtistName(),
                spotifyData.getAlbumName(),
                releaseDate,
                releaseYear);
    }

    private LocalDate parseReleaseDate(String releaseDate) {
        if (releaseDate == null || releaseDate.trim().isEmpty()) {
            return null;
        }

        try {
            return LocalDate.parse(releaseDate);
        } catch (Exception e) {
            log.warn("Invalid release date format: {} for song, setting to null", releaseDate);
            return null;
        }
    }

    private Integer extractYear(String releaseDate) {
        if (releaseDate == null || releaseDate.trim().isEmpty()) {
            return null;
        }

        try {
            LocalDate parsedDate = LocalDate.parse(releaseDate);
            return parsedDate.getYear();
        } catch (Exception e) {
            log.warn("Invalid release date format: {} for song, setting year to null", releaseDate);
            return null;
        }
    }
}
