package net.digitallogic.RestUser.config;

import net.digitallogic.RestUser.security.AccessToken;
import net.digitallogic.RestUser.security.JwtTokenGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtTokenConfig {

    private final String accessTokenSecret;
    private final String emailTokenSecret;
    private final String iss;

    public JwtTokenConfig( @Value("${token.access.secret}") String accessTokenSecret,
                           @Value("${token.email.secret}") String emailTokenSecret,
                           @Value("${token.iss}") String iss ) {
        this.accessTokenSecret = accessTokenSecret;
        this.emailTokenSecret = emailTokenSecret;
        this.iss = iss;
    }

    @Bean
    @AccessToken
    public JwtTokenGenerator generateAccessToken() {
        return new JwtTokenGenerator(accessTokenSecret, iss);
    }

    @Bean
    public JwtTokenGenerator generateEmailToken() {
        return new JwtTokenGenerator(emailTokenSecret, iss);
    }
}
