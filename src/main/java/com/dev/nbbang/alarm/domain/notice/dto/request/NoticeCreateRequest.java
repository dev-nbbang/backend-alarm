package com.dev.nbbang.alarm.domain.notice.dto.request;

import com.dev.nbbang.alarm.domain.notice.entity.Notice;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class NoticeCreateRequest {
    private Boolean registerNotify; // 알림 테이블 저장 여부 판단
    private String title;
    private String noticeDetail;
    private List<String> imageUrls;

    @Builder
    public NoticeCreateRequest(Boolean registerNotify, String title, String noticeDetail, List<String> imageUrls) {
        this.registerNotify = registerNotify;
        this.title = title;
        this.noticeDetail = noticeDetail;
        this.imageUrls = imageUrls;
    }

    public static Notice toEntity(NoticeCreateRequest request) {
        return Notice.builder()
                .title(request.getTitle())
                .noticeDetail(request.getNoticeDetail())
                .regYmd(LocalDateTime.now())
                .build();
    }
}
