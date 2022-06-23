package com.dev.nbbang.alarm.domain.notify.dto.response;

import com.dev.nbbang.alarm.domain.notify.entity.NotifyType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotifyResponse {
    private Long notifyId;
    private String notifySender;
    private String notifyReceiver;
    private String notifyDetail;
    private LocalDateTime notifyYmd;
    private String readYn;
    private NotifyType notifyType;
    private Long notifyTypeId;

    @Builder
    public NotifyResponse(Long notifyId, String notifySender, String notifyReceiver, String notifyDetail, LocalDateTime notifyYmd, String readYn, NotifyType notifyType, Long notifyTypeId) {
        this.notifyId = notifyId;
        this.notifySender = notifySender;
        this.notifyReceiver = notifyReceiver;
        this.notifyDetail = notifyDetail;
        this.notifyYmd = notifyYmd;
        this.readYn = readYn;
        this.notifyType = notifyType;
        this.notifyTypeId = notifyTypeId;
    }
}
