package com.dev.nbbang.alarm.domain.event.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "EVENT_IMAGE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_ID")
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVENT_ID")
    private Event event;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Builder
    public EventImage(Long imageId, Event event, String imageUrl) {
        this.imageId = imageId;
        this.event = event;
        this.imageUrl = imageUrl;
    }

    // 연관관계 편의 메서드
    public void mappingEvent(Event event) {
        if(this.event != null) {
            event.getEventImages().remove(this);
        }
        this.event = event;
        event.getEventImages().add(this);
    }
}
