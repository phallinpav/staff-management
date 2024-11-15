package com.sample.account_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.account_service.entity.Account;
import com.sample.account_service.form.CreateAccountForm;
import com.sample.account_service.repository.AccountRepository;
import com.sample.account_service.view.AccountView;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AccountRepository accountRepository;

	@Test
	void contextLoads() {
	}

	@Test
	@Order(1)
	void testGetAccountList() throws Exception {
		String auth = Base64.getEncoder().encodeToString("admin:Pass123@".getBytes());

		ObjectMapper mapper = new ObjectMapper();
		List<Account> accounts = accountRepository.findAll();
		String result = mapper.writeValueAsString(accounts.stream().map(AccountView::of).toList());

		mockMvc.perform(get("/accounts")
						.header("Authorization", "Basic " + auth))
				.andExpect(status().isOk())
				.andExpect(content().json(result));
	}

	@Test
	@Order(2)
	void testCreateAccount() throws Exception {
		String auth = Base64.getEncoder().encodeToString("admin:Pass123@".getBytes());

		CreateAccountForm form = new CreateAccountForm("test", "P123@p123", "USER");
		AccountView accountView = new AccountView("test", "USER");

		ObjectMapper objectMapper = new ObjectMapper();
		String formJson = objectMapper.writeValueAsString(form);

		mockMvc.perform(post("/accounts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(formJson)
						.header("Authorization", "Basic " + auth))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(accountView)));
	}

	@Test
	@Order(3)
	void testGetAccount2() throws Exception {
		String auth = Base64.getEncoder().encodeToString("test:P123@p123".getBytes());

		ObjectMapper mapper = new ObjectMapper();
		List<Account> accounts = accountRepository.findAll();
		String result = mapper.writeValueAsString(accounts.stream().map(AccountView::of).toList());

		mockMvc.perform(get("/accounts")
						.header("Authorization", "Basic " + auth))
				.andExpect(status().isOk())
				.andExpect(content().json(result));
	}

	@Test
	@Order(4)
	void testWrongPassword() throws Exception {
		String auth = Base64.getEncoder().encodeToString("test:P123@p12".getBytes());

		mockMvc.perform(get("/accounts")
						.header("Authorization", "Basic " + auth))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@Order(5)
	void testNoUser() throws Exception {
		String auth = Base64.getEncoder().encodeToString("test1:P123@p123".getBytes());

		mockMvc.perform(get("/accounts")
						.header("Authorization", "Basic " + auth))
				.andExpect(status().isUnauthorized());
	}
}
