package sideproject.gugumo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import sideproject.gugumo.cond.PostSearchCondition;
import sideproject.gugumo.cond.SortType;
import sideproject.gugumo.domain.dto.simplepostdto.QSimplePostQueryDto;
import sideproject.gugumo.domain.dto.simplepostdto.SimplePostQueryDto;
import sideproject.gugumo.domain.entity.meeting.GameType;
import sideproject.gugumo.domain.entity.meeting.Location;
import sideproject.gugumo.domain.entity.meeting.MeetingStatus;
import sideproject.gugumo.domain.entity.member.FavoriteSport;
import sideproject.gugumo.domain.entity.member.Member;

import java.util.Collections;
import java.util.List;

import static sideproject.gugumo.domain.entity.QBookmark.bookmark;
import static sideproject.gugumo.domain.entity.meeting.QMeeting.meeting;
import static sideproject.gugumo.domain.entity.post.QPost.post;


/**
 * querydsl을 이용한 동적 검색
 */
@Slf4j
public class PostRepositoryImpl implements PostRepositoryCustom {


    private final JPAQueryFactory queryFactory;


    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<SimplePostQueryDto> search(PostSearchCondition cond, Pageable pageable
            , Member member) {


        OrderSpecifier orderSpecifier = createOrderSpecifier(cond.getSortType());

        NumberExpression<Integer> recruitFirst = new CaseBuilder()
                .when(meeting.status.eq(MeetingStatus.RECRUIT)).then(0)
                .when(meeting.status.eq(MeetingStatus.END)).then(1)
                .otherwise(2);

        List<SimplePostQueryDto> result = queryFactory.select(new QSimplePostQueryDto(
                        post.id.as("postId"),
                        meeting.meetingType,
                        meeting.status,
                        meeting.gameType,
                        meeting.location,
                        post.title,
                        meeting.meetingDateTime,
                        meeting.meetingDays,
                        meeting.meetingMemberNum,
                        meeting.meetingDeadline,
                        bookmark.isNotNull().as("isBookmarked")
                ))
                .from(post)
                .leftJoin(post.meeting, meeting)
                .leftJoin(bookmark).on(bookmark.post.eq(post), hasMember(member))
                .where(
                        queryEq(cond.getQ()), locationEq(cond.getLocation()),
                        gameTypeEq(cond.getGameType()), meetingStatusEq(cond.getMeetingStatus()),
                        post.isDelete.isFalse()
                )
                .orderBy(recruitFirst.asc(), orderSpecifier, post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        JPAQuery<Long> count = queryFactory.select(post.count())
                .from(post)
                .leftJoin(post.meeting, meeting)
                .where(queryEq(cond.getQ()), locationEq(cond.getLocation()),
                        gameTypeEq(cond.getGameType()), meetingStatusEq(cond.getMeetingStatus()), post.isDelete.isFalse());


        return PageableExecutionUtils.getPage(result, pageable, count::fetchOne);

    }

    @Override
    public Page<SimplePostQueryDto> searchMy(Pageable pageable, Member member, String q) {
        List<SimplePostQueryDto> result = queryFactory.select(new QSimplePostQueryDto(
                        post.id.as("postId"),
                        meeting.meetingType,
                        meeting.status,
                        meeting.gameType,
                        meeting.location,
                        post.title,
                        meeting.meetingDateTime,
                        meeting.meetingDays,
                        meeting.meetingMemberNum,
                        meeting.meetingDeadline,
                        bookmark.isNotNull().as("isBookmarked")
                ))
                .from(post)
                .leftJoin(post.meeting, meeting)
                .leftJoin(bookmark).on(bookmark.post.eq(post), hasMember(member))
                .where(
                        queryEq(q), post.member.eq(member), post.isDelete.isFalse()
                )
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory.select(post.count())
                .from(post)
                .leftJoin(post.meeting, meeting)
                .where(queryEq(q), post.isDelete.isFalse());

        return PageableExecutionUtils.getPage(result, pageable, count::fetchOne);
    }

    @Override
    public List<SimplePostQueryDto> findRecommendPost(Member member) {

        List<FavoriteSport> favoriteSports = member != null ? member.getFavoriteSports() : Collections.emptyList();

        List<SimplePostQueryDto> result = queryFactory.select(new QSimplePostQueryDto(
                        post.id.as("postId"),
                        meeting.meetingType,
                        meeting.status,
                        meeting.gameType,
                        meeting.location,
                        post.title,
                        meeting.meetingDateTime,
                        meeting.meetingDays,
                        meeting.meetingMemberNum,
                        meeting.meetingDeadline,
                        bookmark.isNotNull().as("isBookmarked")
                ))
                .from(post)
                .leftJoin(post.meeting, meeting)
                .leftJoin(bookmark).on(bookmark.post.eq(post), hasMember(member))
                .where(
                        post.isDelete.isFalse(), favoriteSportsEq(favoriteSports),
                        meeting.status.eq(MeetingStatus.RECRUIT)
                )
                .orderBy(Expressions.numberTemplate(Double.class, "function('random')").asc())
                .limit(8)
                .fetch();

        return result;

    }

    private BooleanBuilder favoriteSportsEq(List<FavoriteSport> favoriteSports) {
        if (favoriteSports.isEmpty()) {
            return null;
        }

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        for (FavoriteSport favoriteSport : favoriteSports) {
            booleanBuilder.or(meeting.gameType.eq(favoriteSport.getGameType()));
        }

        return booleanBuilder;
    }


    private OrderSpecifier createOrderSpecifier(SortType sortType) {
        return switch (sortType) {
            case OLD -> new OrderSpecifier<>(Order.ASC, post.createdAt);
            case LIKE -> new OrderSpecifier<>(Order.DESC, post.viewCount);
            default -> new OrderSpecifier<>(Order.DESC, post.createdAt);
        };
    }

    private BooleanExpression hasMember(Member member) {
        return member != null ? bookmark.member.eq(member) : Expressions.FALSE;
    }

    private BooleanExpression meetingStatusEq(MeetingStatus meetingStatus) {
        return meetingStatus != null ? meeting.status.eq(meetingStatus) : null;
    }


    private BooleanExpression gameTypeEq(GameType gameType) {
        return gameType != null ? meeting.gameType.eq(gameType) : null;
    }

    private BooleanExpression locationEq(Location location) {
        return location != null ? meeting.location.eq(location) : null;
    }

    /**
     * contains("string"): '%string%'으로 나감
     * like("string"): 'string'으로 나감->%나 _ 등을 쓰려면 붙여줘야 함
     */
    private BooleanExpression queryEq(String q) {
        return q != null ? post.title.contains(q).or(post.content.contains(q)) : null;
    }
}
