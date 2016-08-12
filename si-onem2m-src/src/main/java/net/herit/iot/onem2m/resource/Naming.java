package net.herit.iot.onem2m.resource;


public class Naming {
	//Format
	public static final String DATE_FORMAT = "yyyyMMdd'T'HHmmss";
	
	
	//Common
	public static final String CHILDRESOURCE_SN = "ch";
	public static final String SUBSCRIPTION_SN = "sub";

	public static final String PRIVILEGES_SN = "pv";
	public static final String SELFPRIVILEGES_SN = "pvs";
	
	public static final String EXPIRATIONTIME_SN = "et";
	public static final String LINK_SN = "lnk";
	
	public static final String POINTOFACCESS_SN = "poa";
	
	
	public static final String ANNOUNCETO_SN = "at";
	public static final String ANNOUNCEDATTRIBUTE_SN = "aa";
	
	public static final String ACCESSCONTROLPOLICYIDS_SN = "acpi";
	
	public static final String REQUESTREACHABILITY_SN = "rr";
	
	public static final String NODELINK_SN = "nl";
	
	public static final String ONTOLOGYREF_SN = "or";
	
	public static final String STATETAG_SN = "st";
	
	//AccessControlPolicy
//	public static final String PRIVILEGES_SN = "pv";
//	public static final String SELFPRIVILEGES_SN = "pvs";
//	public static final String CHILDRESOURCE_SN = "ch";
//	public static final String SUBSCRIPTION_SN = "sub";

	//ActiveCmdhPolicy
	public static final String ACTIVECMDHPOLICYLINK_SN = "acmlk";
	
	//AE
	public static final String APPNAME_SN = "apn";
	public static final String APPID_SN = "api";
	public static final String AEID_SN = "aei";
//	public static final String POINTOFACCESS_SN = "poa";
//	public static final String ONTOLOGYREF_SN = "or";
//	public static final String NODELINK_SN = "nl";
//	public static final String REQUESTREACHABILITY_SN = "rr";
//	public static final String CHILDRESOURCE_SN = "ch";
	public static final String CONTENTSERIALIZATION_SN = "csz";
	
	//AnnounceableSubordinateResource
//	public static final String EXPIRATIONTIME_SN = "et";
//	public static final String ANNOUNCETO_SN = "at";
//	public static final String ANNOUNCEDATTRIBUTE_SN = "aa";

	
	//AnnouncedResource
//	public static final String ACCESSCONTROLPOLICYIDS_SN = "acpi";
//	public static final String EXPIRATIONTIME_SN = "et";
//	public static final String LINK_SN = "link";

	
	//AnnouncedMgmtResource
	public static final String MGMTDEFINITION_SN = "mgd";
	public static final String OBJECTIDS_SN = "obis";
	public static final String OBJECTPATHS_SN = "obps";
	public static final String DESCRIPTION_SN = "dc";
	

	//AnnounceableResource
//	public static final String ANNOUNCETO_SN = "at";
//	public static final String ANNOUNCEDATTRIBUTE_SN = "aa";

	//AggregatedNotification 
//	public static final String NOTIFICATION_SN = "sgn";
	
	
	// AggregatedRequest
//	public static final String REQUEST_SN = "req";
	// AggregatedRequest.Request
//	public static final String OPERATION_SN = "op";  // .....???? why not "op" ??
	public static final String TO_SN = "to";
	public static final String FROM_SN = "fr";
	public static final String REQUESTIDENTIFIER_SN = "rqi";
	public static final String PRIMITIVECONTENT_SN = "pc";
	public static final String METAINFORMATION_SN = "mi";
	
	
	//AggregatedResponse
	public static final String RESPONSEPRIMITIVE_SN = "rsp";
	
	
	//AreaNwkDeviceInfo
	public static final String DEVID_SN = "dvd";
	public static final String DEVTYPE_SN = "dvt";
	public static final String AREANWKID_SN = "awi";
	public static final String SLEEPINTERVAL_SN = "sli";
	public static final String SLEEPDURATION_SN = "sld";
	public static final String DEVSTATUS_SN = "ss";
	public static final String LISTOFNEIGHBORS_SN = "lnh";
	
	
	//AreaNwkInfo
	public static final String AREANWKTYPE_SN = "ant";
	public static final String LISTOFDEVICES_SN = "ldv";
	
