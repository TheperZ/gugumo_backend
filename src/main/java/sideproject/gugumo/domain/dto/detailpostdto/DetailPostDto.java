package sideproject.gugumo.domain.dto.detailpostdto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import sideproject.gugumo.domain.entity.meeting.GameType;
import sideproject.gugumo.domain.entity.meeting.Location;
import sideproject.gugumo.domain.entity.meeting.MeetingStatus;
import sideproject.gugumo.domain.entity.meeting.MeetingType;

import java.time.LocalDate;
import java.time.LocalDateTime;


//장기, 단기에 따라 dto를 나눠서 전송

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public abstract class DetailPostDto {

    private Long postId;
    private String author;

    private MeetingType meetingType;
    private GameType gameType;
    private int meetingMemberNum;
    private LocalDate meetingDeadline;
    private String openKakao;
    private Location location;

    private String title;
    private String content;
    private LocalDateTime createdAt;

    private MeetingStatus meetingStatus;
    private long viewCount;

    //북마크 수
    private boolean isYours;
    private long bookmarkCount;
    private boolean isBookmarked;

    private boolean isAuthorExpired;
}
