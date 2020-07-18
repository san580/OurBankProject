

package com.rab3tech.admin.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.admin.dao.repository.CustomerSecurityQuestionsRepository;
import com.rab3tech.admin.service.CustomerSecurityQuestionsService;
import com.rab3tech.dao.entity.SecurityQuestions;
import com.rab3tech.vo.SecurityQuestionsVO;

@Service
@Transactional
public class CustomerSecurityQuestionsServiceImpl  implements CustomerSecurityQuestionsService{

	@Autowired
	private CustomerSecurityQuestionsRepository customerSecurityQuestionsDao;


	@Override
	public List<SecurityQuestionsVO> findSecurityQuestions() {
		List<SecurityQuestions> securityQuestions=customerSecurityQuestionsDao.findAll();
		List<SecurityQuestionsVO>  questionsVOs=new ArrayList<>();
		for(SecurityQuestions entity:securityQuestions) {
			SecurityQuestionsVO questionsVO=new SecurityQuestionsVO();
			BeanUtils.copyProperties(entity, questionsVO);
			questionsVOs.add(questionsVO);
		}
		return questionsVOs;
	}

}