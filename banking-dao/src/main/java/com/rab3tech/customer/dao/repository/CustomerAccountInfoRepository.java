package com.rab3tech.customer.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.Login;


/*
 * public interface CustomerAccountInfoRepository extends
 * JpaRepository<CustomerAccountInfoVO, Integer> {
 * 
 * CustomerAccountInfo getAccount(CustomerAccountInfoVO customerAccountInfoVO);
 * 
 * }
 */

public interface CustomerAccountInfoRepository extends JpaRepository<CustomerAccountInfo, Long> {

	//List<CustomerAccountInfo> findPayeeInfo(String customerId);
	Optional <CustomerAccountInfo> findByAccountNumber(String accountNumber);
	Optional <CustomerAccountInfo> findByCustomerId(Login customerId);//check entity, as it has Login customerId

}