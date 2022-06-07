package com.dev.nbbang.alarm.domain.notice.repository;

import com.dev.nbbang.alarm.domain.notice.entity.Notice;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NoticeRepositoryTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Test
    @DisplayName("공지사항 레포지토리 : 공지사항 저장")
    void 공지사항_저장() {
        // given
        Notice notice = testNotice("새로운 공지");

        // when
        Notice savedNotice = noticeRepository.save(notice);
        Notice findNotice = noticeRepository.findByNoticeId(savedNotice.getNoticeId());

        // then
        assertThat(savedNotice.getNoticeId()).isEqualTo(findNotice.getNoticeId());
        assertThat(savedNotice.getTitle()).isEqualTo(findNotice.getTitle());
    }

    @Test
    @DisplayName("공지사항 레포지토리 : 공지사항 조회")
    void 공지사항_조회() {
        // given
        Notice notice = testNotice("조회할 공지");
        Notice savedNotice = noticeRepository.save(notice);

        // when
        Notice findNotice = noticeRepository.findByNoticeId(savedNotice.getNoticeId());

        // then
        assertThat(notice.getTitle()).isEqualTo(findNotice.getTitle());
        assertThat(notice.getNoticeDetail()).isEqualTo(findNotice.getNoticeDetail());
    }

    @Test
    @DisplayName("공지사항 레포지토리 : 공지사항 리스트 조회")
    void 공지사항_리스트_조회() {
        // given
        Notice notice1 = testNotice("새로운 공지 1");
        Notice notice2 = testNotice("새로운 공지 2");
        Notice notice3 = testNotice("새로운 공지 3");
        List<Notice> savedNotices = noticeRepository.saveAll(Arrays.asList(notice1, notice2, notice3));

        // when
        Slice<Notice> findNoticeList = noticeRepository.findNoticeList(10000L, PageRequest.of(0, 2));

        // then
        assertThat(findNoticeList.getSize()).isEqualTo(2);
        assertThat(savedNotices.get(2).getNoticeId()).isEqualTo(findNoticeList.getContent().get(0).getNoticeId());
    }

    @Test
    @DisplayName("공지사항 레포지토리 : 공지사항 삭제")
    void 공지사항_삭제() {
        // given
        Notice notice = testNotice("삭제할 공지");
        Notice savedNotice = noticeRepository.save(notice);

        // when
        noticeRepository.deleteByNoticeId(savedNotice.getNoticeId());

        // then
        Notice findNotice = noticeRepository.findByNoticeId(savedNotice.getNoticeId());
        assertThat(findNotice).isNull();
    }

    private static Notice testNotice(String title) {
        return Notice.builder()
                .title(title)
                .noticeDetail("detail")
                .regYmd(LocalDateTime.now())
                .build();
    }
}