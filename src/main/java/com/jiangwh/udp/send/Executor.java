package com.jiangwh.udp.send;

public interface Executor {

	/**
	 * start the executor
	 */
	Executor start() throws Exception;

	/**
	 * stop all send thread
	 */
	Executor stop();

	/**
	 * begin to execute
	 */
	Executor execute();
}
