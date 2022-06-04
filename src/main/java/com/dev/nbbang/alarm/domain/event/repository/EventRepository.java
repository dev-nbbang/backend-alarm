package com.dev.nbbang.alarm.domain.event.repository;

import com.dev.nbbang.alarm.domain.event.entity.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByEventId(Long eventId);

    Event save(Event event);

    void deleteByEventId(Long eventId);

    Slice<Event> findEventByEventIdLessThanOrderByEventId(Long eventId, Pageable pageable);
}