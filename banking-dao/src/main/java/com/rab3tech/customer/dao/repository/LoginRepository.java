package com.rab3tech.customer.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rab3tech.dao.entity.Login;

/**
 * 
 * @author nagendra
 * 
 * Spring Data JPA repository
 *
 */
public interface LoginRepository extends JpaRepository<Login, String> {
	
	public Optional<Login> findByLoginidAndPassword(String loginid,String password);
	public Optional<Login> findByLoginid(String loginid);
}

