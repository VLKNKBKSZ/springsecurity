package com.vlkn.spring.security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.concurrent.TimeUnit;

import static com.vlkn.spring.security.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {

        UserDetails james = User.builder()
                .username("james")
                .password(passwordEncoder.encode("password"))
                .roles(STUDENT.name())
//                .roles(STUDENT.name())//ROLE_STUDENT
                .build();


        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .authorities(ADMIN.getGrantedAuthorities())
//                .roles(ADMIN.name())//ROLE_ADMIN
                .build();

        UserDetails adminTrainee = User.builder()
                .username("admint")
                .password(passwordEncoder.encode("admint"))
                .authorities(ADMIN_TRAINEE.getGrantedAuthorities())
//                .roles(ADMIN_TRAINEE.name())//ROLE_ADMIN_TRAINEE
                .build();
        return new InMemoryUserDetailsManager(admin, adminTrainee, james);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
// use CSRF protection for any request that could be processed by a browser by normal users.
// If you are only creating a service that is used by non-browser clients,
// you will likely want to disable CSRF protection
//                .csrf()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/", "/index", "/css/*", "/js/*").permitAll()
                    .anyRequest()
                    .authenticated()
                .and()
                // Postgres,Redis
                // By default spring uses inmemoryDB for session ID, when you restart the server,
                // The session ids will be lost.
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/courses",true)
                    .passwordParameter("password")
                    .usernameParameter("username")
                    .and()
                    .rememberMe()
                    .rememberMeParameter("remember-me")
                    .tokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(21))
                    .key("secureverysecuresupersecure")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID","remember-me")
                    .logoutSuccessUrl("/login");// defaults to 2 weeks.
                // CANT LOGOUT with basic auth.
//                .httpBasic();

    }
}
