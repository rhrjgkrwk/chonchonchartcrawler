package com.chon.chart.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.chon.chart.user.vo.User;

public interface UserDAO extends JpaRepository<User, Integer> {
	User findByUserEmail(String userEmail);
}
