package com.dev.nbbang.alarm.domain.notify.service;

import com.dev.nbbang.alarm.domain.notify.dto.response.MemberLeaveResponse;
import com.dev.nbbang.alarm.domain.notify.dto.response.NotifyResponse;
import com.dev.nbbang.alarm.domain.notify.repository.NotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotifyConsumer {
    private final NotifyRepository notifyRepository;

    private final String NOTIFY_QUEUE = "notify.queue";
    private final String MEMBER_LEAVE_ALARM_QUEUE = "member.leave.alarm.queue";

    @Transactional
    @RabbitListener(queues = {MEMBER_LEAVE_ALARM_QUEUE})
    public void receiveMemberLeaveMessage(MemberLeaveResponse response) {
        log.info("[MEMBER LEAVE QUEUE] : 회원 탈퇴 이벤트 수신");
        log.info("[MEMBER LEAVE QUEUE] message : {}", response.toString());

        try {
            // 회원 탈퇴 시 알림 내역 삭제 시작
            notifyRepository.deleteAllByNotifyReceiver(response.getMemberId());
            log.info(response.getMemberId() + "님의 알림 이력 전체 삭제 완료.");
        } catch (Exception e) {
            log.error("회원 탈퇴 메세지 예외 발생 메세지 재처리 필요");

            throw new IllegalStateException("회원 탈퇴 메세지 예외 발생 메세지 재처리 필요");
        }
    }

    @Transactional
    @RabbitListener(queues = {NOTIFY_QUEUE})
    public void receiveNotifyMessage(NotifyResponse response) {
        log.info("[NOTIFY QUEUE] 알림 등록 메세지 수신");
        log.info("[NOTIFY QUEUE] message : {}", response.toString());
        
        try {
            notifyRepository.save(NotifyResponse.toEntity(response));
            log.info("[NOTITFY QUEUE] 알림 등록 성공");
        } catch (Exception e) {
            log.error("알림 등록 예외 발생 재처리 필요");

            throw new IllegalStateException("알림 등록 예외 발생 메세지 재처리 필요");
        }
    }
}
