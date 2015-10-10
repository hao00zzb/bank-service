package com.suntomor.bank.frame.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class BaseDAOImpl<T, PK extends Serializable> implements IBaseDAO<T, PK> {
	
	@Autowired
	@Qualifier("sqlSessionTemplate")
	protected SqlSessionTemplate sqlSession;

	protected Class<T> entityClass = null;
	protected String entityMapper = null;

	public BaseDAOImpl() {
	}
	
	public BaseDAOImpl(Class<T> clazz) {
		this.entityClass = clazz;
		entityMapper = clazz.getName()+"Mapper.";
	}

	@Override
	public long countPageList(Map<String, Object> parameter) {
		Object obj = this.sqlSession.selectOne(entityMapper+"countPageList", parameter);
		return obj==null?0L:Long.parseLong(obj.toString());
	}

	@Override
	public List<T> queryPageList(Map<String, Object> parameter) {
		return this.sqlSession.selectList(entityMapper+"queryPageList", parameter);
	}

	@Override
	public T queryByParameterOne(Map<String, Object> parameter) {
		return this.sqlSession.selectOne(entityMapper+"queryByParameter", parameter);
	}

	@Override
	public List<T> queryByParameterList(Map<String, Object> parameter) {
		return this.sqlSession.selectList(entityMapper+"queryByParameter", parameter);
	}

	@Override
	public int insertByEntity(T entity) {
		return this.sqlSession.insert(entityMapper+"insertByEntity", entity);
	}

	@Override
	public int updateByEntity(T entity) {
		return this.sqlSession.update(entityMapper+"updateByEntity", entity);
	}

	@Override
	public int updateByParameter(Map<String, Object> parameter) {
		return this.sqlSession.update(entityMapper+"updateByParameter", parameter);
	}
	
	@Override
	public int deleteByPrimaryKey(PK idPrimaryKey) {
		return this.sqlSession.delete(entityMapper+"deleteByPrimaryKey", idPrimaryKey);
	}

	@Override
	public int deleteByPrimaryKey(Map<String, Object> parameter) {
		return this.sqlSession.update(entityMapper+"deleteByPrimaryKey", parameter);
	}

}
