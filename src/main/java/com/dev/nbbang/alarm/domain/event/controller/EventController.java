package com.dev.nbbang.alarm.domain.event.controller;

import com.dev.nbbang.alarm.domain.event.dto.EventDTO;
import com.dev.nbbang.alarm.domain.event.dto.request.EventCreateRequest;
import com.dev.nbbang.alarm.domain.event.dto.response.EventListResponse;
import com.dev.nbbang.alarm.domain.event.dto.response.EventResponse;
import com.dev.nbbang.alarm.domain.event.service.EventService;
import com.dev.nbbang.alarm.domain.notify.dto.NotifyDTO;
import com.dev.nbbang.alarm.domain.notify.entity.NotifyType;
import com.dev.nbbang.alarm.domain.notify.service.NotifyService;
import com.dev.nbbang.alarm.global.common.CommonSuccessResponse;
import com.dev.nbbang.alarm.global.exception.GrantAccessDeniedException;
import com.dev.nbbang.alarm.global.exception.NbbangException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final NotifyService notifyService;

    @GetMapping(value = "/{eventId}")
    public ResponseEntity<?> searchEvent(@PathVariable(name = "eventId") Long eventId) {
        log.info("[Event Controller] Search Event : 이벤트 조회");

        // 이벤트 조회
        EventDTO findEvent = eventService.searchEvent(eventId);

        return ResponseEntity.ok(CommonSuccessResponse.response(true, EventResponse.create(findEvent), "이벤트 조회에 성공했습니다."));
    }

    @GetMapping(value = "/list")
    public ResponseEntity<?> searchEventList(@RequestParam(name = "eventId") Long eventId, @RequestParam(name = "size") int size) {
        log.info("[Event Controller] Search Event List : 이벤트 리스트 조회");

        // 이벤트 리스트 조회
        List<EventDTO> findEvents = eventService.searchEventList(eventId, size);

        return ResponseEntity.ok(CommonSuccessResponse.response(true, EventListResponse.createList(findEvents), "이벤트 리스트 조회에 성공했습니다."));
    }

    @PostMapping(value = "/new")
    public ResponseEntity<?> createEvent(@RequestBody EventCreateRequest request, HttpServletRequest servletRequest) {
        log.info("[Event Controller] Create Event : 이벤트 신규 생성");

        // 관리자 확인
        String memberId = servletRequest.getHeader("X-Authorization-Id");
        if(!memberId.equals("manager"))
            throw new GrantAccessDeniedException("권한이 없습니다.", NbbangException.ACCESS_DENIED);

        // 이벤트 및 이미지 저장
        EventDTO savedEvent = eventService.createEvent(EventCreateRequest.toEntity(request), request.getImageUrls());

        // 이벤트 저장 후 필요시 알람 등록
        if(request.getRegisterNotify()) {
            notifyService.createNotify(NotifyDTO.toEntity(memberId, "all", savedEvent.getTitle(), NotifyType.EVENT, savedEvent.getEventId()));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonSuccessResponse.response(true, EventResponse.create(savedEvent), "이벤트가 등록에 성공했습니다."));
    }

    @PutMapping(value = "/{eventId}")
    public ResponseEntity<?> modifyEvent(@PathVariable(name = "eventId") Long eventId, @RequestBody EventCreateRequest request, HttpServletRequest servletRequest) {
        log.info("[Event Controller] Modify Event : 이벤트 수정");

        // 관리자 확인
        String memberId = servletRequest.getHeader("X-Authorization-Id");
        if(!memberId.equals("manager"))
            throw new GrantAccessDeniedException("권한이 없습니다.", NbbangException.ACCESS_DENIED);

        // 이벤트 수정
        EventDTO updatedEvent = eventService.editEvent(eventId, EventCreateRequest.toEntity(request), request.getImageUrls());

        // 이벤트 수정 후 필요시 알람 등록
        if(request.getRegisterNotify()) {
            notifyService.createNotify(NotifyDTO.toEntity(memberId, "all", updatedEvent.getTitle(), NotifyType.EVENT, updatedEvent.getEventId()));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonSuccessResponse.response(true, EventResponse.create(updatedEvent), "이벤트가 수정에 성공했습니다."));
    }

    @DeleteMapping(value = "/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable(name = "eventId") Long eventId, HttpServletRequest servletRequest) {
        log.info("[Event Controller] Delete Event : 이벤트 삭제");

        // 관리자 확인
        String memberId = servletRequest.getHeader("X-Authorization-Id");
        if(!memberId.equals("manager"))
            throw new GrantAccessDeniedException("권한이 없습니다.", NbbangException.ACCESS_DENIED);

        // 이벤트 삭제
        eventService.deleteEvent(eventId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
