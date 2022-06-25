package com.dev.nbbang.alarm.domain.notify.controller;

import com.dev.nbbang.alarm.domain.notify.dto.NotifyDTO;
import com.dev.nbbang.alarm.domain.notify.entity.Notify;
import com.dev.nbbang.alarm.domain.notify.entity.NotifyType;
import com.dev.nbbang.alarm.domain.notify.exception.FailSearchNotifiesException;
import com.dev.nbbang.alarm.domain.notify.exception.NoSuchFixedNotifyException;
import com.dev.nbbang.alarm.domain.notify.exception.NoSuchNotifyException;
import com.dev.nbbang.alarm.domain.notify.service.NotifyService;
import com.dev.nbbang.alarm.global.exception.NbbangException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NotifyController.class)
@ExtendWith(SpringExtension.class)
class NotifyControllerTest {
    @MockBean
    private NotifyService notifyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("알림 컨트롤러 : 안읽은 알림 개수 가져오기 성공")
    void 안읽은_알림_개수_조회_성공() throws Exception {
        // given
        String uri = "/notify/unread/count";
        given(notifyService.unreadNotifyCount(anyString())).willReturn(3);

        // when
        MockHttpServletResponse response = mvc.perform(get(uri)
                .header("X-Authorization-Id", "receiver"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.unreadNotifyCount").value(3))
                .andExpect(jsonPath("$.message").exists())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("알림 컨트롤러 : 특정 알림 삭제하기 성공")
    void 특정_알림_삭제_성공() throws Exception {
        // given
        String uri = "/notify/1";

        // when
        MockHttpServletResponse response = mvc.perform(delete(uri)
                .header("X-Authorization-Id", "receiver"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        verify(notifyService, times(1)).deleteNotify(anyLong());
    }

    @Test
    @DisplayName("알림 컨트롤러 : 특정 알림 삭제하기 실패")
    void 특정_알림_삭제_실패() throws Exception {
        // given
        String uri = "/notify/1";
        doThrow(new NoSuchNotifyException("알림 없음", NbbangException.NOT_FOUND_NOTIFY)).when(notifyService).deleteNotify(anyLong());

        // when
        MockHttpServletResponse response = mvc.perform(delete(uri)
                .header("X-Authorization-Id", "receiver"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").exists())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("알림 컨트롤러 : 회원 알림 전체 삭제 성공")
    void 회원_알림_전체_삭제_성공() throws Exception {
        // given
        String uri = "/notify/all";

        // when
        MockHttpServletResponse response = mvc.perform(delete(uri)
                .header("X-Authorization-Id", "receiver"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        verify(notifyService, times(1)).deleteAllNotify(anyString());
    }

    @Test
    @DisplayName("알림 컨트롤러 : 회원 알림 전체 삭제 실패")
    void 회원_알림_전체_삭제_실패() {

    }

    @Test
    @DisplayName("알림 컨트롤러 : 필터링된 회원 알림 조회 성공")
    void 필터링_회원_알림_조회_성공() throws Exception {
        // given
        String uri = "/notify/qna";
        given(notifyService.searchNotifyList(any(), anyString(), anyLong(), anyInt())).willReturn(testNotifies());

        // when
        MockHttpServletResponse response = mvc.perform(get(uri)
                .header("X-Authorization-Id", "receiver")
                .param("notifyId", "1000")
                .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.[0].notifyId").value(1))
                .andExpect(jsonPath("$.data.[1].notifyId").value(2))
                .andExpect(jsonPath("$.data.[0].notifyDetail").value("detail"))
                .andExpect(jsonPath("$.data.[1].notifyDetail").value("detail2"))
                .andExpect(jsonPath("$.data.[0].notifyType").value("QNA"))
                .andExpect(jsonPath("$.data.[1].notifyType").value("QNA"))
                .andExpect(jsonPath("$.message").exists())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("알림 컨트롤러 : 필터링된 회원 알림 조회 실패")
    void 필터링_회원_알림_조회_실패() throws Exception {
        // given
        String uri = "/notify/qna";
        given(notifyService.searchNotifyList(any(), anyString(), anyLong(), anyInt())).willThrow(new FailSearchNotifiesException("조회 실패", NbbangException.FAIL_SEARCH_NOTIFIES));

        // when
        MockHttpServletResponse response = mvc.perform(get(uri)
                .header("X-Authorization-Id", "receiver")
                .param("notifyId", "1000")
                .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").exists())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("알림 컨트롤러 : 필터링 + 안읽은 알림 조회 성공")
    void 필터링_안읽은_알림_조회_성공() throws Exception {
        //given
        String uri = "/notify/unread/qna";
        given(notifyService.searchUnreadNotifyList(any(), anyString(), anyLong(), anyInt())).willReturn(testNotifies());

        // when
        MockHttpServletResponse response = mvc.perform(get(uri)
                .header("X-Authorization-Id", "receiver")
                .param("notifyId", "1000")
                .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.[0].notifyId").value(1))
                .andExpect(jsonPath("$.data.[1].notifyId").value(2))
                .andExpect(jsonPath("$.data.[0].notifyDetail").value("detail"))
                .andExpect(jsonPath("$.data.[1].notifyDetail").value("detail2"))
                .andExpect(jsonPath("$.data.[0].notifyType").value("QNA"))
                .andExpect(jsonPath("$.data.[1].notifyType").value("QNA"))
                .andExpect(jsonPath("$.message").exists())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    @DisplayName("알림 컨트롤러 : 필터링 + 안읽은 알림 조회 실패")
    void 필터링_안읽은_알림_조회_실패() throws Exception {
        // given
        String uri = "/notify/unread/qna";
        given(notifyService.searchUnreadNotifyList(any(), anyString(), anyLong(), anyInt())).willThrow(new FailSearchNotifiesException("조회 실패", NbbangException.FAIL_SEARCH_NOTIFIES));

        // when
        MockHttpServletResponse response = mvc.perform(get(uri)
                .header("X-Authorization-Id", "receiver")
                .param("notifyId", "1000")
                .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").exists())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    @DisplayName("알림 컨트롤러 : 안읽은 알림 조회 성공")
    void 안읽은_알림_조회_성공() throws Exception {
        // given
        String uri = "/notify/unread/all";
        given(notifyService.searchUnreadNotifyList(anyString(), anyLong(), anyInt())).willReturn(testNotifiesNotFiltered());

        // when
        MockHttpServletResponse response = mvc.perform(get(uri)
                .header("X-Authorization-Id", "receiver")
                .param("notifyId", "1000")
                .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.[0].notifyId").value(1))
                .andExpect(jsonPath("$.data.[1].notifyId").value(2))
                .andExpect(jsonPath("$.data.[2].notifyId").value(3))
                .andExpect(jsonPath("$.data.[0].notifyType").value("QNA"))
                .andExpect(jsonPath("$.data.[1].notifyType").value("QNA"))
                .andExpect(jsonPath("$.data.[2].notifyType").value("PARTY"))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    @DisplayName("알림 컨트롤러 : 안읽은 알림 조회 실패")
    void 안읽은_알림_조회_실패() throws Exception {
        // given
        String uri = "/notify/unread/all";
        given(notifyService.searchUnreadNotifyList(anyString(), anyLong(), anyInt())).willThrow(new FailSearchNotifiesException("조회 실패", NbbangException.FAIL_SEARCH_NOTIFIES));

        // when
        MockHttpServletResponse response = mvc.perform(get(uri)
                .header("X-Authorization-Id", "receiver")
                .param("notifyId", "1000")
                .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").exists())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    @DisplayName("알림 컨트롤러 : 알림 전체 조회 성공")
    void 알림_전체조회_성공() throws Exception {
        // given
        String uri = "/notify/all";
        given(notifyService.searchNotifyList(anyString(), anyLong(), anyInt())).willReturn(testNotifiesNotFiltered());

        // when
        MockHttpServletResponse response = mvc.perform(get(uri)
                .header("X-Authorization-Id", "receiver")
                .param("notifyId", "1000")
                .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.[0].notifyId").value(1))
                .andExpect(jsonPath("$.data.[1].notifyId").value(2))
                .andExpect(jsonPath("$.data.[2].notifyId").value(3))
                .andExpect(jsonPath("$.data.[0].notifyType").value("QNA"))
                .andExpect(jsonPath("$.data.[1].notifyType").value("QNA"))
                .andExpect(jsonPath("$.data.[2].notifyType").value("PARTY"))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("알림 컨트롤러 : 알림 전체 조회 실패 ")
    void 알림_전체조회_실패() throws Exception {
        // given
        String uri = "/notify/all";
        given(notifyService.searchNotifyList(anyString(), anyLong(), anyInt())).willThrow(new FailSearchNotifiesException("조회 실패", NbbangException.FAIL_SEARCH_NOTIFIES));

        // when
        MockHttpServletResponse response = mvc.perform(get(uri)
                .header("X-Authorization-Id", "receiver")
                .param("notifyId", "1000")
                .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").exists())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("알림 컨트롤러 : 알림 읽음 처리 성공")
    void 알림_읽음_처리_성공() throws Exception {
        // given
        String uri = "/notify/unread/change/1";
        given(notifyService.changeUnread(anyLong())).willReturn(NotifyDTO.builder().notifyId(1L).readYn("Y").build());

        // when
        MockHttpServletResponse response = mvc.perform(put(uri))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.readYn").value("Y"))
                .andExpect(jsonPath("$.data.notifyId").value(1))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("알림 컨트롤러 : 알림 읽음 처리 실패")
    void 알림_읽음_처리_실패() throws Exception {
        // given
        String uri = "/notify/unread/change/1";
        given(notifyService.changeUnread(anyLong())).willThrow(new NoSuchNotifyException("알림 없음", NbbangException.NOT_FOUND_NOTIFY));

        // when
        MockHttpServletResponse response = mvc.perform(put(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").exists())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("알림 컨트롤러 : 고정 알림 조회 성공")
    void 고정_알림_조회_성공() throws Exception {
        // given
        String uri = "/notify/fix";
        given(notifyService.searchFixNotify()).willReturn(testNotify(1L, "all", "fix", NotifyType.EVENT));

        // when
        MockHttpServletResponse response = mvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.notifyId").value(1L))
                .andExpect(jsonPath("$.data.notifyReceiver").value("all"))
                .andExpect(jsonPath("$.data.notifyDetail").value("fix"))
                .andExpect(jsonPath("$.data.notifyType").value("EVENT"))
                .andExpect(jsonPath("$.data.notifyTypeId").value(1))
                .andExpect(jsonPath("$.message").exists())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("알림 컨트롤러 : 고정 알림 조회 실패")
    void 고정_알림_조회_실패() throws Exception {
        // given
        String uri = "/notify/fix";
        given(notifyService.searchFixNotify()).willThrow(new NoSuchFixedNotifyException("고정 알림 없음", NbbangException.NOT_FOUND_FIXED_NOTIFY));

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


    private List<NotifyDTO> testNotifies() {
        NotifyDTO notify1 = testNotify(1L, "receiver", "detail", NotifyType.QNA);
        NotifyDTO notify2 = testNotify(2L, "receiver", "detail2", NotifyType.QNA);

        return new ArrayList<>(Arrays.asList(notify1, notify2));
    }

    private List<NotifyDTO> testNotifiesNotFiltered() {
        NotifyDTO notify1 = testNotify(1L, "receiver", "detail", NotifyType.QNA);
        NotifyDTO notify2 = testNotify(2L, "receiver", "detail2", NotifyType.QNA);
        NotifyDTO notify3 = testNotify(3L, "receiver", "detail3", NotifyType.PARTY);

        return new ArrayList<>(Arrays.asList(notify1, notify2, notify3));
    }

    private NotifyDTO testNotify(Long notifyId, String notifyReceiver, String notifyDetail, NotifyType notifyType) {
        return NotifyDTO.builder()
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
}
