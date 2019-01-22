package com.hxx.service;

import com.hxx.pojo.User;

public interface UserService {

	public boolean sendMsg(String countryCode, String phoneNum);

	public boolean verify(String phoneNum, String verifyCode);

	public void register(User user);

	public void update(User user);

}
