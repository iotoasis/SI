package net.herit.business.onem2m.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.herit.business.onem2m.service.PlatformService;
import net.herit.business.onem2m.service.PlatformVO;

@Service("PlatformService")
public class PlatformServiceImpl implements PlatformService {

	@Resource(name = "PlatformDAO")
	private PlatformDAO platformDAO;

	@Override
	public PlatformVO insert(PlatformVO vo) {
		return platformDAO.insert(vo);
	}

	@Override
	public PlatformVO getPlatform(PlatformVO vo) {
		return (PlatformVO) platformDAO.getPlatform(vo);
	}
	
	@Override
	public PlatformVO getPlatformBySpId(PlatformVO vo) {
		return (PlatformVO) platformDAO.getPlatformBySpId(vo);
	}

	@Override
	public List<PlatformVO> listPlatform() {
		return (List<PlatformVO>) platformDAO.listPlatform();
	}
	
	@Override
	public PlatformVO updatePlatform(PlatformVO vo) {
		return (PlatformVO) platformDAO.updatePlatform(vo);
	}
	
	public void deletePlatform(PlatformVO vo) {
		platformDAO.deletePlatform(vo);
	}
	
}
