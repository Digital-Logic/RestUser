package net.digitallogic.RestUser.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import net.digitallogic.RestUser.mapper.UserMapper;
import net.digitallogic.RestUser.persistence.model.UserEntity;
import net.digitallogic.RestUser.web.Routes;
import net.digitallogic.RestUser.web.controller.request.LoginRequest;
import net.digitallogic.RestUser.web.controller.response.UserResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class UsernamePasswordAuthenticationFiter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenGenerator accessToken;
    private final UserMapper userMapper;

    public UsernamePasswordAuthenticationFiter(AuthenticationManager authenticationManager,
                                               JwtTokenGenerator accessToken, UserMapper userMapper) {
        super(new AntPathRequestMatcher(Routes.LOGIN_ROUTE, HttpMethod.POST.name()));
        this.authenticationManager = authenticationManager;
        this.accessToken = accessToken;
        this.userMapper = userMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            LoginRequest loginRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequest.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {

        UserEntity user = (UserEntity) authResult.getPrincipal();

        Cookie cookie = new Cookie(SecurityConstants.ACCESS_TOKEN, accessToken.allocateToken(user));
        cookie.setMaxAge((int) SecurityConstants.TOKEN_EXPIRATION_TIME / 1000);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        String requestAcceptType = request.getHeader("accept");

        UserResponse userResponse = userMapper.toUserResponse(
                userMapper.toDto(user)
        );

        if ("application/xml".equalsIgnoreCase(requestAcceptType)) {
            // provide xml response
            response.setContentType("application/xml");
            new XmlMapper().writeValue(response.getWriter(), userResponse);
        } else {
            // provide json response
            response.setContentType("application/json");
            new ObjectMapper().writeValue(response.getWriter(), userResponse);
        }
    }
}
