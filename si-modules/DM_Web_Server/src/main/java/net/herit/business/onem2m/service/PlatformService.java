package net.herit.business.onem2m.service;

import java.util.List;

public interface PlatformService {

	public PlatformVO insert(PlatformVO vo);
	
	public PlatformVO getPlatform(PlatformVO vo);
	
	public PlatformVO getPlatformBySpId(PlatformVO vo);
	
	public List<PlatformVO> listPlatform();
	
	public PlatformVO updatePlatform(PlatformVO vo);
	
	public void deletePlatform(PlatformVO vo);
	
}
