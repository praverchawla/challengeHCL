package com.db.awmd.challenge.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepository;

import lombok.Getter;

@Service
public class AccountsService {

	@Getter
	private final AccountsRepository accountsRepository;

	@Autowired
	public AccountsService(AccountsRepository accountsRepository) {
		this.accountsRepository = accountsRepository;
	}

	public void createAccount(Account account) {
		this.accountsRepository.createAccount(account);
	}

	public Account getAccount(String accountId) {
		return this.accountsRepository.getAccount(accountId);
	}

	public void credit(BigDecimal creditAmount, String accountId) {
		Account account = getAccount(accountId);
		synchronized (account) {
			account.setBalance(account.getBalance().add(creditAmount));
		}
	}

	public void debit(BigDecimal debitAmount, String accountId) throws Exception {
		Account account = getAccount(accountId);
		synchronized (account) {
			BigDecimal balanceAmount = account.getBalance().subtract(debitAmount);
			if(balanceAmount.compareTo(BigDecimal.ZERO) != -1){
				account.setBalance(balanceAmount);
			} else {
				throw new NumberFormatException("Not enough balance in account");
			}
		}
	}
}
