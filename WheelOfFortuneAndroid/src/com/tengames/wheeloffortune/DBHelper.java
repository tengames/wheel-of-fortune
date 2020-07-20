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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static String DB_PATH = "";
	private SQLiteDatabase mDB;
	private final Context mContext;

	public DBHelper(Context context) {
		super(context, Constant.DB_NAME, null, 1);
		DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		this.mContext = context;
	}

	public void createDataBase() throws IOException {
		boolean mDataBaseExist = checkDataBaseExist();
		if (!mDataBaseExist) {
			this.getReadableDatabase();
			this.close();
			try {
				copyDataBase();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private boolean checkDataBaseExist() {
		File dbFile = new File(DB_PATH + Constant.DB_NAME);
		return dbFile.exists();
	}

	private void copyDataBase() throws IOException {

		InputStream mInput = mContext.getAssets().open(Constant.DB_RESOURCE);
		String outFileName = DB_PATH + Constant.DB_NAME;
		OutputStream mOutput = new FileOutputStream(outFileName);
		byte[] mBuffer = new byte[1024];
		int mLength;
		while ((mLength = mInput.read(mBuffer)) > 0) {
			mOutput.write(mBuffer, 0, mLength);
		}
		mOutput.flush();
		mOutput.close();
		mInput.close();
	}

	public boolean openDataBase() throws SQLException {
		String mPath = DB_PATH + Constant.DB_NAME;
		mDB = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
		return mDB != null;
	}

	@Override
	public synchronized void close() {
		if (mDB != null)
			mDB.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
