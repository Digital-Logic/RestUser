package net.digitallogic.RestUser.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    private final String encoderId;
    private final int bCryptRounds;

    @Autowired
    public AppConfig(
            @Value("${password.encoderId}") String encoderId,
            @Value("${bCryptRounds}") int bCryptRounds) {
        this.encoderId = encoderId;
        this.bCryptRounds = bCryptRounds;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcypt", new BCryptPasswordEncoder(bCryptRounds));

        DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder(encoderId, encoders);

        return encoder;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
