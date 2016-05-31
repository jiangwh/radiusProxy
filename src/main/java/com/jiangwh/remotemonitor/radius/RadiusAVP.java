package com.jiangwh.remotemonitor.radius;

import java.nio.ByteBuffer;

/**
    0                   1                   2                   3
    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |     Type      |    Length     |             Value
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
              Value (cont)         |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 */
public class RadiusAVP implements AVP {

	private byte t; // type
	private byte l; // length
	private byte[] v; //

	public byte getT() {
		return t;
	}

	public void setT(byte t) {
		this.t = t;
	}

	public byte getL() {
		return l;
	}

	public void setL(byte l) {
		this.l = l;
	}

	public byte[] getV() {
		return v;
	}

	public void setV(byte[] v) {
		this.v = v;
	}

	public byte[] getData() {
		return ((ByteBuffer) ByteBuffer.allocate(this.l).put(this.t)
				.put(this.l).put(this.v).flip()).array();
	}
}
