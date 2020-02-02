package com.chon.chart.user.service;

import java.util.List;

import com.chon.chart.user.vo.User;

public interface UserService {
	public List<User> findAll();
	public User register(User user);
	public User findByUserEmail(String userEmail);
}
