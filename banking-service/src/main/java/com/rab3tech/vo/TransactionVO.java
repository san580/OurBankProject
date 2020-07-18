package com.rab3tech.vo;

import java.util.Date;

import com.rab3tech.dao.entity.PayeeInfo;

public class TransactionVO {

	private int payeeId;
	private String debitAccountNumber;
	private String description;
	private float amount;
	private String customerId;
	private String accountTransactionType;
	private PayeeInfo payee;
	private Date transactionDate;
	

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getAccountTransactionType() {
		return accountTransactionType;
	}

	public void setAccountTransactionType(String accountTransactionType) {
		this.accountTransactionType = accountTransactionType;
	}

	public PayeeInfo getPayee() {
		return payee;
	}

	public void setPayee(PayeeInfo payee) {
		this.payee = payee;
	}

	public int getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(int payeeId) {
		this.payeeId = payeeId;
	}

	public String getDebitAccountNumber() {
		return debitAccountNumber;
	}

	public void setDebitAccountNumber(String debitAccountNumber) {
		this.debitAccountNumber = debitAccountNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Override
	public String toString() {
		return "TransactionVO [payeeId=" + payeeId + ", debitAccountNumber=" + debitAccountNumber + ", description="
				+ description + ", amount=" + amount + ", customerId=" + customerId + "]";
	}

}
