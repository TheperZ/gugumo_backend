package sideproject.gugumo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import sideproject.gugumo.domain.entity.meeting.GameType;
import sideproject.gugumo.domain.entity.meeting.Location;
import sideproject.gugumo.domain.entity.meeting.Meeting;
import sideproject.gugumo.domain.entity.meeting.MeetingType;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.exception.exception.ApiException;
import sideproject.gugumo.validate.Conditional;
import sideproject.gugumo.validate.EnumValue;

import java.time.LocalDate;


@Getter
@Conditional.List(
        {
                @Conditional(
                        selected = "meetingType",
                        values = "SHORT",
                        required = "meetingDate"
                ),
                @Conditional(
                        selected = "meetingType",
                        values = "LONG",
                        required = "meetingDays"
                )
        }
)
public class CreatePostReq {


    @NotEmpty
    @EnumValue(enumClass = MeetingType.class)
    private String meetingType;     //단기, 장기
    @NotEmpty
    @EnumValue(enumClass = GameType.class)
    private String gameType;
    @NotNull
    private int meetingMemberNum;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent
    private LocalDate meetingDate;      //날짜: 단기일 경우
    private String meetingDays;     //요일: 장기일 경우
    @NotNull
    private int meetingTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @FutureOrPresent
    private LocalDate meetingDeadline;
    @NotEmpty
    private String openKakao;
    @NotEmpty
    private String location;

    @NotEmpty
    private String title;
    @NotEmpty
    private String content;


    public Meeting toEntity(Member author){
        if (MeetingType.valueOf(this.meetingType) == MeetingType.SHORT) {
            return Meeting.builder()
                    .meetingType(MeetingType.valueOf(this.meetingType))
                    .gameType(GameType.valueOf(this.gameType))
                    .location(Location.valueOf(this.location))
                    .meetingDateTime(this.meetingDate.atStartOfDay().plusHours(this.meetingTime))
                    .meetingDeadline(this.meetingDeadline)
                    .meetingMemberNum(this.meetingMemberNum)
                    .openKakao(this.openKakao)
                    .member(author)
                    .build();


        } else if (MeetingType.valueOf(this.meetingType) == MeetingType.LONG) {
            return Meeting.builder()
                    .meetingType(MeetingType.valueOf(this.meetingType))
                    .gameType(GameType.valueOf(this.gameType))
                    .location(Location.valueOf(this.location))
                    .meetingDateTime(LocalDate.of(1970, 1, 1).atStartOfDay().plusHours(this.meetingTime))       //장기모임의 경우 date를 무시
                    .meetingDays(this.meetingDays)
                    .meetingDeadline(this.meetingDeadline)
                    .meetingMemberNum(this.meetingMemberNum)
                    .openKakao(this.openKakao)
                    .member(author)
                    .build();


        }else{
            throw new ApiException("error: meeting type이 유효하지 않음");
        }
    }

}
