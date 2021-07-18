package first.hellospring;

import first.hellospring.repository.*;
import first.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Configuration
public class SpringConfig {
    private final SpringDataJpaMemberRepository jpamemberRepository;

    @Autowired
    public SpringConfig(SpringDataJpaMemberRepository jpamemberRepository){
        this.jpamemberRepository = jpamemberRepository;
    }

    @Bean
    public MemberService memberService(){
        return new MemberService(jpamemberRepository);
    }
    /*
    private EntityManager em;

    public SpringConfig(EntityManager em) {
        this.em = em;
    }

     */
/*
    private final DataSource dataSource;

    public SpringConfig(DataSource dataSource) {
        this.dataSource=dataSource;
    }

 */
/*
    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository(){
        //return new MemoryMemberRepository();
        //return new JdbcTemplateMemberRepository(dataSource);
        return new JpaMemberRepository(em);
    }

 */

}
