package com.mock.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;

public class StringsUtils {
	
	/**
	 * splice strings
	 * @param strings 
	 * @return String
	 */
	public static String splice(String ... strings){
		if(strings==null||strings.length==0){
			return "";
		}
		StringBuilder sBuilder = new StringBuilder();
		for(String sub:strings){
			sBuilder.append(sub);
		}
		return sBuilder.toString();
	}
	/**
	 * @param sequence
	 * @return Returns true if a character sequence contains only digit
	 */
	public static boolean isDigit(CharSequence sequence){
		if(!CharMatcher.JAVA_DIGIT.matchesAllOf(sequence))
		{
			return false;
		}
		else
		{
			return true;
		}
		    	
	}
	
	/**
	 * @param sequence
	 * @return Returns true if a character sequences array contains only digit
	 */
	public static boolean isAllDigit(CharSequence[] sequences){
		for(CharSequence testSeq : sequences)
		{
			if(!isDigit(testSeq))
			{
				return false;
			}
		}
		return true;
	}
	
	public static boolean isAllDigit(Iterable<String> sequences){
		for(CharSequence testSeq : sequences)
		{
			if(!isDigit(testSeq))
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @Description	one at least not null, if all are blank,return null
	 * @param param
	 * @return
	 */
	public static  String oneAtLeastNotNull(String... param) {
		for (String string : param) {
			if(StringUtils.isNotBlank(string)){
				return string;
			}
		}
		return null;
	}
	
	public static Iterable<String> spliterItFromStr(CharSequence sequence, char separator)
	{
		Iterable<String> strs = Splitter.on(Preconditions.checkNotNull(separator))
				.split(Preconditions.checkNotNull(sequence));
        return strs;
	}
	
	public static String listToString(List<String> list) {  
	    StringBuilder sb = new StringBuilder();  
	    if (list != null && list.size() > 0) {  
	        for (int i = 0; i < list.size(); i++) {  
	            if (i < list.size() - 1) {  
	                sb.append(list.get(i) + ",");  
	            } else {  
	                sb.append(list.get(i));  
	            }  
	        }  
	    }  
	    return sb.toString();  
	} 
	
	/**
	 * 
	 * @param sourceString
	 * @param separator
	 * @return
	 */
	public static List<String> stringToList(String sourceString,String separator){
		ArrayList<String> result = null;
		if(sourceString!=null){
			String[] array = sourceString.split(separator);
			result = new ArrayList<>();
			result.addAll(Arrays.asList(array));
			return result;
		}else{
			return null;
		}
	}
	
	public static Set<String> stringToHashSet(String sourceString,String separator){
		if(sourceString!=null){
			Set<String> setStrings = new HashSet<String>();
			String[] array = sourceString.split(separator);
			setStrings.addAll(Arrays.asList(array));
			return setStrings;
		}else{
			return null;
		}
	}
	
	public static boolean isFloat(String string){
		try{
			Float.parseFloat(string);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}
	
	public static boolean isDouble(String string){
		try{
			Double.parseDouble(string);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}
	
	public static String inputStreamToString(InputStream is)
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = " ";
		try {
			while ((line = in.readLine()) != null){
			     buffer.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
}
