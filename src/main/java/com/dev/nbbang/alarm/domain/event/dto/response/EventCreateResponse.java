package com.dev.nbbang.alarm.domain.event.dto.response;

import com.dev.nbbang.alarm.domain.event.dto.EventDTO;
import com.dev.nbbang.alarm.domain.event.dto.EventImageDTO;
import com.dev.nbbang.alarm.domain.event.entity.EventImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class EventCreateResponse {
    private Long eventId;
    private String title;
    private String eventDetail;
    private LocalDateTime regYmd;
    private LocalDate eventStart;
    private LocalDate eventEnd;
    private List<EventImageCreateResponse> eventImages;

    @Builder
    public EventCreateResponse(Long eventId, String title, String eventDetail, LocalDateTime regYmd, LocalDate eventStart, LocalDate eventEnd, List<EventImageCreateResponse> eventImages) {
        this.eventId = eventId;
        this.title = title;
        this.eventDetail = eventDetail;
        this.regYmd = regYmd;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.eventImages = eventImages;
    }

    public static EventCreateResponse create(EventDTO event) {
        return EventCreateResponse.builder()
                .eventId(event.getEventId())
                .title(event.getTitle())
                .eventDetail(event.getEventDetail())
                .regYmd(event.getRegYmd())
                .eventStart(event.getEventStart())
                .eventEnd(event.getEventEnd())
                .eventImages(EventImageCreateResponse.entityToResponse(event.getEventImages()))
                .build();
    }


}
