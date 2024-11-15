package sideproject.gugumo.event;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import sideproject.gugumo.domain.entity.Cmnt;
import sideproject.gugumo.domain.entity.notification.CustomNoti;
import sideproject.gugumo.domain.entity.notification.FcmNotificationToken;
import sideproject.gugumo.domain.entity.notification.NotificationType;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.post.Post;
import sideproject.gugumo.repository.CustomNotiRepository;
import sideproject.gugumo.repository.FcmNotificationTokenRepository;
import sideproject.gugumo.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentEventListener {

    private final PostRepository postRepository;
    private final FcmNotificationTokenRepository fcmNotificationTokenRepository;
    private final CustomNotiRepository customNotiRepository;
    private final MessageSource ms;

    @Async
    @TransactionalEventListener
    public void sendPostWriter(CommentFcmEvent event) throws FirebaseMessagingException {
        log.info("[{}] 알림 전송 메서드 동작", Thread.currentThread().getStackTrace()[1].getMethodName());
        Cmnt cmnt = event.getCmnt();
        Optional<Post> targetPost = postRepository.findById(cmnt.getPost().getId());
        if (targetPost.isEmpty() || !event.isCmntPostAuthorEq(targetPost.get())) {
            log.info("없는 게시글이거나 게시글 작성자와 댓글 작성자가 일치함");
            return;
        }
        Post post = targetPost.get();
        Member postWriter = post.getMember();

        String message = cmnt.getContent();

        CustomNoti noti = CustomNoti.builder()
                .message(message)
                .notificationType(NotificationType.COMMENT)
                .member(postWriter)
                .postId(post.getId())
                .build();



        //List로 받아서 모든 토큰에 대해 보내도록 변경
        List<FcmNotificationToken> tempToken = fcmNotificationTokenRepository.findByMember(postWriter);
        if (tempToken.isEmpty()) {
            log.info("FCM 토큰 존재하지 않음");
            return;
        }

        List<String> tokens = new ArrayList<>();
        for (FcmNotificationToken fcmNotificationToken : tempToken) {
            log.info("[{}] 알림을 전송할 토큰 리스트에 토큰 추가: token = {}", Thread.currentThread().getStackTrace()[1].getMethodName(), fcmNotificationToken.getToken());
            tokens.add(fcmNotificationToken.getToken());
        }


        //토큰 여러개 집어넣기->한 계정에서의 여러 디바이스 사용
        MulticastMessage commentMessage = getCommentMessage(post.getTitle(), tokens, post.getId());
        log.info("[{}] 알림 전송", Thread.currentThread().getStackTrace()[1].getMethodName());
        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(commentMessage);

        //에러 발생시?
        //db에서 찾기
        List<String> failedTokens = new ArrayList<>();
        if (response.getFailureCount() > 0) {
            List<SendResponse> responses = response.getResponses();
            for (int i = 0; i < responses.size(); i++) {
                if (!responses.get(i).isSuccessful()) {
                    // The order of responses corresponds to the order of the registration tokens.
                    log.info("[{}] 전송에 실패한 토큰 집계: token = {}", Thread.currentThread().getStackTrace()[1].getMethodName(), tokens.get(i));
                    failedTokens.add(tokens.get(i));
                }
            }
        }

        //db에서 삭제
        for (String failedToken : failedTokens) {
            log.info("[{}] 전송에 실패한 토큰 삭제: token = {}", Thread.currentThread().getStackTrace()[1].getMethodName(), failedToken);
            fcmNotificationTokenRepository.deleteAllByToken(failedToken);
        }


        log.info("[{}] 전송한 알림 저장: noti = {}", Thread.currentThread().getStackTrace()[1].getMethodName(), noti);
        customNotiRepository.save(noti);

    }


    private MulticastMessage getCommentMessage(String postTitle, List<String> tokens, Long postId){
        //새 댓글이 작성되었습니다.
        String message = ms.getMessage("push.comment.content",null,null);

        log.info("[{}] 알림 메시지 빌드: tokens = {}", Thread.currentThread().getStackTrace()[1].getClassName(), tokens);
        Notification notification = Notification.builder()
                .setTitle(postTitle)
                .setBody(message)
                .build();
        return MulticastMessage.builder()
                .setNotification(notification)
                .addAllTokens(tokens)
                .putData("postId", String.valueOf(postId))
                .build();
    }
}
