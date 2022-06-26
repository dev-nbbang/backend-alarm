package com.dev.nbbang.alarm.domain.notify.dto.response;

import com.dev.nbbang.alarm.domain.notify.dto.NotifyDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class NotifyStatusChangeResponse {
    private Long notifyId;
    private String readYn;

    @Builder
    public NotifyStatusChangeResponse(Long notifyId, String readYn) {
        this.notifyId = notifyId;
        this.readYn = readYn;
    }

    public static NotifyStatusChangeResponse create(NotifyDTO notify) {
        return NotifyStatusChangeResponse.builder()
                .notifyId(notify.getNotifyId())
                .readYn(notify.getReadYn())
                .build();
    }
}
