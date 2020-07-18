package com.rab3tech.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "customer_account_transaction_tbl")
public class Transaction{
	
	private int id;
	private PayeeInfo payeeId;
	private String debitAccountNumber;
	private String description;
	private float amount;
	private Date transactionDate;
	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="payeeId",nullable=false)
	public PayeeInfo getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(PayeeInfo payeeId) {
		this.payeeId = payeeId;
	}

	public String getDebitAccountNumber() {
		return debitAccountNumber;
	}

	public void setDebitAccountNumber(String debitAccountNumber) {
		this.debitAccountNumber = debitAccountNumber;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", payeeId=" + payeeId + ", debitAccountNumber=" + debitAccountNumber
				+ ", description=" + description + ", amount=" + amount + ", transactionDate=" + transactionDate + "]";
	}

	

	

}