package com.shortthirdman.primekit.keycloak.adminclient.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KeycloakAdminConfig {

    private final KeycloakPropertiesConfig propertiesConfig;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(propertiesConfig.getEndpoint())
                .realm(propertiesConfig.getRealm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(propertiesConfig.getApplication().getClientId())
                .clientSecret(propertiesConfig.getApplication().getClientSecret())
                .build();
    }
}
