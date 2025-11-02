package org.ruoyi.chat.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.ruoyi.chat.supabase.SupabaseAuthService;
import org.ruoyi.chat.supabase.SupabaseAuthService.SupabaseUser;
import org.ruoyi.common.core.web.domain.AjaxResult;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    private final SupabaseAuthService supabaseAuthService;

    public AuthController(SupabaseAuthService supabaseAuthService) {
        this.supabaseAuthService = supabaseAuthService;
    }

    @PostMapping("/login")
    public AjaxResult login(
        @RequestBody @Valid LoginRequest request,
        @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader
    ) throws Exception {
        SupabaseUser supabaseUser = verifySupabaseToken(authorizationHeader);
        Map<String, Object> data = new HashMap<>();
        data.put("token", supabaseUser.rawToken());
        data.put("userId", supabaseUser.userId());
        data.put("email", supabaseUser.email());
        return AjaxResult.success(data);
    }

    @PostMapping("/register")
    public AjaxResult register(
        @RequestBody @Valid RegisterRequest request,
        @RequestHeader(value = "X-Supabase-Uid", required = false) String supabaseUid
    ) {
        Map<String, Object> data = new HashMap<>();
        data.put("supabaseUid", supabaseUid);
        data.put("email", request.getEmail());
        // TODO: 将用户同步到本地 sys_user 并分配默认角色
        return AjaxResult.success(data);
    }

    @PostMapping("/logout")
    public AjaxResult logout() {
        return AjaxResult.success("退出成功");
    }

    private SupabaseUser verifySupabaseToken(String authorizationHeader) throws ParseException, com.nimbusds.jose.proc.BadJOSEException, com.nimbusds.jose.JOSEException, java.net.MalformedURLException {
        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Supabase token 缺失");
        }
        String token = authorizationHeader.substring("Bearer ".length());
        return supabaseAuthService.verifyAccessToken(token);
    }

    public record LoginRequest(
        @Email(message = "邮箱格式不正确") @NotBlank(message = "邮箱不能为空") String email,
        @NotBlank(message = "密码不能为空") String password
    ) {
    }

    public static class RegisterRequest {
        @Email(message = "邮箱格式不正确")
        @NotBlank(message = "邮箱不能为空")
        private String email;

        @NotBlank(message = "密码不能为空")
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
