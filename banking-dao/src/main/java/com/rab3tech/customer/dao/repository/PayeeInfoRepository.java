package com.rab3tech.customer.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rab3tech.dao.entity.PayeeInfo;

public interface PayeeInfoRepository extends JpaRepository<PayeeInfo, Integer> {
	//@Query("SELECT c FROM PayeeInfo c where c.customerId = :pcustomerId")
	//public Optional<PayeeInfo> findByCustomerId(@Param("pcustomerId")String customerId);
	
	public List<PayeeInfo> findAllByCustomerId(String customerId);
	 public Optional<PayeeInfo> findBeneficiaryBypayeeNickName(String payeeNickName);
	// public Optional <PayeeInfo>findBeneficiaryByAccountId(String customerId, String payeeAccountNo );
	 
	 public Optional<PayeeInfo> findByStatus(String status);
	
	
	

}
