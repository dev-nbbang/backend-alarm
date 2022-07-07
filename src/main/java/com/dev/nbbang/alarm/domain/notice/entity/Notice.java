package com.dev.nbbang.alarm.domain.notice.entity;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "NOTICE")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTICE_ID")
    private Long noticeId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "NOTICE_DETAIL")
    private String noticeDetail;

    @Column(name = "REG_YMD")
    private LocalDateTime regYmd;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<NoticeImage> noticeImages = new ArrayList<>();

    // 공지사항 업데이트
    public void updateNotice(String title, String noticeDetail) {
        this.title = title;
        this.noticeDetail = noticeDetail;
    }
}
