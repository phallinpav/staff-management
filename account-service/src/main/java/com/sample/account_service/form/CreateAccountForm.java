package com.sample.account_service.form;

import com.sample.account_service.entity.Account;
import com.sample.account_service.utils.PasswordUtils;
import com.sample.account_service.validator.Password;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountForm {
    @NotBlank
    @Size(min = 2, max=100)
    private String name;

    @NotBlank
    @Password
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
