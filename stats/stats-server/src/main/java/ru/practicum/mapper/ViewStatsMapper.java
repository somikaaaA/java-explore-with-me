package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.ViewStats;

@UtilityClass
public class ViewStatsMapper {

    public static ViewStatsDto toDto(ViewStats stats) {
        return new ViewStatsDto(
                stats.getApp(),
                stats.getUri(),
                stats.getHits()
        );
    }
}
