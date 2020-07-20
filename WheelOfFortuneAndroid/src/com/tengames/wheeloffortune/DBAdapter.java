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
package com.tengames.wheeloffortune;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {

	private SQLiteDatabase mDB;
	private DBHelper mDBHelper;

	private ArrayList<DatabaseConnect> gameData ;

	public DBAdapter(Context context) {
		mDBHelper = new DBHelper(context);
		gameData = new ArrayList<DatabaseConnect>();
	}
	

	public DBAdapter createDataBase() {
		try {
			mDBHelper.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	public DBAdapter open() {
		mDBHelper.openDataBase();
		mDBHelper.close();
		mDB = mDBHelper.getReadableDatabase();
		return this;
	}

	public void close() {
		mDBHelper.close();
	}

	public Cursor getAllData() {
		try {
			String sql = "SELECT * FROM  " + Constant.DB_TABLE_QUIZ;

			Cursor mCur = mDB.rawQuery(sql, null);
			if (mCur != null) {
				mCur.moveToNext();
			}
			return mCur;
		} catch (SQLException mSQLException) {
			throw mSQLException;
		}
	}

	public Cursor getRandomData() {
		Random random = new Random();	
		int randomNum = 1 + random.nextInt(Constant.NUM_TOPIC);
		try {

			String sql = "SELECT Quiz.*, Topic.topic_name FROM "
					+ Constant.DB_TABLE_QUIZ + " , " + Constant.DB_TABLE_TOPIC
					+ "   WHERE Quiz.topic_id = " + randomNum
					+ "  AND Quiz.topic_id = Topic.topic_id "
					+ "  ORDER BY RANDOM() LIMIT 4 ";
			Cursor mCur = mDB.rawQuery(sql, null);
			if (mCur.moveToFirst()) {
				do {
					DatabaseConnect question = new DatabaseConnect();
					question.setQuizId(mCur.getInt(1));
					question.setKeyWord(((removeAccent(mCur.getString(2)))
							.replaceAll("\\s", "")).toUpperCase());
					question.setAnswer(mCur.getString(2));
					question.setNumberChar(mCur.getInt(3));
					question.setHint(mCur.getString(4));
					question.setTopic(mCur.getString(5));
					gameData.add(question);
				} while (mCur.moveToNext());
			}
			return mCur;
		} catch (SQLException mSQLException) {
			throw mSQLException;
		}
	}
	
	private char[] SPECIAL_CHARACTERS = { 'À', 'Á', 'Â', 'Ã', 'È', 'É', 'Ê',
			'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â', 'ã',
			'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý', 'Ă',
			'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ', 'ạ',
			'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ', 'Ắ',
			'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ', 'ẻ',
			'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ', 'Ỉ',
			'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ', 'ổ',
			'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ', 'Ợ',
			'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ', 'ữ',
			'Ự', 'ự', 'Ỵ', 'ỵ', 'Ỳ', 'ỳ' };

	private char[] REPLACEMENTS = { 'A', 'A', 'A', 'A', 'E', 'E', 'E', 'I',
			'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a', 'a', 'a', 'e',
			'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u', 'y', 'A', 'a',
			'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u', 'A', 'a', 'A',
			'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a',
			'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e', 'E', 'e', 'E',
			'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'I', 'i',
			'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
			'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
			'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U',
			'u', 'Y', 'y', 'Y', 'ỳ' };

	public char removeAccent(char ch) {
		int index = Arrays.binarySearch(SPECIAL_CHARACTERS, ch);
		if (index >= 0) {
			ch = REPLACEMENTS[index];
		}
		return ch;
	}

	public String removeAccent(String s) {
		StringBuilder sb = new StringBuilder(s);
		for (int i = 0; i < sb.length(); i++) {
			sb.setCharAt(i, removeAccent(sb.charAt(i)));
		}
		return sb.toString();
	}

	public ArrayList<DatabaseConnect> getGameData() {
		return gameData;
	}


}
