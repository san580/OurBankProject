package com.rab3tech.customer.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rab3tech.dao.entity.CustomerAccountInfo;

public interface CreateAccountRepository extends JpaRepository<CustomerAccountInfo, Long> {

}
