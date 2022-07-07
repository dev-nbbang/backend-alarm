package com.dev.nbbang.alarm.domain.notify.dto.response;

import com.dev.nbbang.alarm.domain.notify.entity.Notify;
import com.dev.nbbang.alarm.domain.notify.entity.NotifyType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class NotifyResponse {
    private String notifySender;
    private String notifyReceiver;
    private String notifyDetail;
    private LocalDateTime notifyYmd;
    private NotifyType notifyType;
    private Long notifyTypeId;

    public NotifyResponse(String notifySender, String notifyReceiver, String notifyDetail, LocalDateTime notifyYmd, NotifyType notifyType, Long notifyTypeId) {
        this.notifySender = notifySender;
        this.notifyReceiver = notifyReceiver;
        this.notifyDetail = notifyDetail;
        this.notifyYmd = notifyYmd;
        this.notifyType = notifyType;
        this.notifyTypeId = notifyTypeId;
    }

    public static Notify toEntity(NotifyResponse response) {
        return Notify.builder()
                .notifySender(response.getNotifySender())
                .notifyReceiver(response.getNotifyReceiver())
                .notifyDetail(response.getNotifyDetail())
                .notifyType(response.getNotifyType())
                .notifyTypeId(response.getNotifyTypeId())
                .notifyYmd(response.getNotifyYmd())
                .readYn("N")
                .build();
    }
}
