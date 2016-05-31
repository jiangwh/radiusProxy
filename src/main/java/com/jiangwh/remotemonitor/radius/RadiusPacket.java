package com.jiangwh.remotemonitor.radius;

import java.nio.ByteBuffer;
import java.util.List;

/** 0                   1                   2                   3
    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |     Code      |  Identifier   |            Length             |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                                                               |
   |                         Authenticator                         |
   |                                                               |
   |                                                               |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |  Attributes ...
   +-+-+-+-+-+-+-+-+-+-+-+-+-
 *
 */
public class RadiusPacket {

	private byte code; // 1
	private byte identifier; // 1
	private byte[] packetLength; // 2
	/**
	 In Access-Request Packets, the Authenticator value is a 16
         octet random number, called the Request Authenticator.  The
         value SHOULD be unpredictable and unique over the lifetime of a
         secret (the password shared between the client and the RADIUS
         server), since repetition of a request value in conjunction
         with the same secret would permit an attacker to reply with a
         previously intercepted response.  Since it is expected that the
         same secret MAY be used to authenticate with servers in
         disparate geographic regions, the Request Authenticator field
         SHOULD exhibit global and temporal uniqueness.
        
        
            Response Authenticator

        The value of the Authenticator field in Access-Accept, Access-
        Reject, and Access-Challenge packets is called the Response
        Authenticator, and contains a one-way MD5 hash calculated over
        a stream of octets consisting of: the RADIUS packet, beginning
        with the Code field, including the Identifier, the Length, the
        Request Authenticator field from the Access-Request packet, and
        the response Attributes, followed by the shared secret.  That
        is, ResponseAuth =
        MD5(Code+ID+Length+RequestAuth+Attributes+Secret) where +
        denotes concatenation.
        MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update((byte)getCode());
		md5.update((byte)getIdentifier());
		md5.update((byte)(packetLength >> 8));
		md5.update((byte)(packetLength & 0x0ff));
		md5.update(authenticator, 0, authenticator.length);
		md5.update(attributes, 0, attributes.length);
		md5.update(StringUtil.getUtf8Bytes(sharedSecret));
        
	 */
	private byte[] authenticator; // 16
	private List<AVP> attr;
	private short avpLength;
	public short getAvpLength() {
		return avpLength;
	}
	public void setAvpLength(short avpLength) {
		this.avpLength = avpLength;
	}
	public byte getCode() {
		return code;
	}
	public void setCode(byte code) {
		this.code = code;
	}
	public byte getIdentifier() {
		return identifier;
	}
	public void setIdentifier(byte identifier) {
		this.identifier = identifier;
	}
	public byte[] getPacketLength() {
		return packetLength;
	}
	public void setPacketLength(byte[] packetLength) {
		this.packetLength = packetLength;
	}
	public byte[] getAuthenticator() {
		return authenticator;
	}
	public void setAuthenticator(byte[] authenticator) {
		this.authenticator = authenticator;
	}
	public List<AVP> getAttr() {
		return attr;
	}
	public void setAttr(List<AVP> attr) {
		this.attr = attr;
	}
	public short getLength(){
		return (short) TypeConverter.byteArrayToint(this.getPacketLength());
	}
	public byte[] getData(){
		return ((ByteBuffer) (ByteBuffer.allocate(getLength()).put(this.code)
				.put(this.identifier).put(this.packetLength)
				.put(this.authenticator).put(getAttrData()).flip())).array();
	}
	public byte[] getAttrData(){
		ByteBuffer buffer = ByteBuffer.allocate(this.getAvpLength());
		for (AVP avp : this.attr) {
			buffer.put(avp.getData());
		}
		return buffer.array();
	}
	
}
