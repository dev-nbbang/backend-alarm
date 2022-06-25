package com.dev.nbbang.alarm.domain.notify.controller;

import com.dev.nbbang.alarm.domain.notify.dto.NotifyDTO;
import com.dev.nbbang.alarm.domain.notify.dto.response.FixedNotifyResponse;
import com.dev.nbbang.alarm.domain.notify.dto.response.NotifyStatusChangeResponse;
import com.dev.nbbang.alarm.domain.notify.dto.response.UnreadNotifyCountResponse;
import com.dev.nbbang.alarm.domain.notify.entity.NotifyType;
import com.dev.nbbang.alarm.domain.notify.service.NotifyService;
import com.dev.nbbang.alarm.global.common.CommonSuccessResponse;
import com.dev.nbbang.alarm.global.exception.NbbangException;
import com.dev.nbbang.alarm.global.exception.NotAuthorizationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

        // 인증 회원 체크
        if (memberId.length() < 1)
            throw new NotAuthorizationException("잘못된 접근입니다.", NbbangException.BAD_REQUEST);


        // 안읽은 알림 개수 조회
        Integer unreadNotifyCount = notifyService.unreadNotifyCount(memberId);

        return ResponseEntity.ok(CommonSuccessResponse.response(true, UnreadNotifyCountResponse.create(unreadNotifyCount), "안읽은 알림 개수 조회에 성공했습니다."));
    }

    @DeleteMapping(value = "/{notifyId}")
    public ResponseEntity<?> deleteNotify(@PathVariable(name = "notifyId") Long notifyId) {
        log.info("[Notify Controller Delete Notify] : 특정 알림 삭제하기");

        notifyService.deleteNotify(notifyId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/all")
    public ResponseEntity<?> deleteAllNotify(HttpServletRequest servletRequest) {
        log.info("[Notify Controller Delete All Notify] : 회원의 알림 전체 삭제하기");

        // 회원 아이디 파싱
        String memberId = servletRequest.getHeader("X-Authorization-Id");

        // 인증 회원 체크
        if (memberId.length() < 1)
            throw new NotAuthorizationException("잘못된 접근입니다.", NbbangException.BAD_REQUEST);

        notifyService.deleteAllNotify(memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/{notifyType}")
    public ResponseEntity<?> searchNotifiesWithFilter(@PathVariable(name = "notifyType") NotifyType notifyType, HttpServletRequest servletRequest,
                                                      @RequestParam(name = "notifyId") Long notifyId, @RequestParam(name = "size") int size) {
        log.info(String.valueOf(notifyId));
        log.info("[Notify Controller Search Notifies With Filter] : 필터링 된 회원의 알림 리스트 읽어오기");

        // 회원 아이디 파싱
        String memberId = servletRequest.getHeader("X-Authorization-Id");

        // 인증 회원 체크
        if (memberId.length() < 1)
            throw new NotAuthorizationException("잘못된 접근입니다.", NbbangException.BAD_REQUEST);


        List<NotifyDTO> findNotifies = notifyService.searchNotifyList(notifyType, memberId, notifyId, size);
        String message = "회원의 알림 리스트 조회에 성공했습니다.";

        // 사이즈에 따라 메세지 다르게
        if(findNotifies.size() < size)
            message = "모든 알림 리스트 조회에 성공했습니다.";

        return ResponseEntity.ok(CommonSuccessResponse.response(true, findNotifies, message));
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> searchNotifies(HttpServletRequest servletRequest, @RequestParam(name = "notifyId") Long notifyId, @RequestParam(name = "size") int size) {
        log.info("[Notify Controller Search Notifies] : 회원의 알림 리스트 가져오기");

        // 회원 아이디 파싱
        String memberId = servletRequest.getHeader("X-Authorization-Id");

        // 인증 회원 체크
        if (memberId.length() < 1)
            throw new NotAuthorizationException("잘못된 접근입니다.", NbbangException.BAD_REQUEST);


        List<NotifyDTO> findNotifies = notifyService.searchNotifyList(memberId, notifyId, size);
        String message = "회원의 알림 리스트 조회에 성공했습니다.";

        // 사이즈에 따라 메세지 다르게
        if(findNotifies.size() < size)
            message = "모든 알림 리스트 조회에 성공했습니다.";

        return ResponseEntity.ok(CommonSuccessResponse.response(true, findNotifies, message));
    }

    @GetMapping(value = "/unread/{notifyType}")
    public ResponseEntity<?> searchUnreadNotifiesWithFilter(@PathVariable(name = "notifyType") NotifyType notifyType, HttpServletRequest servletRequest,
                                                            @RequestParam(name = "notifyId") Long notifyId, @RequestParam(name = "size") int size) {
        log.info("[Notify Controller Search Notifies] : 필터링 된 회원의 읽지 않은 알림 리스트 가져오기 ");

        String memberId = servletRequest.getHeader("X-Authorization-Id");

        // 인증 회원 체크
        if (memberId.length() < 1)
            throw new NotAuthorizationException("잘못된 접근입니다.", NbbangException.BAD_REQUEST);


        List<NotifyDTO> findNotifies = notifyService.searchUnreadNotifyList(notifyType, memberId, notifyId, size);
        String message = "회원의 알림 리스트 조회에 성공했습니다.";

        // 사이즈에 따라 메세지 다르게
        if(findNotifies.size() < size)
            message = "모든 알림 리스트 조회에 성공했습니다.";

        return ResponseEntity.ok(CommonSuccessResponse.response(true, findNotifies, message));
    }

    @GetMapping(value = "/unread/all")
    public ResponseEntity<?> searchUnreadNotifies(HttpServletRequest servletRequest, @RequestParam(name = "notifyId") Long notifyId, @RequestParam(name = "size") int size) {
        log.info("[Notify Controller Search Notifies] : 회원의 읽지 않은 알림 리스트 가져오기");

        String memberId = servletRequest.getHeader("X-Authorization-Id");

        // 인증 회원 체크
        if (memberId.length() < 1)
            throw new NotAuthorizationException("잘못된 접근입니다.", NbbangException.BAD_REQUEST);


        List<NotifyDTO> findNotifies = notifyService.searchUnreadNotifyList(memberId, notifyId, size);
        String message = "회원의 알림 리스트 조회에 성공했습니다.";

        // 사이즈에 따라 메세지 다르게
        if(findNotifies.size() < size)
            message = "모든 알림 리스트 조회에 성공했습니다.";

        return ResponseEntity.ok(CommonSuccessResponse.response(true, findNotifies, message));
    }

    @PutMapping(value = "/unread/change/{notifyId}")
    public ResponseEntity<?> changeUnreadStatus(@PathVariable(name = "notifyId") Long notifyId) {
        log.info("[Notify Controller Change Unread Status] : 읽지 않은 알림을 읽은 경우 상태를 변경");

        NotifyDTO updatedNotify = notifyService.changeUnread(notifyId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonSuccessResponse.response(true, NotifyStatusChangeResponse.create(updatedNotify), "알림 읽음처리를 완료했습니다."));
    }

    @GetMapping(value = "/fix")
    public ResponseEntity<?> seachFixedNotify() {
        log.info("[Notify Cotroller Search Fixed Notify] : 공지사항 혹은 이벤트 고정 알림 조회");

        NotifyDTO fixedNotify = notifyService.searchFixNotify();

        return ResponseEntity.ok(CommonSuccessResponse.response(true, FixedNotifyResponse.create(fixedNotify), "고정 알림 조회에 성공했습니다."));
    }
}
