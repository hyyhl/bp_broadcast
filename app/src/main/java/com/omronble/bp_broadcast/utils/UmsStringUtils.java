package com.omronble.bp_broadcast.utils;

import android.text.TextUtils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.util.Locale;

public class UmsStringUtils extends StringUtils {

	/**
	 * 比较两个字符串是否相等，允许传入null值
	 * 
	 * @author wxtang 2015-4-14上午11:39:15
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean isEqual(String s1, String s2) {
		if (s1 == s2) {
			return true;
		}
		if (s1 == null && s2 != null) {
			return false;
		}
		return s1.equals(s2);
	}

	public static String changeCharArraytoString(char[] a, int length) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			stringBuffer.append(a[i]);
		}
		return stringBuffer.toString();
	}

	/**
	 * 获取文件名称（包含后缀）http://123/23.xml->23.xml
	 * 
	 * @param s
	 * @param ident
	 * @return
	 */
	public static String subString(String s, String ident) {
		return s.substring(s.lastIndexOf(ident) + 1);
	}

	public static String subString(String s, String ident, String identend) {
		return s.substring(s.lastIndexOf(ident) + 1, s.lastIndexOf(identend));
	}

	/**
	 * 超链接字符转换
	 * 
	 * @param str
	 * @return
	 */
	public static String filterToUrl(String str) {
		str = str.replace("%25", "%");
		str = str.replace("%23", "#");
		str = str.replace("%3F", "?");
		str = str.replace("%2F", "/");
		str = str.replace("%3D", "=");
		str = str.replace("%2C", ",");
		str = str.replace("%3B", ";");
		str = str.replace("%26", "&");
		str = str.replace("%20", " ");
		str = str.replace("%3C", "<");
		str = str.replace("%3E", ">");
		str = str.replace("%27", "'");
		str = str.replace("%22", "\"");
		return str;
	}

	public static String utf8tostring(String str) {
		String changeAfter;
		if (str == null || str.equals("")) {
			return null;
		}
		try {
			changeAfter = URLDecoder.decode(str, "UTF-8");
			return changeAfter;
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

	/**
	 * 去除文件名后缀 123.xml -> 123
	 * 
	 * @param str
	 * @return
	 */
	public static String deleteSuffix(String str) {
		int index = str.lastIndexOf(".");
		str = str.substring(0, index);
		return str;
	}

	/**
	 * 删除字符串分隔符"/"后半部分，如123/234/34.xml->123/234 若没有"/"，则返回"";如34.xml->""
	 * 
	 * @param str
	 * @return
	 */
	public static String deleteLastData(String str) {
		StringBuffer sb = new StringBuffer();
		int n = str.lastIndexOf("/");
		if (n == -1) {
			return "";
		}
		sb.append(str.substring(0, n));
		return sb.toString();
	}

	/**
	 * 删除最右边"/"后的字符串， "/"保留。 abc/efg -> abc/
	 * 
	 * @param str
	 * @return
	 */
	public static String deleteRight(String str) {
		StringBuffer sb = new StringBuffer();
		int n = str.lastIndexOf("/");
		if (n == -1) {
			return "";
		}
		sb.append(str.substring(0, n));
		sb.append("/");
		return sb.toString();
	}

	public static boolean isHttpUrl(String str) {
		if (TextUtils.isEmpty(str))
			return false;
		Locale defloc = Locale.getDefault();
		return str.toLowerCase(defloc).startsWith("http");
	}

	public static boolean isObjectUrl(String url) {
		if (TextUtils.isEmpty(url))
			return false;
		return url.startsWith("objc:");
	}

	public static boolean isBlankUrl(String url) {
		if (TextUtils.isEmpty(url))
			return true;
		Locale defloc = Locale.getDefault();
		return url.toLowerCase(defloc).equals("about:blank");
	}

	public static boolean isURI(String str) {
		if (TextUtils.isEmpty(str))
			return false;
		Locale defloc = Locale.getDefault();
		return str.toLowerCase(defloc).startsWith("file:///");
	}

	public static boolean isNull(String str) {
		if (TextUtils.isEmpty(str)) {
			return true;
		}
		Locale defloc = Locale.getDefault();
		return str.toLowerCase(defloc).equals("null");
	}

	public static boolean isNotEmpty(String str) {
		if (str == null || str.length() <= 0) {
			return false;
		}
		return true;
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.length() <= 0) {
			return true;
		}
		return false;
	}

	public static boolean isBlank(CharSequence cs) {
		int strLen = 0;
		if ((cs == null) || ((strLen = cs.length()) == 0))
			return true;
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(CharSequence cs) {
		return !isBlank(cs);
	}

	public static boolean hasValue(String value) {
		if (value != null && UmsStringUtils.isNotEmpty(value)
				&& UmsStringUtils.isNotBlank(value)) {
			return true;
		}
		return false;
	}

	/**
	 * @param str
	 * @return
	 */
	public static Integer convertStr2Integer(String str) {
		try {
			Integer integer = Integer.valueOf(str);
			return integer;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 
	 * 
	 * format2String 的实现描述：TODO 方法实现描述
	 * 
	 * @author yltang 2015-9-29 上午11:09:53
	 * @param bi
	 * @param maxLength
	 * @return 参数说明
	 * 
	 */
	public static String format2String(BigInteger bi, int maxLength) {
		String result = null;
		try {
			if (bi == null || bi.longValue() < 0 || maxLength <= 0) {
				return result;
			}
			result = String.format("%0" + maxLength + "d", bi);
		} catch (Exception e) {
		}
		return result;
	}
}
