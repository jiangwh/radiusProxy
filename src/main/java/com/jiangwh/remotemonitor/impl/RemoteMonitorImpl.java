package com.jiangwh.remotemonitor.impl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.jiangwh.constan.Constan;
import com.jiangwh.entity.ConfigBean;
import com.jiangwh.remotemonitor.RemoteMonitor;
import com.jiangwh.remotemonitor.radius.AVP;
import com.jiangwh.remotemonitor.radius.RadiusAVP;
import com.jiangwh.remotemonitor.radius.RadiusPacket;
import com.jiangwh.remotemonitor.radius.RadiusVendorAVP;
import com.jiangwh.remotemonitor.radius.TypeConverter;
import com.jiangwh.remotemonitor.security.RadiusSecurity;

@Component("remoteMonitor")
@Lazy(false)
public class RemoteMonitorImpl implements RemoteMonitor {

	private final String CHECKUSR_NAS_KEY = "key";
	private DatagramSocket datagramSocket = null;
	private static Map<String, String> paramMap = new HashMap<String, String>();

	private Logger logger = Logger.getLogger(RemoteMonitorImpl.class);
	private AtomicInteger count = new AtomicInteger(0);

	private long updateTimeInMillis = Calendar.getInstance().getTimeInMillis();
	
	@Autowired
	ConfigBean bean;

