package com.dev.nbbang.alarm.domain.notice.service;

import com.dev.nbbang.alarm.domain.notice.dto.NoticeDTO;
import com.dev.nbbang.alarm.domain.notice.dto.NoticeImageDTO;
import com.dev.nbbang.alarm.domain.notice.entity.Notice;
import com.dev.nbbang.alarm.domain.notice.entity.NoticeImage;
import com.dev.nbbang.alarm.domain.notice.exception.NoCreateNoticeException;
import com.dev.nbbang.alarm.domain.notice.exception.NoSuchNoticeException;
import com.dev.nbbang.alarm.domain.notice.repository.NoticeImageRepository;
import com.dev.nbbang.alarm.domain.notice.repository.NoticeRepository;
import com.dev.nbbang.alarm.global.exception.NbbangException;
import com.dev.nbbang.alarm.global.exception.NoCreateImageException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeImageRepository noticeImageRepository;

    /**
     * 공지사항을 생성한다.
     *
     * @param notice    공지사항 데이터
     * @param imageUrls 이미지 URL 리스트
     * @return 생성된 공지사항
     */
    @Override
    public NoticeDTO createNotice(Notice notice, List<String> imageUrls) {
        // 1. 공지사항 저장
        Notice savedNotice = Optional.of(noticeRepository.save(notice))
                .orElseThrow(() -> new NoCreateNoticeException("공지사항 생성에 실패했습니다.", NbbangException.NOT_CREATE_NOTICE));

        // 2. 공지사항 이미지 저장
        if (imageUrls.size() > 0) {
            // 2-1. 이미지 저장
            List<NoticeImage> savedNoticeImages = noticeImageRepository.saveAll(NoticeImageDTO.toEntityList(imageUrls, savedNotice));
            if (imageUrls.size() != savedNoticeImages.size())
                throw new NoCreateImageException("공지사항 이미지 저장에 실패했습니다.", NbbangException.NOT_CREATE_IMAGE);

            for (NoticeImage savedNoticeImage : savedNoticeImages) {
                savedNoticeImage.mappingNotice(savedNotice);
            }
        }

        return NoticeDTO.create(savedNotice);
    }

    /**
     * 공지사항을 조회한다.
     *
     * @param noticeId 고유한 공지사항 아이디
     * @return 조회한 공지사항
     */
    @Override
    public NoticeDTO searchNotice(Long noticeId) {
        // 1. 공지사항 조회
        Notice findNotice = Optional.ofNullable(noticeRepository.findByNoticeId(noticeId))
                .orElseThrow(() -> new NoSuchNoticeException("등록되지 않거나 이미 삭제된 공지사항입니다.", NbbangException.NOT_FOUND_NOTICE));

        return NoticeDTO.create(findNotice);
    }

    /**
     * 공지사항 리스트를 조회한다.
     *
     * @param noticeId 고유한 공지사항 아이디
     * @param size     개수
     * @return 공지사항 리스트
     */
    @Override
    public List<NoticeDTO> searchNoticeList(Long noticeId, int size) {
        // 1. 공지사항 리스트 조회
        Slice<Notice> findNotices = Optional.ofNullable(noticeRepository.findNoticeList(noticeId, PageRequest.of(0, size)))
                .orElseThrow(() -> new NoSuchNoticeException("등록된 공지사항이 없습니다.", NbbangException.NOT_FOUND_NOTICE));

        return NoticeDTO.createList(findNotices);
    }

    /**
     * 공지사항을 수정한다.
     *
     * @param noticeId  고유한 공지사항 아이디
     * @param notice    공지사항
     * @param imageUrls 이미지 URL
     * @return 수정된 공지사항
     */
    @Override
    public NoticeDTO editNotice(Long noticeId, Notice notice, List<String> imageUrls) {
        // 1. 공지사항 조회
        Notice findNotice = Optional.ofNullable(noticeRepository.findByNoticeId(noticeId))
                .orElseThrow(() -> new NoSuchNoticeException("등록되지 않거나 이미 삭제된 공지사항입니다.", NbbangException.NOT_FOUND_NOTICE));

        // 2. 공지사항 수정
        findNotice.updateNotice(notice.getTitle(), notice.getNoticeDetail());

        // 3. 공지사항 이미지 삭제 후 저장
        if (imageUrls.size() > 0) {
            List<NoticeImage> savedNoticeImages = noticeImageRepository.saveAll(NoticeImageDTO.toEntityList(imageUrls, findNotice));
            findNotice.getNoticeImages().clear();

            if (imageUrls.size() != savedNoticeImages.size())
                throw new NoCreateImageException("공지사항 이미지 저장에 실패했습니다.", NbbangException.NOT_CREATE_IMAGE);

            for (NoticeImage savedNoticeImage : savedNoticeImages) {
                savedNoticeImage.mappingNotice(findNotice);
            }
        }

        return NoticeDTO.create(findNotice);
    }

    /**
     * 공지사항을 삭제한다.
     *
     * @param noticeId 고유한 공지사항 아이디
     */
    @Override
    public void deleteNotice(Long noticeId) {
        Optional.ofNullable(noticeRepository.findByNoticeId(noticeId)).ifPresentOrElse(
             action -> {
                 noticeRepository.deleteByNoticeId(noticeId);
             },
             () -> {
                 throw new NoSuchNoticeException("등록된 공지사항이 없습니다.", NbbangException.NOT_FOUND_NOTICE);
             }
        );
    }
}
