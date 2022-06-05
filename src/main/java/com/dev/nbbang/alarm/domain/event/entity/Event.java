package com.dev.nbbang.alarm.domain.event.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "EVENT_START")
    private LocalDate eventStart;

    @Column(name = "EVENT_END")
    private LocalDate eventEnd;

    @OneToMany(mappedBy = "event")
    private List<EventImage> eventImages = new ArrayList<>();

    @Builder
    public Event(Long eventId, String title, String eventDetail, LocalDateTime regYmd, LocalDate eventStart, LocalDate eventEnd, List<EventImage> eventImages) {
        this.eventId = eventId;
        this.title = title;
        this.eventDetail = eventDetail;
        this.regYmd = regYmd;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.eventImages = eventImages;
    }

    public void updateEvent(String title, String eventDetail, LocalDate eventStart, LocalDate eventEnd) {
        this.title = title;
        this.eventDetail = eventDetail;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
    }
}
