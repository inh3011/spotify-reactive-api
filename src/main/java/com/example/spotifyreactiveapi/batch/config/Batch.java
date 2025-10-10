package com.example.spotifyreactiveapi.batch.config;

import com.example.spotifyreactiveapi.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class Batch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final SongRepository songRepository;


}
