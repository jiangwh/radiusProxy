package com.jiangwh.udp.send.impl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jiangwh.constan.Constan;
import com.jiangwh.entity.Packet;
import com.jiangwh.entity.ShareContainer;
import com.jiangwh.udp.filter.PacketFilterFacade;

@Component("sendTask")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SendTask implements Runnable {

	private Logger logger = Logger.getLogger(SendTask.class);

	@Autowired
	ShareContainer container;
	
	@Resource(name="packetFilters")
	List<PacketFilterFacade> packetFilters;
	
	private DatagramSocket socket = null;
	
	public void run() {
		logger.info("start thread " + Thread.currentThread().getName());
		try {
			if (null == socket) {
				socket = new DatagramSocket();
				socket.setSoTimeout(Constan.SOCK_TIMEOUT);
			}
			while (ExecutorImpl.runFlag.get()) {
				try {
					Packet packet = container.getBlockingQueue().take();
					if(null!=packetFilters){
						for (PacketFilterFacade facade : packetFilters) {
							facade.handler(packet);
						}
					}
					DatagramPacket outPacket = new DatagramPacket(packet.getRequestData(),packet.getRequestDataLength(),InetAddress.getByName(packet.getDst()), packet.getDstPort());
					byte[] buffer = new byte[Constan.PACKET_MAX_LENGTH];
					DatagramPacket inPacket = new DatagramPacket(buffer,buffer.length);
					logger.info("send addr:"+outPacket.getSocketAddress());
					socket.send(outPacket);
					socket.receive(inPacket);
					inPacket.setAddress(InetAddress.getByName(packet.getSrc()));
					inPacket.setPort(packet.getSrcPort());
					socket.send(inPacket);
					buffer=null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} catch (SocketException e) {
			e.printStackTrace();
		} finally {
			try {
				ExecutorImpl.countDownLatch.countDown();
			} catch (Exception exception) {}
			if(null==socket){
				if(socket.isClosed()){
					socket.close();
				}
				socket=null;
			}
			logger.error(Thread.currentThread().getName()+" exit ... ");
		}

	}
}
