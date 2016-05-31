package com.jiangwh.entity;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("shareContainer")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ShareContainer {

	
	private BlockingQueue<Packet> packetSendQueue = new LinkedBlockingQueue<Packet>(50000);
	private BlockingQueue<Packet> packetFilterQueue = new LinkedBlockingQueue<Packet>(50000);

	public BlockingQueue<Packet> getBlockingQueue() {
		return packetSendQueue;
	}

	public BlockingQueue<Packet> getPacketFilterQueue() {
		return packetFilterQueue;
	}

}
