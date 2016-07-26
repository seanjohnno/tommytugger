package com.titchyrascal.tt;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.titchyrascal.json.read.Pusher;


public class PusherTest extends PullerTest {

	private int mCurrent;
	
	@Test
	public void testPusher() throws IOException {
		mCurrent = 0;
		InputStream json = getJsonInputStream();
		Pusher pusher = new Pusher();
		pusher.parse(json, (event) -> {
			Assert.assertEquals(event, EVENTS[mCurrent]);
			mCurrent++;
		});
	}
}
