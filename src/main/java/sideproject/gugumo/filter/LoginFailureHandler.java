package sideproject.gugumo.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.response.StatusCode;

import java.io.IOException;

@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        setErrorResponse(response, StatusCode.LOGIN_FAILURE);

    }

    private void setErrorResponse(
            HttpServletResponse response,
            StatusCode statusCode
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(statusCode.getHttpCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        ApiResponse<String> errorResponse = ApiResponse.createFail(statusCode);
        try {
            log.error("[{}] Login fail, code: {}", Thread.currentThread().getStackTrace()[1].getMethodName(), statusCode);
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
