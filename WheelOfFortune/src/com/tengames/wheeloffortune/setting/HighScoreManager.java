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
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;

import com.tengames.wheeloffortune.objects.Player;

public class HighScoreManager {
	public static ArrayList<PlayerInfor> list = new ArrayList<PlayerInfor>(5);;
	private static String file = "data.ckg";

	public static void load() {
		try {
			int count = 1;
			
			FileReader fr = new FileReader(file);
			BufferedReader buffer = new BufferedReader(fr);
			
			while (buffer.readLine() != null) {
				count++;
			}

			int numPlayer = (count / 4);		
			buffer.close();
			fr.close();
			if (numPlayer > 0) {
				FileInputStream fr2 = new FileInputStream(file);
				 Reader reader = new java.io.InputStreamReader(fr2, "UTF-8");
				 BufferedReader input = new BufferedReader(reader);
				for (int i = 0; i < numPlayer; i++) {					
					PlayerInfor player = new PlayerInfor();				 
					player.setName(input.readLine());
					player.setAge(input.readLine());
					player.setAddress(input.readLine());
					player.setScore(Short.parseShort(input.readLine()));
					list.add(player);
				}				
				fr2.close();				
			}				
			
		} catch (FileNotFoundException fe) {
		} catch (IOException io) {
		}
	}

	public static void addPlayer(Player player) {
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getScore() <= player.getScoreGame()) {
					PlayerInfor playerInf = new PlayerInfor();
					playerInf.setName(player.getName());
					playerInf.setAddress(player.getAddress());
					playerInf.setAge(player.getAge());
					playerInf.setScore(player.getScoreGame());

					list.add(i, playerInf);
					break;
				}
			}
		} else {
			PlayerInfor playerInf = new PlayerInfor();
			playerInf.setName(player.getName());
			playerInf.setAddress(player.getAddress());
			playerInf.setAge(player.getAge());
			playerInf.setScore(player.getScoreGame());

			list.add(0, playerInf);
		}
	}

	public static void save() {
		try {
			FileOutputStream fis = new FileOutputStream(file, false);
			PrintWriter pw = new PrintWriter(fis);
			
			int j;
			if (list.size() > 5){
				j = 5;
			}else j=list.size();
			
			for (int i = 0; i < j; i++)
			{
				pw.println(list.get(i).getName());
				pw.println(list.get(i).getAge());
				pw.println(list.get(i).getAddress());
				pw.println(list.get(i).getScore());
			}
			list.clear();
			pw.close();
			fis.close();
		} catch (IOException io) {
		}
		finally {
		}
	}
}