	//Container
//	public static final String STATETAG_SN = "st";
	public static final String MAXNROFINSTANCES_SN ="mni";
	public static final String MAXBYTESIZE_SN = "mbs";
	public static final String MAXINSTANCEAGE_SN = "mia";
	public static final String CURRENTNROFINSTANCES_SN = "cni";
	public static final String CURRENTBYTESIZE_SN = "cbs";
	public static final String LOCATIONID_SN = "li";
//	public static final String ONTOLOGYREF_SN = "or";
//	public static final String CHILDRESOURCE_SN = "ch";
	
	
	//ContentInstance
//	public static final String STATETAG_SN = "st";
	public static final String CONTENTINFO_SN = "cnf";
	public static final String CONTENTSIZE_SN = "cs";
//	public static final String ONTOLOGYREF_SN = "or";
	public static final String CONTENT_SN = "con";

	
	
	//Group
	public static final String MEMBERTYPE_SN = "mt";
	public static final String CURRENTNROFMEMBERS_SN = "cnm";
	public static final String MAXNROFMEMBERS_SN = "mnm";
	public static final String MEMBERIDS_SN = "mid";
	public static final String MEMBERSACCESSCONTROLPOLICYIDS_SN = "macp";
	public static final String MEMBERTYPEVALIDATED_SN = "mtv";
	public static final String CONSISTENCYSTRATEGY_SN = "csy";
	public static final String GROUPNAME_SN = "gn";
	public static final String GROUPID_SN = "gpi";		// v1.2.1
	//public static final String GROUPID_SN = "groupID";		// v1.0.1
//	public static final String CHILDRESOURCE_SN = "ch";
//	public static final String SUBSCRIPTION_SN = "sub";


	
	
	//LocationPolicy
	public static final String LOCATIONSOURCE_SN = "los";
	public static final String LOCATIONUPDATEPERIOD_SN = "lou";
	public static final String LOCATIONTARGETID_SN = "lot";
	public static final String LOCATIONSERVER_SN = "lor";
	public static final String LOCATIONCONTAINERID_SN = "loi";
	public static final String LOCATIONCONTAINERNAME_SN = "lon";
	public static final String LOCATIONSTATUS_SN = "lost";

	
	//Node
	public static final String NODEID_SN = "ni";
	public static final String HOSTEDCSELINK_SN = "hcl";
//	public static final String CHILDRESOURCE_SN = "ch";
	

	
	//RegularResource
//	public static final String ACCESSCONTROLPOLICYIDS_SN = "acpi";
//	public static final String EXPIRATIONTIME_SN = "et";
//	public static final String ACCESSCONTROLPOLICYIDS_FN = "accessControlPolicyIDs";
//	public static final String EXPIRATIONTIME_FN = "expirationTime";
	

	//RemoteCSE
	public static final String CSETYPE_SN = "cst";
//	public static final String POINTOFACCESS_SN = "poa";
	public static final String CSEBASE_SN = "cb";
	public static final String CSEID_SN = "csi";
//	public static final String REQUESTREACHABILITY_SN = "rr";
//	public static final String NODELINK_SN = "nl";
//	public static final String CHILDRESOURCE_SN = "ch";
	
	
	
