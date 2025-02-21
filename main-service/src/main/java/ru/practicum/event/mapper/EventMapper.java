package ru.practicum.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.category.model.Category;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.util.Map;

import static ru.practicum.ConstantDateTime.FORMATTER;

@UtilityClass
public class EventMapper {

    public static EventFullDto toFullDto(Event event, Map<Long, Long> views) {
        String publishedOn = "";
        if (event.getPublishedOn() != null)
            publishedOn = FORMATTER.format(event.getEventDate());
        return new EventFullDto(
                event.getAnnotation(),
                CategoryMapper.toDto(event.getCategory()),
                event.getConfirmedRequests(),
                LocalDateTime.now().toString(),
                event.getDescription(),
                FORMATTER.format(event.getEventDate()),
                event.getId(),
                UserMapper.toShortDto(event.getInitiator()),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                publishedOn,
                event.getRequestModeration(),
                event.getState().toString(),
                event.getTitle(),
                views.get(event.getId())
        );
    }

    public static EventShortDto toShortDto(Event event, Map<Long, Long> views) {
        return new EventShortDto(
                event.getAnnotation(),
                CategoryMapper.toDto(event.getCategory()),
                event.getConfirmedRequests(),
                FORMATTER.format(event.getEventDate()),
                event.getId(),
                UserMapper.toShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                views.get(event.getId())
        );
    }

    public static Event fromNewDto(NewEventDto event, Category category, User user) {
        boolean paid = false;
        if (event.getPaid() != null)
            paid = event.getPaid();
        boolean reqModeration = true;
        if (event.getRequestModeration() != null)
            reqModeration = event.getRequestModeration();
        int limit = 0;
        if (event.getParticipantLimit() != null)
            limit = event.getParticipantLimit();
       return Event.builder()
               .annotation(event.getAnnotation())
               .category(category)
               .description(event.getDescription())
               .eventDate(LocalDateTime.parse(event.getEventDate(), FORMATTER))
               .confirmedRequests(0)
               .location(event.getLocation())
               .createdOn(LocalDateTime.now())
               .paid(paid)
               .participantLimit(limit)
               .requestModeration(reqModeration)
               .title(event.getTitle())
               .initiator(user)
               .state(State.PENDING)
               .build();
    }
}
