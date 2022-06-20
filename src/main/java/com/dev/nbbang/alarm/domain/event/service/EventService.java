package com.dev.nbbang.alarm.domain.event.service;

import com.dev.nbbang.alarm.domain.event.dto.EventDTO;
import com.dev.nbbang.alarm.domain.event.entity.Event;
import com.dev.nbbang.alarm.domain.event.entity.EventImage;

import java.util.List;

public interface EventService {
    // 이벤트 게시글 조회
    EventDTO searchEvent(Long eventId);

    // 이벤트 게시글 등록
    EventDTO createEvent(Event event, List<String> imageUrls);

    // 이벤트 게시글 수정
    EventDTO editEvent(Long eventId, Event event, List<String> imageUrls);

    // 이벤트 게시글 삭제
    void deleteEvent(Long eventId);

    // 이벤트 게시글 리스트 조회
    List<EventDTO> searchEventList(Long eventId, int size);
}
