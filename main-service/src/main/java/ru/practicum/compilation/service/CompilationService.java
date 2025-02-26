package ru.practicum.compilation.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequest;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exception.DataNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional
    public CompilationDto saveCompilation(NewCompilationDto compilation) {
        Set<Event> events;
        if (compilation.getEvents() != null) {
            events = new HashSet<>(eventRepository.findAllByIdIn(compilation.getEvents()));
        } else {
            events = new HashSet<>();
        }
        Compilation newCompilation = CompilationMapper.fromDto(compilation,events);
        return CompilationMapper.toDto(compilationRepository.save(newCompilation));
    }

    @Transactional
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest compilation) {
        Compilation oldCompilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new DataNotFoundException("Подборка не найдена"));
        if (compilation.getEvents() != null && !compilation.getEvents().isEmpty()) {
            Set<Event> events = new HashSet<>(eventRepository.findAllByIdIn(compilation.getEvents()));
            oldCompilation.setEvents(events);
        }
        if (compilation.getPinned() != null) {
            oldCompilation.setPinned(compilation.getPinned());
        }
        if (compilation.getTitle() != null) {
            oldCompilation.setTitle(compilation.getTitle());
        }
        return CompilationMapper.toDto(compilationRepository.save(oldCompilation));
    }

    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilations(Boolean pinned,Integer from,Integer size) {
        Set<Event> eventSet = new HashSet<>();
        Pageable pageable = PageRequest.of(from / size, size);
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageable);
        for (Compilation c : compilations) {
            eventSet.addAll(c.getEvents());
        }
        return compilations.stream().map(CompilationMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new DataNotFoundException("Подборка не найдена"));
        return CompilationMapper.toDto(compilation);
    }
}