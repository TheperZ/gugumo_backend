package sideproject.gugumo.domain.dto.simplepostdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import sideproject.gugumo.domain.entity.meeting.GameType;
import sideproject.gugumo.domain.entity.meeting.Location;
import sideproject.gugumo.domain.entity.meeting.MeetingStatus;

import java.time.LocalDate;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SimplePostDto {

    private Long postId;
    private MeetingStatus meetingStatus;
    private GameType gameType;
    private Location location;
    private String title;
    private int meetingMemberNum;
    private LocalDate meetingDeadline;
    private boolean isBookmarked;
    private long commentCnt;
}
