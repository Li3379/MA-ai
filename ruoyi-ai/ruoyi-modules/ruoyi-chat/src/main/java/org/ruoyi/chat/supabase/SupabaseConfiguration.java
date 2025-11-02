package org.ruoyi.chat.supabase;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(SupabaseProperties.class)
public class SupabaseConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "supabaseRestClient")
    public RestClient supabaseRestClient(SupabaseProperties properties) {
        return RestClient.builder()
            .baseUrl(properties.getApiUrl())
            .defaultHeader("apikey", properties.getServiceKey() != null ? properties.getServiceKey() : "")
            .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
}
