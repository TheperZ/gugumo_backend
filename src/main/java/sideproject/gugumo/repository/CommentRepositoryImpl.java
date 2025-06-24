package sideproject.gugumo.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import sideproject.gugumo.domain.dto.CommentDto;
import sideproject.gugumo.domain.dto.QCommentDto;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.member.MemberStatus;
import sideproject.gugumo.domain.entity.member.QMember;

import java.util.List;

import static sideproject.gugumo.domain.entity.QComment.comment;
import static sideproject.gugumo.domain.entity.member.QMember.member;
import static sideproject.gugumo.domain.entity.post.QPost.post;


public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    public CommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<CommentDto> findComment(Long postId, Member member) {


        //isYours, isAuthorExpired 추가
        List<CommentDto> result = queryFactory.select(new QCommentDto(
                        comment.id,
                        comment.parentComment.id,
                        comment.member.nickname,
                        member != null ? comment.member.eq(member) : Expressions.FALSE,
                        comment.member.isNull().or(comment.member.status.eq(MemberStatus.delete)),
                        comment.content,
                        comment.createdAt
                ))
                .from(comment)
                .join(comment.post, post)
                .leftJoin(comment.member, QMember.member)
                .where(
                        comment.post.id.eq(postId), comment.isDeleted.isFalse()
                )
                .fetch();

        return result;
    }
}
