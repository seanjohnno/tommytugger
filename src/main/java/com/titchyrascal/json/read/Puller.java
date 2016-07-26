package com.titchyrascal.json.read;

import java.io.IOException;
import java.io.InputStream;

import com.titchyrascal.json.read.Event.EventType;

import streams.PushBackInputStream;
import streams.StringInputStream;

/**
 * Json pull parser
 */
public class Puller {

	// PRIVATE INTERFACES - Strategy pattern used to read string & int/bool/null values different

	/**
	 * Used in strategy pattern to determine when we've got to end of a key/val
	 */
	private interface IEndOfValueStrategy {
		/**
		 * @param c Last char that's been read
		 * @return	Return true when we've reached end of string
		 */
		public boolean hasFinished(int c);
	}

	/**
	 * Strategy for reading string value
	 */
	private class EndOfString implements IEndOfValueStrategy  {

		/**
		 * @return Returns false until it finds quote mark (that isn't preceded by \)
		 */
		public boolean hasFinished(int c) {
			int len = mStrBuilder.length();
			if(c == '"') {
				if(len > 0 && mStrBuilder.charAt(len-1) == '\\') {
					return false;
				}
				return true;
			}
			return false;
		}
	}

	/**
	 * Strategy for reading int/bool/null value
	 */
	private class EndOfValue implements IEndOfValueStrategy {

		/**
		 * @return Returns false until we move past end of val
		 */
		public boolean hasFinished(int c) {
			if(c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == ',' || c == '}' || c == ']') {
				mStream.pushLast();
				return true;
			}
			return false;
		}
	}

	// MEMBER DATA

	/**
	 * Strategy used to read until end of string
	 */
	private IEndOfValueStrategy END_OF_STRING_CHECK = new EndOfString();

	/**
	 * Strategy used to read until end of int/boolean/null
	 */
	private IEndOfValueStrategy END_OF_VALUE_CHECK = new EndOfValue();

	/**
	 * Wrapped json stream - useful for rewinding during parsing
	 */
	private PushBackInputStream mStream;

	/**
	 * StringBuilder used to build key/vals
	 */
	private StringBuilder mStrBuilder = new StringBuilder();

	/**
	 * Single json event recycled so we're not creating a new object for event json token
	 */
	private Event mEvent = new Event();

	// CONSTRUCTION

	/**
	 * @param str JSON document
	 */
	public Puller(String str) {
		this(new StringInputStream(str));
	}

	/**
	 * @param is JSON document
	 */
	public Puller(InputStream is) {
		mStream = new PushBackInputStream(is);
	}

	// PUBLIC METHODS

	/**
	 * @return Next JSON event
	 * @throws IOException
	 */
	public Event next() throws IOException {
		int c;
		while((c = mStream.read()) != -1) {
			switch(c) {
			case '{':
				mEvent.set(EventType.StartObject);
				return mEvent;
			case '}':
				mEvent.set(EventType.EndObject);
				return mEvent;
			case '[':
				mEvent.set(EventType.StartArray);
				return mEvent;
			case ']':
				mEvent.set(EventType.EndArray);
				return mEvent;
			case '"':
				// Encountered a string so now read until end
				mStrBuilder.setLength(0);
				setKeyOrVal( read( END_OF_STRING_CHECK ) );
				return mEvent;
			default:
				// Start of int 0-9, bool true/false, decimal or exponent
				if((c >= '0' && c <= '9') || c == 't' || c == 'f' || c == 'n' || c == '.' || c == 'e' || c == 'E') {
					mStrBuilder.setLength(0);
					mStrBuilder.append((char)c);

					// Encountered a non-string val so now read until end
					setKeyOrVal( read(END_OF_VALUE_CHECK) );

					return mEvent;
				}
			}
		}
		
		// If we've reached here then the json document is finished
		mEvent.set(EventType.DocumentFinished);
		return mEvent;
	}

	// PRIVATE METHODS

	/**
	 * Reads next token after string to determine whether it's a key or val
	 * @param val 
	 * @throws IOException
	 */
	private void setKeyOrVal(String val) throws IOException {
		int c;
		while((c = mStream.read()) != -1) {
			switch(c) {
			case ':':
				mEvent.set(EventType.Key, val);
				return;
			case ',':
				mEvent.set(EventType.Value, val);
				return;
			case '}':
			case ']':
				// Push } OR ] back  into stream so the next next() will return the correct event
				mEvent.set(EventType.Value, val);
				mStream.pushLast();
				return;
			}
		}

	}

	/**
	 * Keeps reading a key/value until the supplied strategy determines it's been read
	 * @param strategy
	 * @return
	 * @throws IOException
	 */
	private String read(IEndOfValueStrategy strategy) throws IOException {
		int c;
		while( ( c = mStream.read() ) != -1 ) {
			if(!strategy.hasFinished(c)) {
				mStrBuilder.append((char)c);
			} else {
				return mStrBuilder.toString();
			}
		}
		return mStrBuilder.toString();
	}
}
