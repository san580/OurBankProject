package com.rab3tech.customer.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.customer.dao.repository.CustomerQuestionsAnsRepository;
import com.rab3tech.customer.dao.repository.CustomerRepository;
import com.rab3tech.customer.dao.repository.LoginRepository;
import com.rab3tech.customer.dao.repository.SecurityQuestionsRepository;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerQuestionAnswer;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.dao.entity.SecurityQuestions;
import com.rab3tech.vo.ChangePasswordVO;
import com.rab3tech.vo.CustomerSecurityQueAnsVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.SecurityQuestionsVO;
@Transactional
@Service

public  class SecurityQuestionServiceImpl implements SecurityQuestionService {
	@Autowired
	private SecurityQuestionsRepository questionsRepository;
	
	@Autowired
	private LoginRepository loginRepository;

	
	@Autowired
	private CustomerQuestionsAnsRepository customerQuestionsAnsRepository;
	//Goes to Customer_as_table and save the questions/answer given
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	

	

	
	
	
	@Override
	public boolean checkPassword(String username, String password){
		Login login = loginRepository.findByLoginid(username).get();
		String encodedPassword = bCryptPasswordEncoder.encode(password);
		if(login.getPassword().equals(encodedPassword)) {
			return true;
		}else {
			return false;
		}


	}
	
	
	public String savePassword(ChangePasswordVO changePasswordVO) {
		String encodedPassword = bCryptPasswordEncoder.encode(changePasswordVO.getNewPassword());
		Login login=loginRepository.findById(changePasswordVO.getLoginid()).get();
		login.setPassword(encodedPassword);
		
		
		
		return null;
		
	}
	



	
	
	@Override
	public CustomerSecurityQueAnsVO showSecurityQuestions(String userid) {
		Login login= loginRepository.findByLoginid(userid).get();//db bata userid nikalyo
		List<CustomerQuestionAnswer> answers=customerQuestionsAnsRepository.findQuestionAnswer(login.getLoginid());
		CustomerSecurityQueAnsVO ansVO = new CustomerSecurityQueAnsVO();
		 ansVO.setSecurityQuestion1(answers.get(0).getQuestion());
		 ansVO.setSecurityQuestion2(answers.get(1).getQuestion());
		return ansVO;
	}

	
@Override
	public boolean validateQuestions(CustomerSecurityQueAnsVO customerSecurityQueAnsVO) {
	boolean validate = false;
	Login login=loginRepository.findById(customerSecurityQueAnsVO.getLoginid()).get();//vo bata loginid nikalyo
		//List<CustomerQuestionAnswer> customerQuestionAnswers=customerQuestionsAnsRepository.findQuestionAnswer(customerSecurityQueAnsVO.getLoginid());
		List<CustomerQuestionAnswer> customerQuestionAnswers=customerQuestionsAnsRepository.findQuestionAnswer(customerSecurityQueAnsVO.getLoginid());
		if(customerQuestionAnswers.get(0).getQuestion().equals(customerSecurityQueAnsVO.getSecurityQuestion1()) && customerQuestionAnswers.get(0).getAnswer().equals(customerSecurityQueAnsVO.getSecurityQuestionAnswer1())) {
			validate=true;
		}
		if(customerQuestionAnswers.get(1).getQuestion().equals(customerSecurityQueAnsVO.getSecurityQuestion2()) && customerQuestionAnswers.get(1).getAnswer().equals(customerSecurityQueAnsVO.getSecurityQuestionAnswer2())) {
			validate=true;
		}else {
			validate = false;
		}
         return validate;
	}

	@Override
	public List<SecurityQuestionsVO> findAll() {
		List<SecurityQuestions> securityQuestions =questionsRepository.findAll();//bringing securityQuestions from db so use entity i.e. SecurityQuestion
		List<SecurityQuestionsVO> questionsVOs =new ArrayList<SecurityQuestionsVO>();
		for(SecurityQuestions questions:securityQuestions) {
			SecurityQuestionsVO questionsVO = new SecurityQuestionsVO();
			BeanUtils.copyProperties(questions, questionsVO);
			questionsVOs.add(questionsVO);
		}
		return questionsVOs;
		/* converting securityQuestions into stream and use map as it every object of tt which is SecurityQuestions Type 
		 * Make object of VO and convert to VO [securityQuestionsVO.setQid(tt.getQid())] and return securityQuestionsVO
		 * collect(Collectors.tolist()) i.e returns list of securityQuestionsVO.
		 * return securityQuestions.Stream().map(tt->{ SecurityQuestionsVO questionsVO =
		 * new SecurityQuestionsVO(); BeanUtils.copyProperties(tt, questionsVO); return
		 * questionsVO; });
		 */
	}
	
