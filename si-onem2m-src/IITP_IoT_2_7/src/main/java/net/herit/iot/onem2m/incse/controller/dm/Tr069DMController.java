package net.herit.iot.onem2m.incse.controller.dm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;

import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.EXEC_RESULT;
import net.herit.iot.message.onem2m.format.Enums.EXEC_STATUS;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.controller.dm.Tr069DMAdapter.MOUri;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.manager.dao.ExecInstanceDAO;
import net.herit.iot.onem2m.resource.ActionStatus;
import net.herit.iot.onem2m.resource.AnyArgType;
import net.herit.iot.onem2m.resource.Battery;
import net.herit.iot.onem2m.resource.DeviceCapability;
import net.herit.iot.onem2m.resource.DeviceInfo;
import net.herit.iot.onem2m.resource.DownloadArgsType;
import net.herit.iot.onem2m.resource.EventLog;
import net.herit.iot.onem2m.resource.ExecInstance;
import net.herit.iot.onem2m.resource.ExecReqArgsListType;
import net.herit.iot.onem2m.resource.Firmware;
import net.herit.iot.onem2m.resource.Memory;
import net.herit.iot.onem2m.resource.MgmtCmd;
import net.herit.iot.onem2m.resource.Node;
import net.herit.iot.onem2m.resource.Reboot;
import net.herit.iot.onem2m.resource.Software;
import net.herit.iot.onem2m.resource.UploadArgsType;

public class Tr069DMController implements DMControllerInterface {

	@Override
	public Firmware getStatus(Firmware firmware) throws OneM2MException {
		// TODO Auto-generated method stub
		try {
			Tr069DMAdapter adaptor = new Tr069DMAdapter(CfgManager.getInstance().getTr69DMAddress(), CfgManager.getInstance().getTr69DMTimeout());
			
			String deviceId = firmware.getObjectIDs().get(0);
			String fwName = firmware.getURL();
			HashMap<String, Object> moMap;
			
			//moMap = adaptor.readFirmware(deviceId, fwName);
			moMap = adaptor.readFileInfo(deviceId, fwName, 1);	// 1 : firmware in ACS
			
			firmware.setVersion(moMap.get("version").toString());	// firmware version
			firmware.setFirmwareName(moMap.get("filename").toString());	// package name
			
			return firmware;
		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		}
		
		
	}
	
	@Override
	public Software getStatus(Software software) throws OneM2MException {
		//throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "getStatus Not implemented for mgmtObj:"+software.getMgmtDefinition());
		try {
			Tr069DMAdapter adaptor = new Tr069DMAdapter(CfgManager.getInstance().getTr69DMAddress(), CfgManager.getInstance().getTr69DMTimeout());
			
			String deviceId = software.getObjectIDs().get(0);
			String swName = software.getURL();
			HashMap<String, Object> moMap;
			
			//moMap = adaptor.readFirmware(deviceId, fwName);
			moMap = adaptor.readFileInfo(deviceId, swName, 3);	// 3 : software in ACS
			
			software.setVersion(moMap.get("version").toString());	// firmware version
			software.setSoftwareName(moMap.get("filename").toString());	// package name
			
			return software;
		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		}
	}

	@Override
	public Memory getStatus(Memory memory) throws OneM2MException {
		// TODO Auto-generated method stub
		Tr069DMAdapter adaptor = new Tr069DMAdapter(CfgManager.getInstance().getTr69DMAddress(), CfgManager.getInstance().getTr69DMTimeout());
		
		String deviceId = memory.getObjectIDs().get(0);
		List<String> moIds = new ArrayList<String>();
		moIds.add(MOUri.DVC_MEMORY_TOTAL);
		moIds.add(MOUri.DVC_MEMORY_FREE);
		
		try {
			HashMap<String, Object> moMap = adaptor.read(deviceId, moIds);
			memory.setMemTotal(Integer.parseInt( moMap.get(MOUri.DVC_MEMORY_TOTAL).toString() ));	
			memory.setMemAvailable(Integer.parseInt( moMap.get(MOUri.DVC_MEMORY_FREE).toString() ));
			
		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		}
		
		return memory;
	}

