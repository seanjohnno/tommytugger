package com.titchyrascal.json.read;

/**
 * Represents an event as we're parsing a json document
 */
public class Event {
	
	// ENUMS
	
	/**
	 * Represent the various events we can encounter while parsing document
	 */
	public enum EventType { StartObject, EndObject, StartArray, EndArray, Key, Value, DocumentFinished  }

	// MEMEBER DATA
	
	/*
	 * Current key/val or null if another event
	 */
	private String mValue;
	
	/**
	 * Type of json event
	 */
	private EventType mEvent;

	// CONSTRUCTION
	
	public Event() {
		
	}
	
	public Event(EventType event) {
		mEvent = event;
	}
	
	public Event(EventType event, String val) {
		mEvent = event;
		mValue = val;
	}
	
	// PUBLIC METHODS
	
	/**
	 * @return Valid string if Key/Value event, otherwise null
	 */
	public String getValue() {
		return mValue;
	}

	/**
	 * @return Current json event
	 */
	public EventType getEventType() {
		return mEvent;
	}
	
	/**
	 * Sets the event/value
	 * @param event
	 * @param value
	 */
	public void set(EventType event, String value) {
		mEvent = event;
		mValue = value;
	}

	/**
	 * Sets the event, value is set to null
	 * @param event
	 */
	public void set(EventType event)  {
		set(event, null);
	}
	
	// FROM OBJECT
	
	/**
	 * @return True if events match
	 */
	public boolean equals(Object rhs) {
		if(rhs instanceof Event) {
			Event eRhs = (Event)rhs;
			return mEvent == eRhs.mEvent && 
					( ( mValue == null && eRhs.mValue == null ) || ( mValue != null && eRhs.mValue != null && mValue.equals(eRhs.mValue) ) );
		}
		return false;
	}
	
	public String toString() {
		return "EventType: " + mEvent.toString() + " Value: " + mValue == null ? "null" : mValue;
	}
}
