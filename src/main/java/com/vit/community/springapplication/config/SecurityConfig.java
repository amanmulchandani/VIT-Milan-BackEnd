package com.vit.community.springapplication.config;

import com.vit.community.springapplication.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/* This class holds the complete security configuration for our backend.
*  Extends the WebSecurityConfigurerAdapter class which provides the default
*  security configurations which are overridden and customised.
*
* The @EnableWebSecurity enables the Web Security in our application.
*
* The Lombok library generates the all arguments constructor, and the fields
* are automatically autowired (initialised) by Spring at runtime.
* */

@EnableWebSecurity
@AllArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /* The configure() method disables the CSRF protection because CSRF attacks
       mainly occur when there are sessions and cookies are used to authenticate
       the session information. As REST APIs are stateless by definition and as we are
       using JSON Web Tokens for authorization, we can safely disable this feature.

       This method allows all the incoming requests to the back-end API whose endpoint
       URL starts with:
       1) /api/auth as these endpoints are used for authentication and
       registration. We don’t expect the user to be authenticated at that point of time,
       2) /api/subreddit for creating subreddits,
       3) /api/posts for creating new posts in subreddits

       It also adds a JwtAuthenticationFilter class so that it gets executed before any
       request is forwarded to the server.
       */

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/**")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/subreddit")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/**")
                .permitAll()
                .antMatchers("/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**")
                .permitAll()
                .anyRequest()
                .authenticated();

        httpSecurity.addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);
    }

    /*
     * We have defined a bean for AuthenticationManager and configured AuthenticationManagerBuilder
     * to use the UserDetailsService interface to check for user information.
     */

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

//     We are using the Bcrypt Algorithm to encode the passwords before storing them in the database.

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