	@PostConstruct
	public void init() {
		paramMap.put("userName", "20130703005");
		paramMap.put("password", "lihongli");
		try {
			paramMap.put("nasIp", Inet4Address.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		paramMap.put("nasPort", "1");
//		start();
	}

	public boolean monitor() {
		return this.monitor(null, 0);
	}

	public boolean monitor(String ip, int port) {
		if (null == datagramSocket) {
			try {
				datagramSocket = new DatagramSocket();
				datagramSocket.setSoTimeout(Constan.SOCK_TIMEOUT);
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
		InetAddress dst = null;
		try {
			if (null == ip) {
				dst = InetAddress.getByName(bean.getTargetAddr());
			} else {
				dst = InetAddress.getByName(ip);
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		RadiusPacket packet = generatePapPacket();
		DatagramPacket datagramPacket = new DatagramPacket(packet.getData(),
				packet.getData().length, dst,
				0 == port ? bean.getBindAuthPort() : port);
		DatagramPacket packetIn = new DatagramPacket(
				new byte[Constan.PACKET_MAX_LENGTH], Constan.PACKET_MAX_LENGTH);
		try {
			if (logger.isInfoEnabled()) {
				logger.info("send monitor message "	+ datagramPacket.getSocketAddress());
			}
			datagramSocket.send(datagramPacket);
			datagramSocket.receive(packetIn);
			DataInputStream byteData = new DataInputStream(new ByteArrayInputStream(packetIn.getData()));
			int code = byteData.readUnsignedByte();
			if (code == 2) {
				return true;
			} else if (code == 3) {
				return false;
			} else {
				return false;
			}
		} catch (IOException e) {
			if (null != datagramSocket) {
				if (!datagramSocket.isClosed()) {
					datagramSocket.close();
				}
				datagramSocket = null;
			}

			return false;
		}
	}

	private RadiusPacket generatePapPacket() {
		RadiusPacket packet = new RadiusPacket();
		packet.setCode(Constan.Radius_ACCESS_REQUEST); // access-request
		packet.setIdentifier(generateByte(1)[0]);
		packet.setAuthenticator(generateByte(16));
		List<AVP> list = new ArrayList<AVP>();
		generateAttr(list, packet);
		packet.setAttr(list);
		short length = 0;
		for (AVP avp : list) {
			length += avp.getData().length;
		}
		packet.setAvpLength(length);
		packet.setPacketLength(TypeConverter.shortToBytes((short) (1 + 1 + 2 + 16 + length)));
		return packet;
	}

	private void generateAttr(List<AVP> avps, RadiusPacket packet) {

		RadiusAVP nameAvp = new RadiusAVP();
		nameAvp.setT(Constan.RADIUS_ATTR_NAME_TYPE);
		String name = paramMap.get("userName");
		nameAvp.setL((byte) (2 + name.getBytes().length));
		nameAvp.setV(name.getBytes());

		RadiusAVP userPassAvp = new RadiusAVP();
		userPassAvp.setT(Constan.RADIUS_ATTR_USERPASS_TYPE);
		String usrPasswd = paramMap.get("password");
		byte[] papPasswd = null;
		try {
			papPasswd = RadiusSecurity.encodePAPPassword(usrPasswd.getBytes(),packet.getAuthenticator(), CHECKUSR_NAS_KEY);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		userPassAvp.setL((byte) (2 + papPasswd.length));
		userPassAvp.setV(papPasswd);

		RadiusAVP nasIpAvp = new RadiusAVP();
		nasIpAvp.setT(Constan.RADIUS_ATTR_NASIP_TYPE);
		nasIpAvp.setL((byte) 6);
		byte[] addrValue = new byte[4];
		InetAddress addr = null;
		try {
			addr = Inet4Address.getByName(paramMap.get("nasIp"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.arraycopy(addr.getAddress(), 0, addrValue, 0, addrValue.length);
		nasIpAvp.setV(addrValue);

		RadiusAVP nasPortAvp = new RadiusAVP();
		nasPortAvp.setT(Constan.RADIUS_ATTR_NASPORT_TYPE);
		nasPortAvp.setL((byte) 6);
		nasPortAvp.setV(TypeConverter.intTobyteArray(Integer.parseInt(paramMap.get("nasPort"))));

		RadiusVendorAVP radiusVendorAVP = new RadiusVendorAVP();
		radiusVendorAVP.setT(Constan.RADIUS_ATTR_VENDOR_SPECIFIC_TYPE);
		radiusVendorAVP.setV_i(TypeConverter.intTobyteArray(Constan.RADIUS_VENDOR_ID));
		radiusVendorAVP.setV_l((byte) 6);
		radiusVendorAVP.setV_v("RPUC".getBytes());
		radiusVendorAVP.setV_t((byte) 255);
		radiusVendorAVP.setL((byte) (1 + 1 + 4 + 1 + 1 + 4));
		avps.add(nameAvp);
		avps.add(userPassAvp);
		avps.add(nasIpAvp);
		avps.add(nasPortAvp);
		avps.add(radiusVendorAVP);
	}

	private byte[] generateByte(int length) {
		byte[] rs = new byte[length];
		Random random = new Random();
		random.nextBytes(rs);
		return rs;
	}

	private AtomicBoolean timerFlag = new AtomicBoolean(true);

	public AtomicBoolean getTimerFlag() {
		return timerFlag;
	}

	private Timer timer = null;

	public void setFlag(boolean timerFlag) {
		if (timerFlag && timer == null) {
			start();
		}
		this.timerFlag.set(timerFlag);
	}

	private void start() {
		if (null == timer) {
			timer = new Timer();
		}
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					if (timerFlag.get()) {
						if (!monitor()) {
							if (count.get() >= 3) {
								String first = bean.getTargetAddr();
								String second = bean.getSencondTargetAddr();
								bean.setAddr(second);
								bean.setSencondTargetAddr(first);
								updateTimeInMillis = Calendar.getInstance().getTimeInMillis();
								count.set(0);
							} else{
								if(Calendar.getInstance().getTimeInMillis()-updateTimeInMillis>30*1000){
									updateTimeInMillis = Calendar.getInstance().getTimeInMillis();
									count.set(count.get() + 1);
								}else{
									count.set(0);
								}								
							}
						}
					} else {
						timer.cancel();
						timer = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
					timer.cancel();
					timer = null;
					start();
				}
			}
		}, 1000, 10 * 1000);
	}
}
