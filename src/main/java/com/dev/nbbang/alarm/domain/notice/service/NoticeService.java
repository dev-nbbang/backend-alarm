package com.dev.nbbang.alarm.domain.notice.service;

import com.dev.nbbang.alarm.domain.notice.dto.NoticeDTO;
import com.dev.nbbang.alarm.domain.notice.entity.Notice;

import java.util.List;

public interface NoticeService {
    // 공지 생성
    NoticeDTO createNotice(Notice notice, List<String> imageUrls);

    // 공지 상세 내용 조회
    NoticeDTO searchNotice(Long noticeId);

    // 공지 리스트 조회
    List<NoticeDTO> searchNoticeList(Long noticeId, int page);

    // 공지 수정
    NoticeDTO editNotice(Long noticeId, Notice notice, List<String> imageUrls);

    // 공지 삭제
    void deleteNotice(Long noticeId);
}
