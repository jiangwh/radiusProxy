package com.jiangwh.entity;

public class Packet {

	private String src;
	private int srcPort;
	private String dst;
	private int dstPort;
	private byte[] requestData;
	private byte[] responseData;
	
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getDst() {
		return dst;
	}
	public void setDst(String dst) {
		this.dst = dst;
	}
	public byte[] getRequestData() {
		return requestData;
	}
	public void setRequestData(byte[] requestData) {
		this.requestData = requestData;
	}
	public byte[] getResponseData() {
		return responseData;
	}
	public void setResponseData(byte[] responseData) {
		this.responseData = responseData;
	}
	public int getDstPort() {
		return dstPort;
	}
	public void setDstPort(int dstPort) {
		this.dstPort = dstPort;
	}
	public int getSrcPort() {
		return srcPort;
	}
	public void setSrcPort(int srcPort) {
		this.srcPort = srcPort;
	}
	
	public int getRequestDataLength(){
		return this.requestData.length;
	}
	public int getResponseDataLength(){
		return this.responseData.length;
	}
}