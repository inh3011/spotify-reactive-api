package com.example.spotifyreactiveapi.batch.writer;

import com.example.spotifyreactiveapi.model.SongModel;
import com.example.spotifyreactiveapi.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpotifyDataWriter implements ItemWriter<SongModel> {

     private SongRepository songRepository;

    @Override
    public void write(Chunk<? extends SongModel> chunk) throws Exception {
        log.info("Writing {} items", chunk.size());

        for (SongModel songModel : chunk) {
            log.debug("Writing song: {} by {}", songModel.getSongTitle(), songModel.getArtistName());
            // 실제로는 songRepository.save(songModel)를 호출
            // songRepository.save(songModel).subscribe();
        }

        log.info("Successfully processed {} items", chunk.size());
    }
}
