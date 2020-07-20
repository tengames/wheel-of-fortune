/*
The MIT License

Copyright (c) 2013 kong <tengames.inc@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.tengames.wheeloffortune.main;

public interface GlobalVariables {
	public static final String STORE_URL = "http://sample.com";
	public static final String HSCORE_URL = "http://sample.com";
	
	public static final String REQ_STORE = "store";
	public static final String REQ_DEVICEID = "deviceId";
	public static final String REQ_GAMEID = "gameId";
	public static final String	REQ_GAMENAME = "gameName";
	public static final String REQ_DEVICETYPE = "deviceType";
	public static final String REQ_SCORE = "score";
	public static final String REQ_PARAMS = "params";

	public static final String RES_LINK = "Link";
	public static final String RES_BONUS = "Bonus";
	public static final String RES_MESSAGE = "Message";
	public static final String RES_INFORM = "Inform";

	public static final byte DEV_ADR = 0;
	public static final byte DEV_IOS = 1;
	public static final byte DEV_WP = 2;
	
}
