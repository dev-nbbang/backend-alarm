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
    List<NotifyDTO> searchNotifyList(NotifyType notifyType, String notifyReceiver, Long notifyId, int size);

    // 안읽은 알림 타입별 리스트 조회 (필터링, 페이징)
    List<NotifyDTO> searchUnreadNotifyList(NotifyType notifyType, String notifyReceiver, Long notifyId, int size);

    // 알림 리스트 조회(페이징)
    List<NotifyDTO> searchNotifyList(String notifyReceiver, Long notifyId,  int size);

    // 안읽은 알림 리스트 조회 (페이징)
    List<NotifyDTO> searchUnreadNotifyList(String notifyReceiver, Long notifyId, int size);

    // 알림 저장
    NotifyDTO createNotify(Notify notify);

    // 알림 안읽음 -> 읽음으로 변경
    NotifyDTO changeUnread(Long notifyId);

    // 공지사항 혹은 이벤트 공지 삭제 (공지,이벤트 공지 등록 여부 false 혹은 공지, 이벤트 삭제되는 경우 함께 삭제)
    void deleteNotifyByManager(NotifyType notifyType, Long notifyTypeId);

    // 고정 알림 조회
    NotifyDTO searchFixNotify();
}
