package com.dev.nbbang.alarm.domain.notice.service;

import com.dev.nbbang.alarm.domain.notice.entity.Notice;
import com.dev.nbbang.alarm.domain.notice.repository.NoticeImageRepository;
import com.dev.nbbang.alarm.domain.notice.repository.NoticeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NoticeServiceImplTest {

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private NoticeImageRepository noticeImageRepository;

    @InjectMocks
    private NoticeServiceImplTest noticeService;

    @Test
    void createNotice() {
    }

    @Test
    void searchNotice() {
    }

    @Test
    void searchNoticeList() {
    }

    @Test
    void editNotice() {
    }

    @Test
    void deleteNotice() {
    }

    private Notice testNotice(Long noticeId) {
        return Notice.builder()
                .noticeId(noticeId)
                .title("title")
                .noticeDetail("Detail")
                .regYmd(LocalDateTime.now())
                .build();
    }
}