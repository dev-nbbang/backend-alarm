package com.dev.nbbang.alarm.domain.notify.service;

import com.dev.nbbang.alarm.domain.notify.dto.response.MemberLeaveResponse;
import com.dev.nbbang.alarm.domain.notify.exception.FailDeleteNotifiesException;
import com.dev.nbbang.alarm.domain.notify.repository.NotifyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotifyConsumer {
    private final NotifyRepository notifyRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @KafkaListener(topics = "leave-member", groupId = "alarm-group-id")
    public void receiverLeaveMemberMessage(String message, Acknowledgment ack) throws JsonProcessingException {
        log.info("[Leave Member Message Receive] : 회원 탈퇴 이벤트 수신 (알람 서비스)");
        log.info("[Received Message] : " + message);

        MemberLeaveResponse response = objectMapper.readValue(message, MemberLeaveResponse.class);

        try {
            // 받은 메세지를 이용해 회원 아이디의 알림 전체 삭제
            notifyRepository.deleteAllByNotifyReceiver(response.getMemberId());

            // 로직 수행 성공하면 커밋하라고 응답하기.
            ack.acknowledge();
        } catch (FailDeleteNotifiesException e) {
            log.info(response.getMemberId() +"님의 알림 삭제에 실패했습니다.");
        }
    }
}
