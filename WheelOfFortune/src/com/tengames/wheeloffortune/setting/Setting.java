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
package com.tengames.wheeloffortune.setting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.badlogic.gdx.Gdx;

public class Setting {
	public static final String file = "setting.ckg";
	public static boolean SOUND_ENABLE = false;
	public static boolean MUSIC_ENABLE = false;

	public static void load() {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(Gdx.files.external(file).read()));
			SOUND_ENABLE = Boolean.parseBoolean(in.readLine());
			MUSIC_ENABLE = Boolean.parseBoolean(in.readLine());
			
		} catch (Throwable e) {
		} finally {
			try {
				if (in != null) in.close();
			} catch (IOException e) {
			}
		}
	}
	
	public static void save() {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(Gdx.files.external(file).write(false)));
			out.write(Boolean.toString(SOUND_ENABLE));
			out.write(Boolean.toString(MUSIC_ENABLE));
			
		} catch (Throwable e) {
		} finally {
			try {
				if (out != null) out.close();
			} catch (IOException e) {
			}
		}
	}
}
