package com.sample.account_service.form;

import com.sample.account_service.entity.Account;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAccountForm {
    @Size(min = 2, max=100)
    private String name;
    private String password;
    private String role;

    public Account toAccount() {
        return Account.builder()
                .name(name)
                .password(password)
                .role(role)
                .build();
    }
}
