package com.dev.nbbang.alarm.domain.notice.service;

import com.dev.nbbang.alarm.domain.notice.repository.NoticeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NoticeServiceImplTest {

    @Mock
    private NoticeRepository noticeRepository;

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
}