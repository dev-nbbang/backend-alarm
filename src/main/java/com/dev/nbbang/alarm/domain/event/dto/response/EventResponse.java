package com.dev.nbbang.alarm.domain.event.dto.response;

import com.dev.nbbang.alarm.domain.event.dto.EventDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class EventResponse {
    private Long eventId;
    private String title;
    private String eventDetail;
    private LocalDateTime regYmd;
    private LocalDate eventStart;
    private LocalDate eventEnd;
    private List<EventImageResponse> eventImages;

    @Builder
    public EventResponse(Long eventId, String title, String eventDetail, LocalDateTime regYmd, LocalDate eventStart, LocalDate eventEnd, List<EventImageResponse> eventImages) {
        this.eventId = eventId;
        this.title = title;
        this.eventDetail = eventDetail;
        this.regYmd = regYmd;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.eventImages = eventImages;
    }

    public static EventResponse create(EventDTO event) {
        return EventResponse.builder()
                .eventId(event.getEventId())
                .title(event.getTitle())
                .eventDetail(event.getEventDetail())
                .regYmd(event.getRegYmd())
                .eventStart(event.getEventStart())
                .eventEnd(event.getEventEnd())
                .eventImages(EventImageResponse.entityToResponse(event.getEventImages()))
                .build();
    }

}
