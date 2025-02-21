package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.EndpointHitDto;
import ru.practicum.model.EndpointHit;

@UtilityClass
public class EndpointHitMapper {
    public static EndpointHit fromDto(EndpointHitDto hitDto) {
        return new EndpointHit(
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                hitDto.getTimestamp()
        );
    }

    public static EndpointHitDto toDto(EndpointHit hit) {
        return new EndpointHitDto(
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp()
        );
    }
}
