package com.titchyrascal.json.read;

import java.io.IOException;
import java.io.InputStream;

import com.titchyrascal.json.read.Event.EventType;

import streams.StringInputStream;

/**
 * Wraps the Puller to push events to the event handler
 */
public class Pusher {
	
	// INTERFACES
	
	/**
	 * Implement to receive all the json events from a document
	 */
	public interface IEventHandler {
		public void handleEvent(Event event);
	}
	
	// CONSTRUCTION
	
	public Pusher() {
		
	}
	
	// PUBLIC METHODS
	
	/**
	 * Runs through a json document passing all events to the handler
	 * @param json
	 * @param handler
	 * @throws IOException
	 */
	public void parse(String json, IEventHandler handler) throws IOException {
		parse( new StringInputStream(json), handler );
	}
	
	/**
	 * Runs through a json document passing all events to the handler
	 * @param stream
	 * @param handler
	 * @throws IOException
	 */
	public void parse(InputStream stream, IEventHandler handler) throws IOException {
		Event event;
		Puller puller = new Puller(stream);
		while(true) {
			handler.handleEvent(event = puller.next());
			
			if(event.getEventType() == EventType.DocumentFinished) {
				return;
			}
		}
	}
	
}
