package com.dev.nbbang.alarm.domain.notice.dto.response;

import com.dev.nbbang.alarm.domain.notice.entity.NoticeImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class NoticeImageResponse {
    private Long imageId;
    private Long noticeId;
    private String imageUrl;

    @Builder
    public NoticeImageResponse(Long imageId, Long noticeId, String imageUrl) {
        this.imageId = imageId;
        this.noticeId = noticeId;
        this.imageUrl = imageUrl;
    }

    public static List<NoticeImageResponse> entityToResponse(List<NoticeImage> noticeImages) {
        List<NoticeImageResponse> response = new ArrayList<>();
        for (NoticeImage noticeImage : noticeImages) {
            response.add(NoticeImageResponse.builder()
                    .imageId(noticeImage.getImageId())
                    .noticeId(noticeImage.getNotice().getNoticeId())
                    .imageUrl(noticeImage.getImageUrl())
                    .build());
        }

        return response;
    }
}
