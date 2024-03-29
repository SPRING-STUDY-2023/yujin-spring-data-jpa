package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Team;
import jpabook.jpashop.dto.UsernameOnly;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    private final EntityManager em;

    public MemberTest(EntityManager em) {
        this.em = em;
    }

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);
        Member findMember =
                memberRepository.findById(savedMember.getId()).get();
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member); //JPA 엔티티 동일성
    }

    @Test
    public void JpaEventBaseEntity() throws Exception {
//given
        Member member = new Member("member1");
        memberRepository.save(member); //@PrePersist
        Thread.sleep(100);
        member.setUsername("member2");
        em.flush(); //@PreUpdate
        em.clear();
//when
        Member findMember = memberRepository.findById(member.getId()).get();
//then
        System.out.println("findMember.createdDate = " +
                findMember.getCreatedDate());
        System.out.println("findMember.updatedDate = " +
                findMember.getUpdatedDate());
    }
    @Test
    public void projections() throws Exception {
//given
        Team teamA = new Team("teamA");
        em.persist(teamA);
        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);
        em.flush();
        em.clear();
//when
        List<UsernameOnly> result =
                memberRepository.findProjectionsByUsername("m1");


//        List<UsernameOnly> result2 = memberRepository.findProjectionsByUsername("m1",UsernameOnly.class);
//then
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

}
