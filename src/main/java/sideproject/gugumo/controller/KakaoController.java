package sideproject.gugumo.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sideproject.gugumo.domain.dto.memberDto.KakaoLoginRequestDto;
import sideproject.gugumo.domain.dto.memberDto.SignUpKakaoMemberDto;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.service.KakaoService;
import sideproject.gugumo.service.MemberService;

import static sideproject.gugumo.response.StatusCode.*;

@RestController
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;
    private final MemberService memberService;

    /**
     * deprecated
     * @param
     * @return
     */
//    @GetMapping("/kakao/login")
//    public ApiResponse<String> login(@RequestParam(name = "code") String code) {
//
//        String accessToken = kakaoService.getAccessTokenFromKakao(code);
//        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
//
//        Boolean isJoined = memberService.isJoinedKakaoMember(userInfo.getId());
////        Boolean isJoined = memberService.isExistUsername(userInfo.get)
//        StringBuilder loginResult = new StringBuilder();
//
//        if(isJoined) {
//            loginResult.append("Bearer ").append(memberService.kakaoLogin(userInfo.getKakaoAccount().getProfile().getNickName()));
//        }
//        else {
//            loginResult.append("not joined");
//        }
//
////        return loginResult.toString();
//        return ApiResponse.createSuccess(loginResult.toString());
//    }
    @PostMapping("/api/v1/login/kakao")
    public ResponseEntity<ApiResponse<Void>> login(HttpServletResponse response, @RequestBody KakaoLoginRequestDto kakaoLoginRequestDto) {

        String token = memberService.kakaoLogin(kakaoLoginRequestDto.getUsername());
        response.addHeader("Authorization", "Bearer " + token);

        return ResponseEntity.status(KAKAO_LOGIN.getHttpCode()).body(ApiResponse.createSuccess(KAKAO_LOGIN));
    }

    //TODO: 이메일 회원가입 API와의 병합 고려
    @PostMapping("/api/v1/kakao/member")
    public ResponseEntity<ApiResponse<Void>> join(@RequestBody SignUpKakaoMemberDto signUpKakaoMemberDto) {

        memberService.kakaoJoinMember(signUpKakaoMemberDto);

        return ResponseEntity.status(JOIN_MEMBER_WITH_KAKAO.getHttpCode()).body(ApiResponse.createSuccess(JOIN_MEMBER_WITH_KAKAO));
    }
}
