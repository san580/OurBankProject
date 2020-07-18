package com.rab3tech.customer.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rab3tech.customer.dao.repository.CustomerAccountApprovedRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountInfoRepository;
import com.rab3tech.customer.dao.repository.CustomerRepository;
import com.rab3tech.customer.dao.repository.LoginRepository;
import com.rab3tech.customer.dao.repository.PayeeInfoRepository;
import com.rab3tech.customer.dao.repository.TransactionRepository;
import com.rab3tech.customer.service.TransactionService;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.CustomerSavingApproved;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.dao.entity.Transaction;
import com.rab3tech.vo.PayeeInfoVO;
import com.rab3tech.vo.TransactionVO;
@Service
public class TransactionServiceImpl implements TransactionService {
	@Autowired
	private CustomerAccountApprovedRepository  customerAccountApprovedRepository;
	@Autowired
	private LoginRepository  loginRepository;
	@Autowired
	private CustomerRepository  customerRepository;
	@Autowired
	private CustomerAccountInfoRepository  customerAccountInfoRepository;
	@Autowired
	private PayeeInfoRepository  payeeInfoRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	

	@Override
	public List<TransactionVO> findAllTransactions(String customerId) {
		List<TransactionVO> transactionVO = new ArrayList<TransactionVO>();
		Customer customer=customerRepository.findByEmail(customerId).get();
		Optional <CustomerAccountInfo> account =customerAccountInfoRepository.findByCustomerId(customer.getLogin());
		CustomerAccountInfo debitAccount =account.get();
		//List<Transaction> CustomerTransaction=transactionRepository.findAllTransactions(debitAccount.getAccountNumber());
		List<Transaction> CustomerTransaction =transactionRepository.findAllTransactions(debitAccount.getAccountNumber());
		for(Transaction transaction:CustomerTransaction) {
			TransactionVO transactionVO2 = new TransactionVO();
			if(transaction.getDebitAccountNumber().equalsIgnoreCase(debitAccount.getAccountNumber())){
				transactionVO2.setAccountTransactionType("Debit");
				
			}else {
				transactionVO2.setAccountTransactionType("Credit");//payeeAccount
				
			}
			transactionVO2.setAmount(transaction.getAmount());
			transactionVO2.setDescription(transaction.getDescription());
			transactionVO2.setTransactionDate(transaction.getTransactionDate());
	        PayeeInfo payeevo = new PayeeInfo();
	        //transactionVO2.setPayee(payeevo.getPayeeName());//should work perflectly fine, if it was String PayeeInfo in TransactionVO.
			BeanUtils.copyProperties(transaction.getPayeeId(), payeevo);
			transactionVO2.setPayee(payeevo);//check here??
	        transactionVO.add(transactionVO2);
		}
		return transactionVO;
	}
	@Override
	public String transferFund(TransactionVO transactionVO) {
	String message = null;
	//checking from account-find account number
		Customer customer=customerRepository.findByEmail(transactionVO.getCustomerId()).get();
		Optional <CustomerAccountInfo> account =customerAccountInfoRepository.findByCustomerId(customer.getLogin());
		CustomerAccountInfo debitAccount = new CustomerAccountInfo();
		if(account.isPresent()) {
			debitAccount = account.get();
			
			//trying to get status, active account from customer approved table
			message = validateAccount(transactionVO.getCustomerId());
			if(message != null) {
				return message;
			}
				//check if the debitAccount balance is sufficient
				if((debitAccount.getAvBalance()< transactionVO.getAmount())) {
					message="Debit balance is not sufficient.";
					return message;
				}
		}
				//checking payee's details
				PayeeInfo payeeInfo =payeeInfoRepository.findById(transactionVO.getPayeeId()).get();//first, id and got payee account number from id and check validations, if true, save
				Optional<CustomerAccountInfo> payeeAccount=customerAccountInfoRepository.findByAccountNumber(payeeInfo.getPayeeAccountNo());
				if(payeeAccount.isEmpty()) {
					message="Payee Account Number is invalid!!!!!!";
				}
				CustomerAccountInfo creditAccount = payeeAccount.get();
				//find active or not?????????????
				message = validateAccount(creditAccount.getCustomerId().getLoginid());
				if(message != null) {
					return message;
				}
				//update balance of customer account
				float availableAmount = debitAccount.getAvBalance() -transactionVO.getAmount();
				debitAccount.setAvBalance(availableAmount);
				debitAccount.setTavBalance(availableAmount);
				customerAccountInfoRepository.save(debitAccount);//it has already id present so save
				
				//updating credit or payee balance
				float availableAmountForPayee = creditAccount.getAvBalance()+transactionVO.getAmount();
				creditAccount.setAvBalance(availableAmountForPayee);
				creditAccount.setTavBalance(availableAmountForPayee);
				customerAccountInfoRepository.save(debitAccount);
				//doesn't show description in here?????????
				//updating the transaction table
				Transaction transaction = new Transaction();
				transaction.setAmount(transactionVO.getAmount());
				transaction.setDebitAccountNumber(debitAccount.getAccountNumber());
				transaction.setPayeeId(payeeInfo);
				transaction.setTransactionDate(new Date());
				transaction.setDescription(transactionVO.getDescription());
				transactionRepository.save(transaction);
		message="Fund transfer is successful!!!!!!!";
		return message;

	}
	
	private String validateAccount(String email) {
		String message = null;
		CustomerSavingApproved customerSavingApproved =customerAccountApprovedRepository.findByEmail(email).get();
		//checking account is approved
		if(!customerSavingApproved.getStatus().getCode().equalsIgnoreCase("AS06")) {
			message="You do not have an registered account!!!!";
			return message;
		}
		
		//checking account is active
		if(!customerSavingApproved.getAccType().getCode().equalsIgnoreCase("AC001")) {
			message="You do not have  SAVING account!!!!";
			return message;
		}
		return message;
		
	}


}
