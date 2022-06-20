package com.dev.nbbang.alarm.domain.notify.repository;

import com.dev.nbbang.alarm.domain.notify.entity.Notify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyRepository extends JpaRepository<Notify, Long> {
    // 안읽은 알람 개수

    // 특정 알림 삭제

    // 나에게 온 알림 전체 삭제

    // 알림 타입별 리스트 조회 (필터링)

    // 알림 리스트 조회 (알림 전체 조회 , 페이징)

    //
}
