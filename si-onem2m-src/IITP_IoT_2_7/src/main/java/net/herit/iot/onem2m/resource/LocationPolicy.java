//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.06 at 03:14:07 오후 KST 
//


package net.herit.iot.onem2m.resource;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.Duration;

import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.util.OneM2MException;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.onem2m.org/xml/protocols}announceableResource">
 *       &lt;sequence>
 *         &lt;element name="locationSource" type="{http://www.onem2m.org/xml/protocols}locationSource"/>
 *         &lt;element name="locationUpdatePeriod" type="{http://www.w3.org/2001/XMLSchema}duration" minOccurs="0"/>
 *         &lt;element name="locationTargetID" type="{http://www.onem2m.org/xml/protocols}nodeID" minOccurs="0"/>
 *         &lt;element name="locationServer" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="locationContainerID" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="locationContainerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="locationStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="childResource" type="{http://www.onem2m.org/xml/protocols}childResourceRef" maxOccurs="unbounded"/>
 *           &lt;choice maxOccurs="unbounded">
 *             &lt;element ref="{http://www.onem2m.org/xml/protocols}subscription"/>
 *           &lt;/choice>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "locationSource",
    "locationUpdatePeriod",
    "locationTargetID",
    "locationServer",
    "locationContainerID",
    "locationContainerName",
    "locationStatus",
    "childResource",
    "subscription"
})
//@XmlRootElement(name = "locationPolicy")
@XmlRootElement(name = "m2m:" + Naming.LOCATIONPOLICY_SN)
public class LocationPolicy
    extends AnnounceableResource
{

//	public final static String SCHEMA_LOCATION = "CDT-locationPolicy-v1_2_0.xsd";
//	public final static String SCHEMA_LOCATION = "CDT-locationPolicy-v1_6_0.xsd";
	public final static String SCHEMA_LOCATION = "CDT-locationPolicy-v2_7_0.xsd";
	
    //@XmlElement(required = true)
    @XmlElement(name = Naming.LOCATIONSOURCE_SN)
    protected Integer locationSource;
    @XmlElement(name = Naming.LOCATIONUPDATEPERIOD_SN)
    @XmlList
    protected List<Duration> locationUpdatePeriod;		// Data type changed from Duration to List<Duration> in CDT-2.7.0
    @XmlElement(name = Naming.LOCATIONTARGETID_SN)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String locationTargetID;
    @XmlElement(name = Naming.LOCATIONSERVER_SN)
    @XmlSchemaType(name = "anyURI")
    protected String locationServer;
    @XmlElement(name = Naming.LOCATIONCONTAINERID_SN)
    @XmlSchemaType(name = "anyURI")
    protected String locationContainerID;
    @XmlElement(name = Naming.LOCATIONCONTAINERNAME_SN)
    protected String locationContainerName;
    //@XmlElement(required = true)
    @XmlElement(name = Naming.LOCATIONSTATUS_SN)
    protected String locationStatus;
    @XmlElement(name = Naming.CHILDRESOURCE_SN)
    protected List<ChildResourceRef> childResource;
    @XmlElement(name = Naming.SUBSCRIPTION_SN, namespace = "http://www.onem2m.org/xml/protocols")
    protected List<Subscription> subscription;

    /**
     * Gets the value of the locationSource property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLocationSource() {
        return locationSource;
    }

    /**
     * Sets the value of the locationSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLocationSource(Integer value) {
        this.locationSource = value;
    }

    /**
     * Gets the value of the locationUpdatePeriod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the locationUpdatePeriod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocationUpdatePeriod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Duration }
     * 
     * 
     */
    public List<Duration> getLocationUpdatePeriod() {
      //  if (locationUpdatePeriod == null) {
      //      locationUpdatePeriod = new ArrayList<Duration>();
      //  }
        return this.locationUpdatePeriod;
    }
    
    public void addLocationUpdatePeriod(Duration dur) {
    	if (locationUpdatePeriod == null) {
    		locationUpdatePeriod = new ArrayList<Duration>();
    	}
    	this.locationUpdatePeriod.add(dur);
    }

    /**
     * Gets the value of the locationTargetID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationTargetID() {
        return locationTargetID;
    }

    /**
     * Sets the value of the locationTargetID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationTargetID(String value) {
        this.locationTargetID = value;
    }

    /**
     * Gets the value of the locationServer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationServer() {
        return locationServer;
    }

    /**
     * Sets the value of the locationServer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationServer(String value) {
        this.locationServer = value;
    }

    /**
     * Gets the value of the locationContainerID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationContainerID() {
        return locationContainerID;
    }

    /**
     * Sets the value of the locationContainerID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationContainerID(String value) {
        this.locationContainerID = value;
    }

    /**
     * Gets the value of the locationContainerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationContainerName() {
        return locationContainerName;
    }

    /**
     * Sets the value of the locationContainerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationContainerName(String value) {
        this.locationContainerName = value;
    }

    /**
     * Gets the value of the locationStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationStatus() {
        return locationStatus;
    }

    /**
     * Sets the value of the locationStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationStatus(String value) {
        this.locationStatus = value;
    }

    /**
     * Gets the value of the childResource property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the childResource property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getChildResource().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ChildResourceRef }
     * 
     * 
     */
    public List<ChildResourceRef> getChildResource() {
//    	if (childResource == null) {
//    		childResource = new ArrayList<ChildResourceRef>();
//    	}
    	return this.childResource;
    }
    
    public void addChildResource(ChildResourceRef ch) {
    	if (childResource == null) {
    		childResource = new ArrayList<ChildResourceRef>();
    	}
    	this.childResource.add(ch);
    }


    /**
     * Gets the value of the subscription property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subscription property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubscription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Subscription }
     * 
     * 
     */
    public List<Subscription> getSubscription() {
//    	if (subscription == null) {
//    		subscription = new ArrayList<Subscription>();
//    	}
    	return this.subscription;
    }

    public void addSubscription(Subscription sub) {
    	if (subscription == null) {
    		subscription = new ArrayList<Subscription>();
    	}
    	this.subscription.add(sub);
    }
    
    @Override
	public void validate(OPERATION operation) throws OneM2MException {
		
		if (operation.equals(OPERATION.CREATE)) {	// create 요청에 의해 생성된 리소스에 대한 유효성 체크 (DB저장전)
			if (this.locationSource == null) {
				throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, "'locationSource' is M on CREATE operation");
			}
			if (this.locationContainerID != null) {
				throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, "'locationContainerID' is NP on CREATE operation");
			}
			if (this.locationStatus != null) {
				throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, "'locationStatus' is NP on CREATE operation");
			}
		} else if (operation.equals(OPERATION.UPDATE)) {	// update 요청에 의해 생성된 리소스에 대한 유효성 체크 (DB저장전)
			if (this.locationSource != null) {
				throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, "'locationSource' is NP on UPDATE operation");
			}
			if (this.locationTargetID != null) {
				throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, "'locationTargetID' is NP on UPDATE operation");
			}
			if (this.locationServer != null) {
				throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, "'locationServer' is NP on UPDATE operation");
			}
			if (this.locationContainerID != null) {
				throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, "'locationContainerID' is NP on UPDATE operation");
			}
			if (this.locationStatus != null) {
				throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, "'locationStatus' is NP on UPDATE operation");
			}	
		}
	
		super.validate(operation);
		
	}


}