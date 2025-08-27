package sideproject.gugumo.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.cond.PostSearchCondition;
import sideproject.gugumo.cond.SortType;
import sideproject.gugumo.domain.dto.detailpostdto.DetailPostDto;
import sideproject.gugumo.domain.dto.detailpostdto.LongDetailPostDto;
import sideproject.gugumo.domain.dto.detailpostdto.ShortDetailPostDto;
import sideproject.gugumo.domain.dto.memberDto.CustomUserDetails;
import sideproject.gugumo.domain.dto.simplepostdto.SimplePostDto;
import sideproject.gugumo.domain.dto.simplepostdto.SimplePostLongDto;
import sideproject.gugumo.domain.dto.simplepostdto.SimplePostQueryDto;
import sideproject.gugumo.domain.dto.simplepostdto.SimplePostShortDto;
import sideproject.gugumo.domain.entity.meeting.*;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.member.MemberStatus;
import sideproject.gugumo.domain.entity.post.Post;
import sideproject.gugumo.exception.exception.NoAuthorizationException;
import sideproject.gugumo.exception.exception.NotFoundException;
import sideproject.gugumo.page.PageCustom;
import sideproject.gugumo.repository.*;
import sideproject.gugumo.request.CreatePostReq;
import sideproject.gugumo.request.UpdatePostReq;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static sideproject.gugumo.response.StatusCode.POST_NOT_FOUND;


