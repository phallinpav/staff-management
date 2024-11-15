package com.sample.account_service.controller;

import com.sample.account_service.form.CreateAccountForm;
import com.sample.account_service.service.AccountService;
import com.sample.account_service.view.AccountView;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@SecurityRequirement(name = "basicAuth")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public List<AccountView> list(@RequestParam(required = false) String name, @RequestParam(required = false) String role) {
        // TODO: search by name or role
        //  when name and role = null, return all accounts
        //  when name is not null, return accounts with the name search with LIKE
        //  when role is not null, return accounts with the role search equals
        //  when name and role are not null, return accounts with the name search with LIKE and role equals
        if (name != null) {
            return accountService.getList(name);
        } else {
            return accountService.getList();
        }
    }

    @PostMapping
    public AccountView create(@RequestBody @Valid CreateAccountForm form) {
        return accountService.create(form);
    }

    @PutMapping
    public AccountView update() {
        // TODO: implement update account
        return null;
    }

    @DeleteMapping
    public void delete() {
        // TODO: implement delete account
    }
}
