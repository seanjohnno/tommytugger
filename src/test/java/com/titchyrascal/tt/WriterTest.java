package com.titchyrascal.tt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.titchyrascal.json.read.Pusher;
import com.titchyrascal.json.read.Writer;

public class WriterTest extends PullerTest {

	private String mCurKey;

	@Test
	public void testWriter() throws IOException {
		
		// Create wrapped OutputStream
		final StringBuilder sb = new StringBuilder();
		OutputStream os = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				sb.append((char)b);
			}
		};

		// Write all events to wrapped OutputStream
		Writer writer = new Writer(os);
		InputStream json = getJsonInputStream();
		Pusher pusher = new Pusher();
		pusher.parse(json, (event) -> {
			try {
				switch(event.getEventType()) {
				case StartObject:
					writer.startObject(mCurKey);
					mCurKey = null;
					break;
				case EndObject:
					writer.endObject();
					break;
				case StartArray:
					writer.startArray(mCurKey);
					mCurKey = null;
					break;
				case EndArray:
					writer.endArray();
					break;
				case Key:
					mCurKey = event.getValue();
					break;
				case Value:
					writer.value(mCurKey, event.getValue());
					mCurKey = null;
					break;
				case DocumentFinished:
					break;
				}
			} catch(IOException ex) {
				Assert.fail();
			}
		});

		// Wrapped InputStream
		InputStream wrappedStream = new InputStream() {
			private int charIndex = 0;
			public int read() throws IOException {
				return charIndex >= sb.length() ? -1 : sb.charAt(charIndex++);
			}
		};
		
		// Test it's reformed properly
		testJsonDocument(wrappedStream);
		
	}
}
