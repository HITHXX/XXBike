package com.hxx.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


//Bike类与MongoDB中的bikes collections关联
@Document(collection="bikes")
public class Bike {
	//主键(唯一、建立索引)、id对应mongodb中的_id
	@Id
	private String id;
	
	//private double longitude;
	
	//private double latitude;
	
	//表示经纬度的数组[经度、纬度]
	@GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
	private double[] location;
	
	private int status;
	
	//建立索引
	@Indexed
	private Long bikeNo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

/*	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}*/

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public Long getBikeNo() {
		return bikeNo;
	}

	public void setBikeNo(Long bikeNum) {
		this.bikeNo = bikeNum;
	}

	public double[] getLocation() {
		return location;
	}

	public void setLocation(double[] location) {
		this.location = location;
	}
	
	
	
}
