package streams;

import java.io.IOException;
import java.io.InputStream;

/*
 * Used so we can push back a characters - useful while parsing json
 */
public class PushBackInputStream extends InputStream {

	// MEMBER DATA
	
	/**
	 * Underlying stream we're wrapping
	 */
	private InputStream mUnderlyingStream;
	
	/**
	 * The last character returned from read()
	 */
	private int mLast;
	
	/**
	 * True if we want to re-return the last character
	 */
	private boolean mUseLast;
	
	// CONSTRUCTION
	
	/**
	 * @param stream Underlying stream
	 */
	public PushBackInputStream(InputStream stream) {
		mUnderlyingStream = stream;
	}
	
	// PUBLIC METHODS
	
	/**
	 * Pushes last character so next time read() is called it'll re-deliver last char
	 */
	public void pushLast() {
		mUseLast = true;
	}
	
	/**
	 * Underlying streams read() unless pushLast called, then it re-delivers last char
	 */
	@Override
	public int read() throws IOException {
		if(mUseLast) {
			mUseLast = false;
			return mLast;
		} else {
			return (mLast = mUnderlyingStream.read());
		}
	}
	
}
