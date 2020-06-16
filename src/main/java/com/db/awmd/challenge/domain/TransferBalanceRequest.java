package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

public class TransferBalanceRequest {
    private String fromAccountNumber;

    private String toAccountNumber;

    private BigDecimal amount;

	/**
	 * @return the fromAccountNumber
	 */
	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	/**
	 * @param fromAccountNumber the fromAccountNumber to set
	 */
	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	/**
	 * @return the toAccountNumber
	 */
	public String getToAccountNumber() {
		return toAccountNumber;
	}

	/**
	 * @param toAccountNumber the toAccountNumber to set
	 */
	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}