package com.dev.nbbang.alarm.domain.notify.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberLeaveResponse {
    private String memberId;

    @Builder
    public MemberLeaveResponse(String memberId) {
        this.memberId = memberId;
    }
}
