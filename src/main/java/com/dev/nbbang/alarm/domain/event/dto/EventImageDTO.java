package com.dev.nbbang.alarm.domain.event.dto;

import com.dev.nbbang.alarm.domain.event.dto.request.EventImageCreateRequest;
import com.dev.nbbang.alarm.domain.event.entity.Event;
import com.dev.nbbang.alarm.domain.event.entity.EventImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class EventImageDTO {
    private Long imageId;
    private String imageUrl;
    private Event event;

    @Builder
    public EventImageDTO(Long imageId, String imageUrl, Event event) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.event = event;
    }

    public static List<EventImage> toEntityList(List<String> imageUrls, Event event) {
        List<EventImage> eventImages = new ArrayList<>();
        for (String imageUrl : imageUrls) {
            eventImages.add(EventImage.builder()
                    .imageUrl(imageUrl)
                    .event(event)
                    .build());
        }

        return eventImages;
    }
}
