package com.dev.nbbang.alarm.domain.event.repository;

import com.dev.nbbang.alarm.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByEventId(Long eventId);

    Event save(Event event);

    void deleteByEventId(Long eventId);
}
