package com.sample.account_service.service;

import com.sample.account_service.entity.Account;
import com.sample.account_service.form.CreateAccountForm;
import com.sample.account_service.repository.AccountRepository;
import com.sample.account_service.view.AccountView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void testGetList() {
        AccountView view = new AccountView("test", "USER");
        List<AccountView> accountViews = List.of(view);
//        when(accountService.getList()).thenReturn(accountViews);

        when(accountRepository.findAll()).thenReturn(List.of(Account.builder()
                .id(1L)
                .name("test")
                .role("USER").build()));
        List<AccountView> result = accountService.getList();

        assertEquals(1, result.size());
        assertEquals("test", result.get(0).getName());
    }

    @Test
    void testGetListByName() {
        when(accountRepository.findByNameLike(any())).thenReturn(List.of(Account.builder()
                .id(1L)
                .name("test")
                .role("USER").build()));
        List<AccountView> result = accountService.getList("test");

        assertEquals(1, result.size());
        assertEquals("test", result.get(0).getName());
    }

    @Test
    void testCreate() {
        CreateAccountForm form = new CreateAccountForm("test", "password", "USER");
        Account account = new Account(1L, "test", "password", "USER", "ACTIVE");
        AccountView accountView = new AccountView("test", "USER");

        when(accountRepository.saveAndFlush(any(Account.class))).thenReturn(account);
//        when(accountService.create(form)).thenReturn(accountView);

        AccountView result = accountService.create(form);

        assertEquals("test", result.getName());
        verify(accountRepository, times(1)).saveAndFlush(any(Account.class));
    }
}
