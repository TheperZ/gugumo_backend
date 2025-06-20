package sideproject.gugumo.domain.dto.detailpostdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
@SuperBuilder
public class LongDetailPostDto extends DetailPostDto {

    private LocalTime meetingTime;
    private String meetingDays;
}
