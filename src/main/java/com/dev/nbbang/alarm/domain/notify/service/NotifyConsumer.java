package com.dev.nbbang.alarm.domain.notify.service;

import com.dev.nbbang.alarm.domain.notify.dto.NotifyDTO;
import com.dev.nbbang.alarm.domain.notify.dto.response.MatchingResponse;
import com.dev.nbbang.alarm.domain.notify.dto.response.MemberLeaveResponse;
import com.dev.nbbang.alarm.domain.notify.dto.response.NotifyResponse;
import com.dev.nbbang.alarm.domain.notify.exception.FailCreateNotifyException;
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

    @Transactional
    @KafkaListener(topics = "send-notify", groupId =  "alarm-group-id")
    public void receiveNotifyMessage(String message, Acknowledgment ack) throws JsonProcessingException {
        log.info("[Send Notify Message Receiver] : 전송된 알림 등록 메세지 이벤트 수신");
        log.info("[Received Message] : {}", message);

        NotifyResponse response = objectMapper.readValue(message, NotifyResponse.class);

        try {
            // 수신한 알림 등록
            notifyRepository.save(NotifyResponse.toEntity(response));

            // 로직 수행 성공하면 커밋하라고 응답하기
            ack.acknowledge();
        } catch (FailCreateNotifyException e) {
            log.error("수신한 알림 메세지 등록에 실패했습니다.");
            log.error("실패한 메세지 : {}", response.toString());
        }
    }

    // 이거 notify로 통합 필요
    @Transactional
    @KafkaListener(topics = "matching-info-notify", groupId = "alarm-group-id")
    public void matchingFailMessage(String message, Acknowledgment ack) throws JsonProcessingException {
        MatchingResponse response = objectMapper.readValue(message, MatchingResponse.class);

        try {
            // 받은 메세지를 이용해 회원 아이디의 알림 전체 삭제
            notifyRepository.save(MatchingResponse.toEntity(response));

            // 로직 수행 성공하면 커밋하라고 응답하기.
            ack.acknowledge();
        } catch (FailDeleteNotifiesException e) {
            log.info(response.getNotifySender() +"님의 알림 받기 실패했습니다.");
        }
    }

}
