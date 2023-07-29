package com.example.BlogAPI.config;

import com.example.BlogAPI.helper.Constants;
import com.example.BlogAPI.services.UserAuthDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserAuthDetailService userAuthDetailService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userAuthDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, Constants.USER_API).permitAll()
                .antMatchers(Constants.LOGIN_API).permitAll()
                .antMatchers(HttpMethod.PUT, Constants.USER_API).hasRole("USER")
                .antMatchers(HttpMethod.DELETE, Constants.USER_API).hasRole("USER")
                .antMatchers(Constants.CHANGE_PASSWORD).hasRole("USER")
                .antMatchers(Constants.POST_API).hasRole("USER")
                .antMatchers(Constants.POST_DELETE_API).hasRole("USER")
                .antMatchers(Constants.POST_BY_CATEGORY_API).hasRole("USER")
                .antMatchers(Constants.POST_BY_AUTHOR_API).hasRole("USER")
                .antMatchers(Constants.CATEGORY_API).hasRole("USER")
                .antMatchers(Constants.CATEGORY_UPDATE_API).hasAnyRole("USER").anyRequest().authenticated().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
