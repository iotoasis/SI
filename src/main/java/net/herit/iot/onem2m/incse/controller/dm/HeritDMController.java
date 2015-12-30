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
import net.herit.iot.onem2m.incse.controller.dm.HeritDMAdaptor.MOUri;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.manager.dao.ExecInstanceDAO;
import net.herit.iot.onem2m.resource.ActionStatus;
import net.herit.iot.onem2m.resource.Battery;
import net.herit.iot.onem2m.resource.DeviceCapability;
import net.herit.iot.onem2m.resource.DeviceInfo;
import net.herit.iot.onem2m.resource.EventLog;
import net.herit.iot.onem2m.resource.ExecInstance;
import net.herit.iot.onem2m.resource.ExecReqArgsListType;
import net.herit.iot.onem2m.resource.Firmware;
import net.herit.iot.onem2m.resource.Memory;
import net.herit.iot.onem2m.resource.MgmtCmd;
import net.herit.iot.onem2m.resource.Node;
import net.herit.iot.onem2m.resource.Reboot;
import net.herit.iot.onem2m.resource.SoftwareUpdateArgsType;

public class HeritDMController implements DMControllerInterface {
	

	@Override
	public ExecInstance executeResetCmd(MgmtCmd mgmtCmd, ExecInstance execInst, Node node, ExecInstanceDAO dao) throws OneM2MException {

		HeritDMAdaptor adaptor = new HeritDMAdaptor(CfgManager.getInstance().getHitDMAddress());
		
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
	public ExecInstance executeRebootCmd(MgmtCmd mgmtCmd, ExecInstance execInst, Node node, ExecInstanceDAO dao) throws OneM2MException {

		HeritDMAdaptor adaptor = new HeritDMAdaptor(CfgManager.getInstance().getHitDMAddress());
		
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
	public ExecInstance executeUploadCmd(MgmtCmd mgmtCmd, ExecInstance execInst, Node node, ExecInstanceDAO dao) throws OneM2MException {

		execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
		execInst.setExecResult(EXEC_RESULT.REQUEST_UNSUPPORTED.Value());	
		
		return execInst;
	}

	@Override
	public ExecInstance executeDownloadCmd(MgmtCmd mgmtCmd, ExecInstance execInst, Node node, ExecInstanceDAO dao) throws OneM2MException {

		execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
		execInst.setExecResult(EXEC_RESULT.REQUEST_UNSUPPORTED.Value());	
		
		return execInst;
	}

	@Override
	public ExecInstance executeSoftwareInstallCmd(MgmtCmd mgmtCmd, ExecInstance execInst, Node node, ExecInstanceDAO dao) throws OneM2MException {

		execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
		execInst.setExecResult(EXEC_RESULT.REQUEST_UNSUPPORTED.Value());	
		
		return execInst;
	}

	@Override
	public ExecInstance executeSoftwareUpdateCmd(MgmtCmd mgmtCmd, ExecInstance execInst, Node node, ExecInstanceDAO dao) throws OneM2MException {
		HeritDMAdaptor adaptor = new HeritDMAdaptor(CfgManager.getInstance().getHitDMAddress());
		
		execInst.setExecStatus(EXEC_STATUS.STATUS_NON_CANCELLABLEL.Value());
		dao.update(execInst);
		
		try {
			
			int validArgNo = 0;
			ExecReqArgsListType args = mgmtCmd.getExecReqArgs();
			if (args != null) {
				List<SoftwareUpdateArgsType> argList = args.getSoftwareUpdate();
				if (argList.size() > 0) {
					Iterator<SoftwareUpdateArgsType> it = argList.iterator();
					while (it.hasNext()) {
						SoftwareUpdateArgsType arg = it.next();
						Document result = adaptor.firmwareUpgrade(node.getNodeID(), arg.getUUID(), arg.getVersion());
						validArgNo ++;
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
	public ExecInstance executeSoftwareUninstallCmd(MgmtCmd mgmtCmd, ExecInstance execInst, Node node, ExecInstanceDAO dao) throws OneM2MException {

		execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
		execInst.setExecResult(EXEC_RESULT.REQUEST_UNSUPPORTED.Value());	
		
		return execInst;
	}

	
	
	@Override
	public Battery getStatus(Battery battery) throws OneM2MException {
		
		HeritDMAdaptor adaptor = new HeritDMAdaptor(CfgManager.getInstance().getHitDMAddress());
		
		String deviceId = battery.getObjectIDs().get(0);
		List<String> moIds = new ArrayList<String>();
		moIds.add(MOUri.DVC_BATTERY_LEVEL);

		try {
			HashMap<String, String> moMap = adaptor.read(deviceId, moIds);
			battery.setBatteryLevel(Long.parseLong(moMap.get(MOUri.DVC_BATTERY_LEVEL)));	
			battery.setBatteryStatus(7);	// 7: UNKNOWN
			
		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		}
		
		return battery;
	}

	@Override
	public DeviceInfo getStatus(DeviceInfo devInfo) throws OneM2MException {
		
		HeritDMAdaptor adaptor = new HeritDMAdaptor(CfgManager.getInstance().getHitDMAddress());
		
		String deviceId = devInfo.getObjectIDs().get(0);
		List<String> moIds = new ArrayList<String>();
		if (!devInfo.getDeviceLabel().equals("")) {
			moIds.add(MOUri.DVC_SERIAL_NUMBER);
		}
		if (!devInfo.getManufacturer().equals("")) {
			moIds.add(MOUri.DVC_MANUFACTURER);	
		}
		if (!devInfo.getModel().equals("")) {
			moIds.add(MOUri.DVC_MODEL_NUMBER);
		}
		if (!devInfo.getFwVersion().equals("")) {
			moIds.add(MOUri.FW_VERSION);		
		}
		if (!devInfo.getSwVersion().equals("")) {
			moIds.add(MOUri.DVC_OS_VERSION);
		}
		
		HashMap<String, String> moMap;
		try {
			moMap = adaptor.read(deviceId, moIds);
		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		}
		if (!devInfo.getDeviceLabel().equals("")) {
			devInfo.setDeviceLabel(moMap.get(MOUri.DVC_SERIAL_NUMBER));
		}
		if (!devInfo.getManufacturer().equals("")) {
			devInfo.setManufacturer(moMap.get(MOUri.DVC_MANUFACTURER));	
		}
		if (!devInfo.getModel().equals("")) {
			devInfo.setModel(moMap.get(MOUri.DVC_MODEL_NUMBER));	
		}
		if (!devInfo.getFwVersion().equals("")) {
			devInfo.setFwVersion(moMap.get(MOUri.FW_VERSION));		
		}
		if (!devInfo.getSwVersion().equals("")) {
			devInfo.setSwVersion(moMap.get(MOUri.DVC_OS_VERSION));
		}
		
		return devInfo;
	}

	@Override
	public EventLog getStatus(EventLog evtLog) throws OneM2MException {
		
		HeritDMAdaptor adaptor = new HeritDMAdaptor(CfgManager.getInstance().getHitDMAddress());
		
		String deviceId = evtLog.getObjectIDs().get(0);
		List<String> moIds = new ArrayList<String>();
		if (evtLog.getLogStatus() > 0) {
			moIds.add(MOUri.DIAG_DEBUG_STATUS);	// ram total
		
			HashMap<String, String> moMap;
			try {
				moMap = adaptor.read(deviceId, moIds);
			} catch (HitDMException e) {
				
				throw convertHitDMExToOneM2MEx(e);
			}
		
			evtLog.setLogStatus(Integer.parseInt(moMap.get(MOUri.DIAG_DEBUG_STATUS)) == 1 ? 1 : 2);	// ram total
		}
		
		return evtLog;
	}

	@Override
	public Firmware getStatus(Firmware firmware) throws OneM2MException {

		try {
			HeritDMAdaptor adaptor = new HeritDMAdaptor(CfgManager.getInstance().getHitDMAddress());
			
			String deviceId = firmware.getObjectIDs().get(0);
			List<String> moIds = new ArrayList<String>();
			moIds.add(MOUri.FW_VERSION);	// firmware version
			moIds.add(MOUri.FW_PACKAGE_NAME);	// package name
			moIds.add(MOUri.FW_STATE);	// state
			moIds.add(MOUri.FW_UPDATE_RESULT);	// update result
			
			HashMap<String, String> moMap;
			
			moMap = adaptor.read(deviceId, moIds);
			firmware.setVersion(moMap.get(MOUri.FW_VERSION));	// firmware version
			firmware.setFirmwareName(moMap.get(MOUri.FW_PACKAGE_NAME));	// package name
			String state = moMap.get(MOUri.FW_STATE);	// state
			String updateResult = moMap.get(MOUri.FW_UPDATE_RESULT);	// update result
			
			ActionStatus as = new ActionStatus();
			as.setAction(MOUri.FW_UPDATE);
			if (updateResult == "0") {	// default
				as.setStatus(3);	// in process
			} else if (updateResult.equals("10")) {
				as.setStatus(2);	// successful
			} else /*if (updateResult.equals("1") || updateResult.equals("2") || 
						updateResult.equals("3") || updateResult.equals("4") || 
						updateResult.equals("5") || updateResult.equals("6") || 
						updateResult.equals("11"))*/ {
				as.setStatus(2);	// failure
			}
		
			firmware.setUpdateStatus(new ActionStatus());
			
			return firmware;
		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		}
	}

	@Override
	public Memory getStatus(Memory memory) throws OneM2MException {

		try {
			HeritDMAdaptor adaptor = new HeritDMAdaptor(CfgManager.getInstance().getHitDMAddress());
			
			String deviceId = memory.getObjectIDs().get(0);
			List<String> moIds = new ArrayList<String>();
			moIds.add(MOUri.DVC_RAM_TOTAL);	// ram total
			moIds.add(MOUri.DVC_RAM_FREE);	// ram free
			
			HashMap<String, String> moMap;
			
			moMap = adaptor.read(deviceId, moIds);
			memory.setMemAvailable(Integer.parseInt(moMap.get(MOUri.DVC_RAM_FREE)));	
			memory.setMemTotal(Integer.parseInt(moMap.get(MOUri.DVC_RAM_TOTAL)));
			
			return memory;
		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		}
	}


	@Override
	public EventLog executeMgmtObj(EventLog mgmtObjInDB, EventLog evtLog) throws OneM2MException {
		
		HeritDMAdaptor adaptor = new HeritDMAdaptor(CfgManager.getInstance().getHitDMAddress());

		try {
			
			String deviceId = mgmtObjInDB.getObjectIDs().get(0);
			if (evtLog.isLogStart() != null && evtLog.isLogStart()) {
				adaptor.startDebug(deviceId, "60", "0");
			} else if (evtLog.isLogStop() != null && evtLog.isLogStop()) {
				adaptor.stopDebug(deviceId);
			}		
			return evtLog;

		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		}
	}
	
	@Override
	public Firmware executeMgmtObj(Firmware fwInDB, Firmware firmware) throws OneM2MException {

		try {
			if (fwInDB.isUpdate()) {
				HeritDMAdaptor adaptor = new HeritDMAdaptor(CfgManager.getInstance().getHitDMAddress());
				
				String deviceId = fwInDB.getObjectIDs().get(0);
				String packageName = firmware.getFirmwareName();
				String version = firmware.getVersion();
				
				adaptor.firmwareUpgrade(deviceId, packageName, version);
			}
			
			return firmware;
	
		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		}
	}
	
	@Override
	public Reboot executeMgmtObj(Reboot rbInDB, Reboot reboot) throws OneM2MException {

		try {
			HeritDMAdaptor adaptor = new HeritDMAdaptor(CfgManager.getInstance().getHitDMAddress());
			
			String deviceId = rbInDB.getObjectIDs().get(0);
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
	public DeviceCapability getStatus(DeviceCapability mgmtObj)
			throws OneM2MException {
		
		throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "getStatus Not implemented for mgmtObj:"+mgmtObj.getMgmtDefinition());
	}

	@Override
	public Reboot getStatus(Reboot mgmtObj) throws OneM2MException {
		throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "getStatus Not implemented for mgmtObj:"+mgmtObj.getMgmtDefinition());
	}

	@Override
	public Memory executeMgmtObj(Memory mgmtObjInDB, Memory mgmtObj)
			throws OneM2MException {
		throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "executeMgmtObj Not implemented for mgmtObj:"+mgmtObj.getMgmtDefinition());
	}

	@Override
	public Battery executeMgmtObj(Battery mgmtObjInDB, Battery mgmtObj)
			throws OneM2MException {
		throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "executeMgmtObj Not implemented for mgmtObj:"+mgmtObj.getMgmtDefinition());
	}

	@Override
	public DeviceInfo executeMgmtObj(DeviceInfo mgmtObjInDB, DeviceInfo mgmtObj)
			throws OneM2MException {
		throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "executeMgmtObj Not implemented for mgmtObj:"+mgmtObj.getMgmtDefinition());
	}

	@Override
	public DeviceCapability executeMgmtObj(DeviceCapability mgmtObjInDB,
			DeviceCapability mgmtObj) throws OneM2MException {
		throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "executeMgmtObj Not implemented for mgmtObj:"+mgmtObj.getMgmtDefinition());
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
