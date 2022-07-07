package com.dev.nbbang.alarm.domain.event.repository;

import com.dev.nbbang.alarm.domain.event.entity.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // 이벤트 조회
    Event findByEventId(Long eventId);

    // 이벤트 저장
    Event save(Event event);

    // 이벤트 삭제
    void deleteByEventId(Long eventId);

    // 이벤트 리스트 조회(페이징)
    @Query("SELECT e FROM Event e WHERE e.eventId < :eventId ORDER BY e.eventId DESC")
    Slice<Event> findEventList(@Param("eventId") Long eventId, Pageable pageable);
}
