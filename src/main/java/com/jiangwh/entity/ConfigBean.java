package com.jiangwh.entity;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Configurable
@Component("configBean")
public class ConfigBean {

	@Value("#{settings['bean.bindAuthPort']}")
	String bindAuthPort;
	@Value("#{settings['bean.bindAcctPort']}")
	String bindAcctPort;
	@Value("#{settings['bean.targetAddr']}")
	String targetAddr;
	@Value("#{settings['bean.sendGroupSize']}")
	String sendGroupSize;
	@Value("#{settings['bean.filterGroupSize']}")
	String filterGroupSize;
	@Value("#{settings['bean.secondTargetAddr']}")
	String sencondTargetAddr;

	public int getBindAuthPort() {
		try {
			return Integer.parseInt(this.bindAuthPort);
		} catch (Exception e) {
			return 1812;
		}

	}

	public int getBindAcctPort() {
		try {
			return Integer.parseInt(this.bindAcctPort);
		} catch (Exception e) {
			return 1813;
		}
	}

	public void setSencondTargetAddr(String sencondTargetAddr) {
		this.sencondTargetAddr = sencondTargetAddr;
	}

	public String getSencondTargetAddr() {
		return sencondTargetAddr;
	}

	public String getTargetAddr() {
		return this.targetAddr;
	}

	public int getSendGroupSize() {
		try {
			return Integer.parseInt(this.sendGroupSize);
		} catch (Exception e) {
			return 5;
		}
	}

	public int getFilterGroupSize() {
		try {
			return Integer.parseInt(this.filterGroupSize);
		} catch (Exception e) {
			return 5;
		}
	}

	public void setAuthPort(String bindAuthPort) {
		this.bindAuthPort = bindAuthPort;
	}

	public void setAcctPort(String bindAcctPort) {
		this.bindAcctPort = bindAcctPort;
	}

	public void setAddr(String targetAddr) {
		this.targetAddr = targetAddr;
	}

	public void setSendSize(String sendGroupSize) {
		this.sendGroupSize = sendGroupSize;
	}

	public void setFilterSize(String filterGroupSize) {
		this.filterGroupSize = filterGroupSize;
	}

}
