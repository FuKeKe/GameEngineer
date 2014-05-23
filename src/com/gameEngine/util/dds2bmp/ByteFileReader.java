package com.gameEngine.util.dds2bmp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author JemiZhuu(周士�? zsword)
 * @version 1.0
 * @since 2009-09-12
 * Binary File Reader
 */

public class ByteFileReader extends RandomAccessFile {

	public long read4Bytes() throws IOException {
		return this.readBytesAsLong(4, false);
	}

	public int read4BytesAsInt() throws IOException {
		return this.readBytesAsInt(4, false);
	}

	public int readBytesAsInt(int num, boolean bigEndin) throws IOException {
		byte[] src = new byte[num];
		this.read(src);
		return BitsUtil.getInt(src, bigEndin);
	}

	public int readBytesAsInt(int num) throws IOException {
		return this.readBytesAsInt(num, false);
	}

	public long readBytesAsLong(int num, boolean bigEndin) throws IOException {
		byte[] src = new byte[num];
		this.read(src);
		return BitsUtil.getLong(src, bigEndin);
	}

	public void writeIntBigEndin(int src) throws IOException {
		write((src >>> 0) & 0xFF);
		write((src >>> 8) & 0xFF);
		write((src >>> 16) & 0xFF);
		write((src >>> 24) & 0xFF);
	}

	public ByteFileReader(File file, String mode) throws FileNotFoundException {
		super(file, mode);
	}

	public ByteFileReader(String name, String mode) throws FileNotFoundException {
		super(name, mode);
	}
}
