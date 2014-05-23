package com.gameEngine.util.dds2bmp;

import java.nio.ByteBuffer;

/**
 * 
 * xxBuffer util 
 */
class BufferUtil {

	protected static int readBytesAsInt(ByteBuffer io, int len) {
		return readBytesAsInt(io, len, false);
	}

	protected static int readBytesAsInt(ByteBuffer io, int len, boolean bigEndin) {
		byte[] buf = new byte[len];
		io.get(buf);
		return BitsUtil.getInt(buf, bigEndin);
	}

	protected static long readBytesAsLong(ByteBuffer io, int len) {
		return readBytesAsLong(io, len, false);
	}

	protected static long readBytesAsLong(ByteBuffer io, int len, boolean bigEndin) {
		byte[] buf = new byte[len];
		io.get(buf);
		return BitsUtil.getLong(buf, bigEndin);
	}
}
