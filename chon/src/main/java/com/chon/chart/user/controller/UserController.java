package com.chon.chart.user.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chon.chart.user.service.UserService;
import com.chon.chart.user.vo.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/user")
public class UserController {
	@Autowired
	UserService userService;

	@GetMapping("/login")
	public String login() {
		return "hello user!";
	}
	
	@GetMapping("/duplicationCheck")
	public Boolean duplicationCheck(String userEmail) {
		log.info("userEmail : " + userEmail);
		User user = userService.findByUserEmail(userEmail);
		if (user == null) {
			return false;
		}
		return true;
	}

	@GetMapping("/userList")
	public List<User> userList() {
		return userService.findAll();
	}

	@PutMapping("/register")
	public void register(HttpServletResponse res, String userEmail, String userPassword, String userName)
			throws IOException {
		User user = new User();
		user.setUserEmail(userEmail);
		user.setUserPassword(userPassword);
		user.setUserName(userName);
		
		userService.register(user);
		log.info(user.toString());
	}

}