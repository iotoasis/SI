package org.eclipse.leshan.server.extension.onem2m.resources;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.eclipse.leshan.server.extension.Constants;
import org.eclipse.leshan.server.extension.Util;
import org.json.JSONObject;

public class Resource {
	
	// lbl
	public void resetLbl(){
		lbl = new ArrayList<String>();
	}
	public void addLbl(String txt){
		lbl.add(txt);
	}
	
	// nu
	public void resetNu(){
		nu = new ArrayList<String>();
	}
	public void addNu(String txt){
		nu.add(txt);
	}

	private String strEndTime;
	private Date today = new Date();
	public void setExpired(){
		Date endtime = new Date(today.getTime() + (1000L * 60L * 60L * 24L * Constants.DAYS_FOR_EXPIRED)); // after one year
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd HHmmss");
        String tmpStr = sf.format(endtime);
        String[] arrDateItem = tmpStr.split(" ");
        strEndTime = arrDateItem[0] + "T" + arrDateItem[1];
        setEt(strEndTime);
	}
	
	public String getNameSpace(Resource res){
		StringBuffer sb = new StringBuffer();
		if(!Util.isNoE(Constants.BASIC_NAME_SPACE)){
			sb.append(Constants.BASIC_NAME_SPACE);
			sb.append(":");
		}
		sb.append(res.getShortName());
		
		return sb.toString();
	}
		
	// special variables
	private String shortName;
	private JSONObject resToJson;
	
	// variables
	private boolean rr;
	
	private int mbs;
	private int mni;
	private int mia;
	
	private String api;
	private String apn;
	private String cnf;
	private String con;
	private String et;
	private String rn;
	private String dcrp;
	private String dsp;
	
	private HashMap<String, Object> enc;
		
	private ArrayList<Integer> rss;
	
	private ArrayList<String> lbl;
	private ArrayList<String> nu;
	private ArrayList<String> poa;
	
	
	// getter & setter
	public boolean isRr() {
		return rr;
	}
	public void setRr(boolean rr) {
		this.rr = rr;
	}
	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}
	public String getApn() {
		return apn;
	}
	public void setApn(String apn) {
		this.apn = apn;
	}
	public String getEt() {
		return et;
	}
	public void setEt(String et) {
		this.et = et;
	}
	public String getRn() {
		return rn;
	}
	public void setRn(String rn) {
		this.rn = rn;
	}
	public ArrayList<String> getPoa() {
		return poa;
	}
	public void setPoa(ArrayList<String> poa) {
		this.poa = poa;
	}
	public ArrayList<String> getLbl() {
		return lbl;
	}
	public void setLbl(ArrayList<String> lbl) {
		this.lbl = lbl;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public JSONObject getResToJson() {
		return resToJson;
	}
	public void setResToJson(JSONObject resToJson) {
		this.resToJson = resToJson;
	}
	public int getMni() {
		return mni;
	}
	public void setMni(int mni) {
		this.mni = mni;
	}
	public int getMbs() {
		return mbs;
	}
	public void setMbs(int mbs) {
		this.mbs = mbs;
	}
	public int getMia() {
		return mia;
	}
	public void setMia(int mia) {
		this.mia = mia;
	}
	public String getCnf() {
		return cnf;
	}
	public void setCnf(String cnf) {
		this.cnf = cnf;
	}
	public String getCon() {
		return con;
	}
	public void setCon(String con) {
		this.con = con;
	}
	public HashMap<String, Object> getEnc() {
		return enc;
	}
	public void setEnc(HashMap<String, Object> enc) {
		this.enc = enc;
	}
	public ArrayList<Integer> getRss() {
		return rss;
	}
	public void setRss(ArrayList<Integer> rss) {
		this.rss = rss;
	}
	public ArrayList<String> getNu() {
		return nu;
	}
	public void setNu(ArrayList<String> nu) {
		this.nu = nu;
	}
	public String getDcrp() {
		return dcrp;
	}
	public void setDcrp(String dcrp) {
		this.dcrp = dcrp;
	}
	public String getDsp() {
		return dsp;
	}
	public void setDsp(String dsp) {
		this.dsp = dsp;
	}
	
	
	
	
	
	
	
}
