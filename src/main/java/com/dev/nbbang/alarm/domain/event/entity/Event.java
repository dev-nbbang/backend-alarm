package com.dev.nbbang.alarm.domain.event.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EVENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
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

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EventImage> eventImages = new ArrayList<>();

    public void updateEvent(String title, String eventDetail, LocalDate eventStart, LocalDate eventEnd) {
        this.title = title;
        this.eventDetail = eventDetail;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
    }
}