	//Resource
	public static final String RESOURCETYPE_SN = "ty";		// v1.2.1 
	//public static final String RESOURCETYPE_SN = "ty";		// v1.0.1 requestPrimitive 
	//public static final String FCRESOURCETYPE_SN = "rty";		// v1.0.1 requestPrimitive 
	public static final String FCRESOURCETYPE_SN = "rty";		// -- filterCriteria
	public static final String RESOURCEID_SN = "ri";
	public static final String PARENTID_SN = "pi";
	public static final String CREATIONTIME_SN = "ct";
	public static final String LASTMODIFIEDTIME_SN = "lt";
	public static final String LABELS_SN = "lbl";
	public static final String RESOURCENAME_SN = "rn";		// v1.2.1
	//public static final String RESOURCENAME_SN = "name";
	
	
	//Schedule
	public static final String SCHEDULEELEMENT_SN = "se";
	
	
	/*******************************************************************
	 * //ResourceType Short name
	 *******************************************************************/
	public static final String ACCESSCONTROLPOLICY_SN = "acp";
	public static final String ACCESSCONTROLPOLICYANNC_SN = "acpA";
	public static final String AE_SN = "ae";
	public static final String AEANNC_SN = "aeA";
	public static final String AGGREGATEDREQUEST_SN = "arg";
	public static final String AGGREFATEDRESPONSE_SN = "agr";
	public static final String CONTAINER_SN = "cnt";
	public static final String CONTAINERANNC_SN = "cntA";
	public static final String LATEST_SN = "la";
	public static final String OLDEST_SN = "ol";
	public static final String CONTENTINSTANCE_SN = "cin";
	public static final String CONTENTINSTANCEANNC = "cinA";
	//public static final String CSEBASE_SN = "cb";
	public static final String DELIVERY_SN = "dlv";
	public static final String EVENTCONFIG_SN = "evcg";
	public static final String EXECINSTANCE_SN = "exin";
	public static final String FANOUTPOINT_SN = "fopt";
	public static final String GROUP_SN = "grp";
	public static final String GROUPANNC_SN = "grpA";
	public static final String LOCATIONPOLICY_SN = "lcp";
	public static final String LOCATIONPOLICYANNC_SN = "lcpA";
	public static final String M2MSERVICESUBSCRIPTIONPROFILE_SN = "mssp";
	public static final String MGMTCMD_SN = "mgc";
	public static final String MGMTOBJ_SN = "mgo";
	public static final String MGMTOBJANNC_SN = "mgoA";
	public static final String NODE_SN = "nod";
	public static final String NODEANNC_SN = "nodA";
	public static final String POLLINGCHANNEL_SN = "pch";
	public static final String POLLINGCHANNELURI_SN = "pcu";
	public static final String REMOTECSE_SN = "csr";
	public static final String REMOTECSEANNC_SN = "csrA";
	public static final String REQUEST_SN = "req";
	public static final String SCHEDULE_SN = "sch";
	public static final String SCHEDULEANNC_SN = "schA";
	public static final String SERVICESUBSCRIBEDAPPRULE_SN = "asar";
	public static final String SERVICESUBSCRIBEDNODE_SN = "svsn";
	public static final String STATSCOLLECT_SN = "stcl";
	public static final String STATSCONFIG_SN = "stcg";
	//public static final String SUBSCRIPTION_SN = "sub";
	public static final String FIRMWARE_SN = "fwr";
	public static final String SOFTWARE_SN = "swr";
	public static final String MEMORY_SN = "mem";
	public static final String AREANWKINFO_SN = "ani";
	public static final String AREANWKDEVICEINFO_SN = "andi";
	public static final String BATTERY_SN = "bat";
	public static final String DEVICEINFO_SN = "dvi";
	public static final String DEVICECAPABILITY_SN = "dvc";
	public static final String REBOOT_SN = "rbo";
	public static final String EVENTLOG_SN = "evl";
	public static final String CMDHPOLICY_SN = "cmp";
	public static final String ACTIVECMDHPOLICY_SN = "acmp";
	public static final String CMDHDEFAULTS_SN = "cmdf";
	public static final String CMDHDEFECVALUE_SN = "cmdv";
	public static final String CMDHECDEFPARAMVALUES_SN = "cmpv";
	public static final String CMDHLIMITS_SN = "cml";
	public static final String CMDHNETWORKACCESSRULES_SN = "cmnr";
	public static final String CMDHNWACCESSRULE_SN = "cmwr";
	public static final String CMDHBUFFER_SN = "cmbf";
	public static final String REQUESTPRIMITIVE_SN = "rqp";

	
	//Complex data type Short name
	public static final String CREATEDBEFORE_SN = "crb";
	public static final String CREATEDAFTER_SN = "cra";
	public static final String MODIFIEDSINCE_SN = "ms";
	public static final String UNMODIFIEDSINCE_SN = "us";
	public static final String STATETAGSMALLER_SN = "sts";
	public static final String STATETAGBIGGER_SN = "stb";
	public static final String EXPIREBEFORE_SN = "exb";
	public static final String EXPIREAFTER_SN = "exa";
	//public static final String LABELS_SN = "lbl";
	//public static final String RESOURCETYPE_SN = "ty";
	public static final String SIZEABOVE_SN = "sza";
	public static final String SIZEBELOW_SN = "szb";
	public static final String CONTENTTYPE_SN = "cty";
	public static final String LIMIT_SN = "lim";
	public static final String ATTRIBUTE_SN = "atr";
	public static final String NOTIFICATIONEVENTTYPE_SN = "net";
	public static final String OPERATIONMONITOR_SN = "om";
	public static final String REPRESENTATION_SN = "rep";
	public static final String FILTERUSAGE_SN = "fu";
	public static final String EVENTCATTYPE_SN = "ect";
	public static final String EVENTCATNO_SN = "ecn";
	public static final String NUMBER_SN = "num";
	public static final String DURATION_SN = "dur";
	public static final String NOTIFICATION_SN = "sgn";
	public static final String NOTIFICATIONEVENT_SN = "nev";
	public static final String VERIFICATIONREQUEST_SN = "vrq";
	public static final String SUBSCRIPTIONDELETION_SN = "sud";
	public static final String SUBSCRIPTIONREFERENCE_SN = "sur";
	public static final String CREATOR_SN = "cr";
	public static final String NOTIFICATIONFORWARDINGURI_SN = "nfu";
	public static final String OPERATION_SN = "opr";
	public static final String ORIGINATOR_SN = "org";
	public static final String ACCESSID_SN = "aci";
	public static final String MSISDN_SN = "msd";
	public static final String ACTION_SN = "acn";
	public static final String STATUS_SN = "sus";
	//public static final String CHILDRESOURCE_SN = "ch";
	public static final String ACCESSCONTROLRULE_SN = "acr";	// v1.2.1
	//public static final String ACCESSCONTROLRULE_SN = "accessControlRule";
	public static final String ACCESSCONTROLORIGINATORS_SN = "acor";	// v1.2.1
	//public static final String ACCESSCONTROLORIGINATORS_SN = "accessControlOriginators";	
	public static final String ACCESSCONTROLOPERATIONS_SN = "acop";	// v1.2.1
	//public static final String ACCESSCONTROLOPERATIONS_SN = "accessControlOperations";	
	public static final String ACCESSCONTROLCONTEXTS_SN = "acco";	// v1.2.1
	//public static final String ACCESSCONTROLCONTEXTS_SN = "accessControlContexts";	
	public static final String ACCESSCONTROWINDOW_SN = "actw";	// v1.2.1
	//public static final String ACCESSCONTROWINDOW_SN = "accessControWindow";	
	public static final String ACCESSCONTROLIPADDRESSES_SN = "acip";	// v.2.1
	//public static final String ACCESSCONTROLIPADDRESSES_SN = "accessControlIpAddresses";	
	public static final String IPV4ADDRESSES_SN = "ipv4";	// v1.2.1
	//public static final String IPV4ADDRESSES_SN = "ipv4Addresses";	
	public static final String IPV6ADDRESSES_SN = "ipv6";	// v1.2.1
	//public static final String IPV6ADDRESSES_SN = "ipv6Addresses";	
	public static final String ACCESSCONTROLLOCATIONREGION_SN = "aclr";	// v1.2.1
	//public static final String ACCESSCONTROLLOCATIONREGION_SN = "accessControlLocationRegion";		
	public static final String COUNTRYCODE_SN = "accc";	// v1.2.1
	//public static final String COUNTRYCODE_SN = "countryCode";	
	public static final String CIRCREGION_SN = "accr";	// v1.2.1
	//public static final String CIRCREGION_SN = "circRegion";	
	public static final String NAME_SN = "nm";	// v1.2.1
	//public static final String NAME_SN = "nam";		// v1.0.1		-- cmdhPolicy, firmware, software
	public static final String VALUE_SN = "val";	// v1.2.1
	//public static final String VALUE_SN = "value";	
	public static final String TYPE_SN = "typ";	// v1.2.1
	//public static final String TYPE_SN = "type";	
	public static final String MAXNROFNOTIFY_SN = "mnn";	// v1.2.1
	//public static final String MAXNROFNOTIFY_SN = "maxNrOfNotify";	
	public static final String TIMEWINDOW_SN = "tww";	// v1.2.1
	//public static final String TIMEWINDOW_SN = "timeWindow";	

