package com.rab3tech.customer.service.impl;

import java.beans.PropertyDescriptor;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.admin.dao.repository.AccountStatusRepository;
import com.rab3tech.admin.dao.repository.MagicCustomerRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountApprovedRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountEnquiryRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountInfoRepository;
import com.rab3tech.customer.dao.repository.CustomerQuestionsAnsRepository;
import com.rab3tech.customer.dao.repository.PayeeInfoRepository;
import com.rab3tech.customer.dao.repository.RoleRepository;
import com.rab3tech.customer.dao.repository.SecurityQuestionsRepository;
import com.rab3tech.customer.service.CustomerService;
import com.rab3tech.dao.entity.AccountStatus;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.CustomerQuestionAnswer;
import com.rab3tech.dao.entity.CustomerSaving;
import com.rab3tech.dao.entity.CustomerSavingApproved;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.dao.entity.Role;
import com.rab3tech.dao.entity.SecurityQuestions;
import com.rab3tech.utils.AccountStatusEnum;
import com.rab3tech.utils.PasswordGenerator;
import com.rab3tech.utils.Utils;
import com.rab3tech.vo.CustomerAccountInfoVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.PayeeInfoVO;

@Service
@Transactional
public class CustomerServiceImpl implements  CustomerService{
	@Autowired
	private PayeeInfoRepository  payeeInfoRepository ;
	
	@Autowired
	private MagicCustomerRepository customerRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private SecurityQuestionService securityQuestionService;
	
	@Autowired
	private CustomerQuestionsAnsRepository customerQuestionsAnsRepository;
	//Goes to Customer_as_table and save the questions/answer given
	@Autowired
	private SecurityQuestionsRepository questionsRepository;
	@Autowired
	private CustomerAccountApprovedRepository customerAccountApprovedRepository;

	@Autowired
	private CustomerAccountInfoRepository customerAccountInfoRepository;
	@Autowired
	private CustomerAccountEnquiryRepository customerAccountEnquiryRepository;
	@Autowired
	private AccountStatusRepository accountStatusRepository;
		
	
	
	
	@Override
	public CustomerAccountInfoVO createBankAccount(int csaid) {
		//logic 
		String customerAccount=Utils.generateCustomerAccount();
		CustomerSaving customerSaving=customerAccountEnquiryRepository.findById(csaid).get();

		CustomerAccountInfo customerAccountInfo=new CustomerAccountInfo();
		customerAccountInfo.setAccountNumber(customerAccount);
		customerAccountInfo.setAccountType(customerSaving.getAccType());
		customerAccountInfo.setAvBalance(1000.0F);
		customerAccountInfo.setBranch(customerSaving.getLocation());
		customerAccountInfo.setCurrency("$");
		Customer customer=customerRepository.findByEmail(customerSaving.getEmail()).get();
		customerAccountInfo.setCustomerId(customer.getLogin());
		customerAccountInfo.setStatusAsOf(new Date());
		customerAccountInfo.setTavBalance(1000.0F);
		CustomerAccountInfo customerAccountInfo2=customerAccountInfoRepository.save(customerAccountInfo);
		CustomerSavingApproved customerSavingApproved=new CustomerSavingApproved();
		BeanUtils.copyProperties(customerSaving, customerSavingApproved);
		customerSavingApproved.setAccType(customerSaving.getAccType());
		customerSavingApproved.setStatus(customerSaving.getStatus());
		//saving entity into customer_saving_enquiry_approved_tbl
		customerAccountApprovedRepository.save(customerSavingApproved);


		//delete data from 
		customerAccountEnquiryRepository.delete(customerSaving);


		CustomerAccountInfoVO accountInfoVO=new CustomerAccountInfoVO();
		BeanUtils.copyProperties(customerAccountInfo2, accountInfoVO);
		return accountInfoVO;

	}
	
	
	