/**
 * 모든 enum 타입은 예외 처리 필요
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CommentRepository commentRepository;

    /**
     * 단기모집 기준: meetingDate, meetingTime 반영(default)
     * 장기모집일 경우 meetingDays(요일), meetingTime(1970/1/1을 쓰레기값으로)을 반영해야함
     *
     * @param createPostReq
     */
    @Transactional
    public void save(CustomUserDetails principal, CreatePostReq createPostReq) {

        /**
         *  orElse~를 사용하는 경우 null이 아닐 시 Optional의 인자가 반환된다.
         */

        //if principal==null->로그인을 하지 않아 principal 이 없음->권한이 없습니다 exception
        Member author = checkMemberValid(principal, "저장 실패: 게시글 저장 권한이 없습니다.", "저장 실패: 게시글 저장 권한이 없습니다.");


        Meeting meeting = createPostReq.toEntity(author);

        meetingRepository.save(meeting);


        //post 저장
        Post post = Post.builder()
                .title(createPostReq.getTitle())
                .content(createPostReq.getContent())
                .member(author)
                .meeting(meeting)
                .build();

        postRepository.save(post);

    }




    /**
     *
     * @param meetingDate
     * @param meetingTime: int로 간주->추후 협의 후 수정될 수 있음
     * @return
     */
    private LocalDateTime mergeDatetime(LocalDate meetingDate, int meetingTime) {
        return meetingDate.atStartOfDay().plusHours(meetingTime);
    }


    /**
     * 동적 쿼리를 이용하여 게시글의 정보를 반환
     * post.title, meeting.location, meeting.gametype, page번호를 확인해야함
     * @return page
     */

    public <T extends SimplePostDto> PageCustom<T> findSimplePost(CustomUserDetails principal, Pageable pageable, String q,
                                                                  String gameType, String location, String meetingStatus, String sortType) {
        PostSearchCondition condition = PostSearchCondition.builder()
                .q(q)
                .gameType(gameType == null || gameType == "" ? null : GameType.valueOf(gameType))
                .location(location == null || location == "" ? null : Location.valueOf(location))
                .meetingStatus(meetingStatus.equals("ALL") ? null : MeetingStatus.valueOf(meetingStatus))
                .sortType(SortType.valueOf(sortType))
                .build();

        Member member =
                principal == null ?
                        null : memberRepository.findById(principal.getId())
                        .orElseThrow(
                                () -> new NoAuthorizationException("조회 실패: 권한이 없습니다.")
                        );

        if (member != null && member.getStatus() != MemberStatus.active) {
            member = null;
        }

        Page<SimplePostQueryDto> page = postRepository.search(condition, pageable, member);


        //이걸 한번 더 가공해서(DetailPostDto처럼, 단기모임에서는 meetingDatetime을, 장기모임에서는 meetingTime, meetingDays)
        List<T> result = page.stream()
                .map(p -> (T) convertToTransDto(p))
                .collect(Collectors.toList());

        log.info("principal={}", principal);

        return new PageCustom<>(result, page.getPageable(), page.getTotalElements());


    }


    //장기, 단기에 따라 dto를 나눠서 전송
    @Transactional          //viewCount++가 동작하므로 readonly=false
    public <T extends DetailPostDto> T findDetailPostByPostId(CustomUserDetails principal, Long postId) {


        Post targetPost = postRepository.findByIdAndIsDeleteFalse(postId)
                .orElseThrow(() -> new NotFoundException(POST_NOT_FOUND));


        Meeting targetMeeting = targetPost.getMeeting();

        Member member = principal == null ?
                null : memberRepository.findById(principal.getId()).orElseThrow(
                () -> new NoAuthorizationException("조회 실패: 권한이 없습니다.")
        );

        if (member != null && member.getStatus() != MemberStatus.active) {
            member = null;
        }

        targetPost.addViewCount();


        if (targetMeeting.getMeetingType() == MeetingType.SHORT) {
            ShortDetailPostDto detailPostDto = ShortDetailPostDto.builder()
                    .postId(targetPost.getId())
                    .author(targetPost.getMember() != null && targetPost.getMember().getStatus() != MemberStatus.delete ? targetPost.getMember().getNickname() : "")
                    .meetingType(targetMeeting.getMeetingType())
                    .gameType(targetMeeting.getGameType())
                    .meetingMemberNum(targetMeeting.getMeetingMemberNum())
                    .meetingDateTime(targetMeeting.getMeetingDateTime())        //장기일 경우 1970.1.1/time
                    .meetingDeadline(targetMeeting.getMeetingDeadline())
                    .openKakao(targetMeeting.getOpenKakao())
                    .location(targetMeeting.getLocation())
                    .title(targetPost.getTitle())
                    .content(targetPost.getContent())
                    .createdAt(targetPost.getCreatedAt())
                    .meetingStatus(targetMeeting.getStatus())
                    .viewCount(targetPost.getViewCount())
                    .isYours(
                            member != null && member.getUsername().equals(targetPost.getMember().getUsername())
                    )
                    .bookmarkCount(bookmarkRepository.countByPost(targetPost))
                    .isBookmarked(member != null ? bookmarkRepository.existsByMemberAndPost(member, targetPost) : false)
                    .isAuthorExpired(targetPost.getMember() != null && targetPost.getMember().getStatus() != MemberStatus.delete ? false : true)
                    .build();

            return (T) detailPostDto;

        } else if (targetMeeting.getMeetingType() == MeetingType.LONG) {
            LongDetailPostDto detailPostDto = LongDetailPostDto.builder()
                    .postId(targetPost.getId())
                    .author(targetPost.getMember() != null && targetPost.getMember().getStatus() != MemberStatus.delete ? targetPost.getMember().getNickname() : "")
                    .meetingType(targetMeeting.getMeetingType())
                    .gameType(targetMeeting.getGameType())
                    .meetingMemberNum(targetMeeting.getMeetingMemberNum())
                    .meetingTime(targetMeeting.getMeetingDateTime().toLocalTime())        //장기일 경우 1970.1.1/time
                    .meetingDays(targetMeeting.getMeetingDays())
                    .meetingDeadline(targetMeeting.getMeetingDeadline())
                    .openKakao(targetMeeting.getOpenKakao())
                    .location(targetMeeting.getLocation())
                    .title(targetPost.getTitle())
                    .content(targetPost.getContent())
                    .createdAt(targetPost.getCreatedAt())
                    .meetingStatus(targetMeeting.getStatus())
                    .viewCount(targetPost.getViewCount())
                    .isYours(
                            member != null && member.getUsername().equals(targetPost.getMember().getUsername()))
                    .bookmarkCount(bookmarkRepository.countByPost(targetPost))
                    .isBookmarked(member != null ? bookmarkRepository.existsByMemberAndPost(member, targetPost) : false)
                    .isAuthorExpired(targetPost.getMember() != null && targetPost.getMember().getStatus() != MemberStatus.delete ? false : true)
                    .build();

            return (T) detailPostDto;
        } else {
            //TODO: 해당 타입의 게시글이 없습니다Exception
            return null;

        }


    }

    @Transactional
    public void update(CustomUserDetails principal, Long postId, UpdatePostReq updatePostReq) {
        //토큰에서
        Member member = checkMemberValid(principal, "수정 실패: 비로그인 사용자입니다.", "수정 실패: 게시글 수정 권한이 없습니다.");

        Post targetPost = postRepository.findByIdAndIsDeleteFalse(postId)
                .orElseThrow(() -> new NotFoundException(POST_NOT_FOUND));

        //post의 member 동일인 여부 확인
        if (!targetPost.getMember().equals(member)) {
            throw new NoAuthorizationException("수정 실패: 게시글 수정 권한이 없습니다.");
        }

        targetPost.update(updatePostReq);

        Meeting targetMeeting = targetPost.getMeeting();

        targetMeeting.update(updatePostReq);

    }

    @Transactional
    public void deletePost(CustomUserDetails principal, Long postId) {

        //토큰에서
        Member member = checkMemberValid(principal, "삭제 실패: 비로그인 사용자입니다.", "삭제 실패: 게시글 삭제 권한이 없습니다.");

        Post targetPost = postRepository.findByIdAndIsDeleteFalse(postId)
                .orElseThrow(() -> new NotFoundException(POST_NOT_FOUND));

        //post의 member 동일인 여부 확인
        if (!targetPost.getMember().equals(member)) {
            throw new NoAuthorizationException("삭제 실패: 게시글 삭제 권한이 없습니다.");
        }

        //연관된 북마크 삭제
        bookmarkRepository.deleteByPost(targetPost);

        //targetPost.isDelete=true
        targetPost.tempDelete();

    }

    public <T extends SimplePostDto> PageCustom<T> findMyPost(CustomUserDetails principal, Pageable pageable, String q) {

        //토큰에서
        Member member = checkMemberValid(principal, "내 글 조회 실패: 비로그인 사용자입니다.", "내 글 조회 실패: 접근 권한이 없습니다.");


        Page<SimplePostQueryDto> page = postRepository.searchMy(pageable, member, q);

        List<T> result = page.stream()
                .map(p -> convertToTransDto(p))
                .map(r -> (T) r)
                .collect(Collectors.toList());

        return new PageCustom<>(result, page.getPageable(), page.getTotalElements());

    }

    public <T extends SimplePostDto> List<T> findRecommendPost(CustomUserDetails principal) {
        //토큰에서

        Member member;

        if (principal == null) {
            member = null;
        } else {
            member = memberRepository.findById(principal.getId()).orElseThrow(
                    () -> new NoAuthorizationException("추천글 조회 실패: 권한이 없습니다.")
            );

            if (member.getStatus() != MemberStatus.active) {
                member = null;
            }

        }

        List<SimplePostQueryDto> recommendPost = postRepository.findRecommendPost(member);


        return recommendPost.stream()
                .map(p -> convertToTransDto(p))
                .map(r -> (T) r)
                .collect(Collectors.toList());
    }

    private Member checkMemberValid(CustomUserDetails principal, String noLoginMessage, String notValidUserMessage) {
        if (principal == null) {
            throw new NoAuthorizationException(noLoginMessage);
        }

        //토큰에서
        Member author = memberRepository.findById(principal.getId())
                .orElseThrow(
                        () -> new NoAuthorizationException(notValidUserMessage)
                );

        if (author.getStatus() != MemberStatus.active) {
            throw new NoAuthorizationException(notValidUserMessage);
        }
        return author;
    }

    private <T extends SimplePostDto> T convertToTransDto(SimplePostQueryDto s) {

        SimplePostDto result = new SimplePostDto();

        if (s.getMeetingType() == MeetingType.SHORT) {
            result = SimplePostShortDto.builder()
                    .postId(s.getPostId())
                    .meetingStatus(s.getStatus())
                    .gameType(s.getGameType())
                    .location(s.getLocation())
                    .title(s.getTitle())
                    .meetingMemberNum(s.getMeetingMemberNum())
                    .meetingDeadline(s.getMeetingDeadline())
                    .isBookmarked(s.isBookmarked())
                    .meetingDateTime(s.getMeetingDateTime())
                    .commentCnt(s.getCommentCnt())
                    .build();


        } else if (s.getMeetingType() == MeetingType.LONG) {
            result = SimplePostLongDto.builder()
                    .postId(s.getPostId())
                    .meetingStatus(s.getStatus())
                    .gameType(s.getGameType())
                    .location(s.getLocation())
                    .title(s.getTitle())
                    .meetingMemberNum(s.getMeetingMemberNum())
                    .meetingDeadline(s.getMeetingDeadline())
                    .isBookmarked(s.isBookmarked())
                    .meetingTime(s.getMeetingDateTime().toLocalTime())
                    .meetingDays(s.getMeetingDays())
                    .commentCnt(s.getCommentCnt())
                    .build();


        }

        return (T) result;
    }



}
