package com.dev.nbbang.alarm.domain.notify.dto.response;

import com.dev.nbbang.alarm.domain.notify.dto.NotifyDTO;
import com.dev.nbbang.alarm.domain.notify.entity.Notify;
import com.dev.nbbang.alarm.domain.notify.entity.NotifyType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MatchingResponse {
    private String notifySender;
    private String notifyReceiver;
    private String notifyDetail;
    private LocalDateTime notifyYmd;
    private String notifyType;
    private long notifyTypeId;

    @Builder
    public MatchingResponse(String notifySender, String notifyReceiver, String notifyDetail, LocalDateTime notifyYmd, String notifyType, long notifyTypeId) {
        this.notifySender = notifySender;
        this.notifyReceiver = notifyReceiver;
        this.notifyDetail = notifyDetail;
        this.notifyYmd = notifyYmd;
        this.notifyType = notifyType;
        this.notifyTypeId = notifyTypeId;
    }

    public static Notify toEntity(MatchingResponse matchingResponse) {
        return Notify.builder()
                .notifySender(matchingResponse.notifySender)
                .notifyReceiver(matchingResponse.notifyReceiver)
                .notifyDetail(matchingResponse.notifyDetail)
                .notifyType(NotifyType.valueOf(matchingResponse.notifyType))
                .notifyTypeId(matchingResponse.notifyTypeId)
                .notifyYmd(matchingResponse.notifyYmd)
                .readYn("N")
                .build();
    }
}
