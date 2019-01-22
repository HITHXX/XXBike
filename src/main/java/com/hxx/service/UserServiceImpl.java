package com.hxx.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.hxx.pojo.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
 	public boolean sendMsg(String countryCode, String phoneNum) {
		//调用阿里云短信API
		boolean flag = false;
		
		//生成短信验证码 随机4位数字
		String code = (int)((Math.random()*9+1)*1000)+"";
		
		
		//设置超时时间-可自行调整
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		//初始化ascClient需要的几个参数
		final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
		final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
		//替换成你的AK
		final String accessKeyId = stringRedisTemplate.opsForValue().get("accessKeyId");//你的accessKeyId,参考本文档步骤2
		final String accessKeySecret = stringRedisTemplate.opsForValue().get("accessKeySecret");//你的accessKeySecret，参考本文档步骤2
		//初始化ascClient,暂时不支持多region（请勿修改）
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
		accessKeySecret);
		try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IAcsClient acsClient = new DefaultAcsClient(profile);
		 //组装请求对象
		 SendSmsRequest request = new SendSmsRequest();
		 //使用post提交
		 request.setMethod(MethodType.POST);
		 //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
		 request.setPhoneNumbers(phoneNum);
		 //必填:短信签名-可在短信控制台中找到
		 request.setSignName("胡锡鑫");
		 //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
		 request.setTemplateCode("SMS_142150936");
		 //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		 //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		 request.setTemplateParam("{\"name\":\"Tom\", \"code\":\""+code+"\"}");
		 //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
		 //request.setSmsUpExtendCode("90997");
		 //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		 //request.setOutId("yourOutId");
		//请求失败这里会抛ClientException异常
		SendSmsResponse sendSmsResponse;
		try {
			sendSmsResponse = acsClient.getAcsResponse(request);
			if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
				//请求成功
				flag=true;
				stringRedisTemplate.opsForValue().set(phoneNum, code,180,TimeUnit.SECONDS);
				}
			
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
		//向对应手机号的用户发送短信
		
		//将发送的手机号作为key，验证码作为value保存到redis中
		
		return flag;
	}

	@Override
	public boolean verify(String phoneNum, String verifyCode) {
		boolean flag = false;
		
		//调用RedisTemplate，查找手机对应的验证码
		String code = stringRedisTemplate.opsForValue().get(phoneNum);
		
		//将用户传入验证码与手机验证码进行对比
		if(code!=null && code.equals(verifyCode)) {
			flag=true;
		}
		return flag;
	}

	@Override
	public void register(User user) {
		
		//调用mongodb的dao，将用户保存起来；
		mongoTemplate.insert(user);
		
	}

	@Override
	public void update(User user) {
		//若对应_id的数据不存在，则插入，存在则更新
		//mongoTemplate.insert(user);
		//更新用户对应的属性
		
		Update update = new Update();
		
		if(user.getDeposit()!=null) {
			update.set("deposit", user.getDeposit());
		}
		if(user.getStatus()!=null) {
			update.set("status", user.getStatus());
		}
		if(user.getName()!=null) {
			update.set("name", user.getName());
		}
		if(user.getIdNum()!=null) {
			update.set("idNum", user.getIdNum());
		}
		
		mongoTemplate.updateFirst(new Query(Criteria.where("phoneNum").is(user.getPhoneNum()))
				,update
				,User.class);
	}
	



}
