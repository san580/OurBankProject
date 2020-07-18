package com.rab3tech.customer.service;

import java.util.List;

import com.rab3tech.vo.PayeeInfoVO;

public interface PayeeService {
 
	public List<PayeeInfoVO> findAllPayee(String CustomerId);
	public String  savePayee(PayeeInfoVO payeeInfoVO);
	void deletePayee(int payeeId);
	public PayeeInfoVO findPayee(int payeeId);//all values are in vo so making an api and fetching in jsp
	public String editPayee(PayeeInfoVO payeeInfoVO); 
	
}
