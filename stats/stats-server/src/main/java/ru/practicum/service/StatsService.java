package ru.practicum.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
@Getter
@Setter
public class StatsService {
    private final EndpointHitRepository endpointHitRepository;

    @Transactional
    public EndpointHitDto createHit(EndpointHitDto hit) {
        EndpointHit newEndpointHit = EndpointHitMapper.toModelFromDto(hit);
        return EndpointHitMapper.fromModelToDto(endpointHitRepository.save(newEndpointHit));
    }

    @Transactional(readOnly = true)
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        if (uris == null)
            uris = List.of("0");
        List<ViewStats> newStats;
        if (unique)
            newStats = endpointHitRepository.getStatsWithUniqueIp(uris,start,end);
        else
            newStats = endpointHitRepository.getStats(uris,start,end);
        return newStats.stream()
                .map(ViewStatsMapper::toDto)
                .toList();
    }
}
