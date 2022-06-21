package com.dev.nbbang.alarm.domain.notify.service;

import com.dev.nbbang.alarm.domain.notify.dto.NotifyDTO;
import com.dev.nbbang.alarm.domain.notify.entity.Notify;
import com.dev.nbbang.alarm.domain.notify.entity.NotifyType;

import java.util.List;

public class NotifyServiceImpl implements NotifyService {

    /**
     * 아직 읽지 않은 알림 개수를 가져온다.
     *
     * @param notifyReceiver
     * @return
     */
    @Override
    public Integer unreadNotifyCount(String notifyReceiver) {

        return null;
    }

    @Override
    public void deleteNotify(Long notifyId) {

    }

    @Override
    public void deleteAllNotify(String notifyReceiver) {

    }

    @Override
    public List<NotifyDTO> searchNotifyList(NotifyType notifyType, String notifyReceiver, int size) {
        return null;
    }

    @Override
    public List<NotifyDTO> searchUnreadNotifyList(NotifyType notifyType, String notifyReceiver, int size) {
        return null;
    }

    @Override
    public List<NotifyDTO> searchNotifyList(String notifyReceiver, int size) {
        return null;
    }

    @Override
    public List<NotifyDTO> searchUnreadNotifyList(String notifyReceiver, int size) {
        return null;
    }

    @Override
    public NotifyDTO createNotify(Notify notify) {
        return null;
    }
}
