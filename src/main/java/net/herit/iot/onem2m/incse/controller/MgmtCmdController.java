package net.herit.iot.onem2m.incse.controller;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.corba.se.impl.util.Utility;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESPONSE_TYPE;
import net.herit.iot.message.onem2m.format.Enums.*;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.dm.DMControllerFactory;
import net.herit.iot.onem2m.incse.controller.dm.DMControllerInterface;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.ManagerFactory;
import net.herit.iot.onem2m.incse.manager.ManagerInterface;
import net.herit.iot.onem2m.incse.manager.RequestManager;
import net.herit.iot.onem2m.incse.manager.dao.ExecInstanceDAO;
import net.herit.iot.onem2m.resource.ExecInstance;
import net.herit.iot.onem2m.resource.MgmtCmd;
import net.herit.iot.onem2m.resource.Node;
import net.herit.iot.onem2m.resource.OperationResult;
import net.herit.iot.onem2m.resource.Request;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.ResponsePrimitive;

public class MgmtCmdController implements Runnable {
	
	private OneM2mContext context;
	private ExecInstance execInst;
	private MgmtCmd mgmtCmd;
	private Node node;
	
	private Logger log = LoggerFactory.getLogger(MgmtCmdController.class);
	
	public MgmtCmdController(OneM2mContext context, MgmtCmd mgmtCmd, ExecInstance execInst, Node node) {
		
		this.context = context;
		this.execInst = execInst;
		this.mgmtCmd = mgmtCmd;
		this.node = node;
		
	}

	@Override
	public void run() {

		ExecInstanceDAO dao = new ExecInstanceDAO(context);
		
		try {
			
			DMControllerInterface controller = DMControllerFactory.getController(this.mgmtCmd);
			
			switch (CMD_TYPE.get(mgmtCmd.getCmdType())) {
			case RESET:
				execInst = controller.executeResetCmd(mgmtCmd, execInst, node, dao);
				break;
			case REBOOT:
				execInst = controller.executeRebootCmd(mgmtCmd, execInst, node, dao);
				break;
			case UPLOAD:
				execInst = controller.executeUploadCmd(mgmtCmd, execInst, node, dao);
				break;
			case DOWNLOAD:
				execInst = controller.executeDownloadCmd(mgmtCmd, execInst, node, dao);
				break;
			case SOFTWAREINSTALL:
				execInst = controller.executeSoftwareInstallCmd(mgmtCmd, execInst, node, dao);
				break;
			case SOFTWAREUPDATE:
				execInst = controller.executeSoftwareUpdateCmd(mgmtCmd, execInst, node, dao);
				break;
			case SOFTWAREUNINSTALL:
				execInst = controller.executeSoftwareUninstallCmd(mgmtCmd, execInst, node, dao);
				break;
				
			}
		
						
		} catch (OneM2MException e) {
			log.debug("Handled exception", e);
			
			execInst.setExecResult(EXEC_RESULT.INTERNAL_ERROR.Value());
			execInst.setExecStatus(EXEC_STATUS.FINISHED.Value());
			
		} finally {
			try {
				
				dao.update(execInst);
			} catch (OneM2MException e) {

				log.debug("Handled exception", e);
			}
		}
	}

	public static void executeCommand(OneM2mContext context, MgmtCmd mgmtCmd, ExecInstance execInst, Node node) {

		Runnable arc = new MgmtCmdController(context, mgmtCmd, execInst, node);
		new Thread(arc).start();
	}

}
