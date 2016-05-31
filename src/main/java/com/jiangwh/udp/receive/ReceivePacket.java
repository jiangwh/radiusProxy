package com.jiangwh.udp.receive;

import java.net.SocketAddress;

import com.jiangwh.entity.Packet;

public interface ReceivePacket {

	Packet receive(SocketAddress address, byte[] data, String dst, int dstPort);

	Packet packagePacket(Packet packet);

	void sumbit(Packet packet);

	void work(SocketAddress address, byte[] data, String dst, int dstPort);
}
