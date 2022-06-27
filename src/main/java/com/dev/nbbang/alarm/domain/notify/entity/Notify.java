package com.dev.nbbang.alarm.domain.notify.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "NOTIFY")
@NoArgsConstructor
@Getter
public class Notify {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTIFY_ID")
    private Long notifyId;

    @Column(name = "NOTIFY_SENDER")
    private String notifySender;

    @Column(name = "NOTIFY_RECEIVER")
    private String notifyReceiver;

    @Column(name = "NOTIFY_DETAIL")
    private String notifyDetail;

    @Column(name = "NOTIFY_YMD")
    private LocalDateTime notifyYmd;

    @Column(name = "READ_YN")
    private String readYn;

    @Column(name = "NOTIFY_TYPE")
    @Enumerated(EnumType.STRING)
    private NotifyType notifyType;

    @Column(name = "NOTIFY_TYPE_ID")
    private Long notifyTypeId;

    @Builder

    public Notify(Long notifyId, String notifySender, String notifyReceiver, String notifyDetail, LocalDateTime notifyYmd, String readYn, NotifyType notifyType, Long notifyTypeId) {
        this.notifyId = notifyId;
        this.notifySender = notifySender;
        this.notifyReceiver = notifyReceiver;
        this.notifyDetail = notifyDetail;
        this.notifyYmd = notifyYmd;
        this.readYn = readYn;
        this.notifyType = notifyType;
        this.notifyTypeId = notifyTypeId;
    }

    // 읽음 처리
    public void changeUnread() {
        this.readYn = "Y";
    }
}
