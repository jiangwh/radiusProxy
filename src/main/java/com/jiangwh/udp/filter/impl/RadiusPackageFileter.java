package com.jiangwh.udp.filter.impl;

import org.apache.log4j.Logger;

import com.jiangwh.entity.Packet;
import com.jiangwh.udp.filter.PacketFilterFacade;

public class RadiusPackageFileter implements PacketFilterFacade {
	
	private Logger logger = Logger.getLogger(RadiusPackageFileter.class);

	public void handler(Packet packet) {
		if (logger.isInfoEnabled()) {
			logger.info(this);
		}
	}
}
