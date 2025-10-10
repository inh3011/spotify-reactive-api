package com.example.spotifyreactiveapi.batch.reader;

import com.example.spotifyreactiveapi.config.SpotifyProperties;
import com.example.spotifyreactiveapi.controller.dto.SpotifyData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonFileReader implements ItemReader<SpotifyData> {

    private final SpotifyProperties spotifyProperties;
    private final ResourceLoader resourceLoader;

    private Iterator<SpotifyData> dataIterator;
    private boolean initialized = false;

    @Override
    public SpotifyData read()
            throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!initialized) {
            initializeReader();
        }

        if (dataIterator != null && dataIterator.hasNext()) {
            return dataIterator.next();
        }
        log.info("No more data to read");
        return null;
    }

    private void initializeReader() throws IOException {
        String filePath = spotifyProperties.getFilePath();

        Resource jsonFile = resourceLoader.getResource(filePath);

        ObjectMapper objectMapper = new ObjectMapper();
        List<SpotifyData> spotifyDataList = objectMapper.readValue(
                jsonFile.getInputStream(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, SpotifyData.class));

        this.dataIterator = spotifyDataList.iterator();
        this.initialized = true;

        log.info("총 {}개의 데이터를 읽었습니다.", spotifyDataList.size());
    }
}
