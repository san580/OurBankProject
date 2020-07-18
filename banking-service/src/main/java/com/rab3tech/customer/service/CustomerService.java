package com.rab3tech.customer.service;

import com.rab3tech.vo.CustomerAccountInfoVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.PayeeInfoVO;

public interface CustomerService {

	CustomerVO createAccount(CustomerVO customerVO);

	CustomerVO myAccount(String username);

	void updateMyAccount(CustomerVO customerVO);

	CustomerAccountInfoVO createBankAccount(int csaid);

	


}
