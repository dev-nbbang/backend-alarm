package com.dev.nbbang.alarm.domain.notify.repository;

import com.dev.nbbang.alarm.domain.notify.entity.Notify;
import com.dev.nbbang.alarm.domain.notify.entity.NotifyType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotifyRepository extends JpaRepository<Notify, Long> {
    // 안읽은 알람 개수
    @Query("SELECT COUNT(n) FROM Notify n WHERE n.readYn = 'N' AND n.notifyReceiver IN ('all',:notifyReceiver)")
    Integer unreadNotifyCount(@Param("notifyReceiver") String notifyReceiver);

    // 특정 알림 삭제
    void deleteNotifyByNotifyId(Long notifyId);

    // 나에게 온 알림 전체 삭제 (all로 오는건 안지워지는데 어케하지..?)
    void deleteAllByNotifyReceiver(String notifyReceiver);

    // 알림 타입별 리스트 조회 (필터링, 페이징)
    @Query("SELECT n FROM Notify n WHERE n.notifyType = :notifyType AND n.notifyReceiver = :notifyReceiver AND n.notifyId < :notifyId ORDER BY n.notifyYmd DESC")
    Slice<Notify> findNotifiesWithFilter(@Param("notifyType") NotifyType notifyType, @Param("notifyReceiver") String notifyReceiver,
                                         @Param("notifyId") Long notifyId, Pageable pageable);

    // 읽지 않은 알림 리스트 조회 (필터링 , 페이징)
    @Query("SELECT n FROM Notify n WHERE n.notifyType = :notifyType AND n.notifyReceiver = :notifyReceiver AND n.readYn = 'N' AND n.notifyId < :notifyId ORDER BY n.notifyYmd DESC")
    Slice<Notify> findUnreadNotifiesWithFilter(@Param("notifyType") NotifyType notifyType, @Param("notifyReceiver") String notifyReceiver,
                                               @Param("notifyId") Long notifyId, Pageable pageable);

    // 알림 리스트 조회 (알림 전체 조회 , 페이징)
    @Query("SELECT n FROM Notify n WHERE n.notifyReceiver = :notifyReceiver AND n.notifyId < :notifyId ORDER BY n.notifyYmd DESC")
    Slice<Notify> findNotifies(@Param("notifyReceiver") String notifyReceiver, @Param("notifyId") Long notifyId, Pageable pageable);

    // 읽지 않은 알림 리스트 조회 (알림 전체 조회 페이징)
    @Query("SELECT n FROM Notify n WHERE n.notifyReceiver = :notifyReceiver AND n.readYn = 'N' AND n.notifyId < :notifyId ORDER BY n.notifyYmd DESC")
    Slice<Notify> findUnreadNotifies(@Param("notifyReceiver") String notifyReceiver, @Param("notifyId") Long notifyId, Pageable pageable);

    // 알림 메세지 저장
    Notify save(Notify notify);

    // 특정 알림 조회
    Notify findByNotifyId(Long notifyId);

    // 알림 타입 아이디로 알림 조회 (메소드 이름이 길어서 쿼리로 뺌)
    @Query("SELECT n FROM Notify n WHERE n.notifyType = :notifyType AND n.notifyTypeId = :notifyTypeId")
    Notify findNotifyByType(@Param("notifyType") NotifyType notifyType, @Param("notifyTypeId") Long notifyTypeId);

    // 공지사항 혹은 이벤트 한개 조회하기
    @Query("SELECT n FROM Notify n WHERE n.notifyType IN (:notifyTypes) ORDER BY n.notifyYmd DESC")
    List<Notify> findFixNotify(@Param("notifyTypes") List<NotifyType> notifyTypes, Pageable pageable);
}
