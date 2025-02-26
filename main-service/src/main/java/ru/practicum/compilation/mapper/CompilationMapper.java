package ru.practicum.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;

import java.util.Map;
import java.util.Set;

@UtilityClass
public class CompilationMapper {
    public static Compilation fromDto(NewCompilationDto newDto, Set<Event> events) {
        return Compilation.builder()
                .pinned(newDto.isPinned())
                .title(newDto.getTitle())
                .events(events)
                .build();
    }

    public static CompilationDto toDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getEvents().stream()
                        .map((x) -> (EventMapper.toShortDto(x, Map.of(0L,0L))))
                        .toList(),
                compilation.getId(),
                compilation.isPinned(),
                compilation.getTitle()
        );
    }
}
