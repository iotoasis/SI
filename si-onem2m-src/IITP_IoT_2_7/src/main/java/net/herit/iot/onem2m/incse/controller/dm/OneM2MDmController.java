package net.herit.iot.onem2m.incse.controller.dm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;

import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.EXEC_RESULT;
import net.herit.iot.message.onem2m.format.Enums.EXEC_STATUS;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.dm.HeritDMAdaptor.MOUri;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.manager.dao.ExecInstanceDAO;
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
import net.herit.iot.onem2m.resource.Naming;
import net.herit.iot.onem2m.resource.Node;
import net.herit.iot.onem2m.resource.Reboot;
import net.herit.iot.onem2m.resource.RebootArgsType;
import net.herit.iot.onem2m.resource.ResetArgsType;
import net.herit.iot.onem2m.resource.Software;
import net.herit.iot.onem2m.resource.SoftwareInstallArgsType;
import net.herit.iot.onem2m.resource.SoftwareUninstallArgsType;
import net.herit.iot.onem2m.resource.SoftwareUpdateArgsType;
import net.herit.iot.onem2m.resource.UploadArgsType;

public class OneM2MDmController implements DMControllerInterface {

	private OneM2mContext context;
	
	public OneM2MDmController(OneM2mContext context) {
		this.context = context;
	}
	
	@Override
	public Firmware getStatus(Firmware firmware) throws OneM2MException {
		OneM2MDmAdapter adaptor = new OneM2MDmAdapter(CfgManager.getInstance().getOneM2mAgentAddress());
		
		String deviceId = firmware.getObjectIDs().get(0);
		
		Document nodeDoc = context.getDatabaseManager().getCollection(CfgManager.getInstance().getResourceDatabaseName())
								.find(new BasicDBObject(Naming.NODEID_SN, deviceId)).first();
		String agentAddress = "";
		agentAddress = nodeDoc.getString(Naming.MGMTCLIENTADDRESS);
		if(agentAddress != null && !agentAddress.equals("")) {
			adaptor = new OneM2MDmAdapter(agentAddress);
		}
		
		try {
			
			Document doc = adaptor.readDeviceStatus(deviceId, "firmwarestat");
			firmware.setVersion(doc.getString("version"));
			
			
		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		} catch(IOException ex) {
			ex.printStackTrace();
			return null;
		}
		
		return firmware;
	}
	
