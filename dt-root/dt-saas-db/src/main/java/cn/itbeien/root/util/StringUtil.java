package cn.itbeien.root.util;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public class StringUtil {
	
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	public static final String EMPTY = "";
	
	/**
	 * 空格
	 */
	public static final char DEFAILT_FILLING_CHAR = 32;
	public static final char QP_SIGN_AND = '&';
 	public static final char QP_SIGN_EQU = '=';
 	public static final char QP_SIGN_REQ = '?';

	/**
	 * 方法用途: 指定长度，左补空格<br>
	 * 实现步骤: <br>
	 * @param src
	 * @param len
	 * @return
	 */
	public static final String filling(String src, int len) {
		return filling(src, len, DEFAILT_FILLING_CHAR);
	}

	/**
	 * 方法用途: 指定长度len，左补指定字符fillChar<br>
	 * 实现步骤: <br>
	 * @param src
	 * @param len
	 * @param fillChar
	 * @return
	 */
	public static final String filling(String src, int len, char fillChar) {
		return filling(src, len, fillChar, true);
	}

	/**
	 * 方法用途: 指定长度len, 向左(leftFilling=true)或向右(leftFilling=false)填充指定字符fileChar<br>
	 * 实现步骤: <br>
	 * @param src
	 * @param len
	 * @param fillChar
	 * @param leftFilling
	 * @return
	 */
	public static final String filling(String src, int len, char fillChar, boolean leftFilling) {
		return filling(null, src, len, fillChar, leftFilling);
	}
	
	public static final String filling(String pre, String src, int len, char fillChar) {
		return filling(pre, src, len, fillChar, true);
	}
	
	
	public static final String checkCodeLength(String parameter, int maxLenght, boolean leftTrac) {

		if (parameter.length() >= maxLenght) {

			if (leftTrac) {
				return parameter.substring(parameter.length() - maxLenght, parameter.length());
			} else {
				return parameter;
			}

		} else {
			StringBuffer res = new StringBuffer();
			for (int i = 0; i < maxLenght - parameter.length(); i++) {
				res.append("0");
			}
			return res.append(parameter).toString();
		}

	}
	
	public static final String filling(String pre, String src, int len, char fillChar, boolean leftFilling) {
		char[] res = new char[len];
		char[] srcChars = src.toCharArray();
		int srcLen = srcChars.length;
		char[] preChars = isEmpty(pre) ? new char[0] : pre.toCharArray();
		int preLen = preChars.length;
		
		if(preLen + srcLen > len) {
			res = new char[preLen + srcLen];
			System.arraycopy(preChars, 0, res, 0, preLen);
			System.arraycopy(srcChars, 0, res, preLen, srcLen);
			return new String(res);
		}
		
		System.arraycopy(preChars, 0, res, 0, preLen);
		
		int diffLen = len - preLen - srcLen;

		if (leftFilling) {
			System.arraycopy(srcChars, 0, res, diffLen + preLen, srcLen);
			for (int i = preLen; i < preLen + diffLen; i++) {
				res[i] = fillChar;
			}
		} else {
			System.arraycopy(srcChars, 0, res, preLen, srcLen);
			for (int i = preLen; i < preLen + diffLen; i++) {
				res[i + srcLen] = fillChar;
			}
		}

		return new String(res);
	}
	
	public static final String FORMAT_SPECIFIER = "\\{(\\d+)\\}";
	
	public static final Pattern FORMAT_STRING_PATTERN = Pattern.compile(FORMAT_SPECIFIER);
	
	/**
	 * 方法用途: template like "sub{0},{1}"<br>
	 * 实现步骤: <br> 
	 * 在效率上远高于String.replace()方式
	 * @param template 模板
	 * @param args 替换的变量
	 * @return
	 */
	public static final String format(String template, Object[] args){
		Matcher matcher = FORMAT_STRING_PATTERN.matcher(template);
		char[] tchars = template.toCharArray();
		StringBuilder result = new StringBuilder();
		int lastIndex = 0;
		while(matcher.find()){
			result.append(tchars, lastIndex, matcher.start() - lastIndex);
			try{
				int index = Integer.parseInt(matcher.group(1));
				if(args.length > index && args[index] != null){
					result.append(args[index]);
				}
			}catch(NumberFormatException e){
			}
			
			lastIndex = matcher.end();
		}
		result.append(tchars, lastIndex, tchars.length - lastIndex);
		return result.toString();
	}
	
	/**
	 * 方法用途: 忽略模板中指定的变量进行替换 template like "sub{0},{1}"<br>
	 * 实现步骤: <br>
	 * @param template 模板
	 * @param args 替换的变量
	 * @param ignoreIndexs 忽略的模板变量
	 * @return
	 */
	public static final String format(String template, Object[] args, int[] ignoreIndexs){
		Matcher matcher = FORMAT_STRING_PATTERN.matcher(template);
		char[] tchars = template.toCharArray();
		StringBuilder result = new StringBuilder();
		int lastIndex = 0;
		
		while(matcher.find()){
			int matcherStartIndex = matcher.start();
			int matcherLastIndex = matcher.end();
			
			result.append(tchars, lastIndex, matcherStartIndex - lastIndex);
			try{
				int index = Integer.parseInt(matcher.group(1));
				boolean isIgnore = false;
				if(null != ignoreIndexs && ignoreIndexs.length > 0){
					for(int i=0; i<ignoreIndexs.length; i++){
						if(index == ignoreIndexs[i]){
							result.append(tchars, matcherStartIndex, matcherLastIndex - matcherStartIndex);
							isIgnore = true;
							break;
						}
					}
				}
				
				if(!isIgnore && args.length > index && args[index] != null){
					result.append(args[index]);
				}
				
			}catch(NumberFormatException e){
			}
			
			lastIndex = matcherLastIndex;
		}
		result.append(tchars, lastIndex, tchars.length - lastIndex);
		return result.toString();
	}
	
	/**
	 * 方法用途: 对模板中指定的变量进行替换<br>
	 * 实现步骤: <br>
	 * @param template 模板
	 * @param argIndexs 指定替换模板中的变量
	 * @param args 替换的变量
	 * @return
	 */
	public static final String format(String template, int[] argIndexs, Object[] args){
		Matcher matcher = FORMAT_STRING_PATTERN.matcher(template);
		char[] tchars = template.toCharArray();
		StringBuilder result = new StringBuilder();
		int lastIndex = 0;
		
		while(matcher.find()){
			int matcherStartIndex = matcher.start();
			int matcherLastIndex = matcher.end();
			
			result.append(tchars, lastIndex, matcherStartIndex - lastIndex);
			try{
				int index = Integer.parseInt(matcher.group(1));
				if(null != argIndexs && null != args && argIndexs.length > 0){
					for(int i=0; i<argIndexs.length; i++){
						if(index == argIndexs[i] && args.length > i){
							result.append(args[i]);
							break;
						}
					}
				}
			}catch(NumberFormatException e){
			}
			
			lastIndex = matcherLastIndex;
		}
		result.append(tchars, lastIndex, tchars.length - lastIndex);
		return result.toString();
	}
	
	/**
	 * [null | ""] = false
	 */
	public static final int GETBOOL_CM_BLANK_IS_FALSE = 0;
	
	/**
	 * [null | ""] = null
	 */
	public static final int GETBOOL_CM_BLANK_IS_NULL = 1;
	/**
	 * null = null, "" = false
	 */
	public static final int GETBOOL_CM_EMPTY_IS_FALSE = 2;
	
	/**
	 * converting a string to a boolean
	 * <br/> Ignore case [ N | NO | F | FALSE | 0 ] = false another string is true;
	 * @param booleanStr
	 * @param conversionMode
	 * @return
	 */
	public static Boolean getBoolean(String booleanStr, int conversionMode){
		if(booleanStr == null){
			if(GETBOOL_CM_BLANK_IS_FALSE != conversionMode){
				return null;
			}else{
				return false;
			}
		}
		
		if(isEmpty(booleanStr)){
			if(GETBOOL_CM_BLANK_IS_NULL == conversionMode){
				return null;
			}else{
				return false;
			}
		}
		
		if("N".equalsIgnoreCase(booleanStr) || "NO".equalsIgnoreCase(booleanStr) || "F".equalsIgnoreCase(booleanStr) || "FALSE".equalsIgnoreCase(booleanStr) || "0".equals(booleanStr)){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * converting a string to a numeric
	 * @param numberStr
	 * @param numberClass
	 * @param ableBlank
	 * @return value
	 * @throws Exception
	 */
	public static <T> T getNumber(String numberStr, Class<T> numberClass, boolean ableBlank) throws NumberFormatException{
		if(StringUtil.isBlank(numberStr)){
			if(ableBlank){
				return null;
			}else{
				throw new NumberFormatException("disable blank , but " + numberStr);
			}
		}
		
		if(!isNumeric(numberStr)){
			throw new NumberFormatException("string \""+ numberStr +"\" isnot a number.");
		}
		
		if(numberClass == Integer.class){
			return numberClass.cast(Integer.valueOf(numberStr));
		}
		
		if(numberClass == Long.class){
			return numberClass.cast(Long.valueOf(numberStr));
		}
		
		if(numberClass == Double.class){
			return numberClass.cast(Double.valueOf(numberStr));
		}
		
		if(numberClass == Float.class){
			return numberClass.cast(Float.valueOf(numberStr));
		}
		
		if(numberClass == BigDecimal.class){
			return numberClass.cast(new BigDecimal(numberStr));
		}
		
		throw new NumberFormatException("Class " + numberClass.getName() + " cannot support.");
	}
	
	/**
	 * <p>get the sub string from src.</p>
     *
     * <pre>
     * StringUtil.subString(null, null)      = ""
     * StringUtil.subString("", null)        = ""
     * StringUtil.subString(" ", " ")       = ""
     * StringUtil.subString("bob", "o")     = "b"
     * StringUtil.subString("  bob  ", "o") = "  bo"
     * </pre>
	 * @param src
	 * @param limit
	 * @return
	 */
	public static String subString(String src, String limit){
		if(src == null){
			return EMPTY;
		}
		
		if(limit == null){
			return src;
		}
		
		int skipIndex = src.indexOf(limit);
		
		if(skipIndex != -1){
			return src.substring(0, skipIndex);
		}else{
			return src;
		}
	}
	
	/**
     * <p>Checks if a String is empty ("") or null.</p>
     *
     * <pre>
     * StringUtil.isEmpty(null)      = true
     * StringUtil.isEmpty("")        = true
     * StringUtil.isEmpty(" ")       = false
     * StringUtil.isEmpty("bob")     = false
     * StringUtil.isEmpty("  bob  ") = false
     * </pre>
     *
     * <p>NOTE: This method changed in Lang version 2.0.
     * It no longer trims the String.
     * That functionality is available in isBlank().</p>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
    
    /**
     * <p>Checks if a String is not empty ("") and not null.</p>
     *
     * <pre>
     * StringUtil.isNotEmpty(null)      = false
     * StringUtil.isNotEmpty("")        = false
     * StringUtil.isNotEmpty(" ")       = true
     * StringUtil.isNotEmpty("bob")     = true
     * StringUtil.isNotEmpty("  bob  ") = true
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is not empty and not null
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
	 /**
     * <p>Checks if a String is whitespace, empty ("") or null.</p>
     *
     * <pre>
     * StringUtil.isBlank(null)      = true
     * StringUtil.isBlank("")        = true
     * StringUtil.isBlank(" ")       = true
     * StringUtil.isBlank("bob")     = false
     * StringUtil.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is null, empty or whitespace
     * @since 2.0
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * <p>Checks if the String contains only unicode digits.
     * A decimal point is not a unicode digit and returns false.</p>
     *
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>true</code>.</p>
     *
     * <pre>
     * StringUtils.isNumeric(null)   = false
     * StringUtils.isNumeric("")     = false
     * StringUtils.isNumeric("  ")   = false
     * StringUtils.isNumeric("123")  = true
     * StringUtils.isNumeric("12 3") = false
     * StringUtils.isNumeric("ab2c") = false
     * StringUtils.isNumeric("12-3") = false
     * StringUtils.isNumeric("12.3") = true
	 * StringUtils.isNumeric("-12.3") = true
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if only contains digits, and is non-null
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        int sz = str.length();
        boolean hasFloatSymbol = false;
        for (int i = 0; i < sz; i++) {
        	char c = str.charAt(i);
        	if (i == 0 && c == '-' && sz>1) {
        		continue;
			}
            if (!Character.isDigit(c)) {
                if(c == '.' && !hasFloatSymbol){
                	hasFloatSymbol = true;
                }else{
                	return false;
                }
            }
        }
        return true;
    }
    
    /**
     * <p>Checks if the String contains only unicode digits.
     * A decimal point is not a unicode digit and returns false.</p>
     *
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>true</code>.</p>
     *
     * <pre>
     * StringUtils.isInteger(null)   = false
     * StringUtils.isInteger("")     = false
     * StringUtils.isInteger("  ")   = false
     * StringUtils.isInteger("123")  = true
     * StringUtils.isInteger("12 3") = false
     * StringUtils.isInteger("ab2c") = false
     * StringUtils.isInteger("12-3") = false
     * StringUtils.isInteger("12.3") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if only contains digits, and is non-null
     */
    public static boolean isInteger(String str){
    	if (isEmpty(str)) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * <p>Checks if a String is not empty (""), not null and not whitespace only.</p>
     *
     * <pre>
     * StringUtil.isNotBlank(null)      = false
     * StringUtil.isNotBlank("")        = false
     * StringUtil.isNotBlank(" ")       = false
     * StringUtil.isNotBlank("bob")     = true
     * StringUtil.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is
     *  not empty and not null and not whitespace
     * @since 2.0
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
	
    /**
     * <p>change a blank string to Null</p>
     * <pre>
     * StringUtil.blankToNull("  ")      = null
     * StringUtil.blankToNull(null)      = null
     * StringUtil.blankToNull("")        = null
     * StringUtil.blankToNull(" string") = " string"
     * </pre>
     * @param str
     * @return
     */
    public static String blankToNull(String str) {
    	return isBlank(str) ? null : str;
    }
    
    /**
     * <p>change a blank string to empty("")</p>
     * <pre>
     * StringUtil.blankToNull("  ")      = ""
     * StringUtil.blankToNull(null)      = ""
     * StringUtil.blankToNull("")        = ""
     * StringUtil.blankToNull(" string") = " string"
     * </pre>
     * @param str
     * @return
     */
    public static String blankToEmpty(String str){
    	return isBlank(str) ? EMPTY : str;
    }
    
    /** 
    * 去掉字符串里面的html代码。<br> 
    * 要求数据要规范，比如大于小于号要配套,否则会被集体误杀。 
    * 
    * @param content  内容 
    * @return 去掉后的内容 
    */ 
    public static String stripHtml(String content) {
		// <p>段落替换为换行
		content = content.replaceAll("<p .*?>", "\r\n");
		// <br><br/>替换为换行
		content = content.replaceAll("<br\\s*/?>", "\r\n");
		// 去掉其它的<>之间的东西
		content = content.replaceAll("\\<.*?>", "");
		return content;
	}
    
    
 	/**
 	 * 根据文件路径获取文件名
 	 * 方法用途: <br>
 	 * 实现步骤: <br>
 	 * @param filePath
 	 * @return
 	 */
 	public static String getFileNameByPath(String filePath) {
 		
 		filePath = filePath.trim().replace("\\", "/");
         String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
         
         return fileName;
 	}
 	
 	 /**
 		 * ascending sort comparator
 		 */
 		private static final Comparator<String> ASC_ORDER = new Comparator<String>(){
 			@Override
 			public int compare(String o1, String o2) {
 				return String.CASE_INSENSITIVE_ORDER.compare(o1, o2);
 			}
 		};
 		/**
 		 * descending sort comparator
 		 */
 		private static final Comparator<String> DSC_ORDER = new Comparator<String>(){
 			@Override
 			public int compare(String o1, String o2) {
 				return String.CASE_INSENSITIVE_ORDER.compare(o2, o1);
 			}
 		};
 	
 	/**
 	 * <p>sort String collection </p>
      * <pre>
      * StringUtil.sort({"c", "a", "b"}, true)      = {"a", "b", "c"}
      * StringUtil.sort({"c", "a", "b"}, false)     = {"c", "b", "a"}
      * </pre>
 	 * @param s
 	 * @param isAscending <code>true</code> is ascending sort else descending sort
 	 * @return
 	 */
 	public static final String[] sort(Collection<String> s, boolean isAscending){
 		return sort(s.toArray(new String[0]), isAscending);
 	}
 	
 	/**
 	 * <p>sort String arrays </p>
      * <pre>
      * StringUtil.sort(new String[]{"c", "a", "b"}, true)      = {"a", "b", "c"}
      * StringUtil.sort(new String[]{"c", "a", "b"}, false)     = {"c", "b", "a"}
      * </pre>
 	 * @param s
 	 * @param isAscending <code>true</code> is ascending sort else descending sort
 	 * @return
 	 */
 	public static final String[] sort(String[] s, boolean isAscending){
         Arrays.sort(s, isAscending ? ASC_ORDER : DSC_ORDER);
 		return s;
 	}
 	
 	public static String formatSampleFullURL(String url, Map<String, String> parameters, Charset charset) {
 		if(parameters == null || parameters.isEmpty()){
 			return url;
 		}
 		
 		Map<String, String[]> params = new HashMap<String, String[]>();
 		
 		for (final Entry<String, String> parameter : parameters.entrySet()) {
        	params.put(parameter.getKey(), new String[]{parameter.getValue()});
        }
 		
 		return formatFullURL(url, params, charset);
 	}
 	
 	public static String formatFullURL(String url, Map<String, String[]> parameters, Charset charset) {
 		StringBuilder f_url = new StringBuilder(url);
		
		if(parameters != null && !parameters.isEmpty()){
			if (url.contains(String.valueOf(QP_SIGN_REQ))) {
				f_url.append(QP_SIGN_AND);
			} else {
				f_url.append(QP_SIGN_REQ);
			}
			f_url.append(formatURLEncoded(parameters, null));
		}
		
		return f_url.toString();
 	}
 	
 	public static String formatURLEncoded(Map<String, String[]> parameters, Charset charset) {
        final StringBuilder result = new StringBuilder();
        for (final Entry<String, String[]> parameter : parameters.entrySet()) {
        	String key = parameter.getKey();
        	String[] values = parameter.getValue();
        	
        	for(String v : values){
        		
        		final String encodedName = encodeFormFields(key, charset);
        		final String encodedValue = encodeFormFields(v, charset);
        		if (result.length() > 0) {
        			result.append(QP_SIGN_AND);
        		}
        		result.append(encodedName);
        		if (encodedValue != null) {
        			result.append(QP_SIGN_EQU);
        			result.append(encodedValue);
        		}
        	}
        	
        }
        return result.toString();
    }
 	
 	/**
     * Safe characters for x-www-form-urlencoded data, as per java.net.URLEncoder and browser behaviour,
     * i.e. alphanumeric plus {@code "-", "_", ".", "*"}
     */
    private static final BitSet URLENCODER   = new BitSet(256);
    
    static {
        for (int i = 'a'; i <= 'z'; i++) {
        	URLENCODER.set(i);
        }
        for (int i = 'A'; i <= 'Z'; i++) {
        	URLENCODER.set(i);
        }
        // numeric characters
        for (int i = '0'; i <= '9'; i++) {
        	URLENCODER.set(i);
        }
        URLENCODER.set('_'); // these are the charactes of the "mark" list
        URLENCODER.set('-');
        URLENCODER.set('.');
        URLENCODER.set('*');
    }
    
 	/**
     * Encode/escape www-url-form-encoded content.
     * <p>
     * Uses the {@link #URLENCODER} set of characters, rather than
     *
     * releases, URLEncoder.encode() and most browsers.
     *
     * @param content the content to encode, will convert space to '+'
     * @param charset the charset to use
     * @return encoded string
     */
    private static String encodeFormFields (final String content, final Charset charset) {
        if (content == null) {
            return null;
        }
        return urlEncode(content, charset != null ? charset : DEFAULT_CHARSET, URLENCODER, true);
    }
    
 	private static final int RADIX = 16;
 	
 	private static String urlEncode(
            final String content,
            final Charset charset,
            final BitSet safechars,
            final boolean blankAsPlus) {
        if (content == null) {
            return null;
        }
        final StringBuilder buf = new StringBuilder();
        final ByteBuffer bb = charset.encode(content);
        while (bb.hasRemaining()) {
            final int b = bb.get() & 0xff;
            if (safechars.get(b)) {
                buf.append((char) b);
            } else if (blankAsPlus && b == ' ') {
                buf.append('+');
            } else {
                buf.append("%");
                final char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, RADIX));
                final char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, RADIX));
                buf.append(hex1);
                buf.append(hex2);
            }
        }
        return buf.toString();
    }
 	
 	private static final char[] DETAULT_RANDOM_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
 	
 	private static String getRandomStr(final int len, final char[] set){
 		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(set[((int) Math.floor(Math.random() * set.length))]);
		}
		
 		return sb.toString();
 	}
 	
 	public static String getRandomStr(final int len){
 		return getRandomStr(len, DETAULT_RANDOM_SET);
 	}
 	
 	public static String getRandomStr(final int len, final TreeSet<Character> set){
 		char[] charset = new char[set.size()];
 		for (int i=0; !set.isEmpty(); i++) {
 			charset[i] = set.pollFirst();
 		}
 		return getRandomStr(len, charset);
 	}
 	
	public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


	/**
	 * 替换字符串中的${}
	 * @param template
	 * @param params
	 * @return
	 */
	public static String replaceTemplate(String template, Map<String, Object> params) {
		StringBuffer sb = new StringBuffer();
		Matcher m = Pattern.compile("\\$\\{\\w+\\}").matcher(template);
		while (m.find()) {
			String param = m.group();
			Object value = params.get(param.substring(2, param.length() - 1));
			m.appendReplacement(sb, value == null ? "" : value.toString());
		}
		m.appendTail(sb);
		return sb.toString();
	}

	public static String splitByListByString(List<String> strList, String splitChar){
		StringBuffer res = new StringBuffer();
		for(int i =0;i<strList.size();i++){
			if(i<strList.size()-1){
				res.append(strList.get(i)).append(splitChar);
			}else{
				res.append(strList.get(i));
			}
		}
		return res.toString();
	}

	public static String null2Empty(Object str) {

		return str == null ? "" : str.toString();
	}


	public static String relpaceEncodeStr(String param, int begin, int end) {
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < param.length(); i++) {

			if (i > begin && i < param.length() - end) {
				res.append("*");
			} else {
				res.append(param.charAt(i));
			}
		}
		return res.toString();
	}


	/**
	 * 删除字符串指定位置中的一个字符
	 * @param s
	 * @param pos
	 * @return
	 */
	public static String removeCharAt(String s, int pos) {
		return s.substring(0, pos) + s.substring(pos + 1);
	}

 	public static void main(String[] args) {

		System.out.println(getRandomStr(10));
	}
}
