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
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

	@Test
	void testCreatePasswordEmpty() throws Exception {
		String auth = Base64.getEncoder().encodeToString("admin:Pass123@".getBytes());

		CreateAccountForm form = new CreateAccountForm("test", null, "USER");

		ObjectMapper objectMapper = new ObjectMapper();
		String formJson = objectMapper.writeValueAsString(form);

		mockMvc.perform(post("/accounts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(formJson)
						.header("Authorization", "Basic " + auth))
				.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(jsonPath("$.messages", hasItems("password : must not be blank")));
	}

	@Test
	void testCreateEmptyBody() throws Exception {
		String auth = Base64.getEncoder().encodeToString("admin:Pass123@".getBytes());

		mockMvc.perform(post("/accounts")
						.header("Authorization", "Basic " + auth))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
				.andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
				.andExpect(jsonPath("$.message", is("Required request body is missing")));
	}

	@Test
	void testInvalidMethod() throws Exception {
		String auth = Base64.getEncoder().encodeToString("admin:Pass123@".getBytes());

		mockMvc.perform(patch("/accounts")
						.header("Authorization", "Basic " + auth))
				.andExpect(status().isMethodNotAllowed())
				.andExpect(jsonPath("$.status", is(HttpStatus.METHOD_NOT_ALLOWED.value())))
				.andExpect(jsonPath("$.error", is(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())))
				.andExpect(jsonPath("$.message", is("method")));
	}

	@Test
	void testCreateDuplicate() throws Exception {
		String auth = Base64.getEncoder().encodeToString("admin:Pass123@".getBytes());

		CreateAccountForm form = new CreateAccountForm("admin", "Abc12345$", "ADMIN");

		ObjectMapper objectMapper = new ObjectMapper();
		String formJson = objectMapper.writeValueAsString(form);

		mockMvc.perform(post("/accounts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(formJson)
						.header("Authorization", "Basic " + auth))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status", is(HttpStatus.CONFLICT.value())))
				.andExpect(jsonPath("$.error", is(HttpStatus.CONFLICT.getReasonPhrase())));
	}

	@Test
	void testInternalError() throws Exception {
		String auth = Base64.getEncoder().encodeToString("admin:Pass123@".getBytes());

		CreateAccountForm form = new CreateAccountForm("admin2", "Abc12345$", "ADMIN");

		ObjectMapper objectMapper = new ObjectMapper();
		String formJson = objectMapper.writeValueAsString(form);

		try (MockedStatic<AccountView> mockedStatic = mockStatic(AccountView.class)) {
			when(AccountView.of(any())).thenThrow(new RuntimeException("custom internal error message"));
			mockMvc.perform(post("/accounts")
							.contentType(MediaType.APPLICATION_JSON)
							.content(formJson)
							.header("Authorization", "Basic " + auth))
					.andExpect(status().isInternalServerError())
					.andExpect(jsonPath("$.status", is(HttpStatus.INTERNAL_SERVER_ERROR.value())))
					.andExpect(jsonPath("$.error", is(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())))
					.andExpect(jsonPath("$.message", is("custom internal error message")));
		}

	}
}
