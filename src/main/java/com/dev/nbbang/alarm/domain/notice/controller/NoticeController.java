package com.dev.nbbang.alarm.domain.notice.controller;

import com.dev.nbbang.alarm.domain.notice.dto.NoticeDTO;
import com.dev.nbbang.alarm.domain.notice.dto.request.NoticeCreateRequest;
import com.dev.nbbang.alarm.domain.notice.dto.response.NoticeListResponse;
import com.dev.nbbang.alarm.domain.notice.dto.response.NoticeResponse;
import com.dev.nbbang.alarm.domain.notice.service.NoticeService;
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
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/notice")
public class NoticeController {
    private final NoticeService noticeService; // 공지
    private final NotifyService notifyService; // 알림

    @PostMapping(value = "/new")
    public ResponseEntity<?> createNotice(@RequestBody NoticeCreateRequest request, HttpServletRequest servletRequest) {
        log.info("[Notice Controller] Create Notice : 공지 생성");

        // 관리자 확인
        String memberId = servletRequest.getHeader("X-Authorization-Id");
        if(!memberId.equals("manager"))
            throw new GrantAccessDeniedException("권한이 없습니다.", NbbangException.ACCESS_DENIED);

        // 공지 생성
        NoticeDTO savedNotice = noticeService.createNotice(NoticeCreateRequest.toEntity(request), request.getImageUrls());

        // 공지 생성 후 알림 등록
        if(request.getRegisterNotify()) {
            notifyService.createNotify(NotifyDTO.toEntity(memberId, "all", savedNotice.getTitle(), NotifyType.NOTICE, savedNotice.getNoticeId()));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonSuccessResponse.response(true, NoticeResponse.create(savedNotice), "공지사항 생성에 성공했습니다."));
    }

    @GetMapping(value = "/{noticeId}")
    public ResponseEntity<?> searchNotice(@PathVariable(name = "noticeId") Long noticeId) {
        log.info("[Notice Contoller] Search Notice");

        // 공지 조회
        NoticeDTO findNotice = noticeService.searchNotice(noticeId);

        return ResponseEntity.ok(CommonSuccessResponse.response(true, NoticeResponse.create(findNotice), "공지사항 조회에 성공했습니다."));
    }

    @GetMapping(value = "/list")
    public ResponseEntity<?> searchNoticeList(@RequestParam(name = "noticeId") Long noticeId, @RequestParam(name = "size") int size) {
        log.info("[Notice Controller] Search Notice List : 공지사항 리스트 조회");

        // 공지사항 리스트 조회
        List<NoticeDTO> findNoticeList = noticeService.searchNoticeList(noticeId, size);
        String message = "공지사항 리스트 조회에 성공했습니다.";

        if (findNoticeList.size() < size) message = "더 이상 조회할 공지사항이 없습니다.";

        return ResponseEntity.ok(CommonSuccessResponse.response(true, NoticeListResponse.createList(findNoticeList), message));
    }

    @PutMapping(value = "/{noticeId}")
    public ResponseEntity<?> editNotice(@PathVariable(name = "noticeId") Long noticeId, @RequestBody NoticeCreateRequest request, HttpServletRequest servletRequest) {
        log.info("[Notice Controller] Edit Notice");

        // 관리자 확인
        String memberId = servletRequest.getHeader("X-Authorization-Id");
        if(!memberId.equals("manager"))
            throw new GrantAccessDeniedException("권한이 없습니다.", NbbangException.ACCESS_DENIED);

        // 공지사항 수정
        NoticeDTO updatedNotice = noticeService.editNotice(noticeId, NoticeCreateRequest.toEntity(request), request.getImageUrls());

        // 공지사항 수정 시 알림 등록 여부 true : 고정 알림 등록, false : 기존 고정 알림 삭제
        // 기존 알림을 찾은 뒤 기존 알림 수정? (현홍이랑 고민해볼 필요있음)
        if(request.getRegisterNotify())
            notifyService.createNotify(NotifyDTO.toEntity(memberId, "all", updatedNotice.getTitle(), NotifyType.NOTICE, updatedNotice.getNoticeId()));

        // 공지사항 수정의 경우 알람 찾은 뒤 테이블에서 삭제 (이미 등록 안된 경우에는 예외가 터지는데..?) -> ifPresent로 해결'
        else
            notifyService.deleteNotifyByManager(NotifyType.NOTICE, updatedNotice.getNoticeId());


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonSuccessResponse.response(true, NoticeResponse.create(updatedNotice), "공지사항 수정에 성공했습니다."));
    }

    @DeleteMapping(value = "/{noticeId}")
    public ResponseEntity<?> deleteNotice(@PathVariable(name = "noticeId")Long noticeId, HttpServletRequest servletRequest) {
        log.info("[Notice Controller] Delete Notice : 공지사항 삭제");

        // 관리자 확인
        String memberId = servletRequest.getHeader("X-Authorization-Id");
        if(!memberId.equals("manager"))
            throw new GrantAccessDeniedException("권한이 없습니다.", NbbangException.ACCESS_DENIED);

        // 공지사항 삭제
        noticeService.deleteNotice(noticeId);

        // 삭제된 공지 사항 알림 삭제 (알림이 있는지 확인 후 삭제)
        notifyService.deleteNotifyByManager(NotifyType.NOTICE, noticeId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
