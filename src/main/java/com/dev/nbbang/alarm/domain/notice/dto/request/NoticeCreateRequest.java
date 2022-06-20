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
    private String title;
    private String noticeDetail;
    private List<String> imageUrls;

    @Builder
    public NoticeCreateRequest(String title, String noticeDetail, List<String> imageUrls) {
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
