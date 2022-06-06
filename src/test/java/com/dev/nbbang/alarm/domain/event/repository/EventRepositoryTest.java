package com.dev.nbbang.alarm.domain.event.repository;

import com.dev.nbbang.alarm.domain.event.entity.Event;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.awt.print.Pageable;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventRepositoryTest {
    @Autowired
    private EventRepository eventRepository;

    @Test
    @DisplayName("이벤트 레포지토리 : 이벤트 조회")
    void 이벤트_조회() {
        // given
        Event savedEvent = eventRepository.save(testEventBuilder());

        // when \
        Event findEvent = eventRepository.findByEventId(savedEvent.getEventId());

        // then
        assertThat(savedEvent.getEventId()).isEqualTo(findEvent.getEventId());
        assertThat(savedEvent.getTitle()).isEqualTo(findEvent.getTitle());
        assertThat(savedEvent.getEventDetail()).isEqualTo(findEvent.getEventDetail());
    }

    @Test
    @DisplayName("이벤트 레포지토리 : 이벤트 등록")
    void 이벤트_등록() {
        // given
        Event event = testEventBuilder();

        // when
        Event savedEvent = eventRepository.save(event);

        // then
        assertThat(savedEvent.getTitle()).isEqualTo(event.getTitle());
        assertThat(savedEvent.getEventDetail()).isEqualTo(event.getEventDetail());
    }

    @Test
    @DisplayName("이벤트 레포지토리 : 이벤트 삭제")
    void 이벤트_삭제() {
        // given
        Event savedEvent = eventRepository.save(testEventBuilder());

        // when
        eventRepository.deleteByEventId(savedEvent.getEventId());

        // then
        Event findEvent = eventRepository.findByEventId(savedEvent.getEventId());
        assertThat(findEvent).isNull();
    }

    @Test
    @DisplayName("이벤트 레포지토리 : 이벤트 리스트 조회")
    void 이벤트_리스트_조회() {
        // given
        eventRepository.save(testEventBuilder());
        eventRepository.save(testEventBuilder());
        eventRepository.save(testEventBuilder());

        // when
        Slice<Event> findEventList = eventRepository.findEventByEventIdLessThanOrderByEventIdDesc(10000L, PageRequest.of(0, 2));

        // then
        assertThat(findEventList.getSize()).isEqualTo(2);

    }

    private static Event testEventBuilder() {
        return Event.builder()
                .eventId(1L)
                .title("title")
                .eventDetail("eventDetail")
                .regYmd(LocalDateTime.now())
                .build();
    }
}