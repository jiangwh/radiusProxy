package com.jiangwh.remotemonitor.radius;

import java.nio.ByteBuffer;

public class TypeConverter {

	public static byte[] intToBytes(int a) {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(a);
		byte[] dst = new byte[4];
		buffer.get(dst);
		return dst;
	}

	public static byte[] shortToBytes(short a) {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.putShort(a);
		return buffer.array();
	}

	public static int byteArrayToint(byte[] b) {
		byte[] a = new byte[4];
		int i = a.length - 1, j = b.length - 1;
		for (; i >= 0; i--, j--) {
			if (j >= 0)
				a[i] = b[j];
			else
				a[i] = 0;
		}
		int v0 = (a[0] & 0xff) << 24;
		int v1 = (a[1] & 0xff) << 16;
		int v2 = (a[2] & 0xff) << 8;
		int v3 = (a[3] & 0xff);
		return v0 + v1 + v2 + v3;
	}

	public static byte[] intTobyteArray(int num) {
		byte[] result = new byte[4];
		result[0] = (byte) (num >>> 24);
		result[1] = (byte) (num >>> 16);
		result[2] = (byte) (num >>> 8); 
		result[3] = (byte) (num); 
		return result;
	}
}
