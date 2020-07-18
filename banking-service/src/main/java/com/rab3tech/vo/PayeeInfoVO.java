package com.rab3tech.vo;

import java.sql.Timestamp;

public class PayeeInfoVO {
	private int id;
	private String payeeAccountNo;
	private String payeeName;
	private String payeeNickName;
	private String customerId;
	private Timestamp doe;
	private Timestamp dom;
	private String remarks;
	private String status;
	private int urn;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPayeeAccountNo() {
		return payeeAccountNo;
	}
	public void setPayeeAccountNo(String payeeAccountNo) {
		this.payeeAccountNo = payeeAccountNo;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getPayeeNickName() {
		return payeeNickName;
	}
	public void setPayeeNickName(String payeeNickName) {
		this.payeeNickName = payeeNickName;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public Timestamp getDoe() {
		return doe;
	}
	public void setDoe(Timestamp doe) {
		this.doe = doe;
	}
	public Timestamp getDom() {
		return dom;
	}
	public void setDom(Timestamp dom) {
		this.dom = dom;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getUrn() {
		return urn;
	}
	public void setUrn(int urn) {
		this.urn = urn;
	}
	@Override
	public String toString() {
		return "PayeeInfoVO [id=" + id + ", payeeAccountNo=" + payeeAccountNo + ", payeeName=" + payeeName
				+ ", payeeNickName=" + payeeNickName + ", customerId=" + customerId + ", doe=" + doe + ", dom=" + dom
				+ ", remarks=" + remarks + ", status=" + status + ", urn=" + urn + "]";
	}
	
	
	

}
