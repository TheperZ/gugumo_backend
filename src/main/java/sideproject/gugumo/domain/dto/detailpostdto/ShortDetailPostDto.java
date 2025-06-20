package sideproject.gugumo.domain.dto.detailpostdto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@AllArgsConstructor
public class ShortDetailPostDto extends DetailPostDto {

    private LocalDateTime meetingDateTime;
}
