package trinhthanhbinh;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppHelper {
	
	public static boolean isContainLetter(String text) {
		Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher m = p.matcher(text);
        return m.find();
	}
	

}
