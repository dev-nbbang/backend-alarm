package com.dev.nbbang.alarm.domain.event.dto.response;

import com.dev.nbbang.alarm.domain.event.dto.EventImageDTO;
import com.dev.nbbang.alarm.domain.event.entity.EventImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class EventImageCreateResponse {
    private Long imageId;
    private Long eventId;
    private String imageUrl;

    @Builder
    public EventImageCreateResponse(Long imageId, Long eventId, String imageUrl) {
        this.imageId = imageId;
        this.eventId = eventId;
        this.imageUrl = imageUrl;
    }

    // DTO -> Response
    public static List<EventImageCreateResponse> dtoToResponse(List<EventImageDTO> eventImages) {
        List<EventImageCreateResponse> eventImageList = new ArrayList<>();
        for (EventImageDTO eventImage : eventImages) {
            eventImageList.add(
                    EventImageCreateResponse.builder()
                            .imageId(eventImage.getImageId())
                            .imageUrl(eventImage.getImageUrl())
                            .eventId(eventImage.getEvent().getEventId())
                            .build());
        }
        return eventImageList;
    }

    // Entity -> Response
    public static List<EventImageCreateResponse> entityToResponse(List<EventImage> eventImages) {
        List<EventImageCreateResponse> eventImageList = new ArrayList<>();
        for (EventImage eventImage : eventImages) {
            eventImageList.add(
                    EventImageCreateResponse.builder()
                            .imageId(eventImage.getImageId())
                            .imageUrl(eventImage.getImageUrl())
                            .eventId(eventImage.getEvent().getEventId())
                            .build());
        }
        return eventImageList;
    }
}