	@Override
	public CustomerVO myAccount(String username) {
		Customer customer=customerRepository.findByEmail(username).get();//got username via session and got whole details from db
		CustomerVO customerVO = new CustomerVO();
		BeanUtils.copyProperties(customer, customerVO);
		List<CustomerQuestionAnswer> answers=customerQuestionsAnsRepository.findQuestionAnswer(username);//questions from db , make vo and set questions in there.
		customerVO.setQuestion1(answers.get(0).getQuestion());//listma bhakolai customerVO ma set garne
		customerVO.setQuestion2(answers.get(1).getQuestion());
		customerVO.setAnswer1(answers.get(0).getAnswer());
		customerVO.setAnswer2(answers.get(1).getAnswer());
		List<SecurityQuestions> securityQuestions =questionsRepository.findAll();
		Set<SecurityQuestions> set =  new HashSet<SecurityQuestions>();
		set.addAll(securityQuestions);
		customerVO.setSecurityQuestions(securityQuestions);
		return customerVO;
		//all the user details and security questions are in customerVO now
	}
	@Override
	public void updateMyAccount(CustomerVO customerVO) {//here customerVO has all the values displayed at html
		Customer customer = customerRepository.findByEmail(customerVO.getEmail()).get();//took existing customer from db
		BeanUtils.copyProperties(customerVO, customer, ignoreNullData(customerVO));
		customerRepository.save(customer);
		//customer info has been saved 
		List<CustomerQuestionAnswer> customerQuestionAnswers = customerQuestionsAnsRepository.findQuestionAnswer(customerVO.getEmail());//if dbko answer is equal is equal to VO ko ans
		boolean con1 = customerQuestionAnswers.get(0).getAnswer().equals(customerVO.getAnswer1()) && customerQuestionAnswers.get(1).getAnswer().equals(customerVO.getAnswer2());
		boolean con2 = Integer.toString(customerQuestionAnswers.get(0).getId()).equals(customerVO.getQuestion1()) && Integer.toString(customerQuestionAnswers.get(1).getId()).equals(customerVO.getQuestion2());
		
		if(!(con1 && con2)) {
			CustomerQuestionAnswer entity1 = new CustomerQuestionAnswer();
			entity1=customerQuestionAnswers.get(0);
			String question = questionsRepository.findById(Integer.parseInt(customerVO.getQuestion1())).get().getQuestions();
			entity1.setQuestion(question);
			entity1.setAnswer(customerVO.getAnswer1());
			entity1.setDom(new Timestamp(new Date().getTime()));
			customerQuestionsAnsRepository.save(entity1);
			
			CustomerQuestionAnswer entity2 = new CustomerQuestionAnswer();
			entity2=customerQuestionAnswers.get(1);
			question = questionsRepository.findById(Integer.parseInt(customerVO.getQuestion2())).get().getQuestions();
			entity2.setQuestion(question);
			entity2.setAnswer(customerVO.getAnswer2());
			entity2.setDom(new Timestamp(new Date().getTime()));
			customerQuestionsAnsRepository.save(entity2);
			
			
			/*
			 * CustomerSecurityQueAnsVO customerSecurityQueAnsVO = new
			 * CustomerSecurityQueAnsVO();
			 * customerSecurityQueAnsVO.setLoginid(customerVO.getEmail());
			 * customerSecurityQueAnsVO.setSecurityQuestion1(customerVO.getQuestion1());
			 * customerSecurityQueAnsVO.setSecurityQuestion2(customerVO.getQuestion2());
			 * customerSecurityQueAnsVO.setSecurityQuestionAnswer1(customerVO.getAnswer1());
			 * customerSecurityQueAnsVO.setSecurityQuestionAnswer2(customerVO.getAnswer2());
			 * securityQuestionService.save(customerSecurityQueAnsVO);
			 */
			 
		}
			
		}
	
	public String[] ignoreNullData(Object source) {
		BeanWrapper wrapper = new BeanWrapperImpl(source);
		PropertyDescriptor[] pDesc =  wrapper.getPropertyDescriptors();
		Set<String> nullValue = new HashSet<String>();
		for (PropertyDescriptor pd :pDesc) {
			Object obj = wrapper.getPropertyValue(pd.getName());
			if(obj == null)
				nullValue.add(pd.getName());
		}
		String[] ingnoredData = new String[nullValue.size()];
		return nullValue.toArray(ingnoredData);
	}

		
	
	
	
	@Override
	public CustomerVO createAccount(CustomerVO customerVO) {
		Customer pcustomer = new Customer();
		BeanUtils.copyProperties(customerVO, pcustomer);
		Login login = new Login();
		login.setNoOfAttempt(3);
		login.setLoginid(customerVO.getEmail());
		login.setName(customerVO.getName());
		String genPassword=PasswordGenerator.generateRandomPassword(8);
		customerVO.setPassword(genPassword);
		login.setPassword(bCryptPasswordEncoder.encode(genPassword));
		login.setToken(customerVO.getToken());
		login.setLocked("no");
		
		Role entity=roleRepository.findById(3).get();
		Set<Role> roles=new HashSet<>();
		roles.add(entity);
		//setting roles inside login
		login.setRoles(roles);
		//setting login inside
		pcustomer.setLogin(login);
		Customer dcustomer=customerRepository.save(pcustomer);
		customerVO.setId(dcustomer.getId());
		customerVO.setUserid(customerVO.getUserid());
		Optional<CustomerSaving> optional=customerAccountEnquiryRepository.findByEmail(dcustomer.getEmail());
		if(optional.isPresent()) {
			CustomerSaving customerSaving=optional.get();
			AccountStatus accountStatus=accountStatusRepository.findByCode(AccountStatusEnum.REGISTERED.getCode()).get();
			customerSaving.setStatus(accountStatus);
		}
		
		
		return customerVO;
	}

}
