package com.dev.nbbang.alarm.domain.notice.repository;

import com.dev.nbbang.alarm.domain.notice.entity.Notice;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Notice save(Notice notice);

    Notice findByNoticeId(Long noticeId);

    @Query("SELECT n FROM Notice n WHERE n.noticeId < :noticeId ORDER BY n.noticeId DESC")
    Slice<Notice> findNoticeList(@Param(value = "noticeId") Long noticeId);

    void deleteByNoticeId(Long noticeId);
}
