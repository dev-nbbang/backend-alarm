package com.dev.nbbang.alarm.domain.notify.service;

import com.dev.nbbang.alarm.domain.notify.dto.NotifyDTO;
import com.dev.nbbang.alarm.domain.notify.entity.Notify;
import com.dev.nbbang.alarm.domain.notify.entity.NotifyType;
import com.dev.nbbang.alarm.domain.notify.exception.FailSearchNotifiesException;
import com.dev.nbbang.alarm.domain.notify.exception.NoSuchNotifyException;
import com.dev.nbbang.alarm.domain.notify.repository.NotifyRepository;
import com.dev.nbbang.alarm.global.exception.NbbangException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotifyServiceImpl implements NotifyService {
    private final NotifyRepository notifyRepository;

    /**
     * 아직 읽지 않은 알림 개수를 가져온다.
     *
     * @param notifyReceiver 알림을 전달받는 회원
     * @return 읽지않은 알림 개수
     */
    @Override
    public Integer unreadNotifyCount(String notifyReceiver) {
        // 1. 개수 가져오기
        return notifyRepository.unreadNotifyCount(notifyReceiver);
    }

    /**
     * 특정 알림을 삭제한다.
     *
     * @param notifyId 고유한 알림 아이디
     */
    @Override
    @Transactional
    public void deleteNotify(Long notifyId) {
        // 1. 특정 알림 지우기 (알림이 있는지 먼저 판단)
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

    /**
     * 회원 아이디로 온 전체 알림을 삭제한다.
     *
     * @param notifyReceiver
     */
    @Override
    @Transactional
    public void deleteAllNotify(String notifyReceiver) {
        // 1. 알림 전체 삭제
        notifyRepository.deleteAllByNotifyReceiver(notifyReceiver);
    }

    /**
     * 회원의 특별 타입의 전체 알림을 가져온다.
     *
     * @param notifyType
     * @param notifyReceiver
     * @param size
     * @return
     */
    @Override
    public List<NotifyDTO> searchNotifyList(NotifyType notifyType, String notifyReceiver, Long notifyId, int size) {
        // 1. 알림 조회하기
        Slice<Notify> findNotifies = Optional.ofNullable(notifyRepository.findNotifiesWithFilter(notifyType, notifyReceiver, notifyId, PageRequest.of(0, size)))
                .orElseThrow(() -> new FailSearchNotifiesException("알림 리스트 조회에 실패했습니다.", NbbangException.FAIL_SEARCH_NOTIFIES));

        return NotifyDTO.createList(findNotifies);
    }

    /**
     * 회원의 읽지 않은 특별 타입의 전체 알림을 가져온다.
     *
     * @param notifyType
     * @param notifyReceiver
     * @param size
     * @return
     */
    @Override
    public List<NotifyDTO> searchUnreadNotifyList(NotifyType notifyType, String notifyReceiver, Long notifyId, int size) {
        // 1. 읽지 않은 알림 리스트 조회하기
        Slice<Notify> findNotifies = Optional.ofNullable(notifyRepository.findUnreadNotifiesWithFilter(notifyType, notifyReceiver, notifyId, PageRequest.of(0, size)))
                .orElseThrow(() -> new FailSearchNotifiesException("알림 리스트 조회에 실패했습니다.", NbbangException.FAIL_SEARCH_NOTIFIES));

        return NotifyDTO.createList(findNotifies);
    }

    /**
     * 회원의 전체 알림을 가져온다.
     *
     * @param notifyReceiver
     * @param size
     * @return
     */
    @Override
    public List<NotifyDTO> searchNotifyList(String notifyReceiver, Long notifyId, int size) {
        // 1. 알림 리스트 조회하기
        Slice<Notify> findNotifies = Optional.ofNullable(notifyRepository.findNotifies(notifyReceiver, notifyId, PageRequest.of(0, size)))
                .orElseThrow(() -> new FailSearchNotifiesException("알림 리스트 조회에 실패했습니다.", NbbangException.FAIL_SEARCH_NOTIFIES));

        return NotifyDTO.createList(findNotifies);
    }

    /**
     * 회원의 안읽은 전체 알림을 가져온다.
     *
     * @param notifyReceiver
     * @param size
     * @return
     */
    @Override
    public List<NotifyDTO> searchUnreadNotifyList(String notifyReceiver, Long notifyId, int size) {
        // 1. 알림 리스트 조회하기
        Slice<Notify> findNotifies = Optional.ofNullable(notifyRepository.findUnreadNotifies(notifyReceiver, notifyId, PageRequest.of(0, size)))
                .orElseThrow(() -> new FailSearchNotifiesException("알림 리스트 조회에 실패했습니다.", NbbangException.FAIL_SEARCH_NOTIFIES));

        return NotifyDTO.createList(findNotifies);
    }

    /**
     * 알림을 생성한다.
     *
     * @param notify
     * @return
     */
    @Override
    public NotifyDTO createNotify(Notify notify) {
        return null;
    }
}
