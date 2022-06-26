package com.dev.nbbang.alarm.domain.notify.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UnreadNotifyCountResponse {
    private Integer unreadNotifyCount;

    @Builder
    public UnreadNotifyCountResponse(Integer unreadNotifyCount) {
        this.unreadNotifyCount = unreadNotifyCount;
    }

    public static UnreadNotifyCountResponse create(Integer unreadNotifyCount) {
        return UnreadNotifyCountResponse.builder()
                .unreadNotifyCount(unreadNotifyCount)
                .build();
    }
}
