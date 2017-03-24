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
	public static final String FIRMWAREANNC_SN = "fwrA";
	public static final String SOFTWARE_SN = "swr";
	public static final String SOFTWAREANNC_SN = "swrA";
	public static final String MEMORY_SN = "mem";
	public static final String AREANWKINFO_SN = "ani";
	public static final String AREANWKDEVICEINFO_SN = "andi";
	public static final String BATTERY_SN = "bat";
	public static final String BATTERYSTATUS_SN = "bts";
	public static final String BATTERYLEVEL_SN = "btl";
	public static final String DEVICEINFO_SN = "dvi";
	public static final String DEVICEINFOANNC_SN = "dviA";
	public static final String DEVICECAPABILITY_SN = "dvc";
	public static final String DEVICECAPABILITYANNC_SN = "dvcA";
	public static final String REBOOT_SN = "rbo";
	public static final String REBOOTANNC_SN = "rboA";
	public static final String EVENTLOG_SN = "evl";
	public static final String EVENTLOGANNC_SN = "evlA";
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
	public static final String SUPPORTEDRESOURCETYPE_SN = "srt";
	
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
	public static final String OPERATION_SN = "op";
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
	public static final String ACCESSCONTROLAUTHENTICATION_FLAG_SN = "acaf";	// v2.7.1
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
	
	public static final String APPLICABLEEVENTCATEGORY_SN = "aec";	// v2.7.0
	public static final String MAXBUFFERSIZE_SN = "mbfs";	// v2.7.0
	public static final String STORAGEPRIORITY_SN = "sgp";	// v2.7.0

	
	//Undefined type Short name
	public static final String URI_SN = "uri";
	
	
	// added at v2.7.0
	// resource type
	public static final String SEMANTICDESCRIPTOR_SN = "smd";
	public static final String SEMANTICDESCRIPTORANNC_SN = "smdA";
	public static final String ALLJOYNAPP_SN = "ajap";
	public static final String ALLJOYNAPPANNC_SN = "ajapa";
	public static final String ALLJOYNSVCOBJECT_SN = "ajso";
	public static final String ALLJOYNSVCOBJECTANNC_SN = "ajsoa";
	public static final String ALLJOYNINTERFACE_SN = "ajif";
	public static final String ALLJOYNINTERFACEANNC_SN = "ajifa";
	public static final String ALLJOYNMETHOD_SN = "ajmd";
	public static final String ALLJOYNMETHODANNC_SN = "ajmda";
	public static final String ALLJOYNMETHODCALL_SN = "ajmc";
	public static final String ALLJOYNMETHODCALLANNC_SN = "ajmca";
	public static final String ALLJOYNPROPERTY_SN = "ajpr";
	public static final String ALLJOYNPROPERTYANNC_SN = "ajpra";
	public static final String GENERICINTERWORKINGSERVICE_SN = "gis";
	public static final String GENERICINTERWORKINGSERVICEANNC_SN = "gisa";
	public static final String GENERICINTERWORKINGOPERATIONINSTANCE_SN = "gio";
	public static final String GENERICINTERWORKINGOPERATIONINSTANCEANNC_SN = "gioa";
	public static final String SVCOBJWRAPPER_SN = "ajsw";
	public static final String SVCOBJWRAPPERANNC_SN = "ajswa";
	public static final String SVCFWWRAPPER_SN = "ajfw";
	public static final String SVCFWWRAPPERANNC_SN = "ajfwa";
	public static final String TIMESERIESINSTANCE_SN = "tsi";
	public static final String TIMESERIESINSTANCEANNC_SN = "tsia";
	public static final String TRAFFICPATTERN_SN = "trpt";
	public static final String TRAFFICPATTERNANNC_SN = "trptA";
	public static final String BACKOFFPARAMETERS_SN = "bop";
	public static final String E2ESECINFO_SN = "esi";
	public static final String MEMORYANNC_SN = "memA";
	public static final String SEMANTICFANOUTPOINT_SN = "sfop";
	
	// Group
	public static final String SEMANTICSUPPORTINDICATOR_SN = "ssi";		// added in 2016-10-25, NOT DESIGNATED IN TS-0004 OR XSD.
	
	
	// TimeSeriesInstance
	public static final String DATAGENERATIONTIME_SN = "dgt";
	public static final String SEQUENCELR_SN = "snr";
	
	//AnnouncedFlexContainerResource
	public static final String DYNAMICAUTHORIZATIONCONSULTATIONIDS_SN = "daci";
	public static final String CONTAINERDEFINITION_SN = "cnd";
	
	//SemanticDescriptor
	public static final String DESCRIPTORREPRESENTATION_SN = "dcrp";
	public static final String SEMANTICOPEXEC_SN = "soe";
	public static final String RELATEDSEMANTICS_SN = "rels";
	public static final String DESCRIPTOR_SN = "dsp";
	
	//AllJoynMethodCall
	public static final String INPUT_SN = "inp";
	public static final String CALLSTATUS_SN = "clst";
	public static final String OUTPUT_SN = "out";
	
	//GenericInterworkingOperationInstance
	public static final String OPERATIONNAME_SN = "gion";
	public static final String INPUTDATAPOINTLINKS_SN = "giip";
	public static final String OUTPUTDATAPOINTLINKS_SN = "giop";
	public static final String INPUTLINKS_SN = "giil";
	public static final String OUTPUTLINKS_SN = "giol";
	public static final String OPERATIONSTATE_SN = "gios";
	
	//ListOfDataLinks
	public static final String DATALINKENTRY_SN = "dle";
	
	//DataLink
	public static final String DATACONTAINERID_SN = "dcid";
	public static final String ATTRIBUTENAME_SN = "atn";
	
	//AllJoynApp
	public static final String DIRECTION_SN = "dir";
	
	//AllJoynSvcObject
	public static final String OBJECTPATH_SN = "ajop";
	public static final String ENABLE_SN = "ena";
	
	//AllJoynInterface
	public static final String INTERFACEINTROSPECTXMLREF_SN = "ajir";
	
	//AllJoynProperty
	public static final String CURRENTVALUE_SN = "crv";
	public static final String REQUESTEDVALUE_SN = "rqv";
	
	//GenericInterworkingService
	public static final String SERVICENAME_SN = "gisn";
	
	//TimeSeries
	public static final String PERIODICINTERVAL_SN = "pei";
	public static final String MISSINGDATADETECT_SN = "mdd";
	public static final String MISSINGDATAMAXNR_SN = "mdn";
	public static final String MISSINGDATALIST_SN = "mdlt";
	public static final String MISSINGDATACURRENTNR_SN = "mdc";
	public static final String MISSINGDATADETECTTIMER_SN = "mdt";
	
	//TrafficPattern
	public static final String PROVIDEDTONSE_SN = "ptn";
	public static final String PERIODICINDICATOR_SN = "pri";
	public static final String PERIODICDURATIONTIME_SN = "pdt";
	public static final String PERIODICINTERVALTIME_SN = "pit";
	public static final String STATIONARYINDICATION_SN = "sti";
	public static final String DATASIZEINDICATOR_SN = "dsi";
	public static final String VALIDITYTIME_SN = "vdt";
	public static final String TARGETNETWORK_SN = "ttn";
	
	//BackOffParameters
	public static final String NETWORKACTION_SN = "nwa";
	public static final String INITIALBACKOFFTIME_SN = "ibt";
	public static final String ADDITIONALBACKOFFTIME_SN = "abt";
	public static final String MAXIMUMBACKOFFTIME_SN = "mbt";
	public static final String OPTIONALRANDOMBACKOFFTIME_SN = "rbt";
	public static final String BACKOFFPARAMETERSSET_SN = "bops";
	
	//E2ESecInfo
	public static final String SUPPORTEDE2ESECFEATURES_SN = "esf";
	public static final String CERTIFICATES_SN = "escert";
	public static final String SHAREDRECEIVERESPRIMRANDOBJECT_SN = "espa";
	
	//ReceiverESPrimRandObject
	public static final String ESPRIMRANDID_SN = "esri";
	public static final String ESPRIMRANDVALUE_SN = "esrv";
	public static final String ESPRIMRANDEXPIRY_SN = "esrx";
	public static final String ESPRIMKEYGENALGID_SN = "esk";
	public static final String ESPRIMKEYGENALGIDS_SN = "esks";
	public static final String ESPRIMPROTOCOLANDALGIDS_SN = "espa";
	
	//Role
	public static final String ROLEID_SN = "rlid";
	public static final String ROLENAME_SN = "rlnm";
	public static final String TOKENLINK_SN = "rltl";
	
	//Token
	public static final String TOKENID_SN = "tkid";
	public static final String TOKENOBJECT_SN = "tkob";
	public static final String ISSUER_SN = "tkis";
	public static final String HOLDER_SN = "tkhd";
	public static final String NOTBEFORE_SN = "tknb";
	public static final String NOTAFTER_SN = "tkna";
	public static final String TOKENNAME_SN = "tknm";
	public static final String AUDIENCE_SN = "tkau";
	public static final String PERMISSIONS_SN = "tkps";
	public static final String EXTENSION_SN = "tkex";
	
	//TokenPermission
	public static final String RESOURCEIDS_SN = "ris";
	public static final String ROLEIDS_SN = "rids";
	
	//TokenPermissions
	public static final String PERMISSION_SN = "pm";
	
	//ChildResourceRef
	public static final String SPECIALIZATIONID_SN = "spid";
	
	//CmdhDefEcValue
	public static final String ORDER_SN = "od";
	public static final String DEFECVALUE_SN = "dev";
	public static final String REQUESTORIGIN_SN = "ror";
	public static final String REQUESTCONTEXT_SN = "rct";
	public static final String REQUESTCONTEXTNOTIFICATION_SN = "rctn";
	public static final String REQUESTCHARACTERISTICS_SN = "rch";
	
	//CmdhDefaults
	public static final String MGMTLINK_SN = "cmlk";
	
	//CmdhEcDefParamValues
	public static final String DEFAULTREQUESTEXPTIME_SN = "dqet";
	public static final String DEFAULTRESULTEXPTIME_SN = "dset";
	public static final String DEFAULTOPEXPTIME_SN = "doet";
	public static final String DEFAULTRESPPERSISTENCE_SN = "drp";
	public static final String DEFAULTDELAGGREGATION_SN = "dda";
	
	
	//CmdhLimits
	public static final String LIMITSEVENTCATEGORY_SN = "lec";
	public static final String LIMITSREQUESTEXPTIME_SN = "lqet";
	public static final String LIMITSRESULTEXPTIME_SN = "lset";
	public static final String LIMITSOPEXECTIME_SN = "loet";
	public static final String LIMITSRESPPERSISTENCE_SN = "lrp";
	public static final String LIMITSDELAGGREGATION_SN = "lda";
	
	//CmdhNetworkAccessRules
	public static final String APPLICABLEEVENTCATEGORIES_SN = "aecs";
	
	//CmdhNwAccessRule
	public static final String MINREQVOLUME_SN = "mrv";
	public static final String SPREADINGWAITTIME_SN = "swt";
	public static final String OTHERCONDITIONS_SN = "ohc";
	
	//Container
	public static final String DISABLERETRIEVAL_SN = "disr";
	
	//ContentRef
	public static final String URIREFERENCE_SN = "urir";
	
	//DeletionContexts
	public static final String TIMEOFDAY_SN = "tod";
	public static final String LOCATIONREGIONS_SN = "lr";
	
	//Delivery
	public static final String SOURCE_SN = "sr";
	public static final String TARGET_SN = "tg";
	public static final String LIFESPAN_SN = "Ls";
	public static final String EVENTCAT_SN = "ec";
	public static final String DELIVERYMETADATA_SN = "dmd";
	
	//DeviceCapability
	public static final String CAPABILITYNAME_SN = "can";
	public static final String ATTACHED_SN = "att";
	public static final String CAPABILITYACTIONSTATUS_SN = "cas";
	public static final String DISABLE_SN = "dis";
	public static final String CURRENTSTATE_SN = "cus";
	
	//DeviceInfo
	public static final String DEVICELABEL_SN = "dlb";
	public static final String MANUFACTURER_SN = "man";
	public static final String MODEL_SN = "mod";
	public static final String DEVICETYPE_SN = "dty";
	public static final String FWVERSION_SN = "fwv";
	public static final String SWVERSION_SN = "swv";
	public static final String HWVERSION_SN = "hwv";
	
	//DynAuthDasRequest
	public static final String TARGETEDRESOURCETYPE_SN = "trt";
	public static final String ORIGINATORIP_SN = "oip";
	public static final String IP4ADDRESS_SN = "ip4";
	public static final String IP6ADDRESS_SN = "ip6";
	public static final String ORIGINATORLOCATION_SN = "olo";
	public static final String ORIGINATORROLEIDS_SN = "orid";
	public static final String REQUESTTIMESTAMP_SN = "rts";
	public static final String TARGETEDRESOURCEID_SN = "trid";
	public static final String PROPOSEDPRIVILEGESLIFETIME_SN = "ppl";
	public static final String ROLEIDSFROMACPS_SN = "rfa";
	public static final String TOKENIDS_SN = "tids";
	
	//DynAuthDasResponse
	public static final String DYNAMICACPINFO_SN = "dai";
	public static final String GRANTEDPRIVILEGES_SN = "gp";
	public static final String PRIVILEGESLIFESPAN_SN = "pl";
	public static final String TOKENS_SN = "tkns";
	
	//DynAuthLocalTokenIdAssignments
	public static final String LOCALTOKENIDASSIGNMENT_SN = "ltia";
	public static final String LOCALTOKENID_SN = "lti";
	
	//DynAuthTokenReqInfo
	public static final String DASINFO_SN = "dasi";
	public static final String DASREQUEST_SN = "daq";
	public static final String SECUREDDASREQUEST_SN = "sdr";
	
	//DynamicAuthorizationConsultation
	public static final String DYNAMICAUTHORIZATIONENABLED_SN = "dae";
	public static final String DYNAMICAUTHORIZATIONPOA_SN = "dap";
	public static final String DYNAMICAUTHORIZATIONLIFETIME_SN = "dal";
	
	//EventConfig
	public static final String EVENTID_SN = "evi";
	public static final String EVENTTYPE_SN = "evt";
	public static final String EVENTSTART_SN = "evs";
	public static final String EVENTEND_SN = "eve";
	public static final String OPERATIONTYPE_SN = "opt";
	public static final String DATASIZE_SN = "ds";
	
	//EventLog
	public static final String LOGTYPEID_SN = "lgt";
	public static final String LOGDATA_SN = "lgd";
	public static final String LOGSTATUS_SN = "lgst";
	public static final String LOGSTART_SN = "lga";
	public static final String LOGSTOP_SN = "lgo";
	
	//EventNotificationCriteria
	public static final String MISSINGDATA_SN = "md";
	
	//ExecInstance
	public static final String EXECSTATUS_SN = "exs";
	public static final String EXECRESULT_SN = "exr";
	public static final String EXECDISABLE_SN = "exd";
	public static final String EXECTARGET_SN = "ext";
	public static final String EXECMODE_SN = "exm";
	public static final String EXECFREQUENCY_SN = "exf";
	public static final String EXECDELAY_SN = "exy";
	public static final String EXECNUMBER_SN = "exn";
	public static final String EXECREQARGS_SN = "exra";
	
	//FilterCriteria
	public static final String SEMANTICSFILTER_SN = "smf";
	public static final String FILTEROPERATION_SN = "fo";
	public static final String CONTENTFILTERSYNTAX_SN = "cfs";
	public static final String CONTENTFILTERQUERY_SN = "cfq";
	public static final String LEVEL_SN = "lvl";
	public static final String OFFSET_SN = "ofst";
	
	//Firmware
	public static final String UPDATE_SN = "ud";
	public static final String UPDATESTATUS_SN = "uds";
	
	//Memory
	public static final String MEMAVAILABLE_SN = "mma";
	public static final String MEMTOTAL_SN = "mmt";
	
	//MetaInformation
	public static final String ORIGINATINGTIMESTAMP_SN = "ot";
	public static final String REQUESTEXPIRATIONTIMESTAMP_SN = "rqet";
	public static final String RESULTEXPIRATIONTIMESTAMP_SN = "rset";
	public static final String OPERATIONEXECUTIONTIME_SN = "oet";
	public static final String RESPONSETYPE_SN = "rt";
	public static final String RESULTPERSISTENCE_SN = "rp";
	public static final String RESULTCONTENT_SN = "rcn";
	public static final String EVENTCATEGORY_SN = "ec";
	public static final String DELIVERYAGGREGATION_SN = "da";
	public static final String GROUPREQUESTIDENTIFIER_SN = "gid";
	public static final String FILTERCRITERIA_SN = "fc";
	public static final String DISCOVERYRESULTTYPE_SN = "drt";
	
	//MgmtCmd
	public static final String CMDTYPE_SN = "cmt";
	public static final String EXECENABLE_SN = "exe";
	
	//Notification
	public static final String IPEDISCOVERYREQUEST_SN = "idr";
	
	//NotificationTargetMgmtPolicyRef
	public static final String NOTIFICATIONTARGETURI_SN = "ntu";
	public static final String NOTIFICATIONPOLICYID_SN = "npi";
	
	//NotificationTargetPolicy
	public static final String POLICYLABEL_SN = "plbl";
	public static final String RULESRELATIONSHIP_SN = "rrs";
	
	//PolicyDeletionRules
	public static final String DELETIONRULES_SN = "dr";
	public static final String DELETIONRULESRELATION_SN = "drr";
	
	//SecurityInfo
	public static final String SECURITYINFOTYPE_SN = "sit";
	public static final String DASRESPONSE_SN = "dres";
	public static final String ESPRIMRANDOBJECT_SN = "ero";
	public static final String ESPRIMOBJECT_SN = "epo";
	public static final String ESCERTKEMESSAGE_SN = "eckm";
	
	//OperationResult
	public static final String RESPONSESTATUSCODE_SN = "rsc";
	public static final String CURRENTSTATUS_SN = "cnst";
	public static final String CURRENTOFFSET_SN = "cnot";
	
	//Reboot
	public static final String FACTORYRESET_SN = "far";
	
	//RemoteCSE
	public static final String M2MEXTID_SN = "mei";
	public static final String TRIGGERRECIPIENTID_SN = "tri";
	public static final String TRIGGERREFERENCENUMBER_SN = "trn";
	
	//Request
	public static final String REQUESTID_SN = "rid";
	public static final String REQUESTSTATUS_SN = "rid";
	public static final String OPERATIONRESULT_SN = "ors";
	
	//RequestPrimitive
	public static final String LOCALTOKENIDS_SN = "ltids";
	public static final String TOKENREQINDICATOR_SN = "tqi";
	
	//ResponsePrimitive
	public static final String CONTENTSTATUS_SN = "cnst";
	public static final String CONTENTOFFSET_SN = "cnot";
	public static final String ASSIGNEDTOKENIDENTIFIERS_SN = "ati";
	public static final String TOKENREQINFO_SN = "tqf";
	
	//ServiceSubscribedAppRule
	public static final String ALLOWEDAPPIDS_SN = "aai";
	public static final String ALLOWEDAES_SN = "aae";
	public static final String DEVICEIDENTIFIER_SN = "di";
	public static final String RULELINKS_SN = "rlk";
	
	//Software
	public static final String INSTALL_SN = "in";
	public static final String UNINSTALL_SN = "un";
	public static final String INSTALLSTATUS_SN = "ins";
	public static final String ACTIVATE_SN = "act";
	public static final String DEACTIVATE_SN = "dea";
	public static final String ACTIVESTATUS_SN = "acts";
	
	//StatsCollect
	public static final String STATSCOLLECTID_SN = "sci";
	public static final String COLLECTINGENTITYID_SN = "cei";
	public static final String COLLECTEDENTITYID_SN = "cdi";
	public static final String STATSRULESTATUS_SN = "ss";
	public static final String STATMODEL_SN = "sm";
	public static final String COLLECTPERIOD_SN = "cp";
	
	//Subscription
	public static final String EVENTNOTIFICATIONCRITERIA_SN = "enc";
	public static final String EXPIRATIONCOUNTER_SN = "exc";
	public static final String BATCHNOTIFY_SN = "bn";
	public static final String RATELIMIT_SN = "rl";
	public static final String PRESUBSCRIPTIONNOTIFY_SN = "psn";
	public static final String PENDINGNOTIFICATION_SN = "pn";
	public static final String NOTIFICATIONSTORAGEPRIORITY_SN = "nsp";
	public static final String LATESTNOTIFY_SN = "ln";
	public static final String NOTIFICATIONCONTENTTYPE_SN = "nct";
	public static final String NOTIFICATIONEVENTCAT_SN = "nec";
	public static final String SUBSCRIBERURI_SN = "su";
	
	
	/******************************************************************************************
	 * User Defined Name	
	 */
	public static final String URI_KEY = "_uri";
	public static final String RESID_KEY = RESOURCEID_SN;
	/******************************************************************************************/
	
	
}
