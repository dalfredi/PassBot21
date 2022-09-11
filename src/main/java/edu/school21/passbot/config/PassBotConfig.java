package edu.school21.passbot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class PassBotConfig {
    @Value("${bot.name}")
    String name;

    @Value("${bot.token}")
    String token;

    @Value("${intra.oauth.clientId}")
    String credentialId;

    @Value("${intra.oauth.clientSecret}")
    String credentialSecret;
}
