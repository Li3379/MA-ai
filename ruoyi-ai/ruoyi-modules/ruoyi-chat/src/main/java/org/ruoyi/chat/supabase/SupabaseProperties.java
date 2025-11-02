package org.ruoyi.chat.supabase;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "supabase")
public class SupabaseProperties {

    @NotBlank(message = "Supabase API 地址不能为空", groups = RequiredGroup.class)
    private String apiUrl;

    private String anonKey;

    private String serviceKey;

    private Storage storage = new Storage();

    @Getter
    @Setter
    public static class Storage {
        private String bucketMedical = "medical-files";
        private String bucketCommunity = "community";
    }

    public interface RequiredGroup {
    }
}
