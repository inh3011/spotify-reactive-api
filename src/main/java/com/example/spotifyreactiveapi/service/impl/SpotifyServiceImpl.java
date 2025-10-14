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
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpotifyServiceImpl implements SpotifyService {

    private final SpotifyProperties spotifyProperties;

    private static final int BUFFER_SIZE_64KB = 65536;

    @Override
    public Mono<BufferedInputStream> read() {
        return Mono.fromCallable(() -> {
            String filePath = spotifyProperties.getFilePath();
            Path path = Paths.get(filePath);

            validateFileExists(path);
            validateFileExtension(filePath);

            return new BufferedInputStream(Files.newInputStream(path), BUFFER_SIZE_64KB);
        })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<SpotifyData> parse(BufferedInputStream bufferedInputStream) {
        return Flux.create(emitter -> {
            JsonFactory factory = new JsonFactory();
            ObjectMapper objectMapper = new ObjectMapper();

            try (JsonParser parser = factory.createParser(bufferedInputStream);
                    var validatorFactory = Validation.buildDefaultValidatorFactory()) {

                Validator validator = validatorFactory.getValidator();

                validateEmptyJson(parser, emitter);

                while (parser.nextToken() != null) {
                    if (parser.currentToken() == JsonToken.START_OBJECT) {
                        SpotifyData data = objectMapper.readValue(parser, SpotifyData.class);
                        validateData(data, validator);
                        emitter.next(data);
                    }
                }

                emitter.complete();
            } catch (IOException e) {
                log.error("JSON parsing failed. message={}, cause={}", e.getMessage(), e.getClass().getSimpleName());
                emitter.error(new RuntimeException("Failed to parse JSON data", e));
            }
        });
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

    private void validateEmptyJson(JsonParser parser, FluxSink<SpotifyData> emitter) throws IOException {
        JsonToken jsonToken = parser.nextToken();
        if (jsonToken == null) {
            emitter.error(new IllegalArgumentException("JSON data is empty"));
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