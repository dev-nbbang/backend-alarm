package com.dev.nbbang.alarm.domain.notify.repository;

import com.dev.nbbang.alarm.domain.notify.entity.Notify;
import com.dev.nbbang.alarm.domain.notify.entity.NotifyType;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NotifyRepositoryTest {
    @Autowired
    private NotifyRepository notifyRepository;

    @Test
    @DisplayName("알림 레포지토리 : 안읽은 알림 개수")
    void 안읽은_알림_개수() {
        // given
        Notify notify1 = testNotify("receiver", "notice", NotifyType.NOTICE);
        Notify notify2 = testNotify("receiver", "event", NotifyType.EVENT);
        Notify notify3 = testNotify("all", "qna", NotifyType.QNA);

        List<Notify> savedNotifies = notifyRepository.saveAll(new ArrayList<>(Arrays.asList(notify1, notify2, notify3)));

        // when
        Integer unreadNotifyCount = notifyRepository.unreadNotifyCount("receiver");

        assertThat(unreadNotifyCount).isEqualTo(savedNotifies.size());
    }

    @Test
    @DisplayName("알림 레포지토리 : 특정 알림 삭제")
    void 특정_알림_삭제() {
        // given
        Notify notify = testNotify("receiver", "event", NotifyType.EVENT);
        Notify savedNotify = notifyRepository.save(notify);

        // when
        notifyRepository.deleteNotifyByNotifyId(savedNotify.getNotifyId());

        // then
        assertThat(notifyRepository.findById(savedNotify.getNotifyId()).isPresent()).isFalse();
    }

    @Test
    @DisplayName("알림 레포지토리 : 나에게 온 알림 전체 삭제")
    void 나에게_온_알림_전체_삭제() {
        // given
        Notify notify1 = testNotify("receiver", "event", NotifyType.EVENT);
        Notify notify2 = testNotify("receiver", "qna", NotifyType.QNA);

        List<Notify> savedNotifies = notifyRepository.saveAll(new ArrayList<>(Arrays.asList(notify1, notify2)));

        // when
        notifyRepository.deleteAllByNotifyReceiver("receiver");


        // then
        assertThat(notifyRepository.findByNotifyId(savedNotifies.get(0).getNotifyId())).isNull();
        assertThat(notifyRepository.findByNotifyId(savedNotifies.get(1).getNotifyId())).isNull();
    }

    @Test
    @DisplayName("알림 레포지토리 : 알림 타입별 리스트 조회")
    void 알림_타입별_리스트_조회() {
        // given
        Notify notify1 = testNotify("receiver", "event", NotifyType.EVENT);
        Notify notify2 = testNotify("all", "notice", NotifyType.NOTICE);
        Notify notify3 = testNotify("receiver", "event2", NotifyType.EVENT);

        List<Notify> savedNotifies = notifyRepository.saveAll(new ArrayList<>(Arrays.asList(notify1, notify2, notify3)));

        // when
        Slice<Notify> findNotifies = notifyRepository.findNotifiesWithFilter(NotifyType.EVENT, "receiver", 3L, PageRequest.of(0, 2));

        // then
        assertThat(findNotifies.getSize()).isEqualTo(2);
        for (Notify findNotify : findNotifies) {
            assertThat(findNotify.getNotifyType()).isEqualTo(NotifyType.EVENT);
        }
    }

    @Test
    @DisplayName("알림 레포지토리 : 알림 타입별 리스트 조회(안읽은 알림)")
    void 안읽은_알림_타입별_리스트_조회() {
        // given
        Notify notify1 = testNotify("receiver", "event", NotifyType.EVENT);
        Notify notify2 = testNotify("all", "notice", NotifyType.NOTICE);
        Notify notify3 = testNotify("receiver", "event2", NotifyType.EVENT);

        List<Notify> savedNotifies = notifyRepository.saveAll(new ArrayList<>(Arrays.asList(notify1, notify2, notify3)));

        // when
        Slice<Notify> findNotifies = notifyRepository.findNotifiesWithFilter(NotifyType.EVENT, "receiver", 3L, PageRequest.of(0, 2));

        // then
        assertThat(findNotifies.getSize()).isEqualTo(2);
        for (Notify findNotify : findNotifies) {
            assertThat(findNotify.getNotifyType()).isEqualTo(NotifyType.EVENT);
        }
    }

    @Test
    @DisplayName("알림 레포지토리 : 알림 리스트 전체 조회")
    void 알림_리스트_전체_조회() {
        // given
        Notify notify1 = testNotify("receiver", "event", NotifyType.EVENT);
        Notify notify2 = testNotify("all", "notice", NotifyType.NOTICE);
        Notify notify3 = testNotify("receiver", "event2", NotifyType.EVENT);

        List<Notify> savedNotifies = notifyRepository.saveAll(new ArrayList<>(Arrays.asList(notify1, notify2, notify3)));

        // when
        Slice<Notify> findNotifies = notifyRepository.findUnreadNotifiesWithFilter(NotifyType.EVENT, "receiver",3L, PageRequest.of(0, 3));

        // then
        assertThat(findNotifies.getSize()).isEqualTo(savedNotifies.size());
    }

    @Test
    @DisplayName("알림 레포지토리 : 안읽은 알림 리스트 전체 조회")
    void 안읽은_알림_리스트_전체_조회() {
        // given
        Notify notify1 = testNotify("receiver", "event", NotifyType.EVENT);
        Notify notify2 = testNotify("all", "notice", NotifyType.NOTICE);
        Notify notify3 = testNotify("receiver", "event2", NotifyType.EVENT);

        List<Notify> savedNotifies = notifyRepository.saveAll(new ArrayList<>(Arrays.asList(notify1, notify2, notify3)));

        // when
        Slice<Notify> findNotifies = notifyRepository.findUnreadNotifies("receiver",3L, PageRequest.of(0, 3));

        // then
        assertThat(findNotifies.getSize()).isEqualTo(3);
    }

    @Test
    @DisplayName("알림 레포지토리 : 알림 저장")
    void 알림_저장() {
        // given
        Notify notify = testNotify("receiver", "test", NotifyType.NOTICE);

        // when
        Notify savedNotify = notifyRepository.save(notify);
        Optional<Notify> findNotify = notifyRepository.findById(savedNotify.getNotifyId());

        // then
        assertThat(findNotify.isPresent()).isTrue();
        assertThat(findNotify.get().getNotifyReceiver()).isEqualTo(savedNotify.getNotifyReceiver());
        assertThat(findNotify.get().getNotifyDetail()).isEqualTo(savedNotify.getNotifyDetail());
        assertThat(findNotify.get().getNotifyType()).isEqualTo(savedNotify.getNotifyType());
    }

    @Test
    @DisplayName("알림 레포지토리 : 특정 알림 조회")
    void 특정_알림_조회() {
         // given
        Notify notify = testNotify("receiver", "test", NotifyType.NOTICE);
        Notify savedNotify = notifyRepository.save(notify);

        // when
        Notify findNotify = notifyRepository.findByNotifyId(savedNotify.getNotifyId());

        // then
        assertThat(savedNotify.getNotifyId()).isEqualTo(findNotify.getNotifyId());
        assertThat(savedNotify.getNotifyDetail()).isEqualTo(findNotify.getNotifyDetail());
        assertThat(savedNotify.getNotifyType()).isEqualTo(findNotify.getNotifyType());
    }

    private Notify testNotify(String notifyReceiver, String notifyDetail, NotifyType notifyType) {
        return Notify.builder()
                .notifySender("sender")
                .notifyReceiver(notifyReceiver)
                .notifyDetail(notifyDetail)
                .notifyType(notifyType)
                .notifyTypeId(1L)
                .notifyYmd(LocalDateTime.now())
                .readYn("N")
                .build();
    }
}