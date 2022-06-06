package com.dev.nbbang.alarm.domain.notice.dto;

import com.dev.nbbang.alarm.domain.notice.entity.Notice;
import com.dev.nbbang.alarm.domain.notice.entity.NoticeImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class NoticeDTO {
    private Long noticeId;
    private String title;
    private String noticeDetail;
    private LocalDateTime regYmd;
    private List<NoticeImage> noticeImages;

    @Builder
    public NoticeDTO(Long noticeId, String title, String noticeDetail, LocalDateTime regYmd, List<NoticeImage> noticeImages) {
        this.noticeId = noticeId;
        this.title = title;
        this.noticeDetail = noticeDetail;
        this.regYmd = regYmd;
        this.noticeImages = noticeImages;
    }

    public static NoticeDTO create(Notice notice) {
        return NoticeDTO.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .noticeDetail(notice.getNoticeDetail())
                .regYmd(notice.getRegYmd())
                .noticeImages(notice.getNoticeImages())
                .build();
    }

    public static List<NoticeDTO> createList(Slice<Notice> notices) {
        List<NoticeDTO> noticeList = new ArrayList<>();
        for (Notice notice : notices) {
            noticeList.add(NoticeDTO.builder()
                    .noticeId(notice.getNoticeId())
                    .title(notice.getTitle())
                    .noticeDetail(notice.getNoticeDetail())
                    .regYmd(notice.getRegYmd())
                    .noticeImages(notice.getNoticeImages())
                    .build());
        }

        return noticeList;
    }
}