	@Override
	//for save method, see the controller, what it is sending
	public void save(CustomerSecurityQueAnsVO customerSecurityQueAnsVO) {

		//Deleting all customer questions for existing user
				List<CustomerQuestionAnswer> customerQuestionAnswers=customerQuestionsAnsRepository.findQuestionAnswer(customerSecurityQueAnsVO.getLoginid());
				if(customerQuestionAnswers.size()>0)
				customerQuestionsAnsRepository.deleteAll(customerQuestionAnswers);

				CustomerQuestionAnswer customerQuestionAnswer1=new CustomerQuestionAnswer();
				Login login=loginRepository.findById(customerSecurityQueAnsVO.getLoginid()).get();

				//String quetionText=questionsRepository.findById(Integer.parseInt(customerSecurityQueAnsVO.getSecurityQuestion1())).get().getQuestions();
				//took existing securityQuestion from db
				//customerQuestionAnswer1.setQuestion(quetionText);
				String quetionText=customerSecurityQueAnsVO.getSecurityQuestion1();
				customerQuestionAnswer1.setQuestion(quetionText);
				customerQuestionAnswer1.setAnswer(customerSecurityQueAnsVO.getSecurityQuestionAnswer1());
				customerQuestionAnswer1.setDoe(new Timestamp(new Date().getTime()));
				customerQuestionAnswer1.setDom(new Timestamp(new Date().getTime()));
				customerQuestionAnswer1.setLogin(login);
				customerQuestionsAnsRepository.save(customerQuestionAnswer1);

				CustomerQuestionAnswer customerQuestionAnswer2=new CustomerQuestionAnswer();
				quetionText=questionsRepository.findById(Integer.parseInt(customerSecurityQueAnsVO.getSecurityQuestion2())).get().getQuestions();
				customerQuestionAnswer2.setQuestion(quetionText);
				customerQuestionAnswer2.setAnswer(customerSecurityQueAnsVO.getSecurityQuestionAnswer2());
				customerQuestionAnswer2.setDoe(new Timestamp(new Date().getTime()));
				customerQuestionAnswer2.setDom(new Timestamp(new Date().getTime()));
				customerQuestionAnswer2.setLogin(login);
				customerQuestionsAnsRepository.save(customerQuestionAnswer2);
				
				login.setLlt(new Timestamp(new Date().getTime()));
				loginRepository.save(login);

			}





	/*
	 * @Override public CustomerSecurityQueAnsVO showSecurityQuestions(String
	 * username) {//basically, make list of CustomerQuestionAnswer which contains
	 * securityQuestions required and passed it in CustomerSecurityQueAnsVO
	 * List<CustomerQuestionAnswer>
	 * customerQuestionAnswers=customerQuestionsAnsRepository.findQuestionAnswer(
	 * username); //this repo has the questions required and pojo class already had
	 * the question, so chosen //CustomerSecurityQueAnsVO customerSecurityQueAnsVOs
	 * = new ArrayList<CustomerSecurityQueAnsVO>(); //CustomerQuestionAnswer
	 * customerQuestionAnswer:customerQuestionAnswers) CustomerSecurityQueAnsVO
	 * acustomerSecurityQueAnsVOs = new CustomerSecurityQueAnsVO(); //since
	 * customerQuestionAnswers does have ques1, ques2, ans1, answ2 but needed only
	 * question and setting in CustomerQuestionAnswer
	 * acustomerSecurityQueAnsVOs.setSecurityQuestion1(customerQuestionAnswers.get(0
	 * ).getQuestion());
	 * acustomerSecurityQueAnsVOs.setSecurityQuestion2(customerQuestionAnswers.get(0
	 * ).getQuestion());
	 * 
	 * return acustomerSecurityQueAnsVOs ; }
	 */

	/*
	 * BeanUtils.copyProperties(customerQuestionAnswer, acustomerSecurityQueAnsVOs);
	 * customerSecurityQueAnsVOs.add(acustomerSecurityQueAnsVOs);
	 */


	
	

}
