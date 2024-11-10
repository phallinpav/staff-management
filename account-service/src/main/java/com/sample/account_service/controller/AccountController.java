package com.sample.account_service.controller;

import com.sample.account_service.form.CreateAccountForm;
import com.sample.account_service.service.AccountService;
import com.sample.account_service.view.AccountView;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@SecurityRequirement(name = "basicAuth")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public List<AccountView> list() {
        return accountService.getList();
    }

    @PostMapping
    public AccountView create(@Valid CreateAccountForm form) {
        return accountService.create(form);
    }
}
