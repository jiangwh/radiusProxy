package com.jiangwh.remotemonitor.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RadiusSecurity {

	public static byte[] encodePAPPassword(final byte[] userPass,
			final byte[] authenticator, final String sharedSecret)
			throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] userPassBytes = null;
		if (userPass.length > 128) {
			userPassBytes = new byte[128];
			System.arraycopy(userPass, 0, userPassBytes, 0, 128);
		} else {
			userPassBytes = userPass;
		}

		// declare the byte array to hold the final product
		byte[] encryptedPass = null;

		if (userPassBytes.length < 128) {
			if (userPassBytes.length % 16 == 0) {
				// It is already a multiple of 16 bytes
				encryptedPass = new byte[userPassBytes.length];
			} else {
				// Make it a multiple of 16 bytes
				encryptedPass = new byte[((userPassBytes.length / 16) * 16) + 16];
			}
		} else {
			// the encrypted password must be between 16 and 128 bytes
			encryptedPass = new byte[128];
		}

		// copy the userPass into the encrypted pass and then fill it out with
		// zeroes
		System.arraycopy(userPassBytes, 0, encryptedPass, 0,
				userPassBytes.length);
		for (int i = userPassBytes.length; i < encryptedPass.length; i++) {
			encryptedPass[i] = 0; // fill it out with zeroes
		}
		// add the shared secret
		md5.update(sharedSecret.getBytes());
		// add the Request Authenticator.
		md5.update(authenticator);
		// get the md5 hash( b1 = MD5(S + RA) ).
		byte bn[] = md5.digest();

		for (int i = 0; i < 16; i++) {
			// perform the XOR as specified by RFC 2865.
			encryptedPass[i] = (byte) (bn[i] ^ encryptedPass[i]);
		}

		if (encryptedPass.length > 16) {
			for (int i = 16; i < encryptedPass.length; i += 16) {
				md5.reset();
				// add the shared secret
				md5.update(sharedSecret.getBytes());
				// add the previous(encrypted) 16 bytes of the user password
				md5.update(encryptedPass, i - 16, 16);
				// get the md5 hash( bn = MD5(S + c(i-1)) ).
				bn = md5.digest();
				for (int j = 0; j < 16; j++) {
					// perform the XOR as specified by RFC 2865.
					encryptedPass[i + j] = (byte) (bn[j] ^ encryptedPass[i + j]);
				}
			}
		}
		return encryptedPass;
	}

	public static byte[] encodeChapPassword(final byte[] userPass,
			final byte chapIdentifier, final byte[] chapChallenge)
			throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(chapIdentifier);
		md5.update(userPass);
		md5.update(chapChallenge);
		return md5.digest();
	}

	public static byte[] encodeEapPassword(
			// final MessageDigest md5,
			final byte[] userPass, final byte packetIdentifier,
			final byte[] eapChallenge) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(packetIdentifier);
		md5.update(userPass);
		md5.update(eapChallenge);
		return md5.digest();
	}

	public static String decodePAPPassword(byte password[], byte secret[],
			byte authenticator[]) throws NoSuchAlgorithmException {
		byte c[] = null;
		if (password.length < 128) {
			if (password.length % 16 == 0) {
				c = new byte[password.length];
			} else {
				c = new byte[(password.length / 16) * 16 + 16];
			}
		} else {
			c = new byte[128];
		}
		System.arraycopy(password, 0, c, 0, password.length);
		for (int i = password.length; i < c.length; i++) {
			c[i] = 0;

		}
		MessageDigest md5 =  MessageDigest.getInstance("MD5");
		md5.update(secret);
		md5.update(authenticator);
		byte bn[] = md5.digest();
		for (int i = 0; i < 16; i++) {
			c[i] = (byte) (bn[i] ^ c[i]);

		}
		if (c.length > 16) {
			for (int i = 16; i < c.length; i += 16) {
				md5.reset();
				md5.update(secret);
				md5.update(c, i - 16, i);
				bn = md5.digest();
				for (int j = 0; i < 16; j++) {
					c[i + j] = (byte) (bn[j] ^ c[i + j]);
				}
			}
		}

		byte[] bytes = new String(c, 0, password.length).getBytes();
		return new String(trimZeroBytes(bytes));
	}

	private static byte[] trimZeroBytes(byte[] pwdBytes) {
		byte[] result = null;
		for (int i = pwdBytes.length - 1; i >= 0; i--) {
			if (pwdBytes[i] != 0) {
				result = new byte[i + 1];
				System.arraycopy(pwdBytes, 0, result, 0, i + 1);
				break;
			}
		}
		return result;
	}
}
