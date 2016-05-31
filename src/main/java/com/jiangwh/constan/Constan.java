package com.jiangwh.constan;

public interface Constan {

	final int PACKET_MAX_LENGTH = 4096;
	final int PACKET_MIN_LENGTH = 20;
	final int SOCK_TIMEOUT = 3 * 1000;
	final int RADIUS_VENDOR_ID = 4881;
	final byte RADIUS_ATTR_NAME_TYPE = 1;
	final byte RADIUS_ATTR_USERPASS_TYPE = 2;
	final byte RADIUS_ATTR_NASIP_TYPE = 4;
	final byte RADIUS_ATTR_NASPORT_TYPE = 5;
	final byte RADIUS_ATTR_VENDOR_SPECIFIC_TYPE = 26;
	final byte Radius_ACCESS_REQUEST = 1;
	final String FILTER_RELATIVELY_CLASS_PATH = "impl";
	final String CLASS_FILE_SUFFIX = ".class";
	final String STRING_DOT = ".";
	
}