	public static final String SCHEDULEENTRY_SN = "sce";		// v1.2.1
	//public static final String SCHEDULEENTRY_SN = "scheduleEntry";	
	public static final String AGGREGATEDNOTIFICATION_SN = "agn";
	public static final String ATTRIBUTELIST_SN = "atrl";		// v1.2.1
	//public static final String ATTRIBUTELIST_SN = "attributeList";		
	public static final String AGGREGATEDRESPONSE_SN = "agr";
	public static final String RESOURCE_SN = "rce";		// v1.2.1
	//public static final String RESOURCE_SN = "resource";		
	public static final String URILIST_SN = "uril";			// v1.2.1
	//public static final String URILIST_SN = "URIList";			
	public static final String ANYARG_SN = "any";				// v1.2.1
	//public static final String ANYARG_SN = "anyArg";
	public static final String FILETYPE_SN = "ftyp";			// v1.2.1
	//public static final String FILETYPE_SN = "fileType";
	
	public static final String URL_SN = "url";
	
	public static final String USERNAME_SN = "unm";				// v1.2.1
	//public static final String USERNAME_SN = "username";
	public static final String PASSWORD_SN = "pwd";				// v1.2.1
	//public static final String PASSWORD_SN = "password";
	public static final String FILESIZE_SN = "fsi";				// v1.2.1
	//public static final String FILESIZE_SN = "fileSize";
	public static final String TARGETFILE_SN = "tgf";				// v1.2.1
	//public static final String TARGETFILE_SN = "targetFile";
	public static final String DELAYSECONDS_SN = "dss";			// v1.2.1
	//public static final String DELAYSECONDS_SN = "delaySeconds";
	public static final String SUCCESSURL_SN = "surl";			// v1.2.1
	//public static final String SUCCESSURL_SN = "successURL";
	public static final String STARTTIME_SN = "stt";				// v1.2.1
	//public static final String STARTTIME_SN = "startTime";
	public static final String COMPLETETIME_SN = "cpt";			// v1.2.1
	//public static final String COMPLETETIME_SN = "completeTime";
	public static final String UUID_SN = "uuid";					// v1.2.1
	//public static final String UUID_SN = "UUID";
	public static final String EXECUTIONENVREF_SN = "eer";		// v1.2.1
	//public static final String EXECUTIONENVREF_SN = "executionEnvRef";
	
