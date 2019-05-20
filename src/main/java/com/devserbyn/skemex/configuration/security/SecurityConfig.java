package com.devserbyn.skemex.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:security.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${session.cookieName}")
    public String cookieName;
    @Value("${session.inactiveInterval}")
    public int sessionInactiveInterval;

    @Autowired
    private UserDetailsService userDetailsService;


//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("**.ico","**.png ","**.css","**.js","**.html","/");
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                //.csrfTokenRepository(csrfTokenRepository())
                .cors()
                .configurationSource(corsConfigurationSource(""))
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((req, resp, ex) -> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .authorizeRequests()
                .antMatchers("/index.html", "/", "/home"
                        ,"/favicon.ico","/*.js","/*.css","/*.png", "/info/infoDocURL", "/info/infoDoc").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic().disable()
                .formLogin()
                .successHandler((req, resp, auth) ->{
                     resp.setStatus(HttpServletResponse.SC_OK);
                })
                .failureHandler((req, resp, ex) ->
                        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED))
                .permitAll()
                .and()
                .rememberMe()
                .rememberMeParameter("remember-me")
                .rememberMeCookieName("REMEMBER-ME")
                .tokenValiditySeconds(this.sessionInactiveInterval * 2)
                .and()
                .logout()
                .logoutSuccessHandler((req, resp, auth) -> resp.setStatus(HttpServletResponse.SC_OK))
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies(cookieName);
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository =
                new CookieCsrfTokenRepository();
        repository.setCookiePath("/");
        repository.setCookieHttpOnly(false);
        return repository;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(@Value("#{corsURl}") String corsURL) {
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "DELETE"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList("*"));
        final UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

