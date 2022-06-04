package com.dev.nbbang.alarm.domain.event.dto;

import com.dev.nbbang.alarm.domain.event.entity.Event;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class EventDTO {
    private Long eventId;
    private String title;
    private String eventDetail;
    private LocalDateTime regYmd;

    @Builder
    public EventDTO(Long eventId, String title, String eventDetail, LocalDateTime regYmd) {
        this.eventId = eventId;
        this.title = title;
        this.eventDetail = eventDetail;
        this.regYmd = regYmd;
    }

    public static EventDTO create(Event event) {
        return EventDTO.builder()
                .eventId(event.getEventId())
                .title(event.getTitle())
                .eventDetail(event.getEventDetail())
                .regYmd(event.getRegYmd())
                .build();
    }
}
