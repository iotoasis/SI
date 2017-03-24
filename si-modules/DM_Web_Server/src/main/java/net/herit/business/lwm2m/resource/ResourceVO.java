package net.herit.business.lwm2m.resource;

import java.sql.Timestamp;

public class ResourceVO {
	private String resource_uri;
	private String profile_ver = "1.0";
	private String data_type;
	private String unit;
	private int noti_type = 0;
	private String operation;
	private int device_model_id = 97;
	private String is_diagnostic = "N";
	private String is_mandatory;
	private String display_name;
	private String is_multiple;
	private String is_historical = "N";
	private String is_error = "N";
	private String default_value;
	private String description;
	private String is_display = "Y";
	private String display_type;
	public String getResource_uri() {
		return resource_uri;
	}
	public void setResource_uri(String resource_uri) {
		this.resource_uri = resource_uri;
	}
	public String getProfile_ver() {
		return profile_ver;
	}
	public void setProfile_ver(String profile_ver) {
		this.profile_ver = profile_ver;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getNoti_type() {
		return noti_type;
	}
	public void setNoti_type(int noti_type) {
		this.noti_type = noti_type;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public int getDevice_model_id() {
		return device_model_id;
	}
	public void setDevice_model_id(int device_model_id) {
		this.device_model_id = device_model_id;
	}
	public String getIs_diagnostic() {
		return is_diagnostic;
	}
	public void setIs_diagnostic(String is_diagnostic) {
		this.is_diagnostic = is_diagnostic;
	}
	public String getIs_mandatory() {
		return is_mandatory;
	}
	public void setIs_mandatory(String is_mandatory) {
		this.is_mandatory = is_mandatory;
	}
	public String getDisplay_name() {
		return display_name;
	}
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}
	public String getIs_multiple() {
		return is_multiple;
	}
	public void setIs_multiple(String is_multiple) {
		this.is_multiple = is_multiple;
	}
	public String getIs_historical() {
		return is_historical;
	}
	public void setIs_historical(String is_historical) {
		this.is_historical = is_historical;
	}
	public String getIs_error() {
		return is_error;
	}
	public void setIs_error(String is_error) {
		this.is_error = is_error;
	}
	public String getDefault_value() {
		return default_value;
	}
	public void setDefault_value(String default_value) {
		this.default_value = default_value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIs_display() {
		return is_display;
	}
	public void setIs_display(String is_display) {
		this.is_display = is_display;
	}
	public String getDisplay_type() {
		return display_type;
	}
	public void setDisplay_type(String display_type) {
		this.display_type = display_type;
	}
	
	
	
}
