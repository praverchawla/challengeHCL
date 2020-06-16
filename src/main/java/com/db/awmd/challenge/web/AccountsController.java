package com.db.awmd.challenge.web;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransferBalanceRequest;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.EmailNotificationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {

	private final AccountsService accountsService;
	private final EmailNotificationService emailService;

	@Autowired
	public AccountsController(AccountsService accountsService, EmailNotificationService emailService) {
		this.accountsService = accountsService;
		this.emailService = emailService;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
		/* log.info("Creating account {}", account); */

		try {
			this.accountsService.createAccount(account);
		} catch (DuplicateAccountIdException daie) {
			return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping(path = "/{accountId}")
	public Account getAccount(@PathVariable String accountId) {
		/* log.info("Retrieving account for id {}", accountId); */
		return this.accountsService.getAccount(accountId);
	}

	@PostMapping(path = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> transferMoney(@RequestBody @Valid TransferBalanceRequest transferBalanceRequest) {

		try {
			BigDecimal amount = transferBalanceRequest.getAmount();
			String fromAccount = transferBalanceRequest.getFromAccountNumber();
			String toAccount = transferBalanceRequest.getToAccountNumber();
			if (amount.compareTo(BigDecimal.ZERO) == 1 && !(fromAccount.equals(toAccount))) {
				this.accountsService.debit(amount, fromAccount);
				String debitMsg = (new StringBuilder("Amount - ").append(amount).append("is transferred to ").append(getAccount(toAccount))).toString();
				this.emailService.notifyAboutTransfer(getAccount(fromAccount),debitMsg.toString());
				this.accountsService.credit(amount, toAccount);
				String creditMsg = (new StringBuilder("Amount - ").append(amount).append("is transferred from ").append(getAccount(fromAccount))).toString();
				this.emailService.notifyAboutTransfer(getAccount(toAccount), creditMsg);
			} else {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException exception) {
			return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

}
