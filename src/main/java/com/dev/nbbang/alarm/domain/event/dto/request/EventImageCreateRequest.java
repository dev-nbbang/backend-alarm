package com.dev.nbbang.alarm.domain.event.dto.request;

import com.dev.nbbang.alarm.domain.event.entity.EventImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class EventImageCreateRequest {
    private List<String> imageUrl;

    @Builder
    public EventImageCreateRequest(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static List<EventImage> toEntityList(EventImageCreateRequest request) {
        List<EventImage> eventImages = new ArrayList<>();
        for (String imageUrl : request.getImageUrl()) {
            eventImages.add(EventImage.builder()
                    .imageUrl(imageUrl)
                    .build());
        }

        return eventImages;
    }
}
