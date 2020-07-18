package com.rab3tech.customer.service;

import java.util.Optional;

import com.rab3tech.vo.ChangePasswordVO;
import com.rab3tech.vo.LoginVO;

public interface LoginService {

	Optional<LoginVO> authUser(LoginVO loginVO);

	Optional<LoginVO> findUserByUsername(String loginid);

	void changePassword(ChangePasswordVO changePasswordVO);

	boolean checkPassword(String username, String password);

}
