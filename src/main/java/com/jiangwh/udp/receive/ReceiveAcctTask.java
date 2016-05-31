package com.jiangwh.udp.receive;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.jiangwh.constan.Constan;
import com.jiangwh.entity.ConfigBean;

@Component("receiveAcct")
public class ReceiveAcctTask implements Runnable{

	public Logger logger = Logger.getLogger(ReceiveAuthTask.class);
	private DatagramSocket channel;
	
	@Resource
	ConfigBean bean;
	
	@Resource
	ReceivePacket receivePacket;

	public void run() {
		logger.info("start ReceiveAcctTask  " + Thread.currentThread().getName());
		try {
			if (null == channel) {
				InetAddress addr = InetAddress.getByName("10.10.156.101");
				channel = new DatagramSocket(bean.getBindAcctPort(),addr);
			}
			byte[] dst = new byte[Constan.PACKET_MAX_LENGTH];
			while (true) {
				DatagramPacket datagramPacket = new DatagramPacket(dst,Constan.PACKET_MAX_LENGTH);
				channel.receive(datagramPacket);
				ByteBuffer buffer = ByteBuffer.allocate(datagramPacket.getLength());
				buffer.put(dst, 0, buffer.capacity());
				SocketAddress address = datagramPacket.getSocketAddress();
				receivePacket.work(address, datagramPacket.getData(),bean.getTargetAddr(), bean.getBindAcctPort());
				dst = new byte[Constan.PACKET_MAX_LENGTH];
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		} finally {
			if (null != channel) {
				if (!channel.isClosed()) {
					channel.close();
				}
				channel = null;
			}
		}
	}
}
