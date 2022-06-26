package com.dev.nbbang.alarm.domain.notify.dto;

import com.dev.nbbang.alarm.domain.notify.entity.Notify;
import com.dev.nbbang.alarm.domain.notify.entity.NotifyType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class NotifyDTO {
    private Long notifyId;
    private String notifySender;
    private String notifyReceiver;
    private String notifyDetail;
    private LocalDateTime notifyYmd;
    private String readYn;
    private NotifyType notifyType;
    private Long notifyTypeId;

    @Builder
    public NotifyDTO(Long notifyId, String notifySender, String notifyReceiver, String notifyDetail, LocalDateTime notifyYmd, String readYn, NotifyType notifyType, Long notifyTypeId) {
        this.notifyId = notifyId;
        this.notifySender = notifySender;
        this.notifyReceiver = notifyReceiver;
        this.notifyDetail = notifyDetail;
        this.notifyYmd = notifyYmd;
        this.readYn = readYn;
        this.notifyType = notifyType;
        this.notifyTypeId = notifyTypeId;
    }

    public static NotifyDTO create(Notify notify) {
        return NotifyDTO.builder()
                .notifyId(notify.getNotifyId())
                .notifySender(notify.getNotifySender())
                .notifyReceiver(notify.getNotifyReceiver())
                .notifyDetail(notify.getNotifyDetail())
                .notifyYmd(notify.getNotifyYmd())
                .readYn(notify.getReadYn())
                .notifyType(notify.getNotifyType())
                .notifyTypeId(notify.getNotifyTypeId())
                .build();
    }

    public static List<NotifyDTO> createList(Slice<Notify> notifies) {
        List<NotifyDTO> notifyList = new ArrayList<>();
        for (Notify notify : notifies) {
            notifyList.add(NotifyDTO.builder()
                    .notifyId(notify.getNotifyId())
                    .notifySender(notify.getNotifySender())
                    .notifyReceiver(notify.getNotifyReceiver())
                    .notifyDetail(notify.getNotifyDetail())
                    .notifyYmd(notify.getNotifyYmd())
                    .readYn(notify.getReadYn())
                    .notifyType(notify.getNotifyType())
                    .notifyTypeId(notify.getNotifyTypeId())
                    .build());
        }

        return notifyList;
    }

    public static Notify toEntity(String notifySender, String notifyReceiver, String notifyDetail, NotifyType notifyType, Long notifyTypeId) {
        return Notify.builder()
                .notifySender(notifySender)
                .notifyReceiver(notifyReceiver)
                .notifyDetail(notifyDetail)
                .notifyType(notifyType)
                .notifyTypeId(notifyTypeId)
                .notifyYmd(LocalDateTime.now())
                .readYn("N")
                .build();
    }
}
