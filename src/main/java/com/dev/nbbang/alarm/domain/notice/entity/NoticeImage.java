package com.dev.nbbang.alarm.domain.notice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "NOTICE_IMAGE")
public class NoticeImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_ID")
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOTICE_ID")
    private Notice notice;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Builder
    public NoticeImage(Long imageId, Notice notice, String imageUrl) {
        this.imageId = imageId;
        this.notice = notice;
        this.imageUrl = imageUrl;
    }

    //  연관관계 편의 메소드
    public void mappingNotice(Notice notice) {
        if(this.notice != null)
            notice.getNoticeImages().remove(this);

        this.notice = notice;
        notice.getNoticeImages().add(this);

    }
}
