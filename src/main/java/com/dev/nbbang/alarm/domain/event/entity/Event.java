package com.dev.nbbang.alarm.domain.event.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "EVENT")
@NoArgsConstructor
@Getter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID")
    private Long eventId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "EVENT_DETAIL")
    private String eventDetail;

    @Column(name = "REG_YMD")
    private LocalDateTime regYmd;

    @Builder
    public Event(Long eventId, String title, String eventDetail, LocalDateTime regYmd) {
        this.eventId = eventId;
        this.title = title;
        this.eventDetail = eventDetail;
        this.regYmd = regYmd;
    }
}
