package com.dev.nbbang.alarm.domain.notice.dto;

import com.dev.nbbang.alarm.domain.notice.entity.Notice;
import com.dev.nbbang.alarm.domain.notice.entity.NoticeImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class NoticeImageDTO {
    private Long imageId;
    private Notice notice;
    private String imageUrl;

    @Builder
    public NoticeImageDTO(Long imageId, Notice notice, String imageUrl) {
        this.imageId = imageId;
        this.notice = notice;
        this.imageUrl = imageUrl;
    }

    public static List<NoticeImage> toEntityList(List<String> imageUrls, Notice notice) {
        List<NoticeImage> noticeImages = new ArrayList<>();
        for (String imageUrl : imageUrls) {
            noticeImages.add(NoticeImage.builder()
                    .notice(notice)
                    .imageUrl(imageUrl)
                    .build());
        }

        return noticeImages;
    }
}
