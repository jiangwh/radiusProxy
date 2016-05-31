package com.jiangwh.udp;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.jiangwh.udp.receive.ReceiveAcctTask;
import com.jiangwh.udp.receive.ReceiveAuthTask;
import com.jiangwh.udp.send.Executor;

@Component
@Lazy(false)
@DependsOn({"executor","configBean","receivePacket"})
public class Server{
	
	public Logger logger = Logger.getLogger(Server.class);

	@Autowired
	Executor executor;
	
	@Resource
	ReceiveAuthTask authTask;
	
	@Resource
	ReceiveAcctTask acctTask;

	@PostConstruct
	public void init() {
		if (logger.isInfoEnabled()) {
			logger.info("start udp package receive");
		}
		try {
			start();
		} catch (IOException e) {
			logger.fatal("can't start the receive server");
			System.exit(0);
		}
	}

	public void start() throws IOException {
		ExecutorService service = null;
		try {
			executor.start();
			service = Executors.newFixedThreadPool(2);
			service.submit(authTask);
			service.submit(acctTask);
		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal("can't start send server");
			if(null!=service){
				service.shutdown();
			}
			if(null!=executor){
				executor.stop();
			}
			System.exit(0);
		}
	}
}
