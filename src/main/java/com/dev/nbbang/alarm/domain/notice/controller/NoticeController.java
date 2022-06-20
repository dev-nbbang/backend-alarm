package com.dev.nbbang.alarm.domain.notice.controller;

import com.dev.nbbang.alarm.domain.notice.dto.NoticeDTO;
import com.dev.nbbang.alarm.domain.notice.dto.request.NoticeCreateRequest;
import com.dev.nbbang.alarm.domain.notice.dto.response.NoticeListResponse;
import com.dev.nbbang.alarm.domain.notice.dto.response.NoticeResponse;
import com.dev.nbbang.alarm.domain.notice.service.NoticeService;
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
    private final NoticeService noticeService;

    @PostMapping(value = "/new")
    public ResponseEntity<?> createNotice(@RequestBody NoticeCreateRequest request, HttpServletRequest servletRequest) {
        log.info("[Notice Controller] Create Notice : 공지 생성");

        // 관리자 확인
        String memberId = servletRequest.getHeader("X-Authorization-Id");
        if(!memberId.equals("manager"))
            throw new GrantAccessDeniedException("권한이 없습니다.", NbbangException.ACCESS_DENIED);

        // 공지 생성
        NoticeDTO savedNotice = noticeService.createNotice(NoticeCreateRequest.toEntity(request), request.getImageUrls());

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

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
