package sideproject.gugumo.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.entity.member.Member;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    Member getJoinMemberBuild() {
        return Member.userJoin()
                .username("donald010@naver.com")
                .password("password")
                .nickname("nickname")
                .isAgreeMarketing(true)
                .isAgreeTermsUse(true)
                .isAgreeCollectingUsingPersonalInformation(true)
                .build();
    }


    @Test
    @DisplayName("repository에 에러를 발생하지 않고 저장한다.")
    public void memberSaveTest() {
        //given
        Member member = getJoinMemberBuild();
        //when

        //than
        assertThatCode(() -> memberRepository.save(member)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("id를 통해 member를 조회할 수 있다.")
    public void findByIdMemberByIdTest() {
        //given
        Member member = getJoinMemberBuild();
        memberRepository.save(member);

        //when
        Long id = member.getId();
        Optional<Member> findMember = memberRepository.findById(id);

        //than
        assertThat(findMember.get()).isEqualTo(member);
    }

    @Test
    @DisplayName("username을 통해 member를 조회할 수 있다.")
    public void findByUsernameTest() {
        //given
        Member member = getJoinMemberBuild();
        memberRepository.save(member);

        //when
        Optional<Member> findMember = memberRepository.findByUsername(member.getUsername());

        //than
        assertThat(findMember.get()).isEqualTo(member);

    }

    @Test
    @DisplayName("nickname을 통해 member를 조회할 수 있다.")
    public void findByNicknameTest() {
        //given
        Member member = getJoinMemberBuild();
        memberRepository.save(member);

        //when
        Optional<Member> findMember = memberRepository.findByNickname(member.getNickname());

        //than
        assertThat(findMember.get()).isEqualTo(member);
    }


}