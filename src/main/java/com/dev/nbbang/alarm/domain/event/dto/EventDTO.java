package com.dev.nbbang.alarm.domain.event.dto;

import com.dev.nbbang.alarm.domain.event.entity.Event;
import com.dev.nbbang.alarm.domain.event.entity.EventImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class EventDTO {
    private Long eventId;
    private String title;
    private String eventDetail;
    private LocalDateTime regYmd;
    private LocalDate eventStart;
    private LocalDate eventEnd;
    private List<EventImage> eventImages;

    @Builder
    public EventDTO(Long eventId, String title, String eventDetail, LocalDateTime regYmd, LocalDate eventStart, LocalDate eventEnd, List<EventImage> eventImages) {
        this.eventId = eventId;
        this.title = title;
        this.eventDetail = eventDetail;
        this.regYmd = regYmd;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.eventImages = eventImages;
    }

    public static EventDTO create(Event event) {
        return EventDTO.builder()
                .eventId(event.getEventId())
                .title(event.getTitle())
                .eventDetail(event.getEventDetail())
                .regYmd(event.getRegYmd())
                .eventStart(event.getEventStart())
                .eventEnd(event.getEventEnd())
                .eventImages(event.getEventImages())
                .build();
    }

    public static List<EventDTO> createList(Slice<Event> findEvents) {
        List<EventDTO> response = new ArrayList<>();
        for (Event event : findEvents) {
            response.add(EventDTO.builder()
                    .eventId(event.getEventId())
                    .title(event.getTitle())
                    .eventDetail(event.getEventDetail())
                    .regYmd(event.getRegYmd())
                    .eventStart(event.getEventStart())
                    .eventEnd(event.getEventEnd())
                    .eventImages(event.getEventImages())
                    .build());
        }

        return response;
    }
}
