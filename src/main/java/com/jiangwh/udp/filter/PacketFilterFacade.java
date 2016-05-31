package com.jiangwh.udp.filter;

import com.jiangwh.entity.Packet;

public interface PacketFilterFacade {

	void handler(Packet packet);
	
}
