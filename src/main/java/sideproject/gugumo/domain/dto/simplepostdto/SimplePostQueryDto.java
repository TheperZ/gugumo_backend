package sideproject.gugumo.domain.dto.simplepostdto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import sideproject.gugumo.domain.entity.meeting.GameType;
import sideproject.gugumo.domain.entity.meeting.Location;
import sideproject.gugumo.domain.entity.meeting.MeetingStatus;
import sideproject.gugumo.domain.entity.meeting.MeetingType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class SimplePostQueryDto {

    private Long postId;
    private MeetingType meetingType;
    private MeetingStatus status;
    private GameType gameType;
    private Location location;
    private String title;
    private LocalDateTime meetingDateTime;
    private String meetingDays;
    private int meetingMemberNum;
    private LocalDate meetingDeadline;
    private boolean isBookmarked;
    private long commentCnt;


    @QueryProjection
    public SimplePostQueryDto(Long postId, MeetingType meetingType, MeetingStatus status, GameType gameType, Location location, String title, LocalDateTime meetingDateTime, String meetingDays, int meetingMemberNum, LocalDate meetingDeadline, boolean isBookmarked, long commentCnt) {
        this.postId = postId;
        this.meetingType = meetingType;
        this.status = status;
        this.gameType = gameType;
        this.location = location;
        this.title = title;
        this.meetingDateTime = meetingDateTime;
        this.meetingDays = meetingDays;
        this.meetingMemberNum = meetingMemberNum;
        this.meetingDeadline = meetingDeadline;
        this.isBookmarked = isBookmarked;
        this.commentCnt = commentCnt;
    }
}
