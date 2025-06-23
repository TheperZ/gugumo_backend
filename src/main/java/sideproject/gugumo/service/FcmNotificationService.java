package sideproject.gugumo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.dto.customnotidto.CustomNotiDto;
import sideproject.gugumo.domain.dto.customnotidto.PostCustomNotiDto;
import sideproject.gugumo.domain.dto.memberDto.CustomUserDetails;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.member.MemberStatus;
import sideproject.gugumo.domain.entity.notification.CustomNoti;
import sideproject.gugumo.domain.entity.notification.NotificationType;
import sideproject.gugumo.exception.exception.NoAuthorizationException;
import sideproject.gugumo.exception.exception.NotFoundException;
import sideproject.gugumo.repository.CustomNotiRepository;
import sideproject.gugumo.repository.FcmNotificationTokenRepository;
import sideproject.gugumo.repository.MemberRepository;
import sideproject.gugumo.response.StatusCode;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FcmNotificationService {

    private final FcmNotificationTokenRepository fcmNotificationTokenRepository;
    private final MemberRepository memberRepository;
    private final CustomNotiRepository customNotiRepository;


    public <T extends CustomNotiDto> List<T> findNotification(@AuthenticationPrincipal CustomUserDetails principal) {

        Member member = checkMemberValid(principal, "알림 조회 실패: 비로그인 사용자입니다.",
                "알림 조회 실패: 권한이 없습니다.");

        List<CustomNoti> result = customNotiRepository.findByMemberOrderByCreateDateDesc(member);

        return result.stream()
                .map(n -> convertToDto(n))
                .map(n -> (T) n)
                .collect(Collectors.toList());
    }

    private <T extends CustomNotiDto> T convertToDto(CustomNoti customNoti) {

        if (customNoti.getNotificationType() == NotificationType.COMMENT) {
            return (T) PostCustomNotiDto.builder()
                    .id(customNoti.getId())
                    .message(customNoti.getMessage())
                    .notificationType(NotificationType.COMMENT)
                    .createDate(customNoti.getCreateDate())
                    .isRead(customNoti.isRead())
                    .postId(customNoti.getPostId())
                    .build();
        } else {
            return null;
        }

    }

    @Transactional
    public void read(CustomUserDetails principal, Long id) {
        Member member = checkMemberValid(principal, "알림 읽음처리 실패: 비로그인 사용자입니다.",
                "알림 읽음처리 실패: 권한이 없습니다.");

        CustomNoti notification = customNotiRepository.findById(id).orElseThrow(
                () -> new NotFoundException(StatusCode.NOTIFICATION_NOT_FOUND)
        );

        if (!notification.getMember().equals(member)) {
            throw new NoAuthorizationException("알림 읽음처리 실패: 권한이 없습니다.");
        }

        notification.read();

    }

    @Transactional
    public void readAll(CustomUserDetails principal) {
        Member member = checkMemberValid(principal, "알림 모두 읽음처리 실패: 비로그인 사용자입니다.",
                "알림 모두 읽음처리 실패: 권한이 없습니다.");

        List<CustomNoti> notifications = customNotiRepository.findByMemberOrderByCreateDateDesc(member);

        for (CustomNoti notification : notifications) {
            notification.read();
        }

    }

    @Transactional
    public void deleteNotification(CustomUserDetails principal, Long id) {
        Member member = checkMemberValid(principal, "알림 삭제 실패: 비로그인 사용자입니다.",
                "알림 삭제 실패: 권한이 없습니다.");

        CustomNoti notification = customNotiRepository.findById(id).orElseThrow(
                () -> new NotFoundException(StatusCode.NOTIFICATION_NOT_FOUND)
        );

        customNotiRepository.delete(notification);

    }

    @Transactional
    public void deleteReadNotification(CustomUserDetails principal) {
        Member member = checkMemberValid(principal, "읽은 알림 삭제 실패: 비로그인 사용자입니다.", "읽은 알림 삭제 실패: 권한이 없습니다.");

        customNotiRepository.deleteAllByMemberAndIsReadTrue(member);

    }

    private Member checkMemberValid(CustomUserDetails principal, String noLoginMessage, String notValidUserMessage) {
        if (principal == null) {
            throw new NoAuthorizationException(noLoginMessage);
        }

        Member author = memberRepository.findById(principal.getId())
                .orElseThrow(
                        () -> new NoAuthorizationException(notValidUserMessage)
                );

        if (author.getStatus() != MemberStatus.active) {
            throw new NoAuthorizationException(notValidUserMessage);
        }
        return author;
    }
}
