package com.yy.my_tutor.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new AesPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/login").permitAll()
                .antMatchers("/user/register").permitAll()
                .antMatchers("/user/verificationCode").permitAll()
                .antMatchers("/user/verification").permitAll()
                .antMatchers("/user/getLoginCaptcha").permitAll()
                .antMatchers("/user/verify-email").permitAll()
                .antMatchers("/parent/addWithUsers").permitAll()
                .antMatchers("/user/existAccount").permitAll()
                .antMatchers("/parent/**").permitAll()
                .antMatchers("/payment-method-test.html").permitAll()
                .antMatchers("/api/payment/stripe/webhook").permitAll()
                .antMatchers(HttpMethod.GET,
                        "/api/payment/products",
                        "/api/payment/products/**",
                        "/api/payment/annual-license/quote").permitAll()
                .antMatchers("/api/admin/payment/**").authenticated()
                .antMatchers("/api/payment/**").authenticated()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/chat/**").authenticated()
                .antMatchers("/update").authenticated()
                .antMatchers("/student/**").authenticated()
                .antMatchers("/api/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public HttpFirewall httpFirewall() {
        StrictHttpFirewall strictHttpFirewall = new StrictHttpFirewall();
        strictHttpFirewall.setAllowUrlEncodedDoubleSlash(true);
        return strictHttpFirewall;
    }
}
