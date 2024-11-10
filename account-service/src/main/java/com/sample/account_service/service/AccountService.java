package com.sample.account_service.service;

import com.sample.account_service.form.CreateAccountForm;
import com.sample.account_service.view.AccountView;

import java.util.List;

public interface AccountService {
    List<AccountView> getList();
    AccountView create(CreateAccountForm form);
}
