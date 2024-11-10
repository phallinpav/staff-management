package com.sample.account_service.service;

import com.sample.account_service.entity.Account;
import com.sample.account_service.form.CreateAccountForm;
import com.sample.account_service.repository.AccountRepository;
import com.sample.account_service.view.AccountView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    @Override
    public List<AccountView> getList() {
        return accountRepository.findAll().stream().map(AccountView::of).toList();
    }

    @Override
    public List<AccountView> getList(String name) {
        return accountRepository.findByNameLike(name).stream().map(AccountView::of).toList();
    }

    @Override
    public AccountView create(CreateAccountForm form) {
        return AccountView.of(accountRepository.saveAndFlush(form.toAccount()));
    }
}
