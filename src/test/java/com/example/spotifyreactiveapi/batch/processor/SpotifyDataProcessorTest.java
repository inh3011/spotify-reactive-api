package com.example.spotifyreactiveapi.batch.processor;

import com.example.spotifyreactiveapi.controller.dto.SpotifyData;
import com.example.spotifyreactiveapi.model.SongModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SpotifyDataProcessorTest {

    private SpotifyDataProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new SpotifyDataProcessor();
    }

    private SpotifyData createSpotifyData(String songTitle, String artistName, String albumName, String releaseDate) {
        return SpotifyData.builder()
                .songTitle(songTitle)
                .artistName(artistName)
                .albumName(albumName)
                .releaseDate(releaseDate)
                .build();
    }

    @Test
    void shouldProcessSpotifyDataToSongModel() throws Exception {
        // Given
        SpotifyData spotifyData = createSpotifyData("Test Song", "Test Artist", "Test Album", "2023-04-15");

        // When
        SongModel result = processor.process(spotifyData);

        // Then
        assertNotNull(result);
        assertEquals("Test Song", result.getSongTitle());
        assertEquals("Test Artist", result.getArtistName());
        assertEquals("Test Album", result.getAlbumName());
        assertEquals(LocalDate.of(2023, 4, 15), result.getReleaseDate());
        assertEquals(2023, result.getReleaseYear());
        assertEquals(0L, result.getLikeCount());
    }

    @Test
    void shouldHandleNullReleaseDate() throws Exception {
        // Given
        SpotifyData spotifyData = createSpotifyData("Test Song", "Test Artist", "Test Album", null);

        // When
        SongModel result = processor.process(spotifyData);

        // Then
        assertNotNull(result);
        assertEquals("Test Song", result.getSongTitle());
        assertNull(result.getReleaseDate());
        assertNull(result.getReleaseYear());
    }

    @Test
    void shouldParseValidDateFormat() throws Exception {
        // Given
        SpotifyData spotifyData = createSpotifyData("Song 1", "Artist 1", "Album 1", "2013-04-29");

        // When
        SongModel result = processor.process(spotifyData);

        // Then
        assertEquals(2013, result.getReleaseYear());
        assertEquals(LocalDate.of(2013, 4, 29), result.getReleaseDate());
    }

    @Test
    void shouldHandleInvalidDateFormat() throws Exception {
        // Given
        SpotifyData spotifyData = createSpotifyData("Song 1", "Artist 1", "Album 1", "invalid-date");

        // When
        SongModel result = processor.process(spotifyData);

        // Then
        assertNotNull(result);
        assertEquals("Song 1", result.getSongTitle());
        assertNull(result.getReleaseDate());
        assertNull(result.getReleaseYear());
    }

    @Test
    void shouldProcessWithAllFields() throws Exception {
        // Given
        SpotifyData spotifyData = createSpotifyData("Even When the Waters Cold", "!!!", "Thr!!!er", "2013-04-29");

        // When
        SongModel result = processor.process(spotifyData);

        // Then
        assertNotNull(result);
        assertEquals("Even When the Waters Cold", result.getSongTitle());
        assertEquals("!!!", result.getArtistName());
        assertEquals("Thr!!!er", result.getAlbumName());
        assertEquals(LocalDate.of(2013, 4, 29), result.getReleaseDate());
        assertEquals(2013, result.getReleaseYear());
        assertEquals(0L, result.getLikeCount());
    }
}
