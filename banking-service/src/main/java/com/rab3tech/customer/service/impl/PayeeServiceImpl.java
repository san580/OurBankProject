package com.rab3tech.customer.service.impl;

import java.beans.PropertyDescriptor;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.customer.dao.repository.CustomerAccountInfoRepository;
import com.rab3tech.customer.dao.repository.CustomerRepository;
import com.rab3tech.customer.dao.repository.LoginRepository;
import com.rab3tech.customer.dao.repository.PayeeInfoRepository;
import com.rab3tech.customer.service.PayeeService;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.vo.PayeeInfoVO;

@Transactional
@Service
public class PayeeServiceImpl implements PayeeService {
	@Autowired
	private PayeeInfoRepository payeeInfoRepository;
	@Autowired
	private CustomerAccountInfoRepository customerAccountInfoRepository;
	@Autowired
	private LoginRepository loginRepository;
	@Autowired
	private CustomerRepository customerRepository;
	
@Override	
public void deletePayee(int payeeId) {
	payeeInfoRepository.deleteById(payeeId);
}

	@Override
	public List<PayeeInfoVO> findAllPayee(String CustomerId) {
		List<PayeeInfo> payeeInfos = payeeInfoRepository.findAllByCustomerId(CustomerId);
		List<PayeeInfoVO> payeeInfoVO = new ArrayList<PayeeInfoVO>();
		for (PayeeInfo payeeInfo : payeeInfos) {
			PayeeInfoVO payee = new PayeeInfoVO();
			BeanUtils.copyProperties(payeeInfo, payee);
			payeeInfoVO.add(payee);
		}

		return payeeInfoVO;
	}

	@Override
	public String savePayee(PayeeInfoVO payeeInfoVO) {
		String message = null;
		/*
		 * Optional<Login> login
		 * =loginRepository.findByLoginid(payeeInfoVO.getCustomerId()); Optional
		 * <CustomerAccountInfo> accountInfo=
		 * customerAccountInfoRepository.findByCustomerId(login.get()); //trying to get
		 * customerId from accountinfo table, took login out and set it in account, it
		 * had private login customerId
		 */
		/*
		 * Customer
		 * customer=customerRepository.findByEmail(payeeInfoVO.getCustomerId()).get();//
		 * only had customerid=email rightnow, so got customer from that
		 * Optional<CustomerAccountInfo> accountInfo
		 * =customerAccountInfoRepository.findByCustomerId(customer.getLogin());
		 * //private Login login; //checking user's account if exists
		 * if(accountInfo.isEmpty()) { CustomerAccountInfo myaccount =
		 * accountInfo.get(); if(!myaccount.getAccountType().equals("AC001")) {
		 * message="No savings account!!!!!"; return message; }else {
		 * message="You donot have a valid savings account!!!"; return message; } }
		 * //nickname should be unique Optional<PayeeInfo>
		 * payee=payeeInfoRepository.findBeneficiaryBypayeeNickName(payeeInfoVO.
		 * getPayeeNickName()); if(payee.isPresent()) { message=
		 * "Beneficiary with the same nickname already exists!!!!!!!"; return message; }
		 * //check PAyee's account exist Optional <CustomerAccountInfo> payeeAccountInfo
		 * =customerAccountInfoRepository.findByAccountNumber(payeeInfoVO.
		 * getPayeeAccountNo()); if(payeeAccountInfo.isEmpty()) {
		 * message="Payee Account Number is invalid!!!!!!!!!!!!!!"; return message; }
		 * //checking if payee Account is not an active account CustomerAccountInfo
		 * pAccount =payeeAccountInfo.get();
		 * if(pAccount.getAccountType().getCode().equalsIgnoreCase("AC001")) {
		 * message="Payee Acount is not an active account!!!!!"; } //incase the account
		 * exists it should not belong to the given user
		 * if(pAccount.getCustomerId().getLoginid().equalsIgnoreCase(payeeInfoVO.
		 * getCustomerId())) { message="You cannot add your own account as beneficiary";
		 * return message; }
		 * 
		 * //if the same beneficiary is already added
		 * if(payeeInfoRepository.findBeneficiaryByAccountId(payeeInfoVO.getCustomerId()
		 * , payeeInfoVO.getPayeeAccountNo()).isPresent()) {
		 * message="Beneficiary with the same account number already exists!!!!!";
		 * return message; }
		 */
		// when all looks good
		PayeeInfo payeeInfo = new PayeeInfo();
		BeanUtils.copyProperties(payeeInfoVO, payeeInfo);
		payeeInfo.setDoe(new Timestamp(new Date().getTime()));
		payeeInfo.setDom(new Timestamp(new Date().getTime()));
		payeeInfo.setStatus("AS04");
		payeeInfoRepository.save(payeeInfo);
		message = "Payee info saved";
		return message;
	}

	@Override
	public PayeeInfoVO findPayee(int payeeId) {
		PayeeInfoVO payeeInfo = new PayeeInfoVO();
		PayeeInfo payee =payeeInfoRepository.findById(payeeId).get();
		BeanUtils.copyProperties(payee, payeeInfo);
		return  payeeInfo;
	}

	@Override
	public String editPayee(PayeeInfoVO payeeInfoVO) {
		String message=null;//put payee validation
		PayeeInfo payee =payeeInfoRepository.findById(payeeInfoVO.getId()).get();
		BeanUtils.copyProperties(payeeInfoVO, payee, ignoreNullData(payeeInfoVO));
		payee.setDom(new Timestamp(new Date().getTime()));
		payeeInfoRepository.save(payee);
		message ="Payee has been updated";
		return message;
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


}
