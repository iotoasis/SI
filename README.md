![iotoasis](https://github.com/iotoasis/SO/blob/master/logo_oasis_m.png)


## Oasis SI oneM2M Server

SI oneM2M Server is a server framework (IN-CSE) that supports interworking of devices and applications based on oneM2M, IoT international standard. Using SI oneM2M Server source code, you can build oneM2M-based device and application interworking server, and you can develop various oneM2M components such as AE, MN-CSE using oneM2M core source code.

This is IoT core infrastructure for various application services through the connection with SO & SDA platform as an IoT server platform supports various Bindings, IN-CSE Capability functions, DM server function for device management and interworking with heterogeneous IoT platform such as OIC/LWM2M/Fi-WARE based on oneM2M Release 2



 - Feature
   -	IN-CSE functions based on oneM2M Release 2.0
   -	Resource processing based on Release 2.0(TS-0004 2.7)
   -	Supports Mca, Mcc, Mca' reference points
   -	IN-CSE functions : Registration, Data Management Repository, Sub. & Notification, Discovery etc.
   -	Protocol Bindings : HTTP, MQTT, CoAP, WebSocket
   -	Serialization : XML, JSON, CBOR
   -	Address format : CSE-relative/SP-relative/Absolute  , Hierarchical/Non-Hierarchical Addressing
   -	Semantic Descriptor support
   -	Interworking IPE function : OIC(OCF), LWM2M
   -	Device Management  : mgmtObj, mgmtCmd 
   -	Security : TLS, Creator default ACP, Basic Authentication
   -	Resource data management based on MongoDB



## Downloads
 - [Latest Release](https://github.com/iotoasis/SI/releases/)




## Documents
 - SI Server User Guide
     https://github.com/iotoasis/SI/tree/master/si-user-guide/SI_Server




## Modules
Applications you can try to test for SI oneM2M Server.

- **DM Web Server** : The web module for device control.
  - **Source Path** : /si-modules/DM_Web_Server
  - **User Guide** : /si-user-guide/DM_Web_Server
  - **DB query script** : /si-onem2m-res
  
- **LWM2M DM/IPE** : The module support management of LwM2M devices and interworking with SI oneM2M Server through IPE Server.
  - **Source Path1** : /si-modules/LWM2M_IPE_Server
  - **Source Path2** : /si-modules/LWM2M_IPE_Client
  - **User Guide** : /si-user-guide/LWM2M_DM_IPE
  
- **OIC IPE** : The module based on OIC that support Device Handling and interworking with SI oneM2M Server through IPE Server.
  - **Source Path1** : /si-modules/OIC-IPE
  - **Source Path2** : /si-modules/OIC-IPE-Client
  - **User Guide** : /si-user-guide/OIC_IPE
  



## Q&A
 - [IoT Oasis Q&A -- Coming soon]




## License
Licensed under the BSD License, Version 2.0

