package sideproject.gugumo.domain.entity.notification;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import sideproject.gugumo.domain.entity.BaseEntity;
import sideproject.gugumo.domain.entity.member.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomNoti extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "customnoti_id")
    private Long id;


    @NotNull
    private String message;

    @NotNull
    private NotificationType notificationType;

    @Builder.Default
    @NotNull
    private boolean isRead = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;      //알림 수신자

    //postNoti
    private Long postId;


    public void read() {
        this.isRead = true;
    }

}
