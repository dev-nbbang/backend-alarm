package com.dev.nbbang.alarm.domain.notify.repository;

import com.dev.nbbang.alarm.domain.notify.entity.Notify;
import com.dev.nbbang.alarm.domain.notify.entity.NotifyType;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyRepository extends JpaRepository<Notify, Long> {
    // 안읽은 알람 개수
    @Query("SELECT COUNT(n) FROM Notify n WHERE n.readYn = 'N' AND n.notifyReceiver IN ('all',:notifyReceiver)")
    Integer unreadNotifyCount(@Param("notifyReceiver") String notifyReceiver);

    // 특정 알림 삭제
    void deleteNotifyByNotifyId(Long notifyId);

    // 나에게 온 알림 전체 삭제
    void deleteAllByNotifyReceiver(String notifyReceiver);

    // 알림 타입별 리스트 조회 (필터링, 페이징)
    @Query("SELECT n FROM Notify n WHERE n.notifyType = :notifyType AND n.notifyReceiver IN ('all', :notifyReceiver)")
    Slice<Notify> findNotifiesWithFilter(@Param("notifyType") NotifyType notifyType, String notifyReceiver);

    // 읽지 않은 알림 리스트 조회 (필터링 , 페이징)
    @Query("SELECT n FROM Notify n WHERE n.notifyType = :notifyType AND n.notifyReceiver IN ('all', :notifyReceiver) AND n.readYn = 'N'")
    Slice<Notify> findUnreadNotifiesWithFilter(@Param("notifyType") NotifyType notifyType, String notifyReceiver);

    // 알림 리스트 조회 (알림 전체 조회 , 페이징)
    @Query("SELECT n FROM Notify n WHERE n.notifyReceiver IN ('all', :notifyReceiver)")
    Slice<Notify> findNotifies(@Param("notifyReceiver") String notifyReceiver);

    // 읽지 않은 알림 리스트 조회 (알림 전체 조회 페이징)
    @Query("SELECT n FROM Notify n WHERE n.notifyReceiver IN ('all', :notifyReceiver) AND n.readYn = 'N'")
    Slice<Notify> findUnreadNotifies(@Param("notifyReceiver") String notifyReceiver);

    // 알림 메세지 저장
    Notify save(Notify notify);
}
