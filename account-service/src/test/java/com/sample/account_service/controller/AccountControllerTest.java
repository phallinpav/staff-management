package com.sample.account_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.account_service.form.CreateAccountForm;
import com.sample.account_service.service.AccountService;
import com.sample.account_service.view.AccountView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    void testList() throws Exception {
        List<AccountView> accountViews = List.of(new AccountView("test", "USER"));
        when(accountService.getList()).thenReturn(accountViews);

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(accountViews);

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(content().json(result));
    }

    @Test
    void testListByName() throws Exception {
        List<AccountView> accountViews = List.of(new AccountView("test", "USER"));
        when(accountService.getList("test")).thenReturn(accountViews);

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(accountViews);

        mockMvc.perform(get("/accounts").param("name", "test"))
                .andExpect(status().isOk())
                .andExpect(content().json(result));
    }

    @Test
    void testCreate() throws Exception {
        CreateAccountForm form = new CreateAccountForm("test", "Password123@", "USER");
        AccountView accountView = new AccountView("test", "USER");

        when(accountService.create(any(CreateAccountForm.class))).thenReturn(accountView);

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(accountView);
        String formJson = objectMapper.writeValueAsString(form);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(formJson))
                .andExpect(status().isOk())
                .andExpect(content().json(result));
    }

    @Test
    void testCreateNameInvalid() throws Exception {
        CreateAccountForm form = new CreateAccountForm("", "password", "USER");
        AccountView accountView = new AccountView("test", "USER");

        when(accountService.create(any(CreateAccountForm.class))).thenReturn(accountView);

        ObjectMapper objectMapper = new ObjectMapper();
        String formJson = objectMapper.writeValueAsString(form);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(formJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateRoleInvalid() throws Exception {
        CreateAccountForm form = new CreateAccountForm("test", "password", "");
        AccountView accountView = new AccountView("test", "USER");

        when(accountService.create(any(CreateAccountForm.class))).thenReturn(accountView);

        ObjectMapper objectMapper = new ObjectMapper();
        String formJson = objectMapper.writeValueAsString(form);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(formJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreatePasswordInvalid() throws Exception {
        CreateAccountForm form = new CreateAccountForm("test", "password", "USER");
        AccountView accountView = new AccountView("test", "USER");

        when(accountService.create(any(CreateAccountForm.class))).thenReturn(accountView);

        ObjectMapper objectMapper = new ObjectMapper();
        String formJson = objectMapper.writeValueAsString(form);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(formJson))
                .andExpect(status().isBadRequest());
    }
}
