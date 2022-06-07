package com.dev.nbbang.alarm.domain.event.service;

import com.dev.nbbang.alarm.domain.event.dto.EventDTO;
import com.dev.nbbang.alarm.domain.event.entity.Event;
import com.dev.nbbang.alarm.domain.event.entity.EventImage;
import com.dev.nbbang.alarm.domain.event.exception.NoCreateEventException;
import com.dev.nbbang.alarm.domain.event.exception.NoSuchEventException;
import com.dev.nbbang.alarm.domain.event.repository.EventImageRepository;
import com.dev.nbbang.alarm.domain.event.repository.EventRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    @Mock
    private EventImageRepository eventImageRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    @DisplayName("이벤트 서비스 : 이벤트 조회 성공")
    void 이벤트_조회_성공() {
        // given
        given(eventRepository.findByEventId(anyLong())).willReturn(allEventBuilder());

        // when
        EventDTO findEvent = eventService.searchEvent(1L);

        // then
        assertThat(findEvent.getEventId()).isEqualTo(1L);
        assertThat(findEvent.getEventImages().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("이벤트 서비스 : 이벤트 조회 실패")
    void 이벤트_조회_실패() {
        // given
        given(eventRepository.findByEventId(anyLong())).willThrow(NoSuchEventException.class);

        // then
        assertThrows(NoSuchEventException.class, () -> eventService.searchEvent(1L));
    }

    @Test
    @DisplayName("이벤트 서비스 : 이벤트 저장 성공")
    void 이벤트_저장_성공() {
        // given
        given(eventRepository.save(any())).willReturn(testEvent(1L));
        given(eventImageRepository.saveAll(any())).willReturn(eventImageBuilder());

        EventDTO savedEvent = eventService.createEvent(testEvent(1L), Arrays.asList("test1", "test2", "test3"));

        // then
        assertThat(savedEvent.getEventId()).isEqualTo(1L);
        assertThat(savedEvent.getEventImages().get(0).getImageUrl()).isEqualTo("test1");
        assertThat(savedEvent.getEventImages().get(1).getImageUrl()).isEqualTo("test2");
        assertThat(savedEvent.getEventImages().get(2).getImageUrl()).isEqualTo("test3");
    }

    @Test
    @DisplayName("이벤트 서비스 : 이벤트 저장 실패")
    void 이벤트_저장_실패() {
        // given
        given(eventRepository.save(any())).willThrow(NoCreateEventException.class);

        // then
        assertThrows(NoCreateEventException.class, () -> eventService.createEvent(testEvent(1L), Arrays.asList("test1", "test2", "test3")));
    }

    @Test
    @DisplayName("이벤트 서비스 : 이벤트 수정 성공")
    void 이벤트_수정_성공() {
        // given
        given(eventRepository.findByEventId(anyLong())).willReturn(allEventBuilder());
        given(eventImageRepository.saveAll(anyList())).willReturn(updateImageBuilder());

        // when
        EventDTO updatedEvent = eventService.editEvent(1L, updateEvent(), Arrays.asList("test4", "test5"));

        // then
        assertThat(updatedEvent.getTitle()).isEqualTo("update title");
        assertThat(updatedEvent.getEventDetail()).isEqualTo("update eventDetail");
        assertThat(updatedEvent.getEventImages().size()).isEqualTo(2);
        assertThat(updatedEvent.getEventImages().get(0).getImageUrl()).isEqualTo("test4");
        assertThat(updatedEvent.getEventImages().get(1).getImageUrl()).isEqualTo("test5");
    }

    @Test
    @DisplayName("이벤트 서비스 : 이벤트 수정 실패")
    void 이벤트_수정_실패() {
        // given
        given(eventRepository.findByEventId(anyLong())).willThrow(NoSuchEventException.class);

        // then
        assertThrows(NoSuchEventException.class, () -> eventService.editEvent(1L, updateEvent(), Arrays.asList("test4", "test5")));
    }

    @Test
    @DisplayName("이벤트 서비스 : 이벤트 삭제 성공")
    void 이벤트_삭제_성공() {
        // given
        given(eventRepository.findByEventId(anyLong())).willReturn(allEventBuilder());
//        given(eventImageRepository.findAllByEvent(any())).willReturn(null);

        // when
        eventService.deleteEvent(1L);

        // then
//        verify(eventImageRepository, times(1)).deleteAllByEvent(any());
        verify(eventRepository, times(1)).deleteByEventId(anyLong());
    }

    @Test
    @DisplayName("이벤트 서비스 : 이벤트 삭제 실패")
    void 이벤트_삭제_실패() {
        // given
        given(eventRepository.findByEventId(anyLong())).willThrow(NoSuchEventException.class);

        // then
        assertThrows(NoSuchEventException.class, () -> eventService.deleteEvent(1L));
    }

    @Test
    @DisplayName("이벤트 서비스 : 이벤트 리스트 전체 조회 성공")
    void 이벤트_리스트_전체_조회_성공() {
        // given
        given(eventRepository.findEventByEventIdLessThanOrderByEventIdDesc(anyLong(), any())).willReturn(sliceEvent());

        // when
        List<EventDTO> findEvents = eventService.searchEventList(1000L, 2);

        assertThat(findEvents.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("이벤트 서비스 : 이벤트 리스트 전체 조회 실패")
    void 이벤트_전체_리스트_조회_실패() {
        // given
        given(eventRepository.findEventByEventIdLessThanOrderByEventIdDesc(anyLong(), any())).willThrow(NoSuchEventException.class);

        // when
        assertThrows(NoSuchEventException.class, () -> eventService.searchEventList(1000L, 2));
    }

    private static Event allEventBuilder() {
        return Event.builder()
                .eventId(1L)
                .title("all title")
                .eventDetail("all eventDetail")
                .regYmd(LocalDateTime.now())
                .eventStart(LocalDate.now())
                .eventEnd(LocalDate.now().plusWeeks(1))
                .eventImages(eventImageBuilder())
                .build();
    }

    private static Event updateEvent() {
        return Event.builder()
                .eventId(1L)
                .title("update title")
                .eventDetail("update eventDetail")
                .regYmd(LocalDateTime.now())
                .eventStart(LocalDate.now())
                .eventEnd(LocalDate.now().plusWeeks(1))
                .eventImages(updateImageBuilder())
                .build();
    }

    private static List<EventImage> eventImageBuilder() {
        List<EventImage> eventImages = new ArrayList<>();

        eventImages.add(testEventImages(1L, "test1"));
        eventImages.add(testEventImages(2L, "test2"));
        eventImages.add(testEventImages(3L, "test3"));

        return eventImages;
    }

    private static List<EventImage> updateImageBuilder() {
        List<EventImage> eventImages = new ArrayList<>();

        eventImages.add(testEventImages(4L, "test4"));
        eventImages.add(testEventImages(5L, "test5"));

        return eventImages;
    }

    private static EventImage testEventImages(Long imageId, String imageUrl) {
        return EventImage.builder()
                .imageId(imageId)
                .imageUrl(imageUrl)
                .build();
    }

    private static Event testEvent(Long eventId) {
        return Event.builder()
                .eventId(eventId)
                .title("title")
                .eventDetail("eventDetail")
                .regYmd(LocalDateTime.now())
                .eventStart(LocalDate.now())
                .eventEnd(LocalDate.now().plusWeeks(1))
                .build();
    }

    private static Slice<Event> sliceEvent() {
        Event event1 = testEvent(1L);
        Event event2 = testEvent(2L);

        return new SliceImpl<>(Arrays.asList(event1,event2));
    }
}