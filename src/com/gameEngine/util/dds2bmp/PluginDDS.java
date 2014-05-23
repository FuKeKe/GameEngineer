package com.gameEngine.util.dds2bmp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.graphics.Bitmap;

/**
 * @author JemiZhuu(周士�? zsword)
 * @version 1.1
 * @since 2009-09-13
 * DDS(DIRECTDRAWSURFACE) File Reader
 */
public class PluginDDS {

	private DDSHEADER header;
	private int bpp;
	private int rowBlockSize;
	private Bitmap image;
	private static final Logger logger = Logger.getLogger(PluginDDS.class.getName());
	static {
		logger.setLevel(Level.SEVERE);
	}

	public DDSHEADER getHeader() {
		return header;
	}

	private static final int DDPF_ALPHAPIXELS = 0x00000001;
	private static final int DDPF_ALPHA = 0x00000002;
	private static final int DDPF_FOURCC = 0x00000004;
	private static final int DDPF_RGB = 0x00000040;
	private static final String FOURCC_DXT1 = "DXT1";
	private static final String FOURCC_DXT2 = "DXT2";
	private static final String FOURCC_DXT3 = "DXT3";
	private static final String FOURCC_DXT4 = "DXT4";
	private static final String FOURCC_DXT5 = "DXT5";
	private static final int FI_RGBA_RED = 2;
	private static final int FI_RGBA_GREEN = 1;
	private static final int FI_RGBA_BLUE = 0;
	private static final int FI_RGBA_ALPHA = 3;
	private static final int FI_RGBA_RED_MASK = 0x00FF0000;
	private static final int FI_RGBA_GREEN_MASK = 0x0000FF00;
	private static final int FI_RGBA_BLUE_MASK = 0x000000FF;
	private static final int FI_RGBA_ALPHA_MASK = 0xFF000000;
	private static final int FI_RGBA_RED_SHIFT = 16;
	private static final int FI_RGBA_GREEN_SHIFT = 8;
	private static final int FI_RGBA_BLUE_SHIFT = 0;
	private static final int FI_RGBA_ALPHA_SHIFT = 24;
	private static final int FIT_BITMAP = 1;

	/**
	 * read a DDS Texture File
	 * @param io
	 * @return Bitmap
	 * @throws IOException
	 */
	public Bitmap readDDSFile(ByteFileReader io) throws IOException {
		this.readHeader(io);
		this.readPixeData(io);
		return image;
	}

	/**
	 * read DDS header
	 * @param io
	 * @throws IOException
	 */
	protected void readHeader(ByteFileReader io) throws IOException {
		if (io == null || io.length() <= 0) {
			return;
		}
		header = new DDSHEADER();
		byte[] buf = new byte[4];
		io.read(buf);
		header.dwMagic = new String(buf);
		header.dwSize = io.read4BytesAsInt();
		header.dwFlags = io.read4BytesAsInt();
		header.dwHeight = io.read4BytesAsInt();
		header.dwWidth = io.read4BytesAsInt();
		header.dwPitchOrLinearSize = io.read4BytesAsInt();
		header.dwDepth = io.read4BytesAsInt();
		header.dwMipMapCount = io.read4BytesAsInt();
		for (int i = 0; i < header.dwReserved1.length; i++) {
			header.dwReserved1[i] = io.read4BytesAsInt();
		}
		header.ddpfPixelFormat.dwSize = io.read4BytesAsInt();
		header.ddpfPixelFormat.dwFlags = io.read4BytesAsInt();
		io.read(buf);
		header.ddpfPixelFormat.dwFourCC = new String(buf);
		header.ddpfPixelFormat.dwRGBBitCount = io.read4BytesAsInt();
		header.ddpfPixelFormat.dwRBitMask = io.read4BytesAsInt();
		header.ddpfPixelFormat.dwGBitMask = io.read4BytesAsInt();
		header.ddpfPixelFormat.dwBBitMask = io.read4BytesAsInt();
		header.ddpfPixelFormat.dwRGBAlphaBitMask = io.read4BytesAsInt();
		header.ddsCaps.dwCaps1 = io.read4BytesAsInt();
		header.ddsCaps.dwCaps2 = io.read4BytesAsInt();
		for (int i = 0; i < header.ddsCaps.dwReserved.length; i++) {
			header.ddsCaps.dwReserved[i] = io.read4BytesAsInt();
		}
		header.dwReserved2 = io.read4BytesAsInt();
	}

