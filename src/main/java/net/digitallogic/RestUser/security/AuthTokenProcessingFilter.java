package net.digitallogic.RestUser.security;

import net.digitallogic.RestUser.persistence.model.UserEntity;
import net.digitallogic.RestUser.persistence.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class AuthTokenProcessingFilter extends OncePerRequestFilter {

    private final JwtTokenGenerator accessToken;
    private final UserRepository userRepository;

    public AuthTokenProcessingFilter(JwtTokenGenerator accessToken, UserRepository userRepository) {
        this.accessToken = accessToken;
        this.userRepository = userRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Cookie accessCookie = WebUtils.getCookie(request, SecurityConstants.ACCESS_TOKEN);

        if (accessCookie != null) {

            String userId = accessToken.verifyToken(accessCookie.getValue());
            Optional<UserEntity> userOptional = userRepository.findByIdWithRolesAndAuthorities(UUID.fromString(userId));
            if (userOptional.isEmpty()) {
                return;
            }

            UserEntity user = userOptional.get();

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user, accessCookie.getValue(), user.getAuthorities())
            );
        }

        filterChain.doFilter(request, response);
    }
}
