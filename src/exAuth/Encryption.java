package exAuth;

import java.security.MessageDigest;

public class Encryption
{
	private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	
	private static String toHexString (byte[] data)
	{
		int length = data.length;
		char[] buffer = new char[length << 1];
		
		for (int i = 0, j = 0; i < length; i++)
		{
			buffer[j++] = Encryption.DIGITS[(0xF0 & data[i]) >>> 4];
			buffer[j++] = Encryption.DIGITS[0x0F & data[i]];
		}
		return new String(buffer);
	}
	
	public static String SHA512 (String string)
	{
		try
		{
			return Encryption.toHexString(MessageDigest.getInstance("SHA-512").digest(string.getBytes("UTF-8")));
		}
		catch (Exception e)
		{
			return "";
		}
	}
	
	public static String SHA256 (String string)
	{
		try
		{
			return Encryption.toHexString(MessageDigest.getInstance("SHA-256").digest(string.getBytes("UTF-8")));
		}
		catch (Exception e)
		{
			return "";
		}
	}
	
	public static String MD5 (String string)
	{
		try
		{
			return Encryption.toHexString(MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8")));
		}
		catch (Exception e)
		{
			return "";
		}
	}
	
	public static String SHA1 (String string)
	{
		try
		{
			return Encryption.toHexString(MessageDigest.getInstance("SHA1").digest(string.getBytes("UTF-8")));
		}
		catch (Exception e)
		{
			return "";
		}
	}
}
