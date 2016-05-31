package com.jiangwh.udp.receive.impl;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiangwh.entity.Packet;
import com.jiangwh.udp.send.SendFacade;

@Component("receivePacket")
public class ReceivePacketImpl extends ReceivePacketAbstract {

	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(ReceivePacketImpl.class);

	@Autowired
	SendFacade facade;

	public Packet receive(SocketAddress address, byte[] data, String dst,
			int dstPort) {		
		Packet packet = new Packet();
		packet.setRequestData(data);		
		InetSocketAddress inetSocketAddress = (InetSocketAddress) address;
		packet.setSrc(inetSocketAddress.getAddress().getHostAddress());
		packet.setSrcPort(inetSocketAddress.getPort());
		packet.setDst(dst);
		packet.setDstPort(dstPort);
		return packet;
	}

	public Packet packagePacket(Packet packet) {
		
		return packet;
	}

	public void sumbit(Packet packet) {
		
		facade.submit(packet);
	}
}
