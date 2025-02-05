package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.EndpointHitDto;
import ru.practicum.model.EndpointHit;

@UtilityClass
public class EndpointHitMapper {
    public static EndpointHit toModelFromDto(EndpointHitDto hitDto) {
        return new EndpointHit(
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                hitDto.getDateTime()
        );
    }

    public static EndpointHitDto fromModelToDto(EndpointHit hit) {
        return new EndpointHitDto(
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getDateTime()
        );
    }
}
