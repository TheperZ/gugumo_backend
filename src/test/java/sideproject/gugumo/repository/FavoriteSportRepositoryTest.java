package sideproject.gugumo.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.entity.member.FavoriteSport;
import sideproject.gugumo.domain.entity.member.Member;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class FavoriteSportRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FavoriteSportRepository favoriteSportRepository;

    @Test
    @DisplayName("조회 기능")
    @Transactional
    public void getTest() {

        //given
        Member member = Member.userJoin()
                .username("donald010@naver.com")
                .nickname("nickname")
                .password("password")
                .isAgreeMarketing(true)
                .isAgreeTermsUse(true)
                .isAgreeCollectingUsingPersonalInformation(true)
                .build();

        FavoriteSport favoriteSport = FavoriteSport.createFavoriteSport("BADMINTON", member);
        FavoriteSport favoriteSport1 = FavoriteSport.createFavoriteSport("BASEBALL", member);
        FavoriteSport favoriteSport2 = FavoriteSport.createFavoriteSport("FUTSAL", member);

        favoriteSportRepository.save(favoriteSport);
        favoriteSportRepository.save(favoriteSport1);
        favoriteSportRepository.save(favoriteSport2);

        memberRepository.save(member);
        //when

        Optional<Member> findMember = memberRepository.findByUsername(member.getUsername());

        List<FavoriteSport> favoriteSports = favoriteSportRepository.getFavoriteSports(findMember.get());

        StringBuilder favoriteSportsString = new StringBuilder();

        for (FavoriteSport fs : favoriteSports) {
            favoriteSportsString.append(fs.getGameType().name());
            favoriteSportsString.append(',');
        }
        favoriteSportsString.deleteCharAt(favoriteSportsString.length() - 1);

        //than
        Assertions.assertThat(favoriteSportsString.toString()).isEqualTo("BADMINTON,BASEBALL,FUTSAL");
    }

}