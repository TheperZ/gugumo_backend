package sideproject.gugumo.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import sideproject.gugumo.domain.dto.memberDto.CustomUserDetails;
import sideproject.gugumo.domain.dto.memberDto.CustomUserInfoDto;
import sideproject.gugumo.domain.dto.memberDto.LoginCreateJwtDto;
import sideproject.gugumo.jwt.JwtUtil;
import sideproject.gugumo.redis.RedisUtil;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.response.StatusCode;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessJwtProviderHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        setResponse(response, StatusCode.LOGIN);
        addToken(response, principal);

    }


    private void addToken(HttpServletResponse response, CustomUserDetails principal) {


        CustomUserInfoDto customUserInfoDto = principal.getCustomUserInfoDto();

        LoginCreateJwtDto loginCreateJwtDto = LoginCreateJwtDto.builder()
                .id(customUserInfoDto.getId())
                .username(customUserInfoDto.getUsername())
                .role(customUserInfoDto.getRole().toString())
                .requestTimeMs(LocalDateTime.now())
                .build();

        String accessToken = jwtUtil.createJwt(loginCreateJwtDto);
        log.info("[{}} JWT 토큰 생성 access: {}", Thread.currentThread().getStackTrace()[1].getClassName(), accessToken);


        //응답에 JWT 추가
        log.info("[{}} 응답 헤더에 토큰 담기", Thread.currentThread().getStackTrace()[1].getClassName());
        response.addHeader("Authorization", "Bearer " + accessToken);

    }

    private void setResponse(
            HttpServletResponse response,
            StatusCode statusCode
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(statusCode.getHttpCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        ApiResponse<String> errorResponse = ApiResponse.createSuccess(statusCode);
        try {
            log.info("[{}] login success, code: {}", Thread.currentThread().getStackTrace()[1].getMethodName(), statusCode);
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

