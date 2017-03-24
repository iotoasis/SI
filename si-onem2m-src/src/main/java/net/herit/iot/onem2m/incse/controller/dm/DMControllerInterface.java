package net.herit.iot.onem2m.incse.controller.dm;

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

public interface DMControllerInterface {

	public Firmware getStatus(Firmware mgmtObj) throws OneM2MException;
	public Memory getStatus(Memory mgmtObj) throws OneM2MException;
	public Battery getStatus(Battery mgmtObj) throws OneM2MException;
	public DeviceInfo getStatus(DeviceInfo mgmtObj) throws OneM2MException;
	public DeviceCapability getStatus(DeviceCapability mgmtObj) throws OneM2MException;
	public Reboot getStatus(Reboot mgmtObj) throws OneM2MException;
	public EventLog getStatus(EventLog mgmtObj) throws OneM2MException;

	public Firmware executeMgmtObj(Firmware mgmtObjInDB, Firmware mgmtObj) throws OneM2MException;
	public Memory executeMgmtObj(Memory mgmtObjInDB, Memory mgmtObj) throws OneM2MException;
	public Battery executeMgmtObj(Battery mgmtObjInDB, Battery mgmtObj) throws OneM2MException;
	public DeviceInfo executeMgmtObj(DeviceInfo mgmtObjInDB, DeviceInfo mgmtObj) throws OneM2MException;
	public DeviceCapability executeMgmtObj(DeviceCapability mgmtObjInDB, DeviceCapability mgmtObj) throws OneM2MException;
	public Reboot executeMgmtObj(Reboot mgmtObjInDB, Reboot mgmtObj) throws OneM2MException;
	public EventLog executeMgmtObj(EventLog mgmtObjInDB, EventLog mgmtObj) throws OneM2MException;
	
	public ExecInstance executeResetCmd(MgmtCmd mgmtCmd, ExecInstance execInst, Node node, ExecInstanceDAO dao) throws OneM2MException;
	public ExecInstance executeRebootCmd(MgmtCmd mgmtCmd, ExecInstance execInst, Node node, ExecInstanceDAO dao) throws OneM2MException;
	public ExecInstance executeUploadCmd(MgmtCmd mgmtCmd, ExecInstance execInst, Node node, ExecInstanceDAO dao) throws OneM2MException;
	public ExecInstance executeDownloadCmd(MgmtCmd mgmtCmd, ExecInstance execInst, Node node, ExecInstanceDAO dao) throws OneM2MException;
	public ExecInstance executeSoftwareInstallCmd(MgmtCmd mgmtCmd, ExecInstance execInst, Node node, ExecInstanceDAO dao) throws OneM2MException;
	public ExecInstance executeSoftwareUpdateCmd(MgmtCmd mgmtCmd, ExecInstance execInst, Node node, ExecInstanceDAO dao) throws OneM2MException;
	public ExecInstance executeSoftwareUninstallCmd(MgmtCmd mgmtCmd, ExecInstance execInst, Node node, ExecInstanceDAO dao) throws OneM2MException;
	
}
