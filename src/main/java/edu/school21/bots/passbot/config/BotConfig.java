package edu.school21.bots.passbot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class BotConfig {
    @Value("${bot.name}")
    String name;

    @Value("${bot.token}")
    String token;

    @Value("${user.oauth.clientId}")
    String credentialId;

    @Value("${user.oauth.clientSecret}")
    String credentialSecret;
}