	@Override
	public Software getStatus(Software software) throws OneM2MException {
		throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "getStatus Not implemented for mgmtObj:"+software.getMgmtDefinition());
	}

	@Override
	public Memory getStatus(Memory memory) throws OneM2MException {
		OneM2MDmAdapter adaptor = new OneM2MDmAdapter(CfgManager.getInstance().getOneM2mAgentAddress());
		
		String deviceId = memory.getObjectIDs().get(0);
		
		Document nodeDoc = context.getDatabaseManager().getCollection(CfgManager.getInstance().getResourceDatabaseName())
				.find(new BasicDBObject(Naming.NODEID_SN, deviceId)).first();
		String agentAddress = "";
		agentAddress = nodeDoc.getString(Naming.MGMTCLIENTADDRESS);
		if(agentAddress != null && !agentAddress.equals("")) {
			adaptor = new OneM2MDmAdapter(agentAddress);
		}
		
		try {
			
			Document doc = adaptor.readDeviceStatus(deviceId, "freemem");
			memory.setMemAvailable(doc.getInteger("free"));
			memory.setMemTotal(doc.getInteger("total"));
			
		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		} catch(IOException ex) {
			ex.printStackTrace();
			return null;
		}
		
		return memory;
	}

	@Override
	public Battery getStatus(Battery mgmtObj) throws OneM2MException {
		throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "getStatus Not implemented for mgmtObj:"+mgmtObj.getMgmtDefinition());
	}

	@Override
	public DeviceInfo getStatus(DeviceInfo devInfo) throws OneM2MException {
		
		OneM2MDmAdapter adaptor = new OneM2MDmAdapter(CfgManager.getInstance().getOneM2mAgentAddress());
		
		String deviceId = devInfo.getObjectIDs().get(0);
		
		Document nodeDoc = context.getDatabaseManager().getCollection(CfgManager.getInstance().getResourceDatabaseName())
				.find(new BasicDBObject(Naming.NODEID_SN, deviceId)).first();
		String agentAddress = "";
		agentAddress = nodeDoc.getString(Naming.MGMTCLIENTADDRESS);
		if(agentAddress != null && !agentAddress.equals("")) {
			adaptor = new OneM2MDmAdapter(agentAddress);
		}
		
		try {
			
			Document doc = adaptor.readDeviceStatus(deviceId, "deviceinfo");
			devInfo.setFwVersion(doc.getString("fw_version"));
			devInfo.setSwVersion(doc.getString("os_version"));
			devInfo.setDeviceLabel(doc.getString("serial"));
			devInfo.setManufacturer(doc.getString("manufacturer"));
			devInfo.setModel(doc.getString("model"));
			
		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		} catch(IOException ex) {
			ex.printStackTrace();
			return null;
		}
		
		return devInfo;
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
	public EventLog getStatus(EventLog evtLog) throws OneM2MException {
		
		OneM2MDmAdapter adaptor = new OneM2MDmAdapter(CfgManager.getInstance().getOneM2mAgentAddress());
		
		String deviceId = evtLog.getObjectIDs().get(0);
		
		Document nodeDoc = context.getDatabaseManager().getCollection(CfgManager.getInstance().getResourceDatabaseName())
				.find(new BasicDBObject(Naming.NODEID_SN, deviceId)).first();
		String agentAddress = "";
		agentAddress = nodeDoc.getString(Naming.MGMTCLIENTADDRESS);
		if(agentAddress != null && !agentAddress.equals("")) {
			adaptor = new OneM2MDmAdapter(agentAddress);
		}
		
		try {
			
			Document doc = adaptor.readDeviceStatus(deviceId, "debug/status");
			evtLog.setLogStatus(doc.getBoolean("status") == true ? 1 : 2);
			
		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		} catch(IOException ex) {
			ex.printStackTrace();
			return null;
		}
		
		return evtLog;
	}

	@Override
	public Firmware executeMgmtObj(Firmware fwInDB, Firmware firmware)
			throws OneM2MException {
		
		try {
			if (firmware.isUpdate()) {
				OneM2MDmAdapter adaptor = new OneM2MDmAdapter(CfgManager.getInstance().getOneM2mAgentAddress());
				String deviceId = fwInDB.getObjectIDs().get(0);
				String packageName = fwInDB.getFirmwareName();
				String version = fwInDB.getVersion();
				String url = fwInDB.getURL();
				//System.out.println("firmware update executed..");
				adaptor.firmwareUpdate(deviceId, url, version, packageName);
			}
			
			return firmware;
	
		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		} catch(IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Software executeMgmtObj(Software swInDB, Software software)
			throws OneM2MException {
		
		try {
			if (software.isInstall() != null && software.isInstall()) {
				OneM2MDmAdapter adaptor = new OneM2MDmAdapter(CfgManager.getInstance().getOneM2mAgentAddress());
				String deviceId = swInDB.getObjectIDs().get(0);
				String packageName = swInDB.getSoftwareName();
				String version = swInDB.getVersion();
				String url = swInDB.getURL();
				//System.out.println("software install executed..");
				adaptor.softwareInstall(deviceId, url, version, packageName);
			}
			
			if(software.isUninstall() != null && software.isUninstall()) {
				OneM2MDmAdapter adaptor = new OneM2MDmAdapter(CfgManager.getInstance().getOneM2mAgentAddress());
				String deviceId = swInDB.getObjectIDs().get(0);
				String packageName = swInDB.getSoftwareName();
				String version = swInDB.getVersion();
				String url = swInDB.getURL();
				//System.out.println("software uninstall executed..");
				adaptor.softwareUninstall(deviceId, url, packageName);
			}
			
			return software;
	
		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		} catch(IOException ex) {
			ex.printStackTrace();
			return null;
		}
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

	@Override
	public Reboot executeMgmtObj(Reboot rbInDB, Reboot reboot)
			throws OneM2MException {
		
		try {
			OneM2MDmAdapter adaptor = new OneM2MDmAdapter(CfgManager.getInstance().getOneM2mAgentAddress());
			
			String deviceId = rbInDB.getObjectIDs().get(0);
			if (reboot.isReboot() != null && reboot.isReboot()) {
				adaptor.reboot(deviceId);
			} else if (reboot.isFactoryReset() != null && reboot.isFactoryReset()) {
				adaptor.factoryReset(deviceId);
			}		
			
			return reboot;
			
		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		} catch(IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public EventLog executeMgmtObj(EventLog mgmtObjInDB, EventLog evtLog)
			throws OneM2MException {
		OneM2MDmAdapter adaptor = new OneM2MDmAdapter(CfgManager.getInstance().getOneM2mAgentAddress());

		try {
			Document resDoc = null;
			String deviceId = mgmtObjInDB.getObjectIDs().get(0);
			if (evtLog.isLogStart() != null && evtLog.isLogStart()) {
				adaptor.readDeviceStatus(deviceId, "debug/start");
				evtLog.setLogStatus(1);  //started
			} else if (evtLog.isLogStop() != null && evtLog.isLogStop()) {
				resDoc = adaptor.readDeviceStatus(deviceId,"debug/stop");
				evtLog.setLogData(resDoc.toJson());
				evtLog.setLogStatus(2);  //stopped
				//System.out.println("############### resDoc = " + resDoc.toJson());
			}		
			
			return evtLog;

		} catch (HitDMException e) {
			
			throw convertHitDMExToOneM2MEx(e);
		} catch(IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public ExecInstance executeResetCmd(MgmtCmd mgmtCmd, ExecInstance execInst,
			Node node, ExecInstanceDAO dao) throws OneM2MException {
		
		String agentAddress = "";
		
		OneM2MDmAdapter adaptor = new OneM2MDmAdapter(CfgManager.getInstance().getOneM2mAgentAddress());
		
		agentAddress = node.getMgmtClientAddress();
		
		if(agentAddress != null && !agentAddress.equals("")) {
			adaptor = new OneM2MDmAdapter(agentAddress);
		} 
		
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
		
		String agentAddress = "";
		
		OneM2MDmAdapter adaptor = new OneM2MDmAdapter(CfgManager.getInstance().getOneM2mAgentAddress());

		agentAddress = node.getMgmtClientAddress();
		
		if(agentAddress != null && !agentAddress.equals("")) {
			adaptor = new OneM2MDmAdapter(agentAddress);
		}
		
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
		
		execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
		execInst.setExecResult(EXEC_RESULT.REQUEST_UNSUPPORTED.Value());	
		
		return execInst;
	}

	@Override
	public ExecInstance executeDownloadCmd(MgmtCmd mgmtCmd,
			ExecInstance execInst, Node node, ExecInstanceDAO dao)
			throws OneM2MException {
		
		String agentAddress = "";
		String url = "";
		
		OneM2MDmAdapter adaptor = new OneM2MDmAdapter(CfgManager.getInstance().getOneM2mAgentAddress());
		
		agentAddress = node.getMgmtClientAddress();
		
		if(agentAddress != null && !agentAddress.equals("")) {
			adaptor = new OneM2MDmAdapter(agentAddress);
		}
		
		execInst.setExecStatus(EXEC_STATUS.STATUS_NON_CANCELLABLEL.Value());
		dao.update(execInst);
		
		try {
			int validArgNo = 0;
			ExecReqArgsListType args = mgmtCmd.getExecReqArgs();
			if (args != null) {
				List<DownloadArgsType> argList = args.getDownload();
				
				if (argList.size() > 0) {
					Iterator<DownloadArgsType> it = argList.iterator();
					while (it.hasNext()) {
						DownloadArgsType arg = it.next();
						adaptor.download(node.getNodeID(), arg.getURL() );
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
			
			//Document result = adaptor.download(node.getNodeID(), url);
		
			//execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			//execInst.setExecResult(null);
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
		
		String agentAddress = "";
		String url = "";
		String version = "";
		String pkgName = "";
		
		OneM2MDmAdapter adaptor = new OneM2MDmAdapter(CfgManager.getInstance().getOneM2mAgentAddress());
		
		agentAddress = node.getMgmtClientAddress();
		
		if(agentAddress != null && !agentAddress.equals("")) {
			adaptor = new OneM2MDmAdapter(agentAddress);
		} 
		
		execInst.setExecStatus(EXEC_STATUS.STATUS_NON_CANCELLABLEL.Value());
		dao.update(execInst);
		
		try {
			ExecReqArgsListType args = mgmtCmd.getExecReqArgs();
			
			if(args.getSoftwareInstall() == null || args.getSoftwareInstall().size() <= 0) {
				execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
				execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			} else {
				
				if(args.getSoftwareInstall().get(0) != null && args.getSoftwareInstall().get(0).getURL() != null && !args.getSoftwareInstall().get(0).getURL().equals("")) {
					url = args.getSoftwareInstall().get(0).getURL();
					
					SoftwareInstallArgsType arg = args.getSoftwareInstall().get(0);
					List<AnyArgType> listArgs = arg.getAnyArg();
					
					if(listArgs.size() == 2) {
						Iterator<AnyArgType> itt = listArgs.iterator();
						while(itt.hasNext()) {
							AnyArgType anyArg = itt.next();
							if(anyArg.getName().equals("ver")) {
								version = anyArg.getValue().toString();
							}
							if(anyArg.getName().equals("pkg")) {
								pkgName = anyArg.getValue().toString();
							}
						}
						if(!version.equals("") && !pkgName.equals("")) {
							adaptor.softwareInstall(node.getNodeID(), url, version, pkgName);
							
						} else {
							execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
							execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
						}
						
					} else {
						execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
						execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
					}
					
					
				} else {
					execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
					execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
				}
				
			}
			
			//Document result = adaptor.softwareInstall(node.getNodeID(), url, version, pkgName);
		
			//execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			//execInst.setExecResult(null);
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
		
		String agentAddress = "";
		String url = "";
		String version = "";
		String pkgName = "";
		
		OneM2MDmAdapter adaptor = new OneM2MDmAdapter(CfgManager.getInstance().getOneM2mAgentAddress());
		
		agentAddress = node.getMgmtClientAddress();
		
		if(agentAddress != null && !agentAddress.equals("")) {
			adaptor = new OneM2MDmAdapter(agentAddress);
		}
		
		execInst.setExecStatus(EXEC_STATUS.STATUS_NON_CANCELLABLEL.Value());
		dao.update(execInst);
		
		try {
			ExecReqArgsListType args = mgmtCmd.getExecReqArgs();
			
			if(args.getSoftwareUpdate() == null || args.getSoftwareUpdate().size() <= 0) {
				execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
				execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			} else {
				
				if(args.getSoftwareUpdate().get(0) != null && args.getSoftwareUpdate().get(0).getVersion() != null && !args.getSoftwareUpdate().get(0).getVersion().equals("")
						                                   && args.getSoftwareUpdate().get(0).getURL() != null && !args.getSoftwareUpdate().get(0).getURL().equals("")) {
					
					version = args.getSoftwareUpdate().get(0).getVersion();
					url = args.getSoftwareUpdate().get(0).getURL();
					
					SoftwareUpdateArgsType arg = args.getSoftwareUpdate().get(0);
					List<AnyArgType> listArgs = arg.getAnyArg();
					
					if(listArgs.size() == 1) {
						Iterator<AnyArgType> itt = listArgs.iterator();
						while(itt.hasNext()) {
							AnyArgType anyArg = itt.next();
							
							if(anyArg.getName().equals("pkg")) {
								pkgName = anyArg.getValue().toString();
							}
						}
						if(!pkgName.equals("")) {
							adaptor.firmwareUpdate(node.getNodeID(), url, version, pkgName);
							
						} else {
							execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
							execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
						}
						
					} else {
						execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
						execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
					}
					
					
				} else {
					execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
					execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
				}
				
			}
			//Document result = adaptor.firmwareUpdate(node.getNodeID(), url, version, pkgName);
		
			//execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			//execInst.setExecResult(null);
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
		String agentAddress = "";
		String url = "";
		String pkgName = "";
		
		OneM2MDmAdapter adaptor = new OneM2MDmAdapter(CfgManager.getInstance().getOneM2mAgentAddress());
		
		agentAddress = node.getMgmtClientAddress();
		
		if(agentAddress != null && !agentAddress.equals("")) {
			adaptor = new OneM2MDmAdapter(agentAddress);
		}
		
		execInst.setExecStatus(EXEC_STATUS.STATUS_NON_CANCELLABLEL.Value());
		dao.update(execInst);
		
		try {
			ExecReqArgsListType args = mgmtCmd.getExecReqArgs();
			
			if(args.getSoftwareUninstall() == null || args.getSoftwareUninstall().size() <= 0) {
				execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
				execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			} else {
				
				if(args.getSoftwareUninstall().get(0) != null) {
					
					SoftwareUninstallArgsType arg = args.getSoftwareUninstall().get(0);
					List<AnyArgType> listArgs = arg.getAnyArg();
					
					if(listArgs.size() == 2) {
						Iterator<AnyArgType> itt = listArgs.iterator();
						while(itt.hasNext()) {
							AnyArgType anyArg = itt.next();
							if(anyArg.getName().equals("url")) {
								url = anyArg.getValue().toString();
							}
							if(anyArg.getName().equals("pkg")) {
								pkgName = anyArg.getValue().toString();
							}
						}
						if(!url.equals("") && !pkgName.equals("")) {
							adaptor.softwareUninstall(node.getNodeID(), url, pkgName);
							
						} else {
							execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
							execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
						}
						
					} else {
						execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
						execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
					}
					
					
				} else {
					execInst.setExecResult(EXEC_RESULT.INVALID_ARGUMENTS.Value());
					execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
				}
				
			}
			//Document result = adaptor.softwareUninstall(node.getNodeID(), url, pkgName);
		
			//execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			//execInst.setExecResult(null);
		} catch (HitDMException e) {
			
			execInst.setExecResult(convertHitDMErrorToExecResult(e.getStatusCode()));
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			
		} catch (Exception e) {
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			execInst.setExecResult(EXEC_RESULT.INTERNAL_ERROR.Value());			
		}
		
		return execInst;
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
