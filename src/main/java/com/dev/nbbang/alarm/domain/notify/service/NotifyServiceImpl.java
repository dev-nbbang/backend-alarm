package com.dev.nbbang.alarm.domain.notify.service;

import com.dev.nbbang.alarm.domain.notify.dto.NotifyDTO;
import com.dev.nbbang.alarm.domain.notify.entity.Notify;
import com.dev.nbbang.alarm.domain.notify.entity.NotifyType;
import com.dev.nbbang.alarm.domain.notify.exception.NoSuchNotifyException;
import com.dev.nbbang.alarm.domain.notify.repository.NotifyRepository;
import com.dev.nbbang.alarm.global.exception.NbbangException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotifyServiceImpl implements NotifyService {
    private final NotifyRepository notifyRepository;

    /**
     * 아직 읽지 않은 알림 개수를 가져온다.
     *
     * @param notifyReceiver
     * @return
     */
    @Override
    public Integer unreadNotifyCount(String notifyReceiver) {
        // 1. 개수 가져오기
        return notifyRepository.unreadNotifyCount(notifyReceiver);
    }

    @Override
    public void deleteNotify(Long notifyId) {
        // 1. 특정 알림 지우기
        Optional.ofNullable(notifyRepository.findByNotifyId(notifyId))
                .ifPresentOrElse(
                        logic -> {
                            notifyRepository.deleteNotifyByNotifyId(notifyId);
                        },
                        () -> {
                            throw new NoSuchNotifyException("삭제할 알림이 없습니다.", NbbangException.NOT_FOUND_NOTIFY);
                        }
                );
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
