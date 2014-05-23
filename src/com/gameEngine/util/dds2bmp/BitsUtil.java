package com.gameEngine.util.dds2bmp;

/**
 * sun java bits util
 */
class BitsUtil {

	private BitsUtil() {
	}

	// -- Swapping --
	static short swap(short x) {
		return (short) ((x << 8) | ((x >> 8) & 0xff));
	}

	static char swap(char x) {
		return (char) ((x << 8) | ((x >> 8) & 0xff));
	}

	static int swap(int x) {
		return (int) ((swap((short) x) << 16) | (swap((short) (x >> 16)) & 0xffff));
	}

	static long swap(long x) {
		return (long) (((long) swap((int) (x)) << 32) | ((long) swap((int) (x >> 32)) & 0xffffffffL));
	}

	// -- get char --

	static private char makeChar(byte b1, byte b0) {
		return (char) ((b1 << 8) | (b0 & 0xff));
	}

	private static byte char1(char x) {
		return (byte) (x >> 8);
	}

	private static byte char0(char x) {
		return (byte) (x >> 0);
	}

	// -- get short --
	static private short makeShort(byte b1, byte b0) {
		return (short) ((b1 << 8) | (b0 & 0xff));
	}

	private static byte short1(short x) {
		return (byte) (x >> 8);
	}

	private static byte short0(short x) {
		return (byte) (x >> 0);
	}

	// -- get int --
	static protected int getInt(byte[] src, boolean bigEndin) {
		byte[] temp = { 0, 0, 0, 0 };
		for (int i = 0; i < src.length; i++) {
			if (i > 3)
				break;
			temp[i] = src[i];
		}
		if (!bigEndin) {
			return makeInt(temp[3], temp[2], temp[1], temp[0]);
		} else {
			return makeInt(temp[0], temp[1], temp[2], temp[3]);
		}
	}

	static private int makeInt(byte b3, byte b2, byte b1, byte b0) {
		return (int) ((((b3 & 0xff) << 24) | ((b2 & 0xff) << 16) | ((b1 & 0xff) << 8) | ((b0 & 0xff) << 0)));
	}

	private static byte int3(int x) {
		return (byte) (x >> 24);
	}

	private static byte int2(int x) {
		return (byte) (x >> 16);
	}

	private static byte int1(int x) {
		return (byte) (x >> 8);
	}

	private static byte int0(int x) {
		return (byte) (x >> 0);
	}

	// -- get long --
	static protected long getLong(byte[] src, boolean bigEndin) {
		byte[] temp = { 0, 0, 0, 0, 0, 0, 0, 0 };
		for (int i = 0; i < src.length; i++) {
			if (i > 7)
				break;
			temp[i] = src[i];
		}
		if (!bigEndin) {
			return makeLong(temp[7], temp[6], temp[5], temp[4], temp[3], temp[2], temp[1], temp[0]);
		} else {
			return makeLong(temp[4], temp[5], temp[6], temp[7], temp[0], temp[1], temp[2], temp[3]);
		}
	}

	static private long makeLong(byte b7, byte b6, byte b5, byte b4, byte b3, byte b2, byte b1, byte b0) {
		return ((((long) b7 & 0xff) << 56) | (((long) b6 & 0xff) << 48) | (((long) b5 & 0xff) << 40)
				| (((long) b4 & 0xff) << 32) | (((long) b3 & 0xff) << 24) | (((long) b2 & 0xff) << 16)
				| (((long) b1 & 0xff) << 8) | (((long) b0 & 0xff) << 0));
	}

	private static byte long7(long x) {
		return (byte) (x >> 56);
	}

	private static byte long6(long x) {
		return (byte) (x >> 48);
	}

	private static byte long5(long x) {
		return (byte) (x >> 40);
	}

	private static byte long4(long x) {
		return (byte) (x >> 32);
	}

	private static byte long3(long x) {
		return (byte) (x >> 24);
	}

	private static byte long2(long x) {
		return (byte) (x >> 16);
	}

	private static byte long1(long x) {
		return (byte) (x >> 8);
	}

	private static byte long0(long x) {
		return (byte) (x >> 0);
	}
}
