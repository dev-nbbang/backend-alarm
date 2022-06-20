package com.dev.nbbang.alarm.domain.event.repository;

import com.dev.nbbang.alarm.domain.event.entity.Event;
import com.dev.nbbang.alarm.domain.event.entity.EventImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventImageRepository extends JpaRepository<EventImage, Long> {
    // 이미지 저장
//    List<EventImage> saveAll(List<EventImage> eventImage);

    // 이미지 전체 삭제
    void deleteAllByEvent(Event event);

    // 이미지 찾기
    List<EventImage> findAllByEvent(Event event);
}
