package com.dev.nbbang.alarm.domain.event.service;

import com.dev.nbbang.alarm.domain.event.dto.EventDTO;
import com.dev.nbbang.alarm.domain.event.entity.Event;
import com.dev.nbbang.alarm.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    /**
     * 특정 이벤트 내용을 조회한다.
     *
     * @param eventId 고유한 이벤트 번호
     * @return 이벤트 상세정보 데이터
     */
    @Override
    public EventDTO searchEvent(Long eventId) {
        // 1. 이벤트 아이디로 이벤트 조회

        // 2. 연관된 이미지 조회
        return null;
    }

    /**
     * 새로운 이벤트를 생성한다.
     *
     * @param event 새로 생성할 이벤트 데이터
     * @return 새로 생성된 이벤트
     */
    @Override
    public EventDTO createEvent(Event event) {
        return null;
    }

    /**
     * 이벤트를 수정한다.
     *
     * @param eventId 고유한 이벤트 아이디
     * @param event   수정할 이벤트 데이터
     * @return 수정된 이벤트 데이터
     */
    @Override
    public EventDTO editEvent(Long eventId, Event event) {
        return null;
    }

    /**
     * 특정 이벤트를 삭제한다.
     *
     * @param eventId 고유한 이벤트 아이디
     */
    @Override
    public void deleteEvent(Long eventId) {

    }

    /**
     * 한번에 보여질 이벤트 리스트를 조회한다.
     *
     * @param eventId 시작 이벤트 아이디
     * @param size    한번에 보여줄 이벤트 개수
     * @return 이벤트 정보를 담은 리스트
     */
    @Override
    public List<EventDTO> searchEventList(Long eventId, int size) {
        return null;
    }
}
