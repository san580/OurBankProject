package com.rab3tech.customer.service.impl;

import java.util.List;

import com.rab3tech.vo.CustomerSecurityQueAnsVO;
import com.rab3tech.vo.SecurityQuestionsVO;

public interface SecurityQuestionService {

	List<SecurityQuestionsVO> findAll();

	void save(CustomerSecurityQueAnsVO customerSecurityQueAnsVO);
	//CustomerSecurityQueAnsVO showSecurityQuestions(String username);//list of securityQuestions to bring by passing the username as it is used in controller

	CustomerSecurityQueAnsVO showSecurityQuestions(String userid);

	boolean validateQuestions(CustomerSecurityQueAnsVO customerSecurityQueAnsVO);

	boolean checkPassword(String username, String password);



}
