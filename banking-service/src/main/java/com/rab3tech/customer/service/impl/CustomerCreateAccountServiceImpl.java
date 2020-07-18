package com.rab3tech.customer.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.vo.CustomerAccountInfoVO;

@Transactional
@Service
public class CustomerCreateAccountServiceImpl implements CustomerCreateAccountService {
	@Autowired
	private CreateAccountRepository createAccountRepository;

	@Override
	public void createAccount(CustomerAccountInfoVO customerAccountInfoVO) {
		CustomerAccountInfo customerAccountInfo = new CustomerAccountInfo();
		BeanUtils.copyProperties(customerAccountInfoVO, customerAccountInfo);
		createAccountRepository.save(customerAccountInfo);

	}

}
