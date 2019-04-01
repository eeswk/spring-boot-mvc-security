package com.zerock.security;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Log
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("security config.....");

        http.authorizeRequests().antMatchers("/boards/list").permitAll()
                .antMatchers("/boards/register")
                .hasAnyRole("BASIC","MANAGER","ADMIN");


/*
        http.authorizeRequests().antMatchers("/css/**", "/js/**").permitAll();
        http.authorizeRequests().antMatchers("/guest/**").permitAll();
        http.authorizeRequests().antMatchers("/manager/**").hasRole("MANAGER");
        http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN");
*/
        //http.formLogin().loginPage("/login");
        http.formLogin().loginPage("/login").successHandler(new LoginSuccessHandler());
        http.exceptionHandling().accessDeniedPage("/accessDenied");

        //세션 무효화
        http.logout().logoutUrl("/logout").invalidateHttpSession(true);

        //Custom 인증
        //http.userDetailsService(zerockUserService);
        //RememberMe 사용 DB저장
        http.rememberMe().key("zerock")
                .userDetailsService(zerockUserService)
                .tokenRepository(getJDBCRepository())
                .tokenValiditySeconds(60*60*24);

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        log.info("build Auth global....");

        //inMemory
        /*
        auth.inMemoryAuthentication()
                .withUser("manager")
                .password("{noop}1111")
                .roles("MANAGER");
        */

        /*
        //jdbc
        String query1 = "select uid username, upw password, true enabled from tbl_members where uid = ?";
        String query2 = "select member uid, role_name role from tbl_member_roles where member = ?";

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(query1)
                .rolePrefix("ROLE_")
                .authoritiesByUsernameQuery(query2)
                .passwordEncoder(new BCryptPasswordEncoder());
        */

        auth.userDetailsService(zerockUserService)
                .passwordEncoder(passwordEncoder());
    }

    private PersistentTokenRepository getJDBCRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

    //jdbc 인증
    @Autowired
    DataSource dataSource;

    //Custom 인증
    @Autowired
    ZerockUserService zerockUserService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }






}
