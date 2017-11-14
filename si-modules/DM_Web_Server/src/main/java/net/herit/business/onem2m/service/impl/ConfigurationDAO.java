package net.herit.business.onem2m.service.impl;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import net.herit.business.onem2m.service.ConfigurationVO;
import net.herit.common.util.DateTimeUtil;

@Repository("ConfigurationDAO")
public class ConfigurationDAO {
	
	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	private static final String COLLECTION_NAME = "configuration";
	
	
	public ConfigurationVO getConfiguration(ConfigurationVO vo) {
		Query query = new Query(Criteria.where("CONFIGURATION_NAME").is(vo.getCONFIGURATION_NAME()));
		return mongoTemplate.findOne(query, ConfigurationVO.class, COLLECTION_NAME);
	}

	
	public ConfigurationVO updateConfiguration(ConfigurationVO vo) {
		Query query = new Query(Criteria.where("CONFIGURATION_NAME").is(vo.getCONFIGURATION_NAME()));
		
		Update update = new Update();
		update.set("maxPollingSessionNo", vo.getMaxPollingSessionNo());
		update.set("maxAENo", vo.getMaxAENo());
		update.set("maxCSENo", vo.getMaxCSENo());
		update.set("maxTps", vo.getMaxTps());
		update.set("updateTime", DateTimeUtil.getDateTimeByPattern("yyyy/MM/dd HH:mm:ss"));
		
		mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
		
		return vo;
	}
}