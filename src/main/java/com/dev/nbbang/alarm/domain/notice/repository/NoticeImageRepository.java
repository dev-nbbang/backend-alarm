package com.dev.nbbang.alarm.domain.notice.repository;

import com.dev.nbbang.alarm.domain.notice.entity.NoticeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeImageRepository extends JpaRepository<NoticeImage, Long> {
    // saveAll(List<NoticeImage> noticeImages)


}
