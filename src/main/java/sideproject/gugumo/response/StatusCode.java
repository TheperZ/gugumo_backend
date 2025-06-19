package sideproject.gugumo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
public enum StatusCode {

    //회원 관련
    LOGIN(HttpStatus.OK, "로그인 완료"),
    KAKAO_LOGIN(HttpStatus.OK, "카카오 로그인 완료"),
    JOIN_MEMBER_WITH_EMAIL_AUTH(HttpStatus.CREATED, "이메일 회원가입 완료"),
    JOIN_MEMBER_WITH_KAKAO(HttpStatus.CREATED, "카카오 회원가입 완료"),
    GET_MEMBER_INFO(HttpStatus.OK, "회원 조회 완료"),
    UPDATE_NICKNAME(HttpStatus.OK, "닉네임 수정 완료"),
    CHECK_NICKNAME_DUPLICATE(HttpStatus.OK, "닉네임 중복 확인 완료"),
    UPDATE_PASSWORD(HttpStatus.OK, "비밀번호 수정 완료"),
    DELETE_MEMBER(HttpStatus.OK, "회원 탈퇴 완료"),
    RESET_PASSWORD(HttpStatus.OK, "비밀번호 초기화 완료"),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원이 존재하지 않음"),


    //게시글 관련
    CREATE_POST(HttpStatus.CREATED, "게시글 작성 완료"),
    FIND_POST(HttpStatus.OK, "게시글 조회 완료"),
    FIND_MY_POST(HttpStatus.OK, "내 게시글 조회 완료"),
    FIND_RECOMMEND_POST(HttpStatus.OK, "추천 게시글 조회 완료"),
    FIND_POST_DETAIL(HttpStatus.OK, "게시글 상세조회 완료"),
    UPDATE_POST(HttpStatus.OK, "게시글 수정 완료"),
    DELETE_POST(HttpStatus.OK, "게시글 삭제 완료"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글이 존재하지 않음"),
    INVALID_MEETING(HttpStatus.BAD_REQUEST, "모임 타입이 적절하지 않음"),

    //댓글 관련
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글이 존재하지 않음"),

    //북마크 관련
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "북마크가 존재하지 않음"),


    //알림 관련
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "알림이 존재하지 않음");


    private final HttpStatusCode httpCode;
    private final String customMessage;


}

