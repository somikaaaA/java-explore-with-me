package ru.practicum.users.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.dto.UserDto;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.users.mapper.RequestMapper;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.model.Request;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.RequestRepository;
import ru.practicum.users.repository.UsersRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UsersRepository usersRepository;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    @Transactional
    public UserDto saveUser(NewUserRequest newUserRequest) {
        User newUser = UserMapper.fromDto(newUserRequest);
        return UserMapper.toDto(usersRepository.save(newUser));
    }

    @Transactional(readOnly = true)
    public List<UserDto> findUsers(List<Long> ids,Integer from, Integer size) {
        if (ids == null)
            ids = List.of(0L);
        List<User> users = usersRepository.getUsers(ids,from,size);
        return users.stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Transactional
    public void deleteUser(Long userId) {
        usersRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    public User findUser(Long userId) {
         return usersRepository.findById(userId)
                 .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));
    }

    //Запросы на участие
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequests(Long userId) {
        return requestRepository.findByUserId(userId).stream()
                .map(RequestMapper::toDto)
                .toList();
    }

    @Transactional
    public ParticipationRequestDto saveRequest(Long userId, Long eventId) {
        //проверка запроса
        Optional<Request> oldRequest = requestRepository.findAllByEventIdAndUserId(eventId,userId);
        if (oldRequest.isPresent())
            throw new DataIntegrityViolationException("Данный запрос уже существует");
        //проверка события
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException("Событие не найдено"));
        if (!event.getState().equals(State.PUBLISHED))
            throw new DataIntegrityViolationException("Событие не в статусе ожидания");
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests()))
            throw new DataIntegrityViolationException("Исчерпан лимит");
        //проверка пользователя
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));
        if (userId.equals(event.getInitiator().getId()))
            throw new DataIntegrityViolationException("Инициатор события не может быть участником");
        //сохранение запроса
        LocalDateTime lcd = LocalDateTime.now();
        Request request = new Request(lcd,user,event,"PENDING");
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            request.setStatus("CONFIRMED");
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Transactional
    public ParticipationRequestDto canselRequest(Long userId, Long requestId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));
        Request request = requestRepository.findById(requestId)
                        .orElseThrow(() -> new DataNotFoundException("Запрос не найден"));
        request.setStatus("CANCELED");
        return RequestMapper.toDto(requestRepository.save(request));
    }
}