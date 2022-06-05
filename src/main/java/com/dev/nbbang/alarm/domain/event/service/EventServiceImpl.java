package com.dev.nbbang.alarm.domain.event.service;

import com.dev.nbbang.alarm.domain.event.dto.EventDTO;
import com.dev.nbbang.alarm.domain.event.entity.Event;
import com.dev.nbbang.alarm.domain.event.entity.EventImage;
import com.dev.nbbang.alarm.domain.event.exception.FailDeleteEventImagesException;
import com.dev.nbbang.alarm.domain.event.exception.NoCreateEventException;
import com.dev.nbbang.alarm.domain.event.exception.NoSuchEventException;
import com.dev.nbbang.alarm.domain.event.repository.EventImageRepository;
import com.dev.nbbang.alarm.domain.event.repository.EventRepository;
import com.dev.nbbang.alarm.global.exception.NbbangException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventImageRepository eventImageRepository;

    /**
     * 특정 이벤트 내용을 조회한다.
     *
     * @param eventId 고유한 이벤트 번호
     * @return 이벤트 상세정보 데이터
     */
    @Override
    public EventDTO searchEvent(Long eventId) {
        // 1. 이벤트 아이디로 이벤트 조회
        Event findEvent = Optional.ofNullable(eventRepository.findByEventId(eventId))
                .orElseThrow(() -> new NoSuchEventException("등록되지 않은 이벤트입니다.", NbbangException.NOT_FOUND_EVENT));

        return EventDTO.create(findEvent);
    }

    /**
     * 새로운 이벤트를 생성한다.
     *
     * @param event 새로 생성할 이벤트 데이터
     * @return 새로 생성된 이벤트
     */
    @Transactional
    @Override
    public EventDTO createEvent(Event event, List<EventImage> eventImages) {
        // 1. 이벤트 저장
        Event savedEvent = Optional.of(eventRepository.save(event))
                .orElseThrow(() -> new NoCreateEventException("이벤트 생성에 실패했습니다.", NbbangException.NOT_CREATE_EVENT));

        // 2. 이벤트 이미지 저장
        if(eventImages.size() > 0) {
            List<EventImage> savedEventImages = eventImageRepository.saveAll(eventImages);

            // 양방향 매핑
            for (EventImage eventImage : savedEventImages) {
                eventImage.mappingEvent(savedEvent);
            }
        }

        return EventDTO.create(savedEvent);
    }

    /**
     * 이벤트를 수정한다.
     *
     * @param eventId 고유한 이벤트 아이디
     * @param event   수정할 이벤트 데이터
     * @return 수정된 이벤트 데이터
     */
    @Transactional
    @Override
    public EventDTO editEvent(Long eventId, Event event) {
        // 1. 이벤트를 찾는다.
        Event findEvent = Optional.ofNullable(eventRepository.findByEventId(eventId))
                .orElseThrow(() -> new NoSuchEventException("등록되지 않은 이벤트입니다.", NbbangException.NOT_FOUND_EVENT));

        // 2. 이벤트를 수정한다.
        findEvent.updateEvent(event.getTitle(), event.getEventDetail(), event.getEventStart(), event.getEventEnd());

        // 3. 수정한 이미지가 있는 경우
        if(event.getEventImages().size() > 0) {
            eventImageRepository.deleteAllByEvent(findEvent);
            findEvent.getEventImages().clear();

            List<EventImage> savedEventImages = eventImageRepository.saveAll(event.getEventImages());
            for (EventImage savedEventImage : savedEventImages) {
                savedEventImage.mappingEvent(findEvent);
            }
        }

        return EventDTO.create(findEvent);
    }

    /**
     * 특정 이벤트를 삭제한다.
     *
     * @param eventId 고유한 이벤트 아이디
     */
    @Transactional
    @Override
    public void deleteEvent(Long eventId) {
        // 1. 이벤트 아이디로 이벤트 조회
        Event findEvent = Optional.ofNullable(eventRepository.findByEventId(eventId))
                .orElseThrow(() -> new NoSuchEventException("등록되지 않은 이벤트입니다.", NbbangException.NOT_FOUND_EVENT));

        // 2. 이미지 삭제
        eventImageRepository.deleteAllByEvent(findEvent);

        // 3. 이미지 삭제 확인
        Optional.ofNullable(eventImageRepository.findAllByEvent(findEvent))
                .ifPresent(
                        exception -> {
                            throw new FailDeleteEventImagesException("이벤트 이미지 삭제에 실패했습니다.", NbbangException.FAIL_DELETE_IMAGES);
                        }
                );

        // 4. 이벤트 삭제
        eventRepository.deleteByEventId(eventId);
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
        // 1. 이벤트 리스트 조회
        Slice<Event> findEvents = eventRepository.findEventByEventIdLessThanOrderByEventId(eventId, PageRequest.of(0, size));
        if(findEvents.getSize() == 0) {
            throw new NoSuchEventException("더 이상 조회할 이벤트가 없습니다.", NbbangException.NOT_FOUND_EVENT);
        }

        return EventDTO.createList(findEvents);
    }
}
