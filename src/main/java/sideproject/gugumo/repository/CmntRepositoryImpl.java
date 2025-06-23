package sideproject.gugumo.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import sideproject.gugumo.domain.dto.CmntDto;
import sideproject.gugumo.domain.dto.QCmntDto;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.member.MemberStatus;

import java.util.List;

import static sideproject.gugumo.domain.entity.QComment.comment;
import static sideproject.gugumo.domain.entity.member.QMember.member;
import static sideproject.gugumo.domain.entity.post.QPost.post;


public class CmntRepositoryImpl implements CmntRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    public CmntRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<CmntDto> findComment(Long postId, Member user) {


        //isYours, isAuthorExpired 추가
        List<CmntDto> result = queryFactory.select(new QCmntDto(
                        comment.id,
                        comment.member.nickname,
                        user != null ? comment.member.eq(user) : Expressions.FALSE,
                        comment.member.isNull().or(comment.member.status.eq(MemberStatus.delete)),
                        comment.content,
                        comment.createdAt,
                        comment.isNotRoot,
                        comment.parentComment.id,
                        comment.orderNum
                ))
                .from(comment)
                .join(comment.post, post)
                .leftJoin(comment.member, member)
                .where(
                        comment.post.id.eq(postId), comment.isDelete.isFalse()
                )
                .orderBy(comment.orderNum.asc(), comment.createdAt.asc())
                .fetch();

        return result;
    }
}
