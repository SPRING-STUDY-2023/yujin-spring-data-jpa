package jpabook.jpashop.repository;

import java.util.List;
import jpabook.jpashop.domain.Member;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
