package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ApiError {
    private List<String> errors; // Список стектрейсов или описания ошибок
    private String message; // Сообщение об ошибке
    private String reason; // Общее описание причины ошибки
    private String status; // код статуса http-ответа
    private String timestamp; // Дата и время когда произошла ошибка (в формате "yyyy-MM-dd HH:mm:ss")
}
