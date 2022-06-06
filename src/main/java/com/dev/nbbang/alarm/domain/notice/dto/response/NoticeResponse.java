package com.dev.nbbang.alarm.domain.notice.dto.response;

import com.dev.nbbang.alarm.domain.notice.dto.NoticeDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class NoticeResponse {
    private Long noticeId;
    private String title;
    private String noticeDetail;
    private LocalDateTime regYmd;
    private List<NoticeImageResponse> noticeImages;

    @Builder
    public NoticeResponse(Long noticeId, String title, String noticeDetail, LocalDateTime regYmd, List<NoticeImageResponse> noticeImages) {
        this.noticeId = noticeId;
        this.title = title;
        this.noticeDetail = noticeDetail;
        this.regYmd = regYmd;
        this.noticeImages = noticeImages;
    }

    public static NoticeResponse create(NoticeDTO notice) {
        return NoticeResponse.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .noticeDetail(notice.getNoticeDetail())
                .regYmd(notice.getRegYmd())
                .noticeImages(NoticeImageResponse.entityToResponse(notice.getNoticeImages()))
                .build();
    }
}
