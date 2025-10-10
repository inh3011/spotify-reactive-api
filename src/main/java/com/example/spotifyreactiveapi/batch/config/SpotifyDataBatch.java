package com.example.spotifyreactiveapi.batch.config;

import com.example.spotifyreactiveapi.batch.processor.SpotifyDataProcessor;
import com.example.spotifyreactiveapi.batch.reader.JsonFileReader;
import com.example.spotifyreactiveapi.batch.writer.SpotifyDataWriter;
import com.example.spotifyreactiveapi.controller.dto.SpotifyData;
import com.example.spotifyreactiveapi.model.SongModel;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class SpotifyDataBatch {
    private JobRepository jobRepository;
    private PlatformTransactionManager platformTransactionManager;

    private final JsonFileReader jsonFileReader;
    private final SpotifyDataProcessor spotifyDataProcessor;
    private final SpotifyDataWriter spotifyDataWriter;

    @Bean
    public Step spotifyDataProcessingStep() {
        return new StepBuilder("spotifyDataProcessingStep", jobRepository)
                .<SpotifyData, SongModel>chunk(10, platformTransactionManager)
                .reader(jsonFileReader)
                .processor(spotifyDataProcessor)
//                .writer(spotifyDataWriter)
                .build();
    }

    @Bean
    public Job spotifyDataProcessingJob() {
        System.out.println("Creating spotifyDataProcessingJob");

        return new JobBuilder("spotifyDataProcessingJob", jobRepository)
                .start(spotifyDataProcessingStep())
                .build();
    }
}