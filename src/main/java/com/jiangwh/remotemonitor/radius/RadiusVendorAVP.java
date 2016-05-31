package com.jiangwh.remotemonitor.radius;

import java.nio.ByteBuffer;

/**
   0                   1                   2                   3
       0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
      |     Type      |  Length       |            Vendor-Id
      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
           Vendor-Id (cont)           | Vendor type   | Vendor length |
      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
      |    Attribute-Specific...
      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
 * 
 */
public class RadiusVendorAVP implements AVP {

	private byte t; // 1
	private byte l; // 1
	private byte[] v_i; // 4
	private byte v_t; // 1
	private byte v_l; //  v_t + v_l + v_v
	private byte[] v_v; //

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

	public byte[] getV_i() {
		return v_i;
	}

	public void setV_i(byte[] v_i) {
		this.v_i =v_i;
	}

	public byte getV_t() {
		return v_t;
	}

	public void setV_t(byte v_t) {
		this.v_t = v_t;
	}

	public byte getV_l() {
		return v_l;
	}

	public void setV_l(byte v_l) {
		this.v_l = v_l;
	}

	public byte[] getV_v() {
		return v_v;
	}

	public void setV_v(byte[] v_v) {
		this.v_v = v_v;
	}

	public byte[] getData() {
		return ((ByteBuffer) ByteBuffer.allocate(this.l).put(this.t)
				.put(this.l).put(this.v_i).put(this.v_t).put(this.v_l)
				.put(this.v_v).flip()).array();
	}
}
