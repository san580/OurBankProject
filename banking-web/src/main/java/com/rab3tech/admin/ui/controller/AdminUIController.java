package com.rab3tech.admin.ui.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rab3tech.admin.service.CustomerSecurityQuestionsService;
import com.rab3tech.vo.SecurityQuestionsVO;

@Controller
@RequestMapping("/admin")
//http:localhost:4055
//httt://www.kuebikobank.com/admin/security/questions
public class AdminUIController {

	@Autowired
	private CustomerSecurityQuestionsService securityQuestionsService;

	@GetMapping("/security/questions")
	public String showSecurityQuestions(Model model) {
		List<SecurityQuestionsVO> questionsVOs=securityQuestionsService.findSecurityQuestions();
		model.addAttribute("questionsVOs",questionsVOs);
		return "admin/securityQuestions";
	}



}