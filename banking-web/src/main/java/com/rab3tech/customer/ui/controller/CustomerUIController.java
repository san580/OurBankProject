package com.rab3tech.customer.ui.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rab3tech.customer.service.CustomerService;
import com.rab3tech.customer.service.LoginService;
import com.rab3tech.customer.service.PayeeService;
import com.rab3tech.customer.service.TransactionService;
import com.rab3tech.customer.service.impl.CustomerEnquiryService;
import com.rab3tech.customer.service.impl.SecurityQuestionService;
import com.rab3tech.dao.entity.Transaction;
import com.rab3tech.email.service.EmailService;
import com.rab3tech.vo.ChangePasswordVO;
import com.rab3tech.vo.CustomerSavingVO;
import com.rab3tech.vo.CustomerSecurityQueAnsVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.EmailVO;
import com.rab3tech.vo.LoginVO;
import com.rab3tech.vo.PayeeInfoVO;
import com.rab3tech.vo.SecurityQuestionsVO;
import com.rab3tech.vo.TransactionVO;

/**
 * 
 * @author nagendra This class for customer GUI
 *
 */
@Controller
public class CustomerUIController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerUIController.class);

	@Autowired
	private CustomerEnquiryService customerEnquiryService;

	@Autowired
	private SecurityQuestionService securityQuestionService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private EmailService emailService;
	@Autowired
	private LoginService loginService;
	@Autowired
	private PayeeService payeeService;
	@Autowired
	private TransactionService transactionService;
	@GetMapping("/customer/accountStatement")
	public String findingAccountStatement(Model model,HttpSession session) {
		LoginVO loginVO = (LoginVO) session.getAttribute("userSessionVO");
		if(loginVO!=null) {
			String customerId= loginVO.getUsername();//get customerId
			List<TransactionVO> transactionVO=transactionService.findAllTransactions(customerId);
			model.addAttribute("transactionVO",transactionVO); 
			return "customer/accountSummary1";	
		}else {
	    	 model.addAttribute("message ", "Please login before proceed!"); 
	    	 return "customer/login";
	     }
	}
	
	@GetMapping("/customer/loadingfundTransfer")
	public String loadingFundTransfer(Model model,HttpSession session) {
		LoginVO loginVO = (LoginVO) session.getAttribute("userSessionVO");
		if(loginVO!=null) {
			String customerId= loginVO.getUsername();
			List<PayeeInfoVO> infoVOs = payeeService.findAllPayee(customerId);
			model.addAttribute("payeeInfoVO",infoVOs); 
			return "customer/fundTransfer";	
		}else {
	    	 model.addAttribute("message ", "Please login before proceed!"); 
	    	 return "customer/login";
	     }
	}
	
	@PostMapping("/customer/transferfund")
	public String transferFund(@ModelAttribute TransactionVO transactionVO, Model model, HttpSession session) {
		LoginVO loginVO = (LoginVO) session.getAttribute("userSessionVO");// has loginid in here
		if(loginVO!=null) {
			transactionVO.setCustomerId(loginVO.getUsername());
			String message=transactionService.transferFund(transactionVO);
		model.addAttribute("message",message);
			 return	"customer/fundTransfer";
		}else {
	    	 model.addAttribute("message ", "Please login before proceed!"); 
	    	 return "customer/login";
	     } 
	}	
	
	@GetMapping("/customer/showBeneficiary")
	public String showBeneficiaryInfo(Model model,HttpSession session) {
		LoginVO loginVO = (LoginVO) session.getAttribute("userSessionVO");// has loginid in here
		if(loginVO!=null) {
			String customerId =loginVO.getUsername();
			List<PayeeInfoVO> infoVOs = payeeService.findAllPayee(customerId);
			model.addAttribute("payeeInfoVO",infoVOs); 
			return "customer/beneficiaryList";	
			
		}else {
	    	 model.addAttribute("message ", "Please login before proceed!"); 
	    	 return "customer/login";
	     }
	}
	
	@GetMapping("/customer/addPayee")
	public String addBeneficiary(Model model,HttpSession session) {
		LoginVO loginVO = (LoginVO) session.getAttribute("userSessionVO");// has loginid in here
		if(loginVO!=null) {
			return "customer/addBeneficiary";	
		}else {
	    	 model.addAttribute("message ", "Please login before proceed!"); 
	    	 return "customer/login";
	     }
	}
	
	
	@PostMapping("/customer/addBeneficiary")
	public String saveBeneficiary(@ModelAttribute PayeeInfoVO payeeInfoVO, Model model, HttpSession session) {
		LoginVO loginVO = (LoginVO) session.getAttribute("userSessionVO");// has loginid in here
		if(loginVO!=null) {
			payeeInfoVO.setCustomerId(loginVO.getUsername());
			String message=	payeeService.savePayee(payeeInfoVO);
			model.addAttribute("message",message);
			 return	"customer/addBeneficiary";
		}else {
	    	 model.addAttribute("message ", "Please login before proceed!"); 
	    	 return "customer/login";
	     } 
	}	
	
	
	
	@GetMapping("/customer/updateProfile")
	public String myAccount(HttpSession session, Model model) {
		LoginVO loginVO2 = (LoginVO) session.getAttribute("userSessionVO");// has loginid in here
     if(loginVO2!=null) {
    	 CustomerVO customerVO =customerService.myAccount(loginVO2.getUsername());//get username from session first and set in customerVO as it is the return value
    	 model.addAttribute("customerVO", customerVO);
    	 List<SecurityQuestionsVO> questionsVOs =securityQuestionService.findAll();
    	 model.addAttribute("questionsVOs", questionsVOs);

     }else {
    	 model.addAttribute("message ", "Sorry your credentials doesnot match!"); 
    	 return "customer/login";
     }
		return "customer/updateProfile"; 
	}
	
	@PostMapping("/customer/updateAccount")
	public String updateAccount(@ModelAttribute CustomerVO customerVO, Model model,HttpSession session) {
		LoginVO loginVO2 = (LoginVO) session.getAttribute("userSessionVO");//has loginid through session, call method
		if(loginVO2!=null) {
		customerService.updateMyAccount(customerVO);
		model.addAttribute("message","You account has been updated!!!!");
		 List<SecurityQuestionsVO> questionsVOs =securityQuestionService.findAll();
    	 model.addAttribute("questionsVOs", questionsVOs);
		}else {
			model.addAttribute("message","Sorry your credentials doesnot match!!!!");
			 return "customer/login";
		}
		
	 return	"customer/updateProfile";
		
	}
	
	@GetMapping("customer/forgetPassword")
	public String showForgetPassword(Model model) {
		return "customer/forgetPassword"; 
	}

	@PostMapping("/customer/newChangePassword")
	public String savePassword(@ModelAttribute ChangePasswordVO changePasswordVO, Model model) {
		Optional<LoginVO> loginVO=loginService.findUserByUsername(changePasswordVO.getLoginid());
if(loginVO.isPresent()) {
		if (changePasswordVO.getNewPassword().equals(changePasswordVO.getConfirmPassword())) {
			String viewName = "customer/dashboard";
			loginService.changePassword(changePasswordVO);
		} else {
			model.addAttribute("error", "Sorry , your new password and confirm password are not valid!");
			return "customer/forgetPassword/"; // login.html
		}
}
return "redirect:/customer/forgetPassword";
	
	}
	
	
	
	@PostMapping("/customer/validateQuestions")
	public String  validateQuestions(@ModelAttribute CustomerSecurityQueAnsVO customerSecurityQueAnsVO, Model model,HttpSession session) {
		Optional<LoginVO> loginVO=loginService.findUserByUsername(customerSecurityQueAnsVO.getLoginid());
		if(loginVO.isPresent()) {
		boolean validate = securityQuestionService.validateQuestions(customerSecurityQueAnsVO);
			if (validate) {
				model.addAttribute("loginid",loginVO.get().getUsername());
				return"customer/chagePassword";
			
			} else {
				model.addAttribute("error", "Sorry , your new password and confirm password are not valid!");
				return "customer/newChangePassword"; // login.html
			}
		}
		return "redirect:/customer/login";
   
	
	}

	@PostMapping("/customer/showQuestion")
	public String showSecurityQuestions(@RequestParam String username, Model model) {// model, as want to show security
																						// questions in next page , its
																						// a view to show
		CustomerSecurityQueAnsVO ansVO = securityQuestionService.showSecurityQuestions(username);
		ansVO.setLoginid(username);
		// do i need to make another pojo class in here ?? and create object?
		model.addAttribute("Questions", ansVO);
		return "customer/showQuestionAnswerView"; // login.html
	}

	@PostMapping("/customer/chagePassword")
	public String saveCustomerQuestions(@ModelAttribute ChangePasswordVO changePasswordVO, Model model,
			HttpSession session) {
		LoginVO loginVO2 = (LoginVO) session.getAttribute("userSessionVO");// has loginid in here
		String loginid = loginVO2.getUsername();// Explain??????//Email received from view is get and set inside
												// customerSecurityQueAnsVO
		changePasswordVO.setLoginid(loginid);
		String viewName = "customer/dashboard";
		boolean status = loginService.checkPassword(loginid, changePasswordVO.getCurrentPassword());
		if (changePasswordVO.getNewPassword().equals(changePasswordVO.getConfirmPassword())) {
			viewName = "customer/dashboard";
			loginService.changePassword(changePasswordVO);
		} else {
			model.addAttribute("error", "Sorry , your new password and confirm password are not valid!");
			return "customer/login"; // login.html
		}

		return viewName;
	}

	@PostMapping("/customer/securityQuestion")
	public String saveCustomerQuestions(
			@ModelAttribute("customerSecurityQueAnsVO") CustomerSecurityQueAnsVO customerSecurityQueAnsVO, Model model,
			HttpSession session) {
		LoginVO loginVO2 = (LoginVO) session.getAttribute("userSessionVO");// has loginid in here
		String loginid = loginVO2.getUsername();// Explain??????//Email received from view is get and set inside
												// customerSecurityQueAnsVO
		customerSecurityQueAnsVO.setLoginid(loginid);

		securityQuestionService.save(customerSecurityQueAnsVO);
		//
		return "customer/chagePassword";
	}

	// http://localhost:444/customer/account/registration?cuid=1585a34b5277-dab2-475a-b7b4-042e032e8121603186515
	@GetMapping("/customer/account/registration")
	public String showCustomerRegistrationPage(@RequestParam String cuid, Model model) {

		logger.debug("cuid = " + cuid);
		Optional<CustomerSavingVO> optional = customerEnquiryService.findCustomerEnquiryByUuid(cuid);
		CustomerVO customerVO = new CustomerVO();

		if (!optional.isPresent()) {
			return "customer/error";
		} else {
			// model is used to carry data from controller to the view =- JSP/
			CustomerSavingVO customerSavingVO = optional.get();
			customerVO.setEmail(customerSavingVO.getEmail());
			customerVO.setName(customerSavingVO.getName());
			customerVO.setMobile(customerSavingVO.getMobile());
			customerVO.setAddress(customerSavingVO.getLocation());
			customerVO.setToken(cuid);
			logger.debug(customerSavingVO.toString());
			// model - is hash map which is used to carry data from controller to thyme
			// leaf!!!!!
			// model is similar to request scope in jsp and servlet
			model.addAttribute("customerVO", customerVO);
			return "customer/customerRegistration"; // thyme leaf
		}
	}

	@PostMapping("/customer/account/registration")
	public String createCustomer(@ModelAttribute CustomerVO customerVO, Model model) {
		// saving customer into database
		logger.debug(customerVO.toString());
		customerVO = customerService.createAccount(customerVO);
		// Write code to send email

		EmailVO mail = new EmailVO(customerVO.getEmail(), "javahunk2020@gmail.com",
				"Regarding Customer " + customerVO.getName() + "  userid and password", "", customerVO.getName());
		mail.setUsername(customerVO.getUserid());
		mail.setPassword(customerVO.getPassword());
		emailService.sendUsernamePasswordEmail(mail);
		System.out.println(customerVO);
		model.addAttribute("loginVO", new LoginVO());
		model.addAttribute("message", "Your account has been setup successfully , please check your email.");
		return "customer/login";
	}

	@GetMapping(value = { "/customer/account/enquiry", "/", "/mocha", "/welcome" })
	public String showCustomerEnquiryPage(Model model) {
		CustomerSavingVO customerSavingVO = new CustomerSavingVO();
		// model is map which is used to carry object from controller to view
		model.addAttribute("customerSavingVO", customerSavingVO);
		return "customer/customerEnquiry"; // customerEnquiry.html
	}

	@PostMapping("/customer/account/enquiry")
	public String submitEnquiryData(@ModelAttribute CustomerSavingVO customerSavingVO, Model model) {
		boolean status = customerEnquiryService.emailNotExist(customerSavingVO.getEmail());
		logger.info("Executing submitEnquiryData");
		if (status) {
			CustomerSavingVO response = customerEnquiryService.save(customerSavingVO);
			logger.debug("Hey Customer , your enquiry form has been submitted successfully!!! and appref "
					+ response.getAppref());
			model.addAttribute("message",
					"Hey Customer , your enquiry form has been submitted successfully!!! and appref "
							+ response.getAppref());
		} else {
			model.addAttribute("message", "Sorry , this email is already in use " + customerSavingVO.getEmail());
		}
		return "customer/success"; // customerEnquiry.html

	}
	
	@PostMapping("/customer/editPayeeInfo")
	public String editBeneficiary(@ModelAttribute PayeeInfoVO payeeInfoVO, Model model, HttpSession session) {
		LoginVO loginVO = (LoginVO) session.getAttribute("userSessionVO");// has loginid in here
		if(loginVO!=null) {
			payeeInfoVO.setCustomerId(loginVO.getUsername());
			String message=	payeeService.editPayee(payeeInfoVO);
			model.addAttribute("message",message);
			 return	"redirect:showBeneficiary";
		}else {
	    	 model.addAttribute("message ", "Please login before proceed!"); 
	    	 return "customer/login";
	     } 
	}	
}
