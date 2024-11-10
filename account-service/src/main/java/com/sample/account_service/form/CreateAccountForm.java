package com.sample.account_service.form;

import com.sample.account_service.entity.Account;
import com.sample.account_service.utils.PasswordUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountForm {
    @NotBlank
    @Size(min = 2, max=100)
    private String name;

    // TODO: validate password
    //  - at least 8 characters
    //  - at least 1 uppercase letter
    //  - at least 1 lowercase letter
    //  - at least 1 number
    //  - at least 1 special character
    @NotBlank
    private String password;
    @NotBlank
    private String role;

    public Account toAccount() {
        return Account.builder()
                .name(name)
                .password(PasswordUtils.encodedPassword(password))
                .role(role)
                .build();
    }
}
