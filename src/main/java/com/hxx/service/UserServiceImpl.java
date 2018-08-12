package com.hxx.service;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public boolean sendMsg(String countryCode, String phoneNum) {
		//调用腾讯云短信API
		
		//生成短信验证码 随机4位数字
		
		
		//向对应手机号的用户发送短信
		
		//将发送的手机号作为key，验证码作为value保存到redis中
		
		return false;
	}

}
