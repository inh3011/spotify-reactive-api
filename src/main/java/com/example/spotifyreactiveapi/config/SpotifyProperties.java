package com.example.spotifyreactiveapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
@ConfigurationProperties(prefix = "spotify.data")
public class SpotifyProperties {

    private final String filePath = "classpath:sample-spotify-data.json";

}