	@Override
	public Battery getStatus(Battery battery) throws OneM2MException {
		// TODO Auto-generated method stub
		Tr069DMAdapter adaptor = new Tr069DMAdapter(CfgManager.getInstance().getTr69DMAddress(), CfgManager.getInstance().getTr69DMTimeout());
		
		String deviceId = battery.getObjectIDs().get(0);
		List<String> moIds = new ArrayList<String>();
		moIds.add(MOUri.DVC_BATTERY_LEVEL);
		moIds.add(MOUri.DVC_BATTERY_STATUS);

		try {
			HashMap<String, Object> moMap = adaptor.read(deviceId, moIds);
			battery.setBatteryLevel(Long.parseLong(moMap.get(MOUri.DVC_BATTERY_LEVEL).toString()));	
			battery.setBatteryStatus(Integer.parseInt( moMap.get(MOUri.DVC_BATTERY_STATUS).toString() ));
			
		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		}
		
		return battery;
		
	}

	@Override
	public DeviceInfo getStatus(DeviceInfo devInfo) throws OneM2MException {
		// TODO Auto-generated method stub
		Tr069DMAdapter adaptor = new Tr069DMAdapter(CfgManager.getInstance().getTr69DMAddress(), CfgManager.getInstance().getTr69DMTimeout());
		
		String deviceId = devInfo.getObjectIDs().get(0);
		List<String> moIds = new ArrayList<String>();
		if (!devInfo.getDeviceLabel().equals("")) {
			moIds.add(MOUri.DVC_SERIAL);
		}
		if (!devInfo.getManufacturer().equals("")) {
			moIds.add(MOUri.DVC_MANUFACTURER);	
		}
		if (!devInfo.getModel().equals("")) {
			//moIds.add(MOUri.DVC_MODEL);
			moIds.add(MOUri.DVC_PRODUCTCLASS);	// updated in 2017-09-22
		}
		if (!devInfo.getFwVersion().equals("")) {
			moIds.add(MOUri.DVC_SW_VERSION);		
		}
		if (!devInfo.getSwVersion().equals("")) {
			moIds.add(MOUri.DVC_SW_VERSION);
		}
		if (!devInfo.getHwVersion().equals("")) {
			moIds.add(MOUri.DVC_HW_VERSION);
		}
		
		HashMap<String, Object> moMap;
		try {
			moMap = adaptor.read(deviceId, moIds);
		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		}
		if (!devInfo.getDeviceLabel().equals("")) {
			devInfo.setDeviceLabel(moMap.get(MOUri.DVC_SERIAL).toString());
		}
		if (!devInfo.getManufacturer().equals("")) {
			devInfo.setManufacturer(moMap.get(MOUri.DVC_MANUFACTURER).toString());	
		}
		if (!devInfo.getModel().equals("")) {
			devInfo.setModel(moMap.get(MOUri.DVC_PRODUCTCLASS).toString());	// updated in 2017-09-22
		}
		if (!devInfo.getFwVersion().equals("")) {
			devInfo.setFwVersion(moMap.get(MOUri.DVC_SW_VERSION).toString());		
		}
		if (!devInfo.getSwVersion().equals("")) {
			devInfo.setSwVersion(moMap.get(MOUri.DVC_SW_VERSION).toString());
		}
		if (!devInfo.getHwVersion().equals("")) {
			devInfo.setHwVersion(moMap.get(MOUri.DVC_HW_VERSION).toString());
		}
		
		return devInfo;
		
	}
	
	@Override
	public EventLog getStatus(EventLog evtLog) throws OneM2MException {
		// TODO Auto-generated method stub
		Tr069DMAdapter adaptor = new Tr069DMAdapter(CfgManager.getInstance().getTr69DMAddress(), CfgManager.getInstance().getTr69DMTimeout());
		
		String deviceId = evtLog.getObjectIDs().get(0);
		List<String> moIds = new ArrayList<String>();
		if (evtLog.getLogStatus() > 0) {
			moIds.add(MOUri.DVC_EVT_LOG_ENABLE);	// ram total
			moIds.add(MOUri.DVC_EVT_LOG_STATUS);
			moIds.add(MOUri.DVC_EVT_LOG_DATA);
			moIds.add(MOUri.DVC_EVT_LOG_TYPE_ID);
		
			HashMap<String, Object> moMap;
			try {
				moMap = adaptor.read(deviceId, moIds);
				
			} catch (HitDMException e) {
				
				throw convertHitDMExToOneM2MEx(e);
			}
		
			evtLog.setLogStatus(Integer.parseInt(moMap.get(MOUri.DVC_EVT_LOG_STATUS).toString()));	// ram total
		}
		
		return evtLog;
	}

