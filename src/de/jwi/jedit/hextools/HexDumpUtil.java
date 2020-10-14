package de.jwi.jedit.hextools;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

public final class HexDumpUtil
{
	public static String formatHexDump(byte[] data, int offset, int len, String charsetName)
	{
		final int width = 16;
		
		Charset charset = Charset.forName(charsetName);

		boolean singleByte = charset.newEncoder().maxBytesPerChar() == 1;

		StringBuilder builder = new StringBuilder();

		for (int rowOffset = offset; rowOffset < offset + len; rowOffset += width)
		{
			builder.append(String.format("%06d:  ", rowOffset));

			for (int i = 0; i < width; i++)
			{
				if (rowOffset + i < data.length)
				{
					builder.append(String.format("%02x ", data[rowOffset + i]));
				} else
				{
					builder.append("   ");
				}
			}

			if (singleByte && rowOffset < data.length)
			{
				int asciiWidth = Math.min(width, data.length - rowOffset);
				builder.append("  ");

				String s = new String(data, rowOffset, asciiWidth, charset);
				
				s = s.replaceAll("[^\\p{Print}]", ".");
								
				builder.append(s);
			}

			builder.append(String.format("%n"));
		}

		return builder.toString();
	}

	public static void main(String[] args) throws Exception
	{
		String n = HexDumpUtil.class.getName();
		n = "/" + n.replace(".", "/") + ".class";
		URL url = HexDumpUtil.class.getResource(n);

		InputStream is = url.openStream();

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] buf = new byte[1024];

		while ((nRead = is.read(buf, 0, buf.length)) != -1)
		{
			buffer.write(buf, 0, nRead);
		}

		byte[] b = buffer.toByteArray();

		String s = HexDumpUtil.formatHexDump(b, 0, b.length, "ISO-8859-15");

		System.out.println(s);
	}
}
