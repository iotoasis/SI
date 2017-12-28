package net.herit.iot.onem2m.incse.controller.dm;

import net.herit.iot.message.onem2m.format.Enums.EXEC_RESULT;
import net.herit.iot.message.onem2m.format.Enums.EXEC_STATUS;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.manager.dao.ExecInstanceDAO;
import net.herit.iot.onem2m.resource.Battery;
import net.herit.iot.onem2m.resource.DeviceCapability;
import net.herit.iot.onem2m.resource.DeviceInfo;
import net.herit.iot.onem2m.resource.EventLog;
import net.herit.iot.onem2m.resource.ExecInstance;
import net.herit.iot.onem2m.resource.Firmware;
import net.herit.iot.onem2m.resource.Memory;
import net.herit.iot.onem2m.resource.MgmtCmd;
import net.herit.iot.onem2m.resource.Node;
import net.herit.iot.onem2m.resource.Reboot;
import net.herit.iot.onem2m.resource.Software;

public class DefaultDMController implements DMControllerInterface {

	@Override
	public Firmware getStatus(Firmware mgmtObj) throws OneM2MException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Software getStatus(Software mgmtObj) throws OneM2MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Memory getStatus(Memory mgmtObj) throws OneM2MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Battery getStatus(Battery mgmtObj) throws OneM2MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeviceInfo getStatus(DeviceInfo mgmtObj) throws OneM2MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeviceCapability getStatus(DeviceCapability mgmtObj)
			throws OneM2MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reboot getStatus(Reboot mgmtObj) throws OneM2MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventLog getStatus(EventLog mgmtObj) throws OneM2MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Firmware executeMgmtObj(Firmware mgmtObjInDB, Firmware mgmtObj)
			throws OneM2MException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Software executeMgmtObj(Software mgmtObjInDB, Software mgmtObj)
			throws OneM2MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Memory executeMgmtObj(Memory mgmtObjInDB, Memory mgmtObj)
			throws OneM2MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Battery executeMgmtObj(Battery mgmtObjInDB, Battery mgmtObj)
			throws OneM2MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeviceInfo executeMgmtObj(DeviceInfo mgmtObjInDB, DeviceInfo mgmtObj)
			throws OneM2MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeviceCapability executeMgmtObj(DeviceCapability mgmtObjInDB,
			DeviceCapability mgmtObj) throws OneM2MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reboot executeMgmtObj(Reboot mgmtObjInDB, Reboot mgmtObj)
			throws OneM2MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventLog executeMgmtObj(EventLog mgmtObjInDB, EventLog mgmtObj)
			throws OneM2MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecInstance executeResetCmd(MgmtCmd mgmtCmd, ExecInstance execInst,
			Node node, ExecInstanceDAO dao) throws OneM2MException {
		// TODO Auto-generated method stub
		execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
		execInst.setExecResult(EXEC_RESULT.REQUEST_UNSUPPORTED.Value());	
		
		return execInst;
	}

	@Override
	public ExecInstance executeRebootCmd(MgmtCmd mgmtCmd,
			ExecInstance execInst, Node node, ExecInstanceDAO dao)
			throws OneM2MException {
		// TODO Auto-generated method stub
		execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
		execInst.setExecResult(EXEC_RESULT.REQUEST_UNSUPPORTED.Value());	
		
		return execInst;
	}

	@Override
	public ExecInstance executeUploadCmd(MgmtCmd mgmtCmd,
			ExecInstance execInst, Node node, ExecInstanceDAO dao)
			throws OneM2MException {
		// TODO Auto-generated method stub
		execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
		execInst.setExecResult(EXEC_RESULT.REQUEST_UNSUPPORTED.Value());	
		
		return execInst;
	}

	@Override
	public ExecInstance executeDownloadCmd(MgmtCmd mgmtCmd,
			ExecInstance execInst, Node node, ExecInstanceDAO dao)
			throws OneM2MException {
		// TODO Auto-generated method stub
		execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
		execInst.setExecResult(EXEC_RESULT.REQUEST_UNSUPPORTED.Value());	
		
		return execInst;
	}

	@Override
	public ExecInstance executeSoftwareInstallCmd(MgmtCmd mgmtCmd,
			ExecInstance execInst, Node node, ExecInstanceDAO dao)
			throws OneM2MException {
		// TODO Auto-generated method stub
		execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
		execInst.setExecResult(EXEC_RESULT.REQUEST_UNSUPPORTED.Value());	
		
		return execInst;
	}

	@Override
	public ExecInstance executeSoftwareUpdateCmd(MgmtCmd mgmtCmd,
			ExecInstance execInst, Node node, ExecInstanceDAO dao)
			throws OneM2MException {
		// TODO Auto-generated method stub
		execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
		execInst.setExecResult(EXEC_RESULT.REQUEST_UNSUPPORTED.Value());	
		
		return execInst;
	}

	@Override
	public ExecInstance executeSoftwareUninstallCmd(MgmtCmd mgmtCmd,
			ExecInstance execInst, Node node, ExecInstanceDAO dao)
			throws OneM2MException {
		// TODO Auto-generated method stub
		execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
		execInst.setExecResult(EXEC_RESULT.REQUEST_UNSUPPORTED.Value());	
		
		return execInst;
	}

}
