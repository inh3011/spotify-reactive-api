package com.example.spotifyreactiveapi.controller;

import com.example.spotifyreactiveapi.service.SpotifySaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Tag(name = "Spotify API", description = "Spotify 데이터 처리 API")
@RestController
@RequestMapping("/api/spotify")
@RequiredArgsConstructor
public class SpotifyController {

    private final SpotifySaveService spotifySaveService;

    @Operation(summary = "데이터 적재 API", description = "데이터를 적재합니다.설정된 파일을 읽고 파싱 및 저장까지 처리합니다.")
    @PostMapping("/process")
    public Mono<String> process() {
        return spotifySaveService.saveSpotifyData()
                .then(Mono.just("Processing started successfully."));
    }
}
