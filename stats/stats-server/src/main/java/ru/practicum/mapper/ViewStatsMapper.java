package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.ViewStats;

@UtilityClass
public class ViewStatsMapper {
    public static ViewStatsDto toDto (ViewStats viewStats) {
        return new ViewStatsDto (
                viewStats.getApp(),
                viewStats.getUri(),
                viewStats.getHits()
        );
    }
}
