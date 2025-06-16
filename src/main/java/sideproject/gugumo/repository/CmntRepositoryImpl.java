package sideproject.gugumo.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import sideproject.gugumo.domain.dto.CmntDto;
import sideproject.gugumo.domain.dto.QCmntDto;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.member.MemberStatus;

import java.util.List;

import static sideproject.gugumo.domain.entity.QCmnt.cmnt;
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
                        cmnt.id,
                        cmnt.member.nickname,
                        user != null ? cmnt.member.eq(user) : Expressions.FALSE,
                        cmnt.member.isNull().or(cmnt.member.status.eq(MemberStatus.delete)),
                        cmnt.content,
                        cmnt.createDate,
                        cmnt.isNotRoot,
                        cmnt.parentCmnt.id,
                        cmnt.orderNum
                ))
                .from(cmnt)
                .join(cmnt.post, post)
                .leftJoin(cmnt.member, member)
                .where(
                        cmnt.post.id.eq(postId), cmnt.isDelete.isFalse()
                )
                .orderBy(cmnt.orderNum.asc(), cmnt.createDate.asc())
                .fetch();

        return result;
    }
}
