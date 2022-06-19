package com.dev.nbbang.alarm.domain.notice.service;

import com.dev.nbbang.alarm.domain.notice.dto.NoticeDTO;
import com.dev.nbbang.alarm.domain.notice.entity.Notice;
import com.dev.nbbang.alarm.domain.notice.entity.NoticeImage;
import com.dev.nbbang.alarm.domain.notice.exception.NoCreateNoticeException;
import com.dev.nbbang.alarm.domain.notice.exception.NoSuchNoticeException;
import com.dev.nbbang.alarm.domain.notice.repository.NoticeImageRepository;
import com.dev.nbbang.alarm.domain.notice.repository.NoticeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NoticeServiceImplTest {

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private NoticeImageRepository noticeImageRepository;

    @InjectMocks
    private NoticeServiceImpl noticeService;

    @Test
    @DisplayName("공지 서비스 : 공지 생성 성공")
    void 공지_생성_성공() {
        // given
        given(noticeRepository.save(any())).willReturn(testNotice(1L));
        given(noticeImageRepository.saveAll(any())).willReturn(testImageList());

        // when
        NoticeDTO savedNotice = noticeService.createNotice(testNotice(1L), Arrays.asList("test1", ",test2", "test3"));

        // then
        assertThat(savedNotice.getNoticeId()).isEqualTo(1L);
        assertThat(savedNotice.getNoticeImages().size()).isEqualTo(3);
        assertThat(savedNotice.getNoticeImages().get(0).getImageUrl()).isEqualTo("test1");
        assertThat(savedNotice.getNoticeImages().get(1).getImageUrl()).isEqualTo("test2");
        assertThat(savedNotice.getNoticeImages().get(2).getImageUrl()).isEqualTo("test3");
    }

    @Test
    @DisplayName("공지 서비스 : 공지 생성 실패")
    void 공지_생성_실패() {
        // given
        given(noticeRepository.save(any())).willThrow(NoCreateNoticeException.class);

        // then
        assertThrows(NoCreateNoticeException.class, () -> noticeService.createNotice(testNotice(1L), Arrays.asList("test1", "test2", "test3")));
    }


    @Test
    @DisplayName("공지 서비스 : 공지 조회 성공")
    void 공지_조회_성공() {
        // given
        given(noticeRepository.findByNoticeId(anyLong())).willReturn(testNotice(1L));

        // when
        NoticeDTO findNotice = noticeService.searchNotice(1L);

        // then
        assertThat(findNotice.getNoticeId()).isEqualTo(1L);
        assertThat(findNotice.getNoticeDetail()).isEqualTo("detail");
        assertThat(findNotice.getTitle()).isEqualTo("title");
    }

    @Test
    @DisplayName("공지 서비스 : 공지 조회 실패")
    void 공지_조회_실패() {
        // given
        given(noticeRepository.findByNoticeId(anyLong())).willThrow(NoSuchNoticeException.class);

        // then
        assertThrows(NoSuchNoticeException.class, () -> noticeRepository.findByNoticeId(1L));
    }

    @Test
    @DisplayName("공지 서비스 : 공지 리스트 조회 성공")
    void 공지_리스트_조회_성공() {
        // given
        given(noticeRepository.findNoticeList(anyLong(), any())).willReturn(testNoticeSlice());

        // when
        List<NoticeDTO> findNoticeList = noticeService.searchNoticeList(3L, 2);

        // then
        assertThat(findNoticeList.size()).isEqualTo(2);
        assertThat(findNoticeList.get(0).getNoticeId()).isEqualTo(1L);
        assertThat(findNoticeList.get(1).getNoticeId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("공지 서비스 : 공지 리스트 조회 실패")
    void 공지_리스트_조회_실패() {
        // given
        given(noticeRepository.findNoticeList(anyLong(), any())).willThrow(NoSuchNoticeException.class);

        // then
        assertThrows(NoSuchNoticeException.class, () -> noticeService.searchNoticeList(3L, 2));
    }

    @Test
    @DisplayName("공지 서비스 : 공지 수정 성공")
    void 공지_수정_성공() {
        // given
        given(noticeRepository.findByNoticeId(anyLong())).willReturn(testNotice());
        given(noticeImageRepository.saveAll(anyList())).willReturn(testImageList());

        // when
        NoticeDTO updateNotice = noticeService.editNotice(1L, testNotice(1L), Arrays.asList("test1", "test2","test3"));

        // then
        assertThat(updateNotice.getNoticeImages().get(0).getImageUrl()).isEqualTo("test1");
        assertThat(updateNotice.getNoticeImages().get(1).getImageUrl()).isEqualTo("test2");

    }

    @Test
    @DisplayName("공지 서비스 : 공지 수정 실패")
    void 공지_수정_실패() {
        // given
        given(noticeRepository.findByNoticeId(anyLong())).willThrow(NoSuchNoticeException.class);

        // then
        assertThrows(NoSuchNoticeException.class, () -> noticeService.editNotice(1L, testNotice(1L), Arrays.asList("update", "update2","update3")));
    }

    @Test
    @DisplayName("공지 서비스 : 공지 삭제 성공")
    void 공지_삭제_성공() {
        // given
        given(noticeRepository.findByNoticeId(anyLong())).willReturn(testNotice(1L));

        // when
        noticeService.deleteNotice(1L);

        // then
        verify(noticeRepository, times(1)).deleteByNoticeId(anyLong());

    }

    @Test
    @DisplayName("공지 서비스 : 공지 삭제 실패")
    void 공지_삭제_실패() {
        // given
        given(noticeRepository.findByNoticeId(anyLong())).willThrow(NoSuchNoticeException.class);

        // then
        assertThrows(NoSuchNoticeException.class, () -> noticeService.deleteNotice(1L));

    }

    private Notice testNotice(Long noticeId) {
        return Notice.builder()
                .noticeId(noticeId)
                .title("title")
                .noticeDetail("detail")
                .regYmd(LocalDateTime.now())
                .build();
    }

    private NoticeImage testImage(Long imageId, String imageUrl) {
        return NoticeImage.builder()
                .imageId(imageId)
                .imageUrl(imageUrl)
                .build();
    }

    private Notice testNotice() {
        return Notice.builder()
                .noticeId(1L)
                .title("title")
                .noticeDetail("detail")
                .regYmd(LocalDateTime.now())
                .noticeImages(new ArrayList<>(Arrays.asList(testImage(1L, "test1"), testImage(2L, "test2"))))
                .build();
    }

    private List<NoticeImage> testImageList() {
        NoticeImage test1 = testImage(1L, "test1");
        NoticeImage test2 = testImage(2L, "test2");
        NoticeImage test3 = testImage(3L, "test3");

        return new ArrayList<>(Arrays.asList(test1, test2, test3));
    }

    private Slice<Notice> testNoticeSlice() {
        return new SliceImpl<>(Arrays.asList(testNotice(1L), testNotice(2L)));
    }
}