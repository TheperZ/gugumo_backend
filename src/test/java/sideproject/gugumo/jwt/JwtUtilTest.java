package sideproject.gugumo.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtUtilTest {

    Long expiredMs = 86400000L;
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("jwt token 생성")
    public void createJwtToken() {
        //given
//        String token = jwtUtil.createJwt(1l, "username", "ROLE_USER", expiredMs);
//
//        //when
//        String username = jwtUtil.getUsername(token);
//        String role = jwtUtil.getRole(token);
//
//        //than
//        Assertions.assertThat(username).isEqualTo("username");
//        Assertions.assertThat(role).isEqualTo("ROLE_USER");

    }
}