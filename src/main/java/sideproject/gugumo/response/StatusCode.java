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

    LOGIN_FAILURE(HttpStatus.UNAUTHORIZED, "로그인 실패"),
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
    CREATE_COMMENT(HttpStatus.CREATED, "댓글 작성 완료"),
    FIND_COMMENT(HttpStatus.OK, "댓글 조회 완료"),
    UPDATE_COMMENT(HttpStatus.OK, "댓글 수정 완료"),
    DELETE_COMMENT(HttpStatus.OK, "댓글 삭제 완료"),

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글이 존재하지 않음"),

    //북마크 관련
    CREATE_BOOKMARK(HttpStatus.CREATED, "북마크 등록 완료"),
    FIND_BOOKMARK(HttpStatus.OK, "북마크한 게시글 조회 완료"),
    DELETE_BOOKMARK(HttpStatus.OK, "북마크 삭제 완료"),

    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "북마크가 존재하지 않음"),


    //알림 관련
    CREATE_FCM_TOKEN(HttpStatus.CREATED, "FCM 토큰 저장 완료"),

    FIND_NOTIFICATION(HttpStatus.OK, "알림 조회 완료"),
    READ_NOTIFICATION(HttpStatus.OK, "알림 읽음 완료"),
    READ_ALL_NOTIFICATION(HttpStatus.OK, "알림 모두 읽음 완료"),
    DELETE_NOTIFICATION(HttpStatus.OK, "알림 삭제 완료"),
    DELETE_READ_NOTIFICATION(HttpStatus.OK, "읽은 알림 삭제 완료"),

    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "알림이 존재하지 않음"),

    //이메일 관련
    EMAIL_SEND(HttpStatus.OK, "이메일 전송 완료"),
    EMAIL_VERIFY(HttpStatus.OK, "이메일 전송 완료");


    private final HttpStatusCode httpCode;
    private final String customMessage;


}

