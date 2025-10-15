package com.example.spotifyreactiveapi.mapper;

import com.example.spotifyreactiveapi.controller.dto.SpotifyData;
import com.example.spotifyreactiveapi.model.SongModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface SpotifyDataMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "likeCount", constant = "0L")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "releaseDate", expression = "java(parseReleaseDate(spotifyData.getReleaseDate()))")
    @Mapping(target = "releaseYear", expression = "java(parseReleaseYear(spotifyData.getReleaseDate()))")
    SongModel toSongModel(SpotifyData spotifyData);

    default LocalDate parseReleaseDate(String releaseDate) {
        return releaseDate != null ? LocalDate.parse(releaseDate) : null;
    }

    default Integer parseReleaseYear(String releaseDate) {
        return releaseDate != null ? LocalDate.parse(releaseDate).getYear() : null;
    }
}
