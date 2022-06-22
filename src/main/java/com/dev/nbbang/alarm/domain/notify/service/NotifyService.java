package com.dev.nbbang.alarm.domain.notify.service;

import com.dev.nbbang.alarm.domain.notify.dto.NotifyDTO;
import com.dev.nbbang.alarm.domain.notify.entity.Notify;
import com.dev.nbbang.alarm.domain.notify.entity.NotifyType;

import java.util.List;

public interface NotifyService {
    // 안읽은 알림 개수
    Integer unreadNotifyCount(String notifyReceiver);

    // 특정 알림 삭제
    void deleteNotify(Long notifyId);

    // 나에게 온 알림 전체 삭제
    void deleteAllNotify(String notifyReceiver);

    // 알림 타입별 리스트 조회(필터링, 페이징)
    List<NotifyDTO> searchNotifyList(NotifyType notifyType, String notifyReceiver, int size);

    // 안읽은 알림 타입별 리스트 조회 (필터링, 페이징)
    List<NotifyDTO> searchUnreadNotifyList(NotifyType notifyType, String notifyReceiver, int size);

    // 알림 리스트 조회(페이징)
    List<NotifyDTO> searchNotifyList(String notifyReceiver, int size);

    // 안읽은 알림 리스트 조회 (페이징)
    List<NotifyDTO> searchUnreadNotifyList(String notifyReceiver, int size);

    // 알림 저장
    NotifyDTO createNotify(Notify notify);
}