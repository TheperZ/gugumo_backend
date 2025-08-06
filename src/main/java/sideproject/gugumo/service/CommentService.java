package sideproject.gugumo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.dto.CommentDto;
import sideproject.gugumo.domain.dto.memberDto.CustomUserDetails;
import sideproject.gugumo.domain.entity.Comment;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.member.MemberStatus;
import sideproject.gugumo.domain.entity.post.Post;
import sideproject.gugumo.event.CommentFcmEvent;
import sideproject.gugumo.exception.exception.NoAuthorizationException;
import sideproject.gugumo.exception.exception.NotFoundException;
import sideproject.gugumo.repository.CommentRepository;
import sideproject.gugumo.repository.MemberRepository;
import sideproject.gugumo.repository.PostRepository;
import sideproject.gugumo.request.CreateCommentReq;
import sideproject.gugumo.request.UpdateCommentReq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sideproject.gugumo.response.StatusCode.COMMENT_NOT_FOUND;
import static sideproject.gugumo.response.StatusCode.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void save(Long postId, CreateCommentReq req, CustomUserDetails principal) {

        Member author = checkMemberValid(principal, "댓글 등록 실패: 비로그인 사용자입니다.",
                "댓글 등록 실패: 권한이 없습니다.");

        Post targetPost = postRepository.findByIdAndIsDeleteFalseWithLock(postId)
                .orElseThrow(() -> new NotFoundException(POST_NOT_FOUND));

        //해당 상위 댓글이 없을 경우 예외 처리
        Comment parentComment = req.getParentCommentId() != null ?
                commentRepository.findByIdAndIsDeletedFalse(req.getParentCommentId())
                        .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND)) : null;

        Comment comment = Comment.builder()
                .post(targetPost)
                .member(author)
                .content(req.getContent())
                .build();

        if (parentComment != null) {
            comment.updateParent(parentComment);

            //부모 댓글 작성자에게 알림
            eventPublisher.publishEvent(new CommentFcmEvent(comment, parentComment.getMember()));
        }


        commentRepository.save(comment);

        targetPost.increaseCommentCnt();

        //게시글 작성자에게 알림
        eventPublisher.publishEvent(new CommentFcmEvent(comment, author));

    }

    public List<CommentDto> findComment(Long postId, CustomUserDetails principal) {
        Member member =
                principal == null ?
                        null : memberRepository.findById(principal.getId())
                        .orElseThrow(
                                () -> new NoAuthorizationException("댓글 조회 실패: 권한이 없습니다.")
                        );

        List<CommentDto> comments = commentRepository.findComment(postId, member);


        Map<Long, CommentDto> commentMap = new HashMap<>();
        List<CommentDto> parentComments = new ArrayList<>();

        for (CommentDto comment : comments) {
            commentMap.put(comment.getCommentId(), comment);


            //루트 댓글이면 루트 댓글 리스트에 추가
            if (comment.getParentCommentId() == null) {
                parentComments.add(comment);
            } else {
                //그게 아니면 comment의 부모 댓글 id map에 자식 id 추가
                commentMap.get(comment.getParentCommentId()).getChildComments().add(comment);
            }
        }

        return parentComments;

    }

    @Transactional
    public void updateComment(Long commentId, UpdateCommentReq req, CustomUserDetails principal) {

        //member를 먼저 찾아야 equals가 동작하는 이유?
        Member member = checkMemberValid(principal, "댓글 갱신 실패: 비로그인 사용자입니다.",
                "댓글 갱신 실패: 권한이 없습니다.");

        Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND));


        //댓글 작성자와 토큰 유저 정보가 다를 경우 처리
        if (!comment.getMember().equals(member)) {
            throw new NoAuthorizationException("댓글 갱신 실패: 권한이 없습니다.");
        }

        comment.update(req);
        comment.getPost().decreaseCommentCnt();


    }

    @Transactional
    public void deleteComment(Long commentId, CustomUserDetails principal) {
        //토큰에서
        Member member = checkMemberValid(principal, "댓글 삭제 실패: 비로그인 사용자입니다.",
                "댓글 삭제 실패: 권한이 없습니다.");

        Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND));

        if (!comment.getMember().equals(member)) {
            throw new NoAuthorizationException("댓글 삭제 실패: 권한이 없습니다.");
        }


        comment.tempDelete();
        comment.getPost().decreaseCommentCnt();

    }

    private Member checkMemberValid(CustomUserDetails principal, String noLoginMessage, String notValidUserMessage) {
        if (principal == null) {
            throw new NoAuthorizationException(noLoginMessage);
        }

        Member author = memberRepository.findById(principal.getId())
                .orElseThrow(
                        () -> new NoAuthorizationException(notValidUserMessage)
                );
        ;

        if (author.getStatus() != MemberStatus.active) {
            throw new NoAuthorizationException(notValidUserMessage);
        }
        return author;
    }


}
