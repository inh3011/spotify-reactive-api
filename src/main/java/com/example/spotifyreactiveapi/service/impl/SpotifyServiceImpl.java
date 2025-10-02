package com.example.spotifyreactiveapi.service.impl;

import com.example.spotifyreactiveapi.controller.dto.SpotifyData;
import com.example.spotifyreactiveapi.service.SpotifyService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpotifyServiceImpl implements SpotifyService {


    @Override
    public Mono<String> readFile(MultipartFile jsonFile) {
        return Mono.fromCallable(() -> {
            try (InputStream inputStream = jsonFile.getInputStream()) {
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read file", e);
            }
        });
    }

    @Override
    public Mono<List<SpotifyData>> parseJsonData(String jsonData) {
        return Mono.fromCallable(() ->{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonData);

            List<SpotifyData> dataList = new ArrayList<>();
            for (JsonNode songNode : rootNode) {
                SpotifyData data = mapper.treeToValue(songNode, SpotifyData.class);
                dataList.add(data);
            }
            return dataList;
        });
    }

    @Override
    public Mono<Void> processData(List<SpotifyData> dataList) {
        return Mono.fromRunnable(() -> {
            // 데이터 변환 및 저장 로직
            // 1. Artist 처리
            // 2. Album 처리
            // 3. Song 처리
        });
    }

    @Override
    public Mono<Void> processSpotifyFile(MultipartFile jsonFile) {
        return readFile(jsonFile)
                .flatMap(this::parseJsonData)
                .flatMap(this::processData);
    }
}