package com.dev.nbbang.alarm.domain.notice.service;

import com.dev.nbbang.alarm.domain.notice.entity.Notice;
import com.dev.nbbang.alarm.domain.notice.entity.NoticeImage;
import com.dev.nbbang.alarm.domain.notice.repository.NoticeImageRepository;
import com.dev.nbbang.alarm.domain.notice.repository.NoticeRepository;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("공지 서비스 : 공지 생성 성공")
    void 공지_생성_성공() {
    }

    @Test
    @DisplayName("공지 서비스 : 공지 조회 성공")
    void 공지_조회_성공() {
    }

    @Test
    @DisplayName("공지 서비스 : ")
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

    private NoticeImage testImage(Long imageId, String imageUrl) {
        return NoticeImage.builder()
                .imageId(imageId)
                .imageUrl(imageUrl)
                .build();
    }
}