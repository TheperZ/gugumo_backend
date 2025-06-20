package sideproject.gugumo.domain.dto.customnotidto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@SuperBuilder
public class PostCustomNotiDto extends CustomNotiDto {

    private Long postId;


}
