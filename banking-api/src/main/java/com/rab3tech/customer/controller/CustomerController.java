package com.rab3tech.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rab3tech.customer.service.PayeeService;
import com.rab3tech.vo.ApplicationResponseVO;
import com.rab3tech.vo.PayeeInfoVO;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v3")
public class CustomerController {
	@Autowired
	private PayeeService payeeService;
	
	@GetMapping("/customer/deletePayee/{payeeId}")//url shold be unique, +payeeID is sent from jsp
	public ApplicationResponseVO deletePayee(@PathVariable int payeeId) {
		ApplicationResponseVO responseVO = new ApplicationResponseVO();
		payeeService.deletePayee(payeeId);
		responseVO.setCode(200);
		responseVO.setStatus("Success");
		responseVO.setMessage("Payee has been deleted");
		return responseVO;
		
	}
	//v3/customer/findPayee/
	@GetMapping("/customer/findPayee/{payeeId}")//url shold be unique, +payeeID is sent from jsp
	public PayeeInfoVO findPayee(@PathVariable int payeeId) {
		PayeeInfoVO responseVO = new PayeeInfoVO();
		responseVO = payeeService.findPayee(payeeId);
		return responseVO;
		
	}
	//above two are, calling method in api,and saving values in html
	
	@PostMapping("/customer/editPayee")//url shold be unique, +payeeID is sent from jsp
	public ApplicationResponseVO findPayee(@RequestBody PayeeInfoVO payeeInfoVo) {
		System.out.println("payeeInfoVo------"+payeeInfoVo);
		ApplicationResponseVO responseVO = new ApplicationResponseVO();
		String message=	payeeService.editPayee(payeeInfoVo);
		responseVO.setCode(200);
		responseVO.setStatus("Success");
		responseVO.setMessage(message);
		return responseVO;
	}
	
	
	

}
