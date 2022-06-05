package com.dev.nbbang.alarm.domain.event.controller;

import com.dev.nbbang.alarm.domain.event.dto.EventDTO;
import com.dev.nbbang.alarm.domain.event.dto.request.EventCreateRequest;
import com.dev.nbbang.alarm.domain.event.dto.response.EventCreateResponse;
import com.dev.nbbang.alarm.domain.event.service.EventService;
import com.dev.nbbang.alarm.global.common.CommonSuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping(value = "/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping(value = "/new")
    public ResponseEntity<?> createEvent(@RequestBody EventCreateRequest request, HttpServletRequest servletRequest) {

        // 관리자 확인
        String memberId = servletRequest.getHeader("X-Authorization-Id");

        // 이벤트 및 이미지 저장
        EventDTO savedEvent = eventService.createEvent(EventCreateRequest.toEntity(request), request.getImageUrls());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonSuccessResponse.response(true, EventCreateResponse.create(savedEvent),"이벤트가 등록에 성공했습니다."));
    }
}
