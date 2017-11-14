package net.herit.business.onem2m.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import net.herit.business.onem2m.service.PlatformVO;
import net.herit.common.util.DateTimeUtil;

@Repository("PlatformDAO")
public class PlatformDAO {
	
	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	private static final String COLLECTION_NAME = "platform";

	
	public PlatformVO insert(PlatformVO vo) {
		vo.setCreateTime(DateTimeUtil.getDateTimeByPattern("yyyy/MM/dd HH:mm:ss"));
		vo.setUpdateTime(DateTimeUtil.getDateTimeByPattern("yyyy/MM/dd HH:mm:ss"));
		
		mongoTemplate.insert(vo, COLLECTION_NAME);
		return vo;
	}
	
	public PlatformVO getPlatform(PlatformVO vo) {
		return mongoTemplate.findById(vo.getId(), PlatformVO.class, COLLECTION_NAME);
	}
	
	public PlatformVO getPlatformBySpId(PlatformVO vo) {
		Query query = new Query(Criteria.where("spId").is(vo.getSpId()));
		return mongoTemplate.findOne(query, PlatformVO.class, COLLECTION_NAME);
	}

	public List<PlatformVO> listPlatform() {
		return (List<PlatformVO>) mongoTemplate.findAll(PlatformVO.class, COLLECTION_NAME);
	}

	public PlatformVO updatePlatform(PlatformVO vo) {
	    Query query = new Query(new Criteria("id").is(vo.getId()));
		
		Update update = new Update();
		update.set("spId", vo.getSpId());
		update.set("serverName", vo.getServerName());
		update.set("serverHost", vo.getServerHost());
		update.set("serverPort", vo.getServerPort());
		update.set("protocol", vo.getProtocol());
		update.set("cseId", vo.getCseId());
		update.set("cseName", vo.getCseName());
		update.set("maxTps", vo.getMaxTps());
		update.set("updateTime", DateTimeUtil.getDateTimeByPattern("yyyy/MM/dd HH:mm:ss"));
		
		mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
		
		return vo;
	}
	
	public void deletePlatform(PlatformVO vo) {
		Query query;
		String[] checkList = vo.getCheckList();
		
    	for (int i = 0 ; i < checkList.length ; i++) {
    		query = new Query(new Criteria("id").is(checkList[i]));
    	    mongoTemplate.remove(query, COLLECTION_NAME);
    	}
	}
	
}