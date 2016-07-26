package streams;

import java.io.IOException;
import java.io.InputStream;

/**
 * Wraps a String in an InputStream 
 */
public class StringInputStream extends InputStream {

	// MEMBER DATA
	
	/**
	 * Length of string (just so we're not calling the length method each time during the read)
	 */
	private int mLen;
	
	/**
	 * String we're wrapping
	 */
	private String mStr;
	
	/**
	 * Current character index
	 */
	private int mIndex;
	
	// CONSTRUCTION
	
	/**
	 * Construction
	 * @param str String to wrap
	 */
	public StringInputStream(String str) {
		mStr = str;
		mLen = str.length();
	}
	
	// PUBLIC METHODS
	
	/**
	 * @return Next character in string or -1 if finished
	 */
	@Override
	public int read() throws IOException {
		return mIndex < mLen ? mStr.charAt(mIndex++) : -1;
	}
}
