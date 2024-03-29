package com.dev.nbbang.alarm.domain.notify.service;

import com.dev.nbbang.alarm.domain.notify.dto.NotifyDTO;
import com.dev.nbbang.alarm.domain.notify.entity.Notify;
import com.dev.nbbang.alarm.domain.notify.entity.NotifyType;
import com.dev.nbbang.alarm.domain.notify.exception.FailSearchNotifiesException;
import com.dev.nbbang.alarm.domain.notify.exception.NoSuchFixedNotifyException;
import com.dev.nbbang.alarm.domain.notify.exception.NoSuchNotifyException;
import com.dev.nbbang.alarm.domain.notify.repository.NotifyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotifyServiceImplTest {
    @Mock
    private NotifyRepository notifyRepository;

    @InjectMocks
    private NotifyServiceImpl notifyService;

    @Test
    @DisplayName("알림 서비스 : 안읽은 알림 개수 조회 성공")
    void 안읽은_알림_개수_조회_성공() {
        // given
        given(notifyRepository.unreadNotifyCount(anyString())).willReturn(2);

        // when
        Integer unreadNotifyCount = notifyService.unreadNotifyCount("receiver");

        // then
        assertThat(unreadNotifyCount).isEqualTo(2);
    }

    @Test
    @DisplayName("알림 서비스 : 특정 알림 삭제 성공")
    void 특정_알림_삭제_성공() {
        // given
        given(notifyRepository.findByNotifyId(anyLong())).willReturn(testNotify(1L, "receiver", "detail", NotifyType.QNA));

        // when
        notifyService.deleteNotify(1L);

        // then
        verify(notifyRepository, times(1)).deleteNotifyByNotifyId(anyLong());
    }

    @Test
    @DisplayName("알림 서비스 : 특정 알림 삭제 실패")
    void 특정_알림_삭제_실패() {
        // given
        given(notifyRepository.findByNotifyId(anyLong())).willReturn(null);

        // then
        assertThrows(NoSuchNotifyException.class, () -> notifyService.deleteNotify(1L));
    }

    @Test
    @DisplayName("알림 서비스 : 나에게 온 알림 전체 삭제 성공")
    void 나에게_온_알림_전체_삭제_성공() {
        // when
        notifyService.deleteAllNotify("receiver");

        // given
        verify(notifyRepository, times(1)).deleteAllByNotifyReceiver(anyString());
    }

    @Test
    @DisplayName("알림 서비스 : 알림 타입별 리스트 조회 성공(필터링)")
    void 알림_타입별_리스트_조회_성공() {
        // given
        given(notifyRepository.findNotifiesWithFilter(any(), anyString(), anyLong(), any())).willReturn(testNotifies());

        // when
        List<NotifyDTO> findNotifies = notifyService.searchNotifyList(NotifyType.QNA, "receiver", 1000L, 2);

        // then
        assertThat(findNotifies.size()).isEqualTo(2);
        for (NotifyDTO notify : findNotifies) {
            assertThat(notify.getNotifyType()).isEqualTo(NotifyType.QNA);
        }
    }

    @Test
    @DisplayName("알림 서비스 : 알림 타입별 리스트 조회 실패(필터링)")
    void 알림_타입별_리스트_조회_실패() {
        // given
        given(notifyRepository.findNotifiesWithFilter(any(), anyString(), anyLong(), any())).willThrow(FailSearchNotifiesException.class);

        // then
        assertThrows(FailSearchNotifiesException.class, () -> notifyService.searchNotifyList(NotifyType.QNA, "receiver", 1000L, 2));
    }

    @Test
    @DisplayName("알림 서비스 : 안읽은 알림 타입별 리스트 조회 성공(필터링)")
    void 안읽은_알림_타입별_리스트_조회_성공() {
        // given
        given(notifyRepository.findUnreadNotifiesWithFilter(any(), anyString(), anyLong(), any())).willReturn(testNotifies());

        // when
        List<NotifyDTO> findNotifies = notifyService.searchUnreadNotifyList(NotifyType.QNA, "receiver", 1000L, 2);

        // then
        assertThat(findNotifies.size()).isEqualTo(2);
        for (NotifyDTO notify : findNotifies) {
            assertThat(notify.getNotifyType()).isEqualTo(NotifyType.QNA);
        }
    }

    @Test
    @DisplayName("알림 서비스 : 안읽은 알림 타입별 리스트 조회 실패(필터링)")
    void 안읽은_알림_타입별_리스트_조회_실패() {
        // given
        given(notifyRepository.findUnreadNotifiesWithFilter(any(), anyString(), anyLong(), any())).willThrow(FailSearchNotifiesException.class);

        // then
        assertThrows(FailSearchNotifiesException.class, () -> notifyService.searchUnreadNotifyList(NotifyType.QNA, "receiver", 1000L, 2));

    }

    @Test
    @DisplayName("알림 서비스 : 알림 리스트 조회 성공")
    void 알림_리스트_조회_성공() {
        // given
        given(notifyRepository.findNotifies(anyString(), anyLong(), any())).willReturn(testNotifiesNotFiltered());

        // when
        List<NotifyDTO> findNotifies = notifyService.searchNotifyList("receiver", 1000L, 3);

        // then
        assertThat(findNotifies.size()).isEqualTo(3);
        assertThat(findNotifies.get(1).getNotifyType()).isEqualTo(NotifyType.QNA);
        assertThat(findNotifies.get(2).getNotifyType()).isEqualTo(NotifyType.PARTY);
    }

    @Test
    @DisplayName("알림 서비스 : 알림 리스트 조회 실패")
    void 알림_리스트_조회_실패() {
        // given
        given(notifyRepository.findNotifies(anyString(), anyLong(), any())).willThrow(FailSearchNotifiesException.class);

        // then
        assertThrows(FailSearchNotifiesException.class, () -> notifyService.searchNotifyList("receiver", 1000L, 2));
    }

    @Test
    @DisplayName("알림 서비스 : 안읽은 알림 리스트 조회 성공")
    void 안읽은_알림_리스트_조회_성공() {
        // given
        given(notifyRepository.findUnreadNotifies(anyString(), anyLong(), any())).willReturn(testNotifiesNotFiltered());

        // when
        List<NotifyDTO> findNotifies = notifyService.searchUnreadNotifyList("receiver", 1000L, 3);

        // then
        assertThat(findNotifies.size()).isEqualTo(3);
        assertThat(findNotifies.get(1).getNotifyType()).isEqualTo(NotifyType.QNA);
        assertThat(findNotifies.get(2).getNotifyType()).isEqualTo(NotifyType.PARTY);
    }

    @Test
    @DisplayName("알림 서비스 : 안읽은 알림 리스트 조회 실패")
    void 안읽은_알림_리스트_조회_실패() {
        // given
        given(notifyRepository.findUnreadNotifies(anyString(), anyLong(), any())).willThrow(FailSearchNotifiesException.class);

        // then
        assertThrows(FailSearchNotifiesException.class, () -> notifyService.searchUnreadNotifyList("receiver", 1000L, 2));
    }

    @Test
    @DisplayName("알림 서비스 : 알림 읽음 여부 처리 성공")
    void 알림_읽음_처리_성공() {
        // given
        given(notifyRepository.findByNotifyId(anyLong())).willReturn(testNotify(1L, "receiver", "detail", NotifyType.NOTICE));

        // when
        NotifyDTO updatedNotify = notifyService.changeUnread(1L);

        // then
        assertThat(updatedNotify.getNotifyId()).isEqualTo(1L);
        assertThat(updatedNotify.getReadYn()).isEqualTo("Y");
    }

    @Test
    @DisplayName("알림 서비스 : 알림 읽음 여부 처리 실패")
    void 알림_읽음_처리_실패() {
        // given
        given(notifyRepository.findByNotifyId(anyLong())).willThrow(NoSuchNotifyException.class);

        // then
        assertThrows(NoSuchNotifyException.class, () -> notifyService.changeUnread(1L));
    }

    @Test
    @DisplayName("알림 서비스 : 공지 혹으 이벤트 삭제 시 고정 알림 삭제 성공")
    void 고정_알림_삭제_성공() {
        // given
        given(notifyRepository.findNotifyByType(any(), anyLong())).willReturn(testNotify(1L, "receiver", "detail", NotifyType.NOTICE));

        // when
        notifyService.deleteNotifyByManager(NotifyType.NOTICE, 1L);

        // then
        verify(notifyRepository, times(1)).deleteNotifyByNotifyId(anyLong());
    }

    @Test
    @DisplayName("알림 서비스 : 공지 혹은 이벤트 삭제 시 공정 알림 삭제 실패")
    void 고정_알림_삭제_실패() {
        // given
        given(notifyRepository.findNotifyByType(any(), anyLong())).willThrow(NoSuchNotifyException.class);

        // then
        assertThrows(NoSuchNotifyException.class, () -> notifyService.deleteNotifyByManager(NotifyType.NOTICE, 1L));
    }

    @Test
    @DisplayName("알림 서비스 : 고정 알림 조회 성공")
    void 고정_알림_조회_성공() {
        // given
        given(notifyRepository.findFixNotify(anyList(), any())).willReturn(testFixNotify());

        // when
        NotifyDTO fixedNotify = notifyService.searchFixNotify();

        // then
        assertThat(fixedNotify.getNotifyType()).isEqualTo(NotifyType.EVENT);
        assertThat(fixedNotify.getNotifyTypeId()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("알림 서비스 :  고정 알림 조회 실패")
    void 고정_알림_조회_실패() {
        // given
        given(notifyRepository.findFixNotify(anyList(), any())).willReturn(Collections.emptyList());

        // then
        assertThrows(NoSuchFixedNotifyException.class, () -> notifyService.searchFixNotify());
    }
    private Slice<Notify> testNotifies() {
        Notify notify1 = testNotify(1L, "receiver", "detail", NotifyType.QNA);
        Notify notify2 = testNotify(2L, "receiver", "detail2", NotifyType.QNA);

        return new SliceImpl<>(Arrays.asList(notify1, notify2));
    }

    private Slice<Notify> testNotifiesNotFiltered() {
        Notify notify1 = testNotify(1L, "receiver", "detail", NotifyType.QNA);
        Notify notify2 = testNotify(2L, "receiver", "detail2", NotifyType.QNA);
        Notify notify3 = testNotify(3L, "receiver", "detail3", NotifyType.PARTY);

        return new SliceImpl<>(Arrays.asList(notify1, notify2, notify3));
    }

    private Notify testNotify(Long notifyId, String notifyReceiver, String notifyDetail, NotifyType notifyType) {
        return Notify.builder()
                .notifyId(notifyId)
                .notifySender("sender")
                .notifyReceiver(notifyReceiver)
                .notifyDetail(notifyDetail)
                .notifyType(notifyType)
                .notifyTypeId(1L)
                .notifyYmd(LocalDateTime.now())
                .readYn("N")
                .build();
    }

    private List<Notify> testFixNotify() {
        Notify fixedNotify = Notify.builder()
                .notifyId(1L)
                .notifySender("manager")
                .notifyReceiver("all")
                .notifyDetail("detail")
                .notifyType(NotifyType.EVENT)
                .notifyTypeId(1000L)
                .notifyYmd(LocalDateTime.now())
                .readYn("N")
                .build();

        return new ArrayList<>(Collections.singletonList(fixedNotify));
    }
}