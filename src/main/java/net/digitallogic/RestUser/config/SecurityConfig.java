package net.digitallogic.RestUser.config;

import net.digitallogic.RestUser.mapper.UserMapper;
import net.digitallogic.RestUser.persistence.repository.UserRepository;
import net.digitallogic.RestUser.security.AccessToken;
import net.digitallogic.RestUser.security.JwtTokenGenerator;
import net.digitallogic.RestUser.security.UsernamePasswordAuthenticationFiter;
import net.digitallogic.RestUser.security.AuthTokenProcessingFilter;
import net.digitallogic.RestUser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenGenerator accessToken;
    private final UserMapper userMapper;




    @Autowired
    public SecurityConfig(AuthenticationManagerBuilder authenticationManagerBuilder,
                          UserService userService, @AccessToken JwtTokenGenerator accessToken,
                          UserMapper userMapper, UserRepository userRepository) throws Exception {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
        this.accessToken = accessToken;
        this.userMapper = userMapper;
        this.userRepository = userRepository;

        authenticationManagerBuilder.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                    .disable()
                .csrf()
                    .disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .addFilterAt(new UsernamePasswordAuthenticationFiter(authenticationManager(), accessToken, userMapper),
                    UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new AuthTokenProcessingFilter(accessToken, userRepository),UsernamePasswordAuthenticationFilter.class)
        .authorizeRequests()
            .anyRequest()
            .authenticated()
        ;
    }


}
