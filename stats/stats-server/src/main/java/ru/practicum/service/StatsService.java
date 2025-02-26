package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.exception.StatsException;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class StatsService {
    private final EndpointHitRepository endpointHitRepository;

    @Transactional
    public EndpointHitDto createHit(EndpointHitDto hit) {
        EndpointHit newEndpointHit = EndpointHitMapper.fromDto(hit);
        return EndpointHitMapper.toDto(endpointHitRepository.save(newEndpointHit));
    }

    @Transactional(readOnly = true)
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start.isAfter(end)) {
            throw new StatsException("Начало промежутка позже окончания");
        }
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
