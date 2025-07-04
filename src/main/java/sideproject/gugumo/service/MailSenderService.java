package sideproject.gugumo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import sideproject.gugumo.exception.exception.NoAuthorizationException;
import sideproject.gugumo.redis.RedisUtil;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;

    private int authNumber;

    public void checkAuthNum(String email, String authNum) {

        boolean isFailEmailAuth = (redisUtil.getData(authNum) == null || !Objects.equals(redisUtil.getData(authNum), email));

        if (isFailEmailAuth) {
            throw new NoAuthorizationException("이메일 인증 에러");
        }
    }

    //임의의 6자리 양수를 반환합니다.
    public void makeRandomNumber() {
        Random r = new Random();
        StringBuilder randomNumber = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            randomNumber.append(Integer.toString(r.nextInt(10)));
        }

        authNumber = Integer.parseInt(randomNumber.toString());
    }


    //mail을 어디서 보내는지, 어디로 보내는지 , 인증 번호를 html 형식으로 어떻게 보내는지 작성합니다.
    public String joinEmail(String email) {

        makeRandomNumber();

        String setFrom = "gugumo024@gmail.com"; // email-config에 설정한 자신의 이메일 주소를 입력
        String title = "[구구모] 인증번호가 도착하였습니다."; // 이메일 제목
        String content =
                "구기종목 매칭 서비스 구구모에 오신 것을 환영합니다." +    //html 형식으로 작성 !
                        "<br><br>" +
                        "인증 번호는 <" + authNumber + "> 입니다." +
                        "<br><br>" +
                        "문의 - gugumo024@gmail.com";

        mailSend(setFrom, email, title, content);

        return Integer.toString(authNumber);
    }

    // reset Password
    public void resetPassword(String email, String newPassword) {

        String setFrom = "gugumo024@gmail.com";
        String title = "구구모 새 비밀번호입니다.";
        String content =
                "안녕하세요. 구구모 초기화 비밀번호 알려드립니다." +
                        "<br><br>" +
                        "새로운 비밀번호 : " + newPassword +
                        "<br><br>" +
                        "감사합니다." +
                        "<br><br>" +
                        "문의 - gugumo024@gmail.com";

        mailSend(setFrom, email, title, content);
    }

    //이메일을 전송합니다.
    public void mailSend(String setFrom, String toMail, String title, String content) {

        MimeMessage message = mailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");//이메일 메시지와 관련된 설정을 수행합니다.
            // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
            helper.setFrom(setFrom);//이메일의 발신자 주소 설정
            helper.setTo(toMail);//이메일의 수신자 주소 설정
            helper.setSubject(title);//이메일의 제목을 설정
            helper.setText(content, true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
            mailSender.send(message);
        } catch (MessagingException e) {//이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
            // 이러한 경우 MessagingException이 발생
//            e.printStackTrace();//e.printStackTrace()는 예외를 기본 오류 스트림에 출력하는 메서드
            log.info("메일 전송 에러 발생");
        }

        redisUtil.setDataExpire(Integer.toString(authNumber), toMail, 60 * 5L);
    }
}
