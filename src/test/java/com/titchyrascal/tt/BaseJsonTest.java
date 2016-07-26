package com.titchyrascal.tt;

import java.io.InputStream;

public class BaseJsonTest {

	public InputStream getJsonInputStream() {
		return this.getClass().getResourceAsStream("test.json");
	}
}
