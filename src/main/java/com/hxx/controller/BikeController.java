package com.hxx.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hxx.pojo.Bike;
import com.hxx.service.BikeService;

/**
 * 标记这个类是一个用于介绍请求和相应用户的一个控制器
 *加上@Controller注解后，Spring容器就会对其实例化 
 * @author Administrator
 *
 */

@Controller
public class BikeController {
	
	//到spring容器中查找BikeService类型的实例，然后注入到BikeController实例中
	@Autowired
	private BikeService bikeService;

	@RequestMapping("/bike/add")
	@ResponseBody
	public String addBike(@RequestBody Bike bike) {
		//System.out.println(bike);
		//调用service层，将数据存入Mongodb中
		bikeService.save(bike);
		return "success" ;
	}
	
	@RequestMapping("/bike/findNear")
	@ResponseBody
	public List<GeoResult<Bike>> findNear(double longitude,double latitude) {
		//调用service,将数据返回
		List<GeoResult<Bike>> bikes = bikeService.findNear(longitude,latitude);
		return bikes;
		
	}
	
	
}
