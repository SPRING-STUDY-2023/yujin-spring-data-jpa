package jpabook.jpashop.dto;

import jpabook.jpashop.domain.Member;
import lombok.Data;
@Data
public class MemberDto {
    private Long id;
    private String username;
    private String teamName;
    public MemberDto(Member m) {
        this.id = m.getId();
        this.username = m.getUsername();
    }
    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }
}