package com.dev.nbbang.alarm.domain.notify.service;

import com.dev.nbbang.alarm.domain.notify.dto.NotifyDTO;
import com.dev.nbbang.alarm.domain.notify.entity.Notify;
import com.dev.nbbang.alarm.domain.notify.entity.NotifyType;
import com.dev.nbbang.alarm.domain.notify.exception.FailSearchNotifiesException;
import com.dev.nbbang.alarm.domain.notify.exception.NoCreateNotifyException;
import com.dev.nbbang.alarm.domain.notify.exception.NoSuchFixedNotifyException;
import com.dev.nbbang.alarm.domain.notify.exception.NoSuchNotifyException;
import com.dev.nbbang.alarm.domain.notify.repository.NotifyRepository;
import com.dev.nbbang.alarm.global.exception.NbbangException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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
     * @param notifyReceiver 알람을 수신한 회원
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
     * @param notifyType 알림 타입
     * @param notifyReceiver 알림을 수신한 회원
     * @param size 알림을 불러올 개수
     * @return 회원이 수신한 알림 리스트
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
     * @param notifyType 알림 타입
     * @param notifyReceiver 알림을 수신한 회원
     * @param size 알림을 불러올 개수
     * @return 회원이 수신한 알림 리스트 (읽지않음)
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
     * @param notifyReceiver 알림을 수신한 회원
     * @param size 알림을 불렁로 개수
     * @return 회원이 수신한 알림 리스트
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
     * @param notifyReceiver 알림을 수신한 회원
     * @param size 알림을 불러올 개수
     * @return 회원이 수신한 알림 리스트
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
     * @param notify 새로 생성할 알림 정보
     * @return 생성된 알림
     */
    @Override
    @Transactional
    public NotifyDTO createNotify(Notify notify) {
        // 1. 알림 등록
        Notify savedNotify = Optional.of(notifyRepository.save(notify))
                .orElseThrow(() -> new NoCreateNotifyException("알림 등록에 실패했습니다.", NbbangException.NOT_CREATE_NOTIFY));

        return NotifyDTO.create(savedNotify);
    }

    /**
     * 알림 안읽음 상태에서 읽음 상태로 변경하기
     *
     * @param notifyId 고유한 할림 아이디
     * @return 수정된 알림
     */
    @Override
    @Transactional
    public NotifyDTO changeUnread(Long notifyId) {
        // 1. 알림 아이디로 알림 찾기
        Notify findNotify = Optional.ofNullable(notifyRepository.findByNotifyId(notifyId))
                .orElseThrow(() -> new NoSuchNotifyException("등록된 알림이 없습니다.", NbbangException.NOT_FOUND_NOTIFY));

        // 2. 알림을 읽은 상태로 변경한다.
        findNotify.changeUnread();

        return NotifyDTO.create(findNotify);
    }

    /**
     * 공지사항 혹은 이벤트가 삭제되거나 고정 알람 등록 여부를 false한 경우 (수정 시)
     *
     * @param notifyType 알림 타입
     * @param notifyTypeId 알림 타입 아이디
     */
    @Override
    @Transactional
    public void deleteNotifyByManager(NotifyType notifyType, Long notifyTypeId) {
        // 1. 알림 타입과 알림 타입 아이디로 고정 알림 찾기
        Optional.ofNullable(notifyRepository.findNotifyByType(notifyType, notifyTypeId))
                .ifPresent(
                        notify -> {
                            //2. 등록된 알림 삭제
                            notifyRepository.deleteNotifyByNotifyId(notify.getNotifyId());
                        }
                );
    }

    /**
     * 고정 알림 1건을 조회한다. (조건 최신 등록된 공지사항, 이벤트)
     *
     * @return 고정 알림 1건
     */
    @Override
    public NotifyDTO searchFixNotify() {
        // 1. 고정 알림 1건 조회
        List<Notify> fixNotify = notifyRepository.findFixNotify(Arrays.asList(NotifyType.NOTICE, NotifyType.EVENT), PageRequest.of(0, 1));

        if(fixNotify.size() < 1)
            throw new NoSuchFixedNotifyException("등록된 고정 알림이 없습니다.", NbbangException.NOT_FOUND_FIXED_NOTIFY);

        return NotifyDTO.create(fixNotify.get(0));
    }
}
