package com.dev.nbbang.alarm.domain.notice.dto.response;

import com.dev.nbbang.alarm.domain.notice.dto.NoticeDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class NoticeListResponse {
    private Long noticeId;
    private String title;
    private LocalDateTime regYmd;

    @Builder
    public NoticeListResponse(Long noticeId, String title, LocalDateTime regYmd) {
        this.noticeId = noticeId;
        this.title = title;
        this.regYmd = regYmd;
    }

    public static List<NoticeListResponse> createList(List<NoticeDTO> notices) {
        List<NoticeListResponse> response = new ArrayList<>();
        for (NoticeDTO notice : notices) {
            response.add(NoticeListResponse.builder()
                    .noticeId(notice.getNoticeId())
                    .title(notice.getTitle())
                    .regYmd(notice.getRegYmd())
                    .build());
        }
        return response;
    }
}
