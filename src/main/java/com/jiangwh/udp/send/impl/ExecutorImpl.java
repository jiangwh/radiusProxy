package com.jiangwh.udp.send.impl;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.jiangwh.entity.ConfigBean;
import com.jiangwh.entity.ShareContainer;
import com.jiangwh.udp.send.Executor;

@Component("executor")
public class ExecutorImpl implements Executor {
	
	
	private ExecutorService service;
	
	public static volatile CountDownLatch countDownLatch=null;	
	public static volatile AtomicBoolean runFlag = new AtomicBoolean(true);
	
	private Logger logger= Logger.getLogger(ExecutorImpl.class);
	
	@Autowired ConfigBean bean;
	
	@Autowired ShareContainer container;
	
	@Autowired ApplicationContext applicationContext;
	
	private Executor init() {
		if (null == service) {
			service = Executors.newFixedThreadPool(bean.getSendGroupSize());
		}
		return this;
	}

	public Executor stop() {
		if (null != service) {
			try {
				runFlag.compareAndSet(true,false);
				countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!service.isShutdown()) {
				try {
					service.shutdownNow();
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
			countDownLatch = null;
			service = null;
		}
		return this;
	}

	public Executor start() throws Exception {
		if (null == service) {
			return init().execute();
		} else {
			return stop().start();
		}
	}
	
	public Executor execute() {
		if(null!=countDownLatch){
			logger.error("there is "+countDownLatch.getCount()+ " send thread is running call start fail!!!");
			return this;
		}else{
			countDownLatch = new CountDownLatch(bean.getSendGroupSize());
		}		
		runFlag.set(true);
		for(int i=0;i<bean.getSendGroupSize();i++){
			service.submit(applicationContext.getBean("sendTask", SendTask.class));
		}
		return this;
	}
}