	@Override
	public DeviceCapability getStatus(DeviceCapability mgmtObj)
			throws OneM2MException {
		// TODO Auto-generated method stub
		throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "getStatus Not implemented for mgmtObj:"+mgmtObj.getMgmtDefinition());
	}

	@Override
	public Reboot getStatus(Reboot mgmtObj) throws OneM2MException {
		// TODO Auto-generated method stub
		throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "getStatus Not implemented for mgmtObj:"+mgmtObj.getMgmtDefinition());
	}

	

	@Override
	public Firmware executeMgmtObj(Firmware mgmtObjInDB, Firmware firmware)
			throws OneM2MException {
		// TODO Auto-generated method stub
		try {
			if (firmware.isUpdate() != null && firmware.isUpdate()) {
				Tr069DMAdapter adaptor = new Tr069DMAdapter(CfgManager.getInstance().getTr69DMAddress(), CfgManager.getInstance().getTr69DMTimeout());
				
				String deviceId = mgmtObjInDB.getObjectIDs().get(0);
				String packageName = mgmtObjInDB.getFirmwareName();
				String version = mgmtObjInDB.getVersion();
				
				adaptor.download(deviceId, packageName);
				
				/* blocked in 2017-09-16
				if(version.compareTo(mgmtObjInDB.getVersion()) > 0) {
					adaptor.download(deviceId, packageName);
				}
				*/
				
			}
			
			return firmware;
	
		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		}
	}
	
	@Override
	public Software executeMgmtObj(Software mgmtObjInDB, Software software) 
			throws OneM2MException {
		
		try {
			if (software.isInstall() != null && software.isInstall()) {
				Tr069DMAdapter adaptor = new Tr069DMAdapter(CfgManager.getInstance().getTr69DMAddress(), CfgManager.getInstance().getTr69DMTimeout());
				
				String deviceId = mgmtObjInDB.getObjectIDs().get(0);
				String packageName = mgmtObjInDB.getSoftwareName();
				String version = mgmtObjInDB.getVersion();
				
				adaptor.download(deviceId, packageName);
				
				/* blocked in 2017-09-16
				if(version.compareTo(mgmtObjInDB.getVersion()) > 0) {
					adaptor.download(deviceId, packageName);
				}
				*/
				
			}
			
			if (software.isUninstall() != null && software.isUninstall()) {
				Tr069DMAdapter adaptor = new Tr069DMAdapter(CfgManager.getInstance().getTr69DMAddress(), CfgManager.getInstance().getTr69DMTimeout());
				
				String deviceId = mgmtObjInDB.getObjectIDs().get(0);
				String packageName = mgmtObjInDB.getSoftwareName();
				String version = mgmtObjInDB.getVersion();
				
				adaptor.download(deviceId, packageName);
				
			}
			
			return software;
	
		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		}
		
	}

	@Override
	public Memory executeMgmtObj(Memory mgmtObjInDB, Memory mgmtObj)
			throws OneM2MException {
		// TODO Auto-generated method stub
		throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "executeMgmtObj Not implemented for mgmtObj:"+mgmtObj.getMgmtDefinition());
	}

	@Override
	public Battery executeMgmtObj(Battery mgmtObjInDB, Battery mgmtObj)
			throws OneM2MException {
		// TODO Auto-generated method stub
		throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "executeMgmtObj Not implemented for mgmtObj:"+mgmtObj.getMgmtDefinition());
	}

	@Override
	public DeviceInfo executeMgmtObj(DeviceInfo mgmtObjInDB, DeviceInfo mgmtObj)
			throws OneM2MException {
		// TODO Auto-generated method stub
		throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "executeMgmtObj Not implemented for mgmtObj:"+mgmtObj.getMgmtDefinition());
	}

	@Override
	public DeviceCapability executeMgmtObj(DeviceCapability mgmtObjInDB,
			DeviceCapability mgmtObj) throws OneM2MException {
		// TODO Auto-generated method stub
		throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "executeMgmtObj Not implemented for mgmtObj:"+mgmtObj.getMgmtDefinition());
	}

	@Override
	public Reboot executeMgmtObj(Reboot mgmtObjInDB, Reboot reboot)
			throws OneM2MException {
		// TODO Auto-generated method stub
		try {
			Tr069DMAdapter adaptor = new Tr069DMAdapter(CfgManager.getInstance().getTr69DMAddress(), CfgManager.getInstance().getTr69DMTimeout());
			
			String deviceId = mgmtObjInDB.getObjectIDs().get(0);
			if (reboot.isReboot() != null && reboot.isReboot()) {
				adaptor.reboot(deviceId);
			} else if (reboot.isFactoryReset() != null && reboot.isFactoryReset()) {
				adaptor.factoryReset(deviceId);
			}		
			
			return reboot;
			
		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		}
	}

	@Override
	public EventLog executeMgmtObj(EventLog mgmtObjInDB, EventLog evtLog)
			throws OneM2MException {
		// TODO Auto-generated method stub
		Tr069DMAdapter adaptor = new Tr069DMAdapter(CfgManager.getInstance().getTr69DMAddress(), CfgManager.getInstance().getTr69DMTimeout());
		
		try {
			
			String deviceId = mgmtObjInDB.getObjectIDs().get(0);
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			
			paramMap.put(MOUri.DVC_EVT_LOG_TYPE_ID, mgmtObjInDB.getLogTypeId());
			paramMap.put(MOUri.DVC_EVT_LOG_DATA, mgmtObjInDB.getLogData());
			paramMap.put(MOUri.DVC_EVT_LOG_STATUS, mgmtObjInDB.getLogStatus());
			
			if (evtLog.isLogStart() != null && evtLog.isLogStart()) {
				//adaptor.startDebug(deviceId, "60", "0");
				//paramMap.put(MOUri.DVC_EVT_LOG_ENABLE, evtLog.isLogStart());
				paramMap.put(MOUri.DVC_EVT_LOG_ENABLE, true);
				adaptor.write(deviceId, paramMap);
			} else if (evtLog.isLogStop() != null && evtLog.isLogStop()) {
				//adaptor.stopDebug(deviceId);
				//paramMap.put(MOUri.DVC_EVT_LOG_ENABLE, evtLog.isLogStop());
				paramMap.put(MOUri.DVC_EVT_LOG_ENABLE, false);
				adaptor.write(deviceId, paramMap);
				
				List<String> moIds = new ArrayList<String>();
				
				moIds.add(MOUri.DVC_EVT_LOG_ENABLE);	// ram total
				moIds.add(MOUri.DVC_EVT_LOG_STATUS);
				moIds.add(MOUri.DVC_EVT_LOG_DATA);
				moIds.add(MOUri.DVC_EVT_LOG_TYPE_ID);
			
				HashMap<String, Object> moMap;
				try {
					moMap = adaptor.read(deviceId, moIds);
					evtLog.setLogTypeId( Integer.parseInt( moMap.get(MOUri.DVC_EVT_LOG_TYPE_ID).toString() ) ); 
					evtLog.setLogData(moMap.get(MOUri.DVC_EVT_LOG_DATA).toString());
					
					
				} catch (HitDMException e) {
					
					throw convertHitDMExToOneM2MEx(e);
				}
			
				evtLog.setLogStatus(Integer.parseInt(moMap.get(MOUri.DVC_EVT_LOG_STATUS).toString()));	// ram total
			
			}		
			return evtLog;

		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		}
		
	}

	@Override
	public ExecInstance executeResetCmd(MgmtCmd mgmtCmd, ExecInstance execInst,
			Node node, ExecInstanceDAO dao) throws OneM2MException {
		// TODO Auto-generated method stub
		Tr069DMAdapter adaptor = new Tr069DMAdapter(CfgManager.getInstance().getTr69DMAddress(), CfgManager.getInstance().getTr69DMTimeout());
		
		execInst.setExecStatus(EXEC_STATUS.STATUS_NON_CANCELLABLEL.Value());
		dao.update(execInst);
		
		try {
			Document result = adaptor.factoryReset(node.getNodeID());
		
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			execInst.setExecResult(null);
		} catch (HitDMException e) {
			//if (e.getResponseStatusCode() == RESPONSE_STATUS.DMSERVER_ERROR) {
			execInst.setExecResult(convertHitDMErrorToExecResult(e.getStatusCode()));
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			//}
		} catch (Exception e) {
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			execInst.setExecResult(EXEC_RESULT.INTERNAL_ERROR.Value());			
		}
		
		return execInst;
	}

	@Override
	public ExecInstance executeRebootCmd(MgmtCmd mgmtCmd,
			ExecInstance execInst, Node node, ExecInstanceDAO dao)
			throws OneM2MException {
		// TODO Auto-generated method stub
		Tr069DMAdapter adaptor = new Tr069DMAdapter(CfgManager.getInstance().getTr69DMAddress(), CfgManager.getInstance().getTr69DMTimeout());
		
		execInst.setExecStatus(EXEC_STATUS.STATUS_NON_CANCELLABLEL.Value());
		dao.update(execInst);
		
		try {
			Document result = adaptor.reboot(node.getNodeID());
		
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			execInst.setExecResult(null);
		} catch (HitDMException e) {
			execInst.setExecResult(convertHitDMErrorToExecResult(e.getStatusCode()));
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());

		} catch (Exception e) {
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			execInst.setExecResult(EXEC_RESULT.INTERNAL_ERROR.Value());
			
		}
		
		return execInst;

	}

	@Override
	public ExecInstance executeUploadCmd(MgmtCmd mgmtCmd,
			ExecInstance execInst, Node node, ExecInstanceDAO dao)
			throws OneM2MException {
		
		Tr069DMAdapter adaptor = new Tr069DMAdapter(CfgManager.getInstance().getTr69DMAddress(), CfgManager.getInstance().getTr69DMTimeout());
		
		execInst.setExecStatus(EXEC_STATUS.STATUS_NON_CANCELLABLEL.Value());
		dao.update(execInst);
		
		try {
			int validArgNo = 0;
			ExecReqArgsListType args = mgmtCmd.getExecReqArgs();
			if (args != null) {
				List<UploadArgsType> argList = args.getUpload();
				
				if (argList.size() > 0) {
					Iterator<UploadArgsType> it = argList.iterator();
					while (it.hasNext()) {
						UploadArgsType arg = it.next();
						List<AnyArgType> listArgs = arg.getAnyArg();
						
						if(listArgs.size() == 2) {
							String version = "", filePath = "";
							Iterator<AnyArgType> itt = listArgs.iterator();
							while(itt.hasNext()) {
								AnyArgType anyArg = itt.next();
								if(anyArg.getName().equals("ver")) {
									version = anyArg.getValue().toString();
								}
								if(anyArg.getName().equals("path")) {
									filePath = anyArg.getValue().toString();
								}
							}
							if(!version.equals("") && !filePath.equals("")) {
								adaptor.upload(node.getNodeID(), arg.getFileType(), arg.getURL(), filePath, version);
								validArgNo++;
							} else {
								execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
								execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
								break;
							}
							
						} else {
							execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
							execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
						}
						
					}
					execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
					execInst.setExecResult(null);
				}
			}
			if (validArgNo == 0) {
				execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
				execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			}
		} catch (HitDMException e) {
			execInst.setExecResult(convertHitDMErrorToExecResult(e.getStatusCode()));
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());

		} catch (Exception e) {
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			execInst.setExecResult(EXEC_RESULT.INTERNAL_ERROR.Value());
			
		}
		
		return execInst;
	}

	@Override
	public ExecInstance executeDownloadCmd(MgmtCmd mgmtCmd,
			ExecInstance execInst, Node node, ExecInstanceDAO dao)
			throws OneM2MException {
		// TODO Auto-generated method stub
		Tr069DMAdapter adaptor = new Tr069DMAdapter(CfgManager.getInstance().getTr69DMAddress(), CfgManager.getInstance().getTr69DMTimeout());
		
		execInst.setExecStatus(EXEC_STATUS.STATUS_NON_CANCELLABLEL.Value());
		dao.update(execInst);
		try {
			int validArgNo = 0;
			ExecReqArgsListType args = mgmtCmd.getExecReqArgs();
			if (args != null) {
				List<DownloadArgsType> argList = args.getDownload();
				//List<UploadArgsType> aaa = args.getUpload();
				if (argList.size() > 0) {
					Iterator<DownloadArgsType> it = argList.iterator();
					while (it.hasNext()) {
						DownloadArgsType arg = it.next();
						adaptor.download(node.getNodeID(), arg.getTargetFile() );
						validArgNo++;
					}
					execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
					execInst.setExecResult(null);
				}
			}
			if (validArgNo == 0) {
				execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
				execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			}
		} catch (HitDMException e) {
			execInst.setExecResult(convertHitDMErrorToExecResult(e.getStatusCode()));
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());

		} catch (Exception e) {
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			execInst.setExecResult(EXEC_RESULT.INTERNAL_ERROR.Value());
			
		}
		
		return execInst;
		
	}

	@Override
	public ExecInstance executeSoftwareInstallCmd(MgmtCmd mgmtCmd,
			ExecInstance execInst, Node node, ExecInstanceDAO dao)
			throws OneM2MException {
		// TODO Auto-generated method stub
		Tr069DMAdapter adaptor = new Tr069DMAdapter(CfgManager.getInstance().getTr69DMAddress(), CfgManager.getInstance().getTr69DMTimeout());
		
		execInst.setExecStatus(EXEC_STATUS.STATUS_NON_CANCELLABLEL.Value());
		dao.update(execInst);
	/*	
		try {
			int validArgNo = 0;
			ExecReqArgsListType args = mgmtCmd.getExecReqArgs();
			
			List<String> moIds = new ArrayList<String>();
			moIds.add(MOUri.DVC_SW_VERSION);
			HashMap<String, Object> moMap = adaptor.read(node.getNodeID(), moIds);
			String currSoftVersion = moMap.get(MOUri.DVC_SW_VERSION).toString();
			
			if (args != null) {
				List<DownloadArgsType> argList = args.getDownload();
				//List<UploadArgsType> aaa = args.getUpload();
				if (argList.size() > 0) {
					Iterator<DownloadArgsType> it = argList.iterator();
					while (it.hasNext()) {
						DownloadArgsType arg = it.next();
						
						List<AnyArgType> listArgs = arg.getAnyArg();
						String version = listArgs.get(0).toString();
						
						if(arg.getFileType().startsWith("3 Vendor Configuration File") && version.compareTo(currSoftVersion) > 0) {
							Document result = adaptor.download(node.getNodeID(), arg.getTargetFile() );
							validArgNo++;
						}
					}
					execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
					execInst.setExecResult(null);
				}
			}
			if (validArgNo == 0) {
				execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
				execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			}
		} catch (HitDMException e) {
			execInst.setExecResult(convertHitDMErrorToExecResult(e.getStatusCode()));
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());

		} catch (Exception e) {
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			execInst.setExecResult(EXEC_RESULT.INTERNAL_ERROR.Value());
			
		}	
		*/
		try {
			
			ExecReqArgsListType args = mgmtCmd.getExecReqArgs();
			
			if(args.getSoftwareInstall() == null || args.getSoftwareInstall().size() <= 0) {
				execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
				execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			} else {
				String fileName = "";
				
				if(args.getSoftwareInstall().get(0) != null && args.getSoftwareInstall().get(0).getURL() != null && !args.getSoftwareInstall().get(0).getURL().equals("")) {
					fileName = args.getSoftwareInstall().get(0).getURL();
				} else {
					execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
					execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
				}
						
				adaptor.download(node.getNodeID(), fileName);
				
				execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
				execInst.setExecResult(null);
			}
			
		} catch (HitDMException e) {
			execInst.setExecResult(convertHitDMErrorToExecResult(e.getStatusCode()));
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());

		} catch (Exception e) {
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			execInst.setExecResult(EXEC_RESULT.INTERNAL_ERROR.Value());
			
		}
		
		return execInst;
	}

	@Override
	public ExecInstance executeSoftwareUpdateCmd(MgmtCmd mgmtCmd,
			ExecInstance execInst, Node node, ExecInstanceDAO dao)
			throws OneM2MException {
		// TODO Auto-generated method stub
		Tr069DMAdapter adaptor = new Tr069DMAdapter(CfgManager.getInstance().getTr69DMAddress(), CfgManager.getInstance().getTr69DMTimeout());
		
		execInst.setExecStatus(EXEC_STATUS.STATUS_NON_CANCELLABLEL.Value());
		dao.update(execInst);
	/*	
		try {
			int validArgNo = 0;
			ExecReqArgsListType args = mgmtCmd.getExecReqArgs();
			
			List<String> moIds = new ArrayList<String>();
			moIds.add(MOUri.DVC_HW_VERSION);
			HashMap<String, Object> moMap = adaptor.read(node.getNodeID(), moIds);
			String currHardVersion = moMap.get(MOUri.DVC_HW_VERSION).toString();
			
			if (args != null) {
				List<DownloadArgsType> argList = args.getDownload();
				//List<UploadArgsType> aaa = args.getUpload();
				if (argList.size() > 0) {
					Iterator<DownloadArgsType> it = argList.iterator();
					while (it.hasNext()) {
						DownloadArgsType arg = it.next();
						
						List<AnyArgType> listArgs = arg.getAnyArg();
						String version = listArgs.get(0).toString();
						
						if(arg.getFileType().startsWith("1 Firmware Upgrade Image") && version.compareTo(currHardVersion) > 0) {
							Document result = adaptor.download(node.getNodeID(), arg.getTargetFile() );
							validArgNo++;
						}
					}
					execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
					execInst.setExecResult(null);
				}
			}
			if (validArgNo == 0) {
				execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
				execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			}
		} catch (HitDMException e) {
			execInst.setExecResult(convertHitDMErrorToExecResult(e.getStatusCode()));
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());

		} catch (Exception e) {
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			execInst.setExecResult(EXEC_RESULT.INTERNAL_ERROR.Value());
			
		}	
	*/
		try {
			
			ExecReqArgsListType args = mgmtCmd.getExecReqArgs();
			
			if(args.getSoftwareUpdate() == null || args.getSoftwareUpdate().size() <= 0) {
				execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
				execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			} else {
				String fileName = "", version = "";
				
				if(args.getSoftwareUpdate().get(0) != null && args.getSoftwareUpdate().get(0).getURL() != null && !args.getSoftwareUpdate().get(0).getURL().equals("")) {
					fileName = args.getSoftwareUpdate().get(0).getURL();
				} else {
					execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
					execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
				}
				
				if(args.getSoftwareUpdate().get(0) != null && args.getSoftwareUpdate().get(0).getVersion() != null && !args.getSoftwareUpdate().get(0).getVersion().equals("")) {
					version = args.getSoftwareUpdate().get(0).getVersion();
				} else {
					execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
					execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
				}
						
				adaptor.download(node.getNodeID(), fileName);
				
				execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
				execInst.setExecResult(null);
			}
			
		} catch (HitDMException e) {
			execInst.setExecResult(convertHitDMErrorToExecResult(e.getStatusCode()));
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());

		} catch (Exception e) {
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			execInst.setExecResult(EXEC_RESULT.INTERNAL_ERROR.Value());
			
		}
		
		return execInst;
	}

	@Override
	public ExecInstance executeSoftwareUninstallCmd(MgmtCmd mgmtCmd,
			ExecInstance execInst, Node node, ExecInstanceDAO dao)
			throws OneM2MException {
		// TODO Auto-generated method stub
		/*
		Tr069DMAdapter adaptor = new Tr069DMAdapter(CfgManager.getInstance().getTr69DMAddress(), CfgManager.getInstance().getTr69DMTimeout());
				
		execInst.setExecStatus(EXEC_STATUS.STATUS_NON_CANCELLABLEL.Value());
		dao.update(execInst);
		
		try {
			int validArgNo = 0;
			ExecReqArgsListType args = mgmtCmd.getExecReqArgs();
			
			List<String> moIds = new ArrayList<String>();
			moIds.add(MOUri.DVC_SW_VERSION);
			HashMap<String, Object> moMap = adaptor.read(node.getNodeID(), moIds);
			String currSoftVersion = moMap.get(MOUri.DVC_SW_VERSION).toString();
			
			if (args != null) {
				List<DownloadArgsType> argList = args.getDownload();
				//List<UploadArgsType> aaa = args.getUpload();
				if (argList.size() > 0) {
					Iterator<DownloadArgsType> it = argList.iterator();
					while (it.hasNext()) {
						DownloadArgsType arg = it.next();
						
						List<AnyArgType> listArgs = arg.getAnyArg();
						String version = listArgs.get(0).toString();
						
						if(arg.getFileType().startsWith("3 Vendor Configuration File") && version.compareTo(currSoftVersion) <= 0) {
							Document result = adaptor.download(node.getNodeID(), arg.getTargetFile() );
							validArgNo++;
						}
					}
					execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
					execInst.setExecResult(null);
				}
			}
			if (validArgNo == 0) {
				execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
				execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			}
		} catch (HitDMException e) {
			execInst.setExecResult(convertHitDMErrorToExecResult(e.getStatusCode()));
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());

		} catch (Exception e) {
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			execInst.setExecResult(EXEC_RESULT.INTERNAL_ERROR.Value());
			
		}	
		*/
		
		execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
		execInst.setExecResult(EXEC_RESULT.REQUEST_UNSUPPORTED.Value());
		
		return execInst;
	}
	
	// added in 2017-08-04 to send setParameterValues to ACS, when get notification from IN-CSE
	
	public Document setStatus(String deviceId, HashMap<String, Object> paramMap) {

		Tr069DMAdapter adaptor = new Tr069DMAdapter(CfgManager.getInstance().getTr69DMAddress(), CfgManager.getInstance().getTr69DMTimeout());
		
		Document doc = null;
		
		try {
			doc = adaptor.write(deviceId, paramMap);
		} catch (HitDMException e) {
			e.printStackTrace();
			return null;
		}
		
		return doc;
	}
	
	protected OneM2MException convertHitDMExToOneM2MEx(HitDMException e) {

		RESPONSE_STATUS result = null;
		switch (e.getStatusCode()) {
		case 200:
			result = RESPONSE_STATUS.OK;
			break;
			
		case 404:
			result = RESPONSE_STATUS.EXTERNAL_OBJECT_NOT_FOUND;
			break;
			
		case 830:
		case 831:				
			result = RESPONSE_STATUS.EXTERNAL_OBJECT_NOT_REACHABLE;
			break;

		case 721:
			result = RESPONSE_STATUS.INVALID_ARGUMENTS;				
			break;

		case 5002:
			result = RESPONSE_STATUS.NETWORK_FAILURE;				
			break;

//		case 405:
//		case 466:
//		case 670:
//		case 671:
//		case 672:
//		case 701:
//		case 752:
//		case 500:
//		case 5001:
//		case 5002:		
		default:
			result = RESPONSE_STATUS.INTERNAL_SERVER_ERROR;				
		}
		
		return new OneM2MException(result, e.getMessage());
		
	}
	
	protected int convertHitDMErrorToExecResult(int statusCode) {
		
		EXEC_RESULT result = null;
		switch (statusCode) {
		case 200:
			break;
			
		case 404:
			result = EXEC_RESULT.UNKNOWN_DEPLOYMENT_UNIT;
			break;
			
		case 830:
		case 831:				
			result = EXEC_RESULT.INVALID_DEPLOYMENT_UNIT_STATE;
			break;

		case 721:
			result = EXEC_RESULT.REQUEST_UNSUPPORTED;				
			break;
			
		case 701:	// package is not registereed
			result = EXEC_RESULT.INVALID_ARGUMENTS;
			break;

//		case 405:
//		case 466:
//		case 670:
//		case 671:
//		case 672:
//		case 752:
//		case 500:
//		case 5001:
//		case 5002:		
		default:
			result = EXEC_RESULT.INTERNAL_ERROR;				
		}
		
		return result == null ? null : result.Value();

	}

}
