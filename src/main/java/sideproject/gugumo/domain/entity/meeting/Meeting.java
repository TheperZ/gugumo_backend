package sideproject.gugumo.domain.entity.meeting;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.post.Post;
import sideproject.gugumo.request.UpdatePostReq;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
public class Meeting {

    @Id
    @GeneratedValue
    @Column(name = "meeting_id")
    private Long id;


    @Enumerated(EnumType.STRING)
    @NotNull
    private MeetingType meetingType;
    @Enumerated(EnumType.STRING)
    @NotNull
    private GameType gameType;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Location location;

    @NotNull
    private LocalDateTime meetingDateTime;


    private String meetingDays;
    @NotNull
    private LocalDate meetingDeadline;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @NotNull
    private MeetingStatus status = MeetingStatus.RECRUIT;

    @NotNull
    private int meetingMemberNum;

    @NotNull
    private String openKakao;


    @NotNull
    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;


    public void update(UpdatePostReq updatePostReq) {
        this.meetingType = MeetingType.valueOf(updatePostReq.getMeetingType());
        this.gameType = GameType.valueOf(updatePostReq.getGameType());
        this.location = Location.valueOf(updatePostReq.getLocation());
        this.meetingDateTime = updatePostReq.getMeetingDate().atStartOfDay().plusHours(updatePostReq.getMeetingTime());
        this.meetingDays = updatePostReq.getMeetingDays();
        this.meetingDeadline = updatePostReq.getMeetingDeadline();
        this.status = MeetingStatus.valueOf(updatePostReq.getMeetingStatus());
        this.meetingMemberNum = updatePostReq.getMeetingMemberNum();
        this.openKakao = updatePostReq.getOpenKakao();


    }

    public void expireStatus() {
        this.status = MeetingStatus.END;
    }


}
