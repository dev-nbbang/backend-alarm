package com.dev.nbbang.alarm.domain.notify.dto.response;

import com.dev.nbbang.alarm.domain.notify.dto.NotifyDTO;
import com.dev.nbbang.alarm.domain.notify.entity.NotifyType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class FixedNotifyResponse {
    private Long notifyId;
    private String notifySender;
    private String notifyReceiver;
    private String notifyDetail;
    private LocalDateTime notifyYmd;
    private NotifyType notifyType;
    private Long notifyTypeId;

    @Builder
    public FixedNotifyResponse(Long notifyId, String notifySender, String notifyReceiver, String notifyDetail, LocalDateTime notifyYmd, NotifyType notifyType, Long notifyTypeId) {
        this.notifyId = notifyId;
        this.notifySender = notifySender;
        this.notifyReceiver = notifyReceiver;
        this.notifyDetail = notifyDetail;
        this.notifyYmd = notifyYmd;
        this.notifyType = notifyType;
        this.notifyTypeId = notifyTypeId;
    }

    public static FixedNotifyResponse create(NotifyDTO notify) {
        return FixedNotifyResponse.builder()
                .notifyId(notify.getNotifyId())
                .notifySender(notify.getNotifySender())
                .notifyReceiver(notify.getNotifyReceiver())
                .notifyDetail(notify.getNotifyDetail())
                .notifyYmd(notify.getNotifyYmd())
                .notifyType(notify.getNotifyType())
                .notifyTypeId(notify.getNotifyTypeId())
                .build();
    }
}