	/**
	 * read DDS pixel datas
	 * @param io
	 * @throws IOException
	 */
	protected void readPixeData(ByteFileReader io) throws IOException {
		if (header == null) {
			throw new IOException("DDS Header is NULL");
		}
		//image = new Bitmap(header.dwWidth, header.dwHeight, Bitmap.TYPE_INT_ARGB_PRE);
		image = Bitmap.createBitmap(header.dwWidth, header.dwHeight, Bitmap.Config.ARGB_8888);
		if (0 != (header.ddpfPixelFormat.dwFlags & DDPF_RGB)) {
			LoadRGB(io);
		} else if (0 != (header.ddpfPixelFormat.dwFlags & DDPF_FOURCC)) {
			this.LoadDXT(io);
		} else {
			throw new IOException("Unkown DDS Format");
		}
	}

	/**
	 * load DXT data by buffer
	 * @param io
	 * @throws IOException
	 */
	protected void LoadDXT(ByteFileReader io) throws IOException {
		if (header == null) {
			throw new IOException("DDS Header is NULL");
		}
		rowBlockSize = header.dwWidth * 4;
		int width = header.dwWidth & ~3;
		int height = header.dwHeight & ~3;
		int bytesPerBlock = 8;
		if (FOURCC_DXT3.equals(header.ddpfPixelFormat.dwFourCC)) {
			bytesPerBlock = 16;
		} else if (FOURCC_DXT5.equals(header.ddpfPixelFormat.dwFourCC)) {
			bytesPerBlock = 16;
		}
		//allocate a 32-bit dib
		bpp = 32;
		int line = ((width * bpp) + 7) / 8;
		int widthRest = width & 3;
		int heightRest = height & 3;
		int inputLine = (width + 3) / 4;
		ByteBuffer buff = ByteBuffer.allocate(inputLine * bytesPerBlock);
		if (height >= 4) {
			for (int yheight = 0; yheight < height; yheight += 4) {
				buff.clear();
				io.read(buff.array());
				if (width >= 4) {
					for (int xwidth = 0; xwidth < width; xwidth += 4) {
						this.DecodeDXTBlock(image, buff, xwidth, yheight, 4, 4);
					}
				}
				if (widthRest != 0) {
					this.DecodeDXTBlock(image, buff, widthRest, yheight, widthRest, 4);
				}
			}
		}
		if (heightRest != 0) {
			buff.clear();
			io.read(buff.array());
			if (width >= 4) {
				for (int xwidth = 0; xwidth < width; xwidth += 4) {
					this.DecodeDXTBlock(image, buff, xwidth, heightRest, 4, heightRest);
				}
			}
			if (widthRest != 0) {
				this.DecodeDXTBlock(image, buff, widthRest, heightRest, widthRest, heightRest);
			}
		}
	}

	/**
	 * dacode DXT pixel data
	 * @param img
	 * @param io
	 * @param width
	 * @param height
	 * @param bw
	 * @param bh
	 */
	protected void DecodeDXTBlock(Bitmap img, ByteBuffer io, int width, int height, int bw, int bh) {
		int alpha[] = null;
		int aphrow[] = null;
		int data[] = null;
		//read alpha data
		if (FOURCC_DXT3.equals(header.ddpfPixelFormat.dwFourCC)) {
			aphrow = new int[4];
			for (int i = 0; i < 4; i++) {
				aphrow[i] = BufferUtil.readBytesAsInt(io, 2);
			}
		} else if (FOURCC_DXT5.equals(header.ddpfPixelFormat.dwFourCC)) {
			alpha = new int[8];
			for (int i = 0; i < 2; i++) {
				alpha[i] = BufferUtil.readBytesAsInt(io, 1);
			}
			if (alpha[0] > alpha[1]) {
				//8 alpha block
				for (int i = 0; i < 6; i++) {
					alpha[i + 2] = (((6 - i) * alpha[0] + (1 + i) * alpha[1] + 3) / 7);
				}
			} else {
				//6 alpha block
				for (int i = 0; i < 4; i++) {
					alpha[i + 2] = (((4 - i) * alpha[0] + (1 + i) * alpha[1] + 2) / 5);
				}
				alpha[6] = 0x00;
				alpha[7] = 0xFF;
			}
			data = new int[6];
			for (int i = 0; i < 6; i++) {
				data[i] = BufferUtil.readBytesAsInt(io, 1);
			}
		}
		//read color data
		Color8888[] colors = new Color8888[4];
		for (int i = 0; i < 4; i++) {
			colors[i] = new Color8888();
		}
		int colval[] = new int[2];
		for (int i = 0; i < 2; i++) {
			colval[i] = BufferUtil.readBytesAsInt(io, 2);
		}
		GetBlockColors(colval, colors, true);
		int row[] = new int[4];
		for (int i = 0; i < 4; i++) {
			row[i] = BufferUtil.readBytesAsInt(io, 1);
		}
		//decode color data
		for (int y = 0; y < bh; y++) {
			int m_colorRow = row[y];
			int m_alphaRow = 0;
			int m_alphaBits = 0;
			int m_offset = 0;
			if (FOURCC_DXT3.equals(header.ddpfPixelFormat.dwFourCC)) {
				m_alphaRow = (short) aphrow[y];
			} else if (FOURCC_DXT5.equals(header.ddpfPixelFormat.dwFourCC)) {
				int ti = y / 2;
				m_alphaBits = (data[0 + ti * 3] | (data[1 + ti * 3] << 8) | (data[2 + ti * 3] << 16));
				m_offset = (y & 1) * 12;
			}
			for (int x = 0; x < bw; x++) {
				Color8888 getCol = new Color8888();
				int bits = ((m_colorRow >> (x * 2)) & 3);
				getCol.Copy(colors[bits]);
				if (FOURCC_DXT3.equals(header.ddpfPixelFormat.dwFourCC)) {
					bits = ((m_alphaRow >> (x * 4)) & 0xF);
					getCol.a = (short) ((bits * 0xFF) / 0xF);
				} else if (FOURCC_DXT5.equals(header.ddpfPixelFormat.dwFourCC)) {
					bits = ((m_alphaBits >> (x * 3 + m_offset)) & 7);
					getCol.a = (short) alpha[bits];
				}
				//image.setRGB(width + x, height + y, convertPixelColorToValue(getCol));
				image.setPixel(width + x, height + y, convertPixelColorToValue(getCol));
			}
		}
	}

	/**
	 * read DXT data by File IO(slowly)
	 * @param io
	 * @throws IOException
	 */
	protected void LoadDXTByReader(ByteFileReader io) throws IOException {
		if (header == null) {
			throw new IOException("DDS Header is NULL");
		}
		rowBlockSize = header.dwWidth * 4;
		int width = header.dwWidth & ~3;
		int height = header.dwHeight & ~3;
		//allocate a 32-bit dib
		bpp = 32;
		int widthRest = width & 3;
		int heightRest = height & 3;
		if (height >= 4) {
			for (int yheight = 0; yheight < height; yheight += 4) {
				if (width >= 4) {
					for (int xwidth = 0; xwidth < width; xwidth += 4) {
						this.DecodeDXTBlock(image, io, xwidth, yheight, 4, 4);
					}
				}
				if (widthRest != 0) {
					this.DecodeDXTBlock(image, io, widthRest, yheight, widthRest, 4);
				}
			}
		}
		if (heightRest != 0) {
			if (width >= 4) {
				for (int xwidth = 0; xwidth < width; xwidth += 4) {
					this.DecodeDXTBlock(image, io, xwidth, heightRest, 4, heightRest);
				}
			}
			if (widthRest != 0) {
				this.DecodeDXTBlock(image, io, widthRest, heightRest, widthRest, heightRest);
			}
		}
	}

	/**
	 * get pixel color int value
	 * @param x
	 * @param y
	 * @return
	 */
	protected int getPixelColorValue(int x, int y) {
		//int index = x % 4 + (x / 4) * 16 + (y / 4) * rowBlockSize + (y % 4) * 4;
		return image.getPixel(x, y);
	}

	/**
	 * get pixel color
	 * @param x
	 * @param y
	 * @return
	 */
	protected Color8888 getPixelColor(int x, int y) {
		//int index = x % 4 + (x / 4) * 16 + (y / 4) * rowBlockSize + (y % 4) * 4;
		int colVal = this.image.getPixel(x, y);
		Color8888 color = new Color8888();
		color.a = (short) ((colVal >> 24) & 0xFF);
		color.r = (short) ((colVal >> 16) & 0xFF);
		color.g = (short) ((colVal >> 8) & 0xFF);
		color.b = (short) (colVal & 0xFF);
		return color;
	}

	/**
	 * convert pixel color to int
	 * @param color
	 * @return
	 */
	protected int convertPixelColorToValue(Color8888 color) {
		return (color.a << 24) + (color.r << 16) + (color.g << 8) + color.b;
	}

	/**
	 * decode DXT pixcel data
	 * @param img
	 * @param io
	 * @param width
	 * @param height
	 * @param bw
	 * @param bh
	 * @throws IOException
	 */
	protected void DecodeDXTBlock(Bitmap img, ByteFileReader io, int width, int height, int bw, int bh)
			throws IOException {
		int alpha[] = null;
		int aphrow[] = null;
		int data[] = null;
		//read alpha data
		if (FOURCC_DXT3.equals(header.ddpfPixelFormat.dwFourCC)) {
			aphrow = new int[4];
			for (int i = 0; i < 4; i++) {
				aphrow[i] = io.readBytesAsInt(2);
			}
		} else if (FOURCC_DXT5.equals(header.ddpfPixelFormat.dwFourCC)) {
			alpha = new int[8];
			for (int i = 0; i < 2; i++) {
				alpha[i] = io.readBytesAsInt(1);
			}
			if (alpha[0] > alpha[1]) {
				//8 alpha block
				for (int i = 0; i < 6; i++) {
					alpha[i + 2] = (((6 - i) * alpha[0] + (1 + i) * alpha[1] + 3) / 7);
				}
			} else {
				//6 alpha block
				for (int i = 0; i < 4; i++) {
					alpha[i + 2] = (((4 - i) * alpha[0] + (1 + i) * alpha[1] + 2) / 5);
				}
				alpha[6] = 0x00;
				alpha[7] = 0xFF;
			}
			data = new int[6];
			for (int i = 0; i < 6; i++) {
				data[i] = io.readBytesAsInt(1);
			}
		}
		//read color data
		Color8888[] colors = new Color8888[4];
		for (int i = 0; i < 4; i++) {
			colors[i] = new Color8888();
		}
		int colval[] = new int[2];
		for (int i = 0; i < 2; i++) {
			colval[i] = io.readBytesAsInt(2);
		}
		GetBlockColors(colval, colors, true);
		int row[] = new int[4];
		for (int i = 0; i < 4; i++) {
			row[i] = io.readBytesAsInt(1);
		}
		//decode color data
		for (int y = 0; y < bh; y++) {
			int m_colorRow = row[y];
			int m_alphaRow = 0;
			int m_alphaBits = 0;
			int m_offset = 0;
			if (FOURCC_DXT3.equals(header.ddpfPixelFormat.dwFourCC)) {
				m_alphaRow = (short) aphrow[y];
			} else if (FOURCC_DXT5.equals(header.ddpfPixelFormat.dwFourCC)) {
				int ti = y / 2;
				m_alphaBits = (data[0 + ti * 3] | (data[1 + ti * 3] << 8) | (data[2 + ti * 3] << 16));
				m_offset = (y & 1) * 12;
			}
			for (int x = 0; x < bw; x++) {
				Color8888 getCol = new Color8888();
				int bits = ((m_colorRow >> (x * 2)) & 3);
				getCol.Copy(colors[bits]);
				if (FOURCC_DXT3.equals(header.ddpfPixelFormat.dwFourCC)) {
					bits = ((m_alphaRow >> (x * 4)) & 0xF);
					getCol.a = (short) ((bits * 0xFF) / 0xF);
				} else if (FOURCC_DXT5.equals(header.ddpfPixelFormat.dwFourCC)) {
					bits = ((m_alphaBits >> (x * 3 + m_offset)) & 7);
					getCol.a = (short) alpha[bits];
				}
				img.setPixel(width + x, height + y, convertPixelColorToValue(getCol));
			}
		}
	}

	/**
	 * get pixel block colors
	 * @param colval
	 * @param colors
	 * @param isDXT1
	 */
	protected void GetBlockColors(int[] colval, Color8888[] colors, boolean isDXT1) {
		Color565[] srcCols = new Color565[2];
		srcCols[0] = new Color565(colval[0]);
		srcCols[1] = new Color565(colval[1]);
		//expand from 565 to 888
		for (int i = 0; i < 2; i++) {
			colors[i].a = 0xFF;
			colors[i].r = (short) ((srcCols[i].r << 3) | (srcCols[i].r >> 2));
			colors[i].g = (short) ((srcCols[i].g << 2) | (srcCols[i].g >> 4));
			colors[i].b = (short) ((srcCols[i].b << 3) | (srcCols[i].b >> 2));
		}
		if (colval[0] > colval[1] || !isDXT1) {
			//4 color block
			for (int i = 0; i < 2; i++) {
				colors[i + 2].a = 0xFF;
				colors[i + 2].r = (short) ((colors[0].r * (2 - i) + colors[1].r * (1 + i)) / 3);
				colors[i + 2].g = (short) ((colors[0].g * (2 - i) + colors[1].g * (1 + i)) / 3);
				colors[i + 2].b = (short) ((colors[0].b * (2 - i) + colors[1].b * (1 + i)) / 3);
			}
		} else {
			//3 color block number 4 is transparent
			colors[2].a = 0xFF;
			colors[2].r = (short) ((colors[0].r + colors[1].r) / 2);
			colors[2].g = (short) ((colors[0].g + colors[1].g) / 2);
			colors[2].b = (short) ((colors[0].b + colors[1].b) / 2);
			colors[3].a = 0x00;
			colors[3].r = 0x00;
			colors[3].g = 0x00;
			colors[3].b = 0x00;
		}
	}

	/**
	 * not implement
	 * @param io
	 */
	protected void LoadRGB(ByteFileReader io) {
		int width = header.dwWidth & ~3;
		int height = header.dwHeight & ~3;
		int bpp = header.ddpfPixelFormat.dwRGBBitCount;
	}

	protected class DXT5Block {

		short alpha[] = new short[2];
		short data[] = new short[6];
		Color565 colors[] = new Color565[2];
		short row[] = new short[4];
	}

	protected class Color8888 {

		short b;
		short g;
		short r;
		short a;

		protected void Copy(Color8888 src) {
			this.a = src.a;
			this.r = src.r;
			this.g = src.g;
			this.b = src.b;
		}

		public String toString() {
			return Integer.toHexString(this.hashCode()) + " = [r:" + r + ", g:" + g + ", b:" + b + ", a:" + a + "]";
		}
	}

	protected class Color565 {

		int b = 5;
		int g = 6;
		int r = 5;

		Color565(int i) {
			this.r = i >> (16 - 5) & 0x1F;//first 5 bits
			this.g = i >> (16 - 6 - 5) & 0x3F;//center 6 bits
			this.b = i & 0x1F;//last 5 bits
		}
	}

	public class DDSHEADER {

		protected String dwMagic; // FOURCC: "DDS "
		protected int dwSize; //size of this structure(c) (must be 124)
		protected int dwFlags; // combination of the DDSS_* flags
		protected int dwHeight;
		protected int dwWidth;
		protected int dwPitchOrLinearSize;
		protected int dwDepth; // Depth of a volume texture
		protected int dwMipMapCount;
		protected int dwReserved1[] = new int[11];
		protected DDPIXEFORMAT ddpfPixelFormat;
		protected DDCAPS2 ddsCaps;
		protected int dwReserved2;

		public int getDwHeight() {
			return dwHeight;
		}

		public void setDwHeight(int dwHeight) {
			this.dwHeight = dwHeight;
		}

		public int getDwWidth() {
			return dwWidth;
		}

		public void setDwWidth(int dwWidth) {
			this.dwWidth = dwWidth;
		}

		public void setPixelFormatStr(String pixelFormat) {
			this.ddpfPixelFormat.dwFourCC = pixelFormat;
		}

		public DDSHEADER() {
			this.ddpfPixelFormat = new DDPIXEFORMAT();
			this.ddsCaps = new DDCAPS2();
		}

		class DDPIXEFORMAT {

			protected int dwSize; // size of this structure (must be 32)
			protected int dwFlags; // see DDPF_*
			protected String dwFourCC;
			protected int dwRGBBitCount; // Total number of bits for RGB formats
			protected int dwRBitMask;
			protected int dwGBitMask;
			protected int dwBBitMask;
			protected int dwRGBAlphaBitMask;
		}

		class DDCAPS2 {

			int dwCaps1; // Zero or more of the DDSCAPS_* members
			int dwCaps2; // Zero or more of the DDSCAPS2_* members
			int dwReserved[] = new int[2];
		}
	}
}
