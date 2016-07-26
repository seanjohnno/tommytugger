package com.titchyrascal.tt;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.titchyrascal.json.read.Event;
import com.titchyrascal.json.read.Puller;

public class PullerTest extends BaseJsonTest {

	private static Event[] events =  {
		new Event(Event.EventType.StartObject),
		new Event(Event.EventType.Key, "Name"),
		new Event(Event.EventType.Value, "Dave"),
		new Event(Event.EventType.Key, "Surname"),
		new Event(Event.EventType.Value, "Davidson"),
		new Event(Event.EventType.Key, "Age"),
		new Event(Event.EventType.Value, "30"),
		new Event(Event.EventType.Key, "Children"),
		new Event(Event.EventType.StartArray),
		new Event(Event.EventType.StartObject),
		new Event(Event.EventType.Key, "Name"),
		new Event(Event.EventType.Value, "Phil"),
		new Event(Event.EventType.Key, "Surname"),
		new Event(Event.EventType.Value, "Davidson"),
		new Event(Event.EventType.EndObject),
		new Event(Event.EventType.StartObject),
		new Event(Event.EventType.Key, "Name"),
		new Event(Event.EventType.Value, "Steve"),
		new Event(Event.EventType.Key, "Surname"),
		new Event(Event.EventType.Value, "Davidson"),
		new Event(Event.EventType.EndObject),
		new Event(Event.EventType.EndArray),
		new Event(Event.EventType.Key, "Hobbies"),
		new Event(Event.EventType.StartArray),
		new Event(Event.EventType.Value, "Snowboarding"),
		new Event(Event.EventType.Value, "Skateboarding"),
		new Event(Event.EventType.Value, "Surfing"),
		new Event(Event.EventType.EndArray),
		new Event(Event.EventType.EndObject),
		new Event(Event.EventType.DocumentFinished)
	};
	
	@Test
	public void testJsonDocument() throws IOException {
		InputStream doc = getJsonInputStream();
		Puller pp = new Puller(doc);
		for(int i = 0; i < events.length; i++) {
			Event lhs = pp.next();
			Event rhs = events[i];
			Assert.assertEquals( lhs.toString() + " vs " + rhs.toString(), lhs, rhs );
		}
		doc.close();
	}
}
