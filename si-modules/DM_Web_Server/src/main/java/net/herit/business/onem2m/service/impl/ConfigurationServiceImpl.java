package net.herit.business.onem2m.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.herit.business.onem2m.service.ConfigurationService;
import net.herit.business.onem2m.service.ConfigurationVO;

@Service("ConfigurationService")
public class ConfigurationServiceImpl implements ConfigurationService {

	@Resource(name = "ConfigurationDAO")
	private ConfigurationDAO configurationDAO;

	
	@Override
	public ConfigurationVO getConfiguration(ConfigurationVO vo) {
		return (ConfigurationVO) configurationDAO.getConfiguration(vo);
	}
	
	@Override
	public ConfigurationVO updateConfiguration(ConfigurationVO vo) {
		return (ConfigurationVO) configurationDAO.updateConfiguration(vo);
	}
}
