package com.hxx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hxx.pojo.Bike;

/**
 * 标记这个类是一个用于介绍请求和相应用户的一个控制器
 *加上@Controller注解后，Spring容器就会对其实例化 
 * @author Administrator
 *
 */

@Controller
public class BikeController {

	@RequestMapping("/addBike")
	@ResponseBody
	public String addBike(@RequestBody Bike bike) {
		System.out.println(bike);
		return "Hello " ;
	}
	
	
}
