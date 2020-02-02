package com.chon.chart.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chon.chart.user.dao.UserDAO;
import com.chon.chart.user.vo.User;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserDAO userDAO;

	@Override
	public List<User> findAll() {
		return userDAO.findAll();
	}

	@Override
	public User register(User user) {
		user.setUserPassword(user.getUserPassword());
		userDAO.saveAndFlush(user);
		return user;
	}

	@Override
	public User findByUserEmail(String userEmail) {
		return userDAO.findByUserEmail(userEmail);
	}
}
