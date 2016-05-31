package com.jiangwh.udp.send;

import com.jiangwh.entity.Packet;

public interface SendFacade {
	void submit(Packet packet);
}
