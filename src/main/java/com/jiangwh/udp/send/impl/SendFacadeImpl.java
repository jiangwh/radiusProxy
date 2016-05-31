package com.jiangwh.udp.send.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiangwh.entity.Packet;
import com.jiangwh.entity.ShareContainer;
import com.jiangwh.udp.send.SendFacade;

@Component("sendFacde")
public class SendFacadeImpl implements SendFacade {

	@Autowired
	ShareContainer container;

	public void submit(Packet packet) {
		container.getBlockingQueue().add(packet);
	}

}
