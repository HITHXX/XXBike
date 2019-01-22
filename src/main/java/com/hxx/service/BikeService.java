package com.hxx.service;

import java.util.List;

import org.springframework.data.geo.GeoResult;

import com.hxx.pojo.Bike;

public interface BikeService {

	public void save(Bike bike);

	public List<GeoResult<Bike>> findNear(double longitude, double latitude);

}
