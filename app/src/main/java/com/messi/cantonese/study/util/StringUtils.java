package com.messi.cantonese.study.util;

public class StringUtils {
	
	public static void setSpeakerByLan(String lan) {
		if(lan.equals("en")){
			Settings.role = XFUtil.SpeakerEn;
		}else{
			Settings.role = XFUtil.SpeakerZh;
		}
	}
	
	public static void setSpeaker(String content) {
		Settings.role = XFUtil.SpeakerZh;
		LogUtil.DefalutLog(content+"---"+isEnglish(content));
		if (isEnglish(content)) {
			Settings.role = XFUtil.SpeakerEn;
		}
	}

	public static boolean isEnglish(String charaString) {
		char[] arr = charaString.toCharArray();
		int count = 0;
		for (int i = 0; i < arr.length; i++) {
			if ((arr[i] >= 65 && arr[i] <= 90) || (arr[i] >= 97 && arr[i] <= 125) || (arr[i] == 39)
					|| (arr[i] == 8217)) {
				count++;
			}
		}
		return (double)count / arr.length  > 0.5;
	}
	
	public static boolean isChinese(String str) {
		boolean result = true;
		char[] ch = str.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!isChinese(c)) {
				result = false;
				break;
			}
		}
		return result;
	}
	
	

	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

}
