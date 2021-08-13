package com.example.demoProject.orm;

import javax.persistence.Query;

import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class JpaQueryProviderImpl<Transaction> extends AbstractJpaQueryProvider {

	 private Class<Transaction> entityClass;
	 
	  private String query;
	@Override
	public Query createQuery() {
		
		return getEntityManager().createNamedQuery(query, entityClass);
	
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.isTrue(StringUtils.hasText(query), "Query cannot be empty");
	    Assert.notNull(entityClass, "Entity class cannot be NULL");
	  
		
	}
	 public void setQuery(String query) {
		    this.query = query;
		  }

}
