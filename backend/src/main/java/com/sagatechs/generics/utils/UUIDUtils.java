package com.sagatechs.generics.utils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Named
@ApplicationScoped
public class UUIDUtils {

	private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

	/**
	 * Type 4 UUID Generation
	 */
	@SuppressWarnings("unused")
	public UUID generateType4UUID() {
		return UUID.randomUUID();
	}

	/**
	 * Type 3 UUID Generation
	 *
	 */
	@SuppressWarnings("unused")
	public UUID generateType3UUID(String namespace, String name) {
		String source = namespace + name;
		byte[] bytes = source.getBytes(StandardCharsets.UTF_8);
		return UUID.nameUUIDFromBytes(bytes);
	}

	/**
	 * Type 5 UUID Generation
	 *
	 */
	@SuppressWarnings("unused")
	public UUID generateType5UUID(String namespace, String name) {
		String source = namespace + name;
		byte[] bytes = source.getBytes(StandardCharsets.UTF_8);
		return type5UUIDFromBytes(bytes);
	}

	@SuppressWarnings("WeakerAccess")
	public UUID type5UUIDFromBytes(byte[] name) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException nsae) {
			throw new InternalError("MD5 not supported", nsae);
		}
		byte[] bytes = md.digest(name);
		bytes[6] &= 0x0f; /* clear version */
		bytes[6] |= 0x50; /* set to version 5 */
		bytes[8] &= 0x3f; /* clear variant */
		bytes[8] |= 0x80; /* set to IETF variant */
		return constructType5UUID(bytes);
	}

	private UUID constructType5UUID(byte[] data) {
		long msb = 0;
		long lsb = 0;
		assert data.length == 16 : "data must be 16 bytes in length";

		for (int i = 0; i < 8; i++)
			msb = (msb << 8) | (data[i] & 0xff);

		for (int i = 8; i < 16; i++)
			lsb = (lsb << 8) | (data[i] & 0xff);
		return new UUID(msb, lsb);
	}

	/**
	 * Unique Keys Generation Using Message Digest and Type 4 UUID
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	@SuppressWarnings("unused")
	public String generateUniqueKeysWithUUIDAndMessageDigest()
			throws NoSuchAlgorithmException {
		MessageDigest salt = MessageDigest.getInstance("SHA-256");
		salt.update(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
		return bytesToHex(salt.digest());
	}

	@SuppressWarnings("WeakerAccess")
	public String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
}
