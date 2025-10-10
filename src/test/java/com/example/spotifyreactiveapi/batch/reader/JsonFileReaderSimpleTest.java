package com.example.spotifyreactiveapi.batch.reader;

import com.example.spotifyreactiveapi.config.SpotifyProperties;
import com.example.spotifyreactiveapi.controller.dto.SpotifyData;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

import static org.junit.jupiter.api.Assertions.*;

class JsonFileReaderSimpleTest {

    @Test
    void testJsonFileReader() throws Exception {
        // Given
        SpotifyProperties spotifyProperties = new SpotifyProperties();
        spotifyProperties.setFilePath("classpath:sample-spotify-data.json");

        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();

        JsonFileReader reader = new JsonFileReader(spotifyProperties, resourceLoader);

        // When & Then
        int count = 0;
        SpotifyData spotifyData;
        while ((spotifyData = reader.read()) != null) {
            count++;
            System.out.println(spotifyData.getArtistName() + " - " + spotifyData.getSongTitle());
        }

        System.out.println("Total items read: " + count);
        assertTrue(count > 0, "Should read at least one item");
    }
}
