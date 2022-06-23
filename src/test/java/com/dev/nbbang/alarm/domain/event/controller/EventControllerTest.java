package com.dev.nbbang.alarm.domain.event.controller;

import com.dev.nbbang.alarm.domain.event.dto.EventDTO;
import com.dev.nbbang.alarm.domain.event.dto.request.EventCreateRequest;
import com.dev.nbbang.alarm.domain.event.entity.Event;
import com.dev.nbbang.alarm.domain.event.entity.EventImage;
import com.dev.nbbang.alarm.domain.event.exception.NoCreateEventException;
import com.dev.nbbang.alarm.domain.event.exception.NoSuchEventException;
import com.dev.nbbang.alarm.domain.event.service.EventService;
import com.dev.nbbang.alarm.domain.notify.dto.NotifyDTO;
import com.dev.nbbang.alarm.domain.notify.entity.NotifyType;
import com.dev.nbbang.alarm.domain.notify.service.NotifyService;
import com.dev.nbbang.alarm.global.exception.NbbangException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EventController.class)
@ExtendWith(SpringExtension.class)
class EventControllerTest {
    @MockBean
    private EventService eventService;

    @MockBean
    private NotifyService notifyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("이벤트 컨트롤러 : 이벤트 조회 성공")
    void 이벤트_조회_성공() throws Exception {
        // given
        String uri = "/event/1";
        given(eventService.searchEvent(anyLong())).willReturn(testEvent(1L, "title"));

        // when
        MockHttpServletResponse response = mvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.eventId").value(1))
                .andExpect(jsonPath("$.data.title").value("title"))
                .andExpect(jsonPath("$.data.eventDetail").value("detail"))
                .andExpect(jsonPath("$.data.eventStart").value("2022-06-07"))
                .andExpect(jsonPath("$.data.eventEnd").value("2022-06-14"))
                .andExpect(jsonPath("$.data.eventImages.[0].imageId").value(1))
                .andExpect(jsonPath("$.data.eventImages.[0].imageUrl").value("test1"))
                .andExpect(jsonPath("$.data.eventImages.[1].imageId").value(2))
                .andExpect(jsonPath("$.data.eventImages.[1].imageUrl").value("test2"))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("이벤트 컨트롤러 : 이벤트 조회 실패")
    void 이벤트_조회_실패() throws Exception {
        // given
        String uri = "/event/1";
        given(eventService.searchEvent(anyLong())).willThrow(new NoSuchEventException("조회 실패", NbbangException.NOT_FOUND_EVENT));

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
    @DisplayName("이벤트 컨트롤러 : 이벤트 리스트 조회 성공")
    void 이벤트_리스트_조회_성공() throws Exception {
        // given
        String uri = "/event/list";
        given(eventService.searchEventList(anyLong(), anyInt())).willReturn(Arrays.asList(testEvent(1L, "title1"), testEvent(2L, "title2")));

        // when
        MockHttpServletResponse response = mvc.perform(get(uri)
                .param("eventId", "1000")
                .param("size", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.[0].eventId").value(1))
                .andExpect(jsonPath("$.data.[1].eventId").value(2))
                .andExpect(jsonPath("$.data.[0].title").value("title1"))
                .andExpect(jsonPath("$.data.[1].title").value("title2"))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("이벤트 컨트롤러 : 이벤트 리스트 조회 실패")
    void 이벤트_리스트_조회_실패() throws Exception {
        // given
        String uri = "/event/list";
        given(eventService.searchEventList(anyLong(), anyInt())).willThrow(new NoSuchEventException("더 이상 조회할 이벤트가 없습니다.", NbbangException.NOT_FOUND_EVENT));

        // when
        MockHttpServletResponse response = mvc.perform(get(uri)
                .param("eventId", "1000")
                .param("size", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").exists())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("이벤트 컨트롤러 : 이벤트 생성 성공")
    void 이벤트_생성_성공() throws Exception {
        // given
        String uri = "/event/new";
        given(eventService.createEvent(any(), anyList())).willReturn(testEvent(1L, "title"));
        given(notifyService.createNotify(any())).willReturn(testNotify(1L, "receiver", "detail", NotifyType.EVENT));

        // when
        MockHttpServletResponse response = mvc.perform(post(uri)
                .header("X-Authorization-Id", "manager")
                .content(objectMapper.writeValueAsString(testEventCreate()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.eventId").value(1))
                .andExpect(jsonPath("$.data.title").value("title"))
                .andExpect(jsonPath("$.data.eventDetail").value("detail"))
                .andExpect(jsonPath("$.data.eventStart").value("2022-06-07"))
                .andExpect(jsonPath("$.data.eventEnd").value("2022-06-14"))
                .andExpect(jsonPath("$.data.eventImages.[0].imageUrl").value("test1"))
                .andExpect(jsonPath("$.data.eventImages.[1].imageUrl").value("test2"))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("이벤트 컨트롤러 : 이벤트 생성 실패")
    void 이벤트_생성_실패() throws Exception {
        // given
        String uri = "/event/new";
        given(eventService.createEvent(any(), anyList())).willThrow(new NoCreateEventException("생성 실패", NbbangException.NOT_CREATE_EVENT));

        // when
        MockHttpServletResponse response = mvc.perform(post(uri)
                .header("X-Authorization-Id", "manager")
                .content(objectMapper.writeValueAsString(testEventCreate()))
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
    @DisplayName("이벤트 컨트롤러 : 이벤트 수정 성공")
    void 이벤트_수정_성공() throws Exception {
        // given
        String uri = "/event/1";
        given(eventService.editEvent(anyLong(), any(), anyList())).willReturn(testEvent(1L, "title"));
        given(notifyService.createNotify(any())).willReturn(testNotify(1L, "receiver", "detail", NotifyType.EVENT));

        // when
        MockHttpServletResponse response = mvc.perform(put(uri)
                .header("X-Authorization-Id", "manager")
                .content(objectMapper.writeValueAsString(testEventCreate()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.eventId").value(1))
                .andExpect(jsonPath("$.data.title").value("title"))
                .andExpect(jsonPath("$.data.eventDetail").value("detail"))
                .andExpect(jsonPath("$.data.eventStart").value("2022-06-07"))
                .andExpect(jsonPath("$.data.eventEnd").value("2022-06-14"))
                .andExpect(jsonPath("$.data.eventImages.[0].imageUrl").value("test1"))
                .andExpect(jsonPath("$.data.eventImages.[1].imageUrl").value("test2"))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("이벤트 컨트롤러 : 이벤트 수정 실패")
    void 이벤트_수정_실패() throws Exception {
        // given
        String uri = "/event/1";
        given(eventService.editEvent(anyLong(), any(), anyList())).willThrow(new NoSuchEventException("수정 실패", NbbangException.NOT_FOUND_EVENT));

        // when
        MockHttpServletResponse response = mvc.perform(put(uri)
                .header("X-Authorization-Id", "manager")
                .content(objectMapper.writeValueAsString(testEventCreate()))
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
    @DisplayName("이벤트 컨트롤러 : 이벤트 삭제 성공")
    void 이벤트_삭제_성공() throws Exception {
        // given
        String uri = "/event/1";
        doNothing().when(eventService).deleteEvent(anyLong());

        // when
        MockHttpServletResponse response = mvc.perform(delete(uri)
                .header("X-Authorization-Id", "manager"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("이벤트 컨트롤러 : 이벤트 삭제 실패")
    void 이벤트_삭제_실패() throws Exception {
        // given
        String uri = "/event/1";
        doThrow(new NoSuchEventException("삭제 실패", NbbangException.NOT_FOUND_EVENT)).when(eventService).deleteEvent(anyLong());

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

    @Test
    @DisplayName("이벤트 컨트롤러 : 헤더 권한 X")
    void 헤더_권한_X() throws Exception {
        // given
        String uri = "/event/1";

        // when
        MockHttpServletResponse response = mvc.perform(delete(uri)
                .header("X-Authorization-Id", "guest"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").exists())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    private static EventDTO testEvent(Long eventId, String title) {
        return EventDTO.builder()
                .eventId(eventId)
                .title(title)
                .eventDetail("detail")
                .eventStart(LocalDate.now())
                .eventEnd(LocalDate.now().plusWeeks(1))
                .regYmd(LocalDateTime.now())
                .eventImages(Arrays.asList(testEventImage(eventId, 1L, "test1"), testEventImage(eventId, 2L, "test2")))
                .build();

    }

    private static EventImage testEventImage(Long eventId, Long imageId, String imageUrl) {
        return EventImage.builder()
                .event(Event.builder().eventId(eventId).build())
                .imageId(imageId)
                .imageUrl(imageUrl)
                .build();
    }

    private static EventCreateRequest testEventCreate() {
        return EventCreateRequest.builder()
                .registerNotify(true)
                .title("title")
                .eventDetail("detail")
                .eventStart(LocalDate.now())
                .eventEnd(LocalDate.now().plusWeeks(1))
                .imageUrls(Arrays.asList("test1", "test2"))
                .build();
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