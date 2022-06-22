package com.dev.nbbang.alarm.domain.notify.controller;

import com.dev.nbbang.alarm.domain.notify.dto.response.UnreadNotifyCountResponse;
import com.dev.nbbang.alarm.domain.notify.entity.NotifyType;
import com.dev.nbbang.alarm.domain.notify.service.NotifyService;
import com.dev.nbbang.alarm.global.common.CommonSuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.usertype.UserVersionType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(value = "/notify")
public class NotifyController {
    private final NotifyService notifyService;

    @GetMapping(value = "/unread/count")
    public ResponseEntity<?> unreadNotifyCount(HttpServletRequest servletRequest) {
        log.info("[Notify Controller Unread Notify Count] : 안읽은 알림 개수 가져오기");
        // 회원 아이디 파싱
        String memberId = servletRequest.getHeader("X-Authorization-Id");

        // 회원 아이디 파싱 안되는 경우 잘못된 접근


        // 안읽은 알림 개수 조회
        Integer unreadNotifyCount = notifyService.unreadNotifyCount(memberId);

        return ResponseEntity.ok(CommonSuccessResponse.response(true, UnreadNotifyCountResponse.create(unreadNotifyCount), "안읽은 알림 개수 조회에 성공했습니다."));
    }

    @DeleteMapping(value = "/{notifyId}")
    public ResponseEntity<?> deleteNotify(@PathVariable(name = "notifyId") Long notifyId) {
        log.info("[Notify Controller Delete Notify] : 특정 알림 삭제하기");

        //
        notifyService.deleteNotify(notifyId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/all")
    public ResponseEntity<?> deleteAllNotify(HttpServletRequest servletRequest) {
        log.info("[Notify Controller Delete All Notify] : 회원의 알림 전체 삭제하기");

        // 회원 아이디 파싱
        String memberId = servletRequest.getHeader("X-Authorization-Id");

        notifyService.deleteAllNotify(memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/{notifyType}")
    public ResponseEntity<?> searchNotifiesWithFilter(@PathVariable(name = "notifyType") NotifyType notifyType, HttpServletRequest servletRequest,
                                                      @RequestParam(name = "notifyId") Long notifyId, @RequestParam(name = "size") int size) {
        log.info("[Notify Controller Search Notifies With Filter] : 필터링 된 회원의 알림 리스트 읽어오기");


    }
}
