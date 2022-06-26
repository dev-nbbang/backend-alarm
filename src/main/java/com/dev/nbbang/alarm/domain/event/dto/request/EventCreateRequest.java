package com.dev.nbbang.alarm.domain.event.dto.request;

import com.dev.nbbang.alarm.domain.event.dto.EventImageDTO;
import com.dev.nbbang.alarm.domain.event.entity.Event;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class EventCreateRequest {
    private Boolean registerNotify; // 알림 등록 여부
    private String title;
    private String eventDetail;
    private LocalDate eventStart;
    private LocalDate eventEnd;
    private List<String> imageUrls;

    @Builder
    public EventCreateRequest(Boolean registerNotify, String title, String eventDetail, LocalDate eventStart, LocalDate eventEnd, List<String> imageUrls) {
        this.registerNotify = registerNotify;
        this.title = title;
        this.eventDetail = eventDetail;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.imageUrls = imageUrls;
    }

    public static Event toEntity(EventCreateRequest request) {
        return Event.builder()
                .title(request.getTitle())
                .eventDetail(request.eventDetail)
                .regYmd(LocalDateTime.now())
                .eventStart(request.getEventStart())
                .eventEnd(request.getEventEnd())
                .build();
    }
}
