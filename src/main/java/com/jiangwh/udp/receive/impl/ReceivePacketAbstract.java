package com.jiangwh.udp.receive.impl;

import java.net.SocketAddress;

import com.jiangwh.udp.receive.ReceivePacket;

public abstract class ReceivePacketAbstract implements ReceivePacket {

		
	
	public void work(SocketAddress address, byte[] data,String dst,int dstPort) {
		
		sumbit(packagePacket(receive(address, data, dst, dstPort)));
	}

}
