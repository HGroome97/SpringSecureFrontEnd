package com.qa;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


	@Autowired
	DataSource dataSource;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        		.antMatchers("/admin").access("hasRole('ROLE_ADMIN')")
        		.antMatchers("/", "/home").permitAll()
                .anyRequest().authenticated()
            .and()
            	.formLogin()
                .loginPage("/login")
                .permitAll()
            .and()
            	.logout()
                .permitAll()
            .and()
            	.exceptionHandling()
            	.accessDeniedPage("/accessDenied");
    }
    
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
      auth.jdbcAuthentication().dataSource(dataSource)
     .usersByUsernameQuery("select username,password, enabled from users where username=?")
     .authoritiesByUsernameQuery("select username, role from user_roles where username=?");
    } 

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("javacodegeeks").password("very_secure").roles("USER");
//        auth.inMemoryAuthentication().withUser("tom").password("toby").roles("USER");
//        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
//    }
}
