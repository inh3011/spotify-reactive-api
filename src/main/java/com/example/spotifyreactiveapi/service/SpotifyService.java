package com.example.spotifyreactiveapi.service;

import com.example.spotifyreactiveapi.controller.dto.SpotifyData;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SpotifyService {

    Mono<String> readFile(MultipartFile jsonFile);

    Mono<List<SpotifyData>> parseJsonData(String jsonData);

    Mono<Void> processData(List<SpotifyData> dataList);

    Mono<Void> processSpotifyFile(MultipartFile jsonFile);
}
