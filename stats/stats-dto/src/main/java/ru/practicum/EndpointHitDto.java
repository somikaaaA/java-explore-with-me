package ru.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class EndpointHitDto {
    private String app;
    private String uri;
    private String ip;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    public EndpointHitDto(String app, String uri, String ip, LocalDateTime dateTime) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.dateTime = dateTime;
    }
}
