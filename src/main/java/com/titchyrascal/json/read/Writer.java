package com.titchyrascal.json.read;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Stack;

/**
 * Writes a JSON string
 */
public class Writer {

	// STATIC DATA
	
	private static Charset UTF8 = Charset.forName("UTF-8");
	
	// MEMBER DATA
	
	/**
	 * Records whether the current object has children (so we know whether to write commas)
	 */
	private Stack<Integer> mChildCount = new Stack<>();
	
	/**
	 * Underlying outputstream to write to
	 */
	private OutputStream mStream;
	
	// CONSTRUCTION
		
	/**
	 * @param stream Underlying OutputStream to write to
	 */
	public Writer(OutputStream stream) {
		mStream = stream;
		mChildCount.push(0);
	}
	
	// PUBLIC METHODS
	
	/**
	 * Writes { without key
	 * @throws IOException
	 */
	public void startObject() throws IOException {
		startObject(null);
	}
	
	/**
	 * Writes "key": { with preceding comma if it's not the 1st child
	 * @param key
	 * @throws IOException
	 */
	public void startObject(String key) throws IOException {
		writeKeyVal(key, "{");
		mChildCount.push(0);
	}
	
	/**
	 * Writes }
	 * @throws IOException
	 */
	public void endObject() throws IOException {
		
		mStream.write('}');
		mChildCount.pop();
	}
	
	/**
	 * Writes [
	 * @throws IOException
	 */
	public void startArray() throws IOException {
		startArray(null);
	}
	
	/**
	 * Writes "key": [ with preceding comma if it's not the 1st child
	 * @param key
	 * @throws IOException
	 */
	public void startArray(String key) throws IOException {
		
		writeKeyVal(key, "[");
		mChildCount.push(0);
	}
	
	/**
	 * Writes ]
	 * @throws IOException
	 */
	public void endArray() throws IOException {
		
		mStream.write(']');
		mChildCount.pop();
	}
	
	/**
	 * Writes "val" with preceding comma if it's not the 1st child
	 * @param val
	 * @throws IOException
	 */
	public void value(String val) throws IOException {
		value(null, val);
	}
	
	/**
	 * Writes "key": "val" with preceding comma if it's not the 1st child
	 * @param key
	 * @param val
	 * @throws IOException
	 */
	public void value(String key, String val) throws IOException {
		writeKeyVal(key, val, true);
	}
	
	/**
	 * Writes true/false with preceding comma if it's not the 1st child
	 * @param val
	 * @throws IOException
	 */
	public void value(boolean val) throws IOException {
		writeKeyVal(null, val ? "true" : "false");
	}
	
	/**
	 * Writes "key": true/false with preceding comma if it's not the 1st child
	 * @param key
	 * @param val
	 * @throws IOException
	 */
	public void value(String key, boolean val) throws IOException {
		writeKeyVal(key, val ? "true" : "false");
	}
	
	/**
	 * Writes int with preceding comma if it's not the 1st child
	 * @param val
	 * @throws IOException
	 */
	public void value(int val) throws IOException {
		writeKeyVal(null, Integer.toString(val));
	}
	
	/**
	 * Writes "key": int with preceding comma if it's not the 1st child
	 * @param key
	 * @param val
	 * @throws IOException
	 */
	public void value(String key, int val) throws IOException {
		writeKeyVal(key, Integer.toString(val));
	}
	
	// PRIVATE METHODS
	
	/**
	 * Writes the "key": val (val needs to be passed in with surrounding quotes if it's a string) 
	 * with preceding comma if it's not the 1st childwith preceding comma if it's not the 1st child
	 * @param key
	 * @param val
	 * @throws IOException
	 */
	private void writeKeyVal(String key, String val) throws IOException {
		writeKeyVal(key, val, false);
	}
	
	/**
	 * Writes the "key": val (val needs to be passed in with surrounding quotes if it's a string) 
	 * with preceding comma if it's not the 1st childwith preceding comma if it's not the 1st child
	 * @param key
	 * @param val
	 * @throws IOException
	 */
	private void writeKeyVal(String key, String val, boolean strEncode) throws IOException {
		// If the current object has a child then write ,
		if(mChildCount.peek() > 0) {
			mStream.write(',');
		}
		
		// Write "key": if it isn't null
		if(key != null) {
			mStream.write('"');
			mStream.write(key.getBytes(UTF8));
			mStream.write('"');
			mStream.write(':');
		}		
		if(strEncode) {
			writeEncodedVal(val);
		} else {
			mStream.write(val.getBytes(UTF8));
		}

		// Increment child count so subsequent children will be preceeded by a ,
		int count = mChildCount.pop();
		mChildCount.push(++count);
	}
	
	/**
	 * Backslashes quotemarks so it doesn't muck up json
	 * @param val
	 * @throws IOException 
	 */
	private void writeEncodedVal(String val) throws IOException {
		mStream.write('"');
		
		char c;
		for(int i = 0; i < val.length(); i++) {
			c = val.charAt(i);
			if(c == '"') {
				mStream.write('\\');
			}
			mStream.write((char)c);
		}
		mStream.write('"');
	}
}
