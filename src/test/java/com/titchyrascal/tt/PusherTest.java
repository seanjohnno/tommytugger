package com.titchyrascal.tt;

import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

import com.titchyrascal.json.read.Writer;

public class PusherTest {

	@Test
	public void testPusher() throws IOException {
//		InputStream json = this.getClass().getResourceAsStream("pusherjson.json");
//		Pusher pusher = new Pusher();
//		pusher.parse(json, (event) -> {
//			if(event.getEventType() == EventType.Key) {
//				System.out.println( event.getEventType().toString() + ": " + event.getValue() );
//			} else if(event.getEventType() == EventType.Value) {
//				System.out.println( event.getEventType().toString() + ": " + event.getValue() );
//			} else {
//				System.out.println( event.getEventType().toString() );
//			}
//		});
	}
	
	@Test
	public void testWriter() throws IOException {
		final StringBuilder sb = new StringBuilder();
		OutputStream os = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				sb.append((char)b);
			}
		};
		
		Writer writer = new Writer(os);
		writer.startObject();
			writer.value("Name", "Dave");
			writer.value("Surname", "Phillips");
			writer.startArray("Children");
				writer.startObject();
				writer.value("Name", "Phil");
				writer.value("Surname", "Phillips");
				writer.endObject();
				writer.startObject();
				writer.value("Name", "Steve");
				writer.value("Surname", "Phillips");
				writer.endObject();
			writer.endArray();
			writer.startArray("Hobbies");
				writer.value("Snowboarding");
				writer.value("Skating");
				writer.value("Surfing");
			writer.endArray();
		writer.endObject();
		System.out.println(sb.toString());
	}
}
