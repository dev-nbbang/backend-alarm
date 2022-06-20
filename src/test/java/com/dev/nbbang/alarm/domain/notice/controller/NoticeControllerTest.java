package com.dev.nbbang.alarm.domain.notice.controller;

import com.dev.nbbang.alarm.domain.notice.dto.NoticeDTO;
import com.dev.nbbang.alarm.domain.notice.dto.request.NoticeCreateRequest;
import com.dev.nbbang.alarm.domain.notice.dto.response.NoticeListResponse;
import com.dev.nbbang.alarm.domain.notice.entity.Notice;
import com.dev.nbbang.alarm.domain.notice.entity.NoticeImage;
import com.dev.nbbang.alarm.domain.notice.exception.NoCreateNoticeException;
import com.dev.nbbang.alarm.domain.notice.exception.NoSuchNoticeException;
import com.dev.nbbang.alarm.domain.notice.service.NoticeService;
import com.dev.nbbang.alarm.global.exception.NbbangException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NoticeController.class)
@ExtendWith(SpringExtension.class)
class NoticeControllerTest {
    @MockBean
    private NoticeService noticeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("공지 컨트롤러 : 공지 리스트 조회 성공")
    void 공지_리스트_조회_성공() throws Exception {
        // given
        String uri = "/notice/list";
        given(noticeService.searchNoticeList(anyLong(), anyInt())).willReturn(testNotices());

        // when
        MockHttpServletResponse response = mvc.perform(get(uri)
                .param("noticeId", "1000")
                .param("size", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.[0].noticeId").value(1))
                .andExpect(jsonPath("$.data.[0].title").value("title"))
                .andExpect(jsonPath("$.data.[1].noticeId").value(2))
                .andExpect(jsonPath("$.data.[1].title").value("title2"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("공지 컨트롤러 : 공지 리스트 조회 실패")
    void 공지_리스트_조회_실패() throws Exception {
        // given
        String uri = "/notice/list";
        given(noticeService.searchNoticeList(anyLong(), anyInt())).willThrow(new NoSuchNoticeException("공지 없음", NbbangException.NOT_FOUND_NOTICE));

        // when
        MockHttpServletResponse response = mvc.perform(get(uri)
                .param("noticeId", "1000")
                .param("size", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").exists())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    @DisplayName("공지 컨트롤러 : 공지 조회 성공")
    void 공지_조회_성공() throws Exception {
        // given
        String uri = "/notice/1";
        given(noticeService.searchNotice(anyLong())).willReturn(testNotice(1L, "title", testImages()));

        // when
        MockHttpServletResponse response = mvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.noticeId").value(1))
                .andExpect(jsonPath("$.data.title").value("title"))
                .andExpect(jsonPath("$.data.noticeDetail").value("detail"))
                .andExpect(jsonPath("$.data.noticeImages.[0].imageUrl").value("test1"))
                .andExpect(jsonPath("$.data.noticeImages.[1].imageUrl").value("test2"))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("공지 컨트롤러 : 공지 조회 실패")
    void 공지_조회_실패() throws Exception {
        // given
        String uri = "/notice/1";
        given(noticeService.searchNotice(anyLong())).willThrow(new NoSuchNoticeException("공지 없음", NbbangException.NOT_FOUND_NOTICE));

        // when
        MockHttpServletResponse response = mvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").exists())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    @DisplayName("공지 컨트롤러 : 공지 생성 성공")
    void 공지_생성_성공() throws Exception {
        // given
        String uri = "/notice/new";
        given(noticeService.createNotice(any(), anyList())).willReturn(testNotice(1L, "title", testImages()));

        // when
        MockHttpServletResponse response = mvc.perform(post(uri)
                .header("X-Authorization-Id", "manager")
                .content(objectMapper.writeValueAsString(createRequest()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.noticeId").value(1))
                .andExpect(jsonPath("$.data.title").value("title"))
                .andExpect(jsonPath("$.data.noticeDetail").value("detail"))
                .andExpect(jsonPath("$.data.noticeImages.[0].imageUrl").value("test1"))
                .andExpect(jsonPath("$.data.noticeImages.[1].imageUrl").value("test2"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("공지 컨트롤러 : 공지 생성 실패")
    void 공지_생성_실패() throws Exception {
        // given
        String uri = "/notice/new";
        given(noticeService.createNotice(any(), anyList())).willThrow(new NoCreateNoticeException("생성 실패", NbbangException.NOT_CREATE_NOTICE));

        // when
        MockHttpServletResponse response = mvc.perform(post(uri)
                .header("X-Authorization-Id", "manager")
                .content(objectMapper.writeValueAsString(createRequest()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").exists())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    @DisplayName("공지 컨트롤러 : 공지 수정 성공")
    void 공지_수정_성공() throws Exception {
        // given
        String uri = "/notice/1";
        given(noticeService.editNotice(anyLong(), any(), anyList())).willReturn(testNotice(1L, "update", testImages()));

        // when
        MockHttpServletResponse response = mvc.perform(put(uri)
                .header("X-Authorization-Id", "manager")
                .content(objectMapper.writeValueAsString(createRequest()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.noticeId").value(1))
                .andExpect(jsonPath("$.data.title").value("update"))
                .andExpect(jsonPath("$.data.noticeDetail").value("detail"))
                .andExpect(jsonPath("$.data.noticeImages.[0].imageUrl").value("test1"))
                .andExpect(jsonPath("$.data.noticeImages.[1].imageUrl").value("test2"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("공지 컨트롤러 : 공지 수정 실패")
    void 공지_수정_실패() throws Exception {
        // given
        String uri = "/notice/1";
        given(noticeService.editNotice(anyLong(), any(), anyList())).willThrow(new NoSuchNoticeException("공지 없음", NbbangException.NOT_FOUND_NOTICE));

        // when
        MockHttpServletResponse response = mvc.perform(put(uri)
                .header("X-Authorization-Id", "manager")
                .content(objectMapper.writeValueAsString(createRequest()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").exists())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    @DisplayName("공지 컨트롤러 : 공지 삭제 성공")
    void 공지_삭제_성공() throws Exception {
        // given
        String uri = "/notice/1";

        // when
        MockHttpServletResponse response = mvc.perform(delete(uri)
                .header("X-Authorization-Id", "manager"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn().getResponse();

        verify(noticeService, times(1)).deleteNotice(anyLong());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("공지 컨트롤러 : 공지 삭제 실패")
    void 공지_삭제_실패() throws Exception {
        // given
        String uri = "/notice/1";
        doThrow(new NoSuchNoticeException("공지 없음", NbbangException.NOT_FOUND_NOTICE)).when(noticeService).deleteNotice(anyLong());

        // when
        MockHttpServletResponse response = mvc.perform(delete(uri)
                .header("X-Authorization-Id", "manager"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").exists())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
    private List<NoticeDTO> testNotices() {
        NoticeDTO notice1 = testNotice(1L, "title", new ArrayList<>(Arrays.asList(testImage(1L,1L, "test1"), testImage(1L,2L, "test2"))));
        NoticeDTO notice2 = testNotice(2L, "title2", new ArrayList<>(Arrays.asList(testImage(2L,3L, "test3"), testImage(2L,4L, "test4"))));

        return new ArrayList<>(Arrays.asList(notice1, notice2));
    }

    private NoticeDTO testNotice(Long noticeId, String title, List<NoticeImage> noticeImages) {
        return NoticeDTO.builder()
                .noticeId(noticeId)
                .title(title)
                .noticeDetail("detail")
                .noticeImages(noticeImages)
                .regYmd(LocalDateTime.now())
                .build();
    }

    private List<NoticeImage> testImages() {
        NoticeImage test1 = testImage(1L,1L, "test1");
        NoticeImage test2 = testImage(1L,2L, "test2");

        return new ArrayList<>(Arrays.asList(test1, test2));
    }

    private NoticeImage testImage(Long noticeId, Long imageId, String imageUrl) {
        return NoticeImage.builder()
                .notice(Notice.builder().noticeId(noticeId).build())
                .imageId(imageId)
                .imageUrl(imageUrl)
                .build();
    }

    private NoticeCreateRequest createRequest() {
        return NoticeCreateRequest.builder()
                .title("title")
                .noticeDetail("detail")
                .imageUrls(new ArrayList<>(Arrays.asList("test1", "test2")))
                .build();
    }
}