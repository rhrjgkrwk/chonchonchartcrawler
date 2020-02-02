package com.chon.chart.user.vo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;

	private String userEmail;
	private String userName;
	private String userPassword;
	private String userRegdate;
	
	public User () {
		
	}
	
	public User(int userId, String userEmail, String userName, String userPassword, String userRegdate) {
		super();
		this.userId = userId;
		this.userEmail = userEmail;
		this.userName = userName;
		this.userPassword = userPassword;
		this.userRegdate = userRegdate;
	}
	
	

}