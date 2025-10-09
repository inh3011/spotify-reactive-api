package com.example.spotifyreactiveapi.service.impl;

import com.example.spotifyreactiveapi.config.SpotifyProperties;
import com.example.spotifyreactiveapi.controller.dto.SpotifyData;
import com.example.spotifyreactiveapi.service.SpotifyService;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpotifyServiceImpl implements SpotifyService {

    private final SpotifyProperties spotifyProperties;

    @Override
    public Mono<InputStream> read() {
        return Mono.fromCallable(() -> {
            String filePath = spotifyProperties.getFilePath();
            Path path = Paths.get(filePath);

            validateFileExists(path);
            validateFileExtension(filePath);

            return Files.newInputStream(path);
        })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<SpotifyData> parse(InputStream inputStream) {
        return Mono.fromCallable(() -> {
            JsonFactory factory = new JsonFactory();
            ObjectMapper objectMapper = new ObjectMapper();

            try (InputStream is = inputStream;
                    JsonParser parser = factory.createParser(is);
                    var validatorFactory = Validation.buildDefaultValidatorFactory()) {

                Validator validator = validatorFactory.getValidator();

                List<SpotifyData> dataList = new ArrayList<>();

                while (parser.nextToken() != null && parser.currentToken() == JsonToken.START_OBJECT) {
                    SpotifyData data = objectMapper.readValue(parser, SpotifyData.class);
                    try {
                        validateData(data, validator);
                        dataList.add(data);
                    } catch (RuntimeException ex) {
                        log.warn("Skip invalid record. reason={}, record={}", ex.getMessage(), data);
                    }
                }

                return dataList;
            } catch (IOException e) {
                log.error("JSON parsing failed. message={}, cause={}", e.getMessage(), e.getClass().getSimpleName());
                throw new RuntimeException("Failed to parse JSON data", e);
            }
        }).flatMapMany(Flux::fromIterable);
    }

    @Override
    public Flux<SpotifyData> processFile() {
        return read()
                .flatMapMany(this::parse);
    }

    private void validateFileExists(Path path) {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("File does not exist: " + path);
        }

        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException("Path is not a regular file: " + path);
        }
    }

    private void validateFileExtension(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path is required and cannot be empty");
        }

        if (!filePath.toLowerCase().endsWith(".json")) {
            throw new IllegalArgumentException("Only .json files are allowed");
        }
    }

    private void validateData(SpotifyData data, Validator validator) {
        var violations = validator.validate(data);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Validation failed: ");
            for (var violation : violations) {
                errorMessage.append(violation.getMessage()).append("; ");
            }
            throw new RuntimeException(errorMessage.toString().trim());
        }
    }
}