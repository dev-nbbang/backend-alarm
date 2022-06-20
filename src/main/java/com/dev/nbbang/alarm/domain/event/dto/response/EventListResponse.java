package com.dev.nbbang.alarm.domain.event.dto.response;

import com.dev.nbbang.alarm.domain.event.dto.EventDTO;
import com.dev.nbbang.alarm.domain.event.entity.EventImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class EventListResponse {
    private Long eventId;
    private String title;
    private String eventDetail;
    private LocalDateTime regYmd;;
    private LocalDate eventStart;
    private LocalDate eventEnd;

    @Builder
    public EventListResponse(Long eventId, String title, String eventDetail, LocalDateTime regYmd, LocalDate eventStart, LocalDate eventEnd) {
        this.eventId = eventId;
        this.title = title;
        this.eventDetail = eventDetail;
        this.regYmd = regYmd;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
    }

    public static List<EventListResponse> createList(List<EventDTO> events) {
        List<EventListResponse> response = new ArrayList<>();
        for (EventDTO event : events) {
            response.add(EventListResponse.builder()
                    .eventId(event.getEventId())
                    .title(event.getTitle())
                    .eventDetail(event.getEventDetail())
                    .regYmd(event.getRegYmd())
                    .eventStart(event.getEventStart())
                    .eventEnd(event.getEventEnd())
                    .build());
        }

        return response;
    }
}
