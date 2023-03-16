package jpabook.jpashop;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Team;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

@SpringBootTest
@Transactional
class QueryByExampleTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;
    @Test
    public void basic() throws Exception {
//given
        Team teamA = new Team("teamA");
        em.persist(teamA);
        em.persist(new Member("m1", 0, teamA));
        em.persist(new Member("m2", 0, teamA));
        em.flush();
//when //Probe 생성
        Member member = new Member("m1");
        Team team = new Team("teamA"); //내부조인으로 teamA 가능
        member.setTeam(team);
//ExampleMatcher 생성, age 프로퍼티는 무시
 ExampleMatcher matcher = ExampleMatcher.matching()
                  .withIgnorePaths("age");
        Example<Member> example = Example.of(member, matcher);
        List<Member> result = memberRepository.findAll(example);
//then
        assertThat(result.size()).isEqualTo(1);
    }
}
