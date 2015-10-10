package com.suntomor.bank.frame.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IBaseDAO<T, PK extends Serializable> {

	T queryByParameterOne(Map<String,Object> parameter);
	
	List<T> queryByParameterList(Map<String,Object> parameter);
	
	int insertByEntity(T entity);
	
	int updateByEntity(T entity);
	
	int updateByParameter(Map<String, Object> parameter);
	
	int deleteByPrimaryKey(PK idPrimaryKey);
	
	int deleteByPrimaryKey(Map<String, Object> parameter);
	
	long countPageList(Map<String, Object> parameter);

	List<T> queryPageList(Map<String, Object> parameter);
	
}