	public static final String VERSION_SN = "vr";				
	
	public static final String RESET_SN = "rst";				// v1.2.1
	//public static final String RESET_SN = "reset";

	//public static final String REBOOT_SN = "rbo";
	
	public static final String UPLOAD_SN = "uld";				// v1.2.1
	//public static final String UPLOAD_SN = "upload";
	public static final String DOWNLOAD_SN = "dld";				// v1.2.1
	//public static final String DOWNLOAD_SN = "download";
	public static final String SOFTWAREINSTALL_SN = "swin";				// v1.2.1
	//public static final String SOFTWAREINSTALL_SN = "softwareInstall";
	public static final String SOFTWAREUPDATE_SN = "swup";				// v1.2.1
	//public static final String SOFTWAREUPDATE_SN = "softwareUpdate";
	public static final String SOFTWAREUNINSTALL_SN = "swun";				// v1.2.1
	//public static final String SOFTWAREUNINSTALL_SN = "softwareUninstall";
	public static final String TRACINGOPTION_SN = "tcop";				// v1.2.1
	//public static final String TRACINGOPTION_SN = "tracingOption";
	public static final String TRACINGINFO_SN = "tcin";				// v1.2.1
	//public static final String TRACINGINFO_SN = "tracingInfo";
	public static final String RESPONSETYPEVALUE_SN = "rtv";				// v1.2.1
	//public static final String RESPONSETYPEVALUE_SN = "responseTypeValue";
	
	public static final String NOTIFICATIONURI_SN = "nu";				

	public static final String FIRMWARENAME_SN = "fwnnam";	// v1.2.1
	//public static final String FIRMWARENAME_SN = "firmwareName";	
	public static final String SOFTWARENAME_SN = "swn";	// v1.2.1
	//public static final String SOFTWARENAME_SN = "softwareName";	
	public static final String CMDHPOLICYNAME_SN = "cpn";	// v1.2.1
	//public static final String CMDHPOLICYNAME_SN = "cmdhPolicyName";	

	public static final String APPLICABLECREDIDS_SN = "apci";	// v1.2.1
	//public static final String APPLICABLECREDIDS_SN = "aci";	// v1.0.1

	
	//Undefined type Short name
	public static final String URI_SN = "uri";
	
	
	
	
	
	
	
	/******************************************************************************************
	 * User Defined Name	
	 */
	public static final String URI_KEY = "_uri";
	public static final String RESID_KEY = RESOURCEID_SN;
	/******************************************************************************************/
	
	
}
