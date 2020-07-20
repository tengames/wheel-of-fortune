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

import java.util.ArrayList;

import woodyx.basicapi.accessor.ButtonAccessor;
import woodyx.basicapi.accessor.DialogAccessor;
import woodyx.basicapi.accessor.SpriteAccessor;
import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.tengames.wheeloffortune.screens.LoadingScreen;

/**
 * Class dùng để khởi tạo một game trong framework LibGdx
 *
 * @author kong
 *
 */
public class WheelOfFortune extends Game {
	// interface for android
	public AndroidInterface androidInterface;
	
	// get database
	public static ArrayList<ConnectDatabase> conectDataBases = new ArrayList<ConnectDatabase>();
	
	// width, height default of game
	public static final float DEFAULT_WIDTH = 800f;
	public static final float DEFAULT_HEIGHT = 480f;
	
	// gravity of the world
	public static final Vector2 GRAVITY = new Vector2(0, -20);
	
	// static final string
	public static final String S_NAME = "Nhập Tên";
	public static final String S_AGE = "Nhập Tuổi";
	public static final String S_ADDRESS = "Nhập Địa Chỉ";
	public static final String S_DEF_NAME = "Người Chơi ";
	public static final String S_DEF_VALUE = "Ẩn";
	
	public static final String S_ABOUT = "\n\n\n\n<<THÔNG TIN>>\n"
										+ "\n\n Phát triển: \n\n Nguyễn Duy Công - congcoi123*gmai.com\n\n Nguyễn Trường Giang - truonggiang.bka2010*gmail.com \n\n Phan Hữu Kiên - senior1206*gmail.com\n\n\n"
										+ "Mọi ý kiến đóng góp xin gửi về: tengames.inc@.gmail.com\n\n Chúc các bạn chơi game vui vẻ!";
	
	public static final String S_HIGHSCORE = "BẢNG XẾP HẠNG\n\n\n";
	public static final String S_WELCOME = "CHÀO MỪNG QUÍ VỊ ĐẾN VỚI TRÒ CHƠI \n\n CHIẾC NÓN KÌ DIỆU";
	public static final String S_TOUCH = "Chạm vào màn hình để bắt đầu!";
	public static final String S_TOUCH2 = "Chạm vào màn hình để tiếp tục!";
	public static final String S_WELCOME_ROUND = "CHÀO MỪNG QUÍ VỊ ĐẾN VỚI VÒNG: ";
	public static final String S_FIRST_PERSON = "NGƯỜI CHƠI ĐẦU TIÊN: ";
	public static final String S_TOPIC = "CHỦ ĐỀ CỦA HÔM NAY: ";
	public static final String S_NUMBER_TEXT = "Đây là một ô chữ gồm ";
	public static final String S_RESULT = "Ô Chữ Của Chúng Ta Vòng Này Là:\n\n";
	public static final String S_CONGRAS = "Xin Chúc Mừng Người Chơi Chiến Thắng Trong Vòng Này: ";
	public static final String S_QUIZ = "NỘI DUNG CÂU HỎI: ";
	public static final String S_HINT = "LƯU Ý:\n\n Viết câu trả lời không dấu và không chứa dấu cách.\n\nNếu trả lời đúng bạn được cộng thêm 1000 điểm.\n\n Nếu sai sẽ bị mất quyền chơi trong vòng này!";
	public static final String S_FINAL_QUIZ = "CÂU HỎI VÒNG ĐẶC BIỆT:\n\n ";
	public static final String S_CHOOSE_WORD = "Bạn Chọn Chữ: \n\n";
	public static final String S_CONGRAS_FINISH = "XIN CHÚC MỪNG NGƯỜI CHƠI ĐÃ CHIẾN THẮNG TRONG BUỔI HÔM NAY:\n\n";
	public static final String S_PASS_SPECIAL = "Chúc Mừng Bạn Đã Vượt Qua Vòng Thi Đặc Biệt!\n\nSỐ ĐIỂM: ";
	public static final String S_FAIL_SPECIAL = "Rất Tiếc Bạn Chưa Vượt Qua Vòng Thi Đặc Biệt Của Chương Trình\n\nSỐ ĐIỂM: ";
	public static final String S_GOODBYE = "\n\nCảm ơn bạn đã tham gia chương trình.\n Chúc Bạn May Mắn Và Thành Công Trong Cuộc Sống!";
	
	public static final String S_CONFIRM_EXIT = "Xác nhận thoát game?";
	public static final String S_CONFIRM_EXIT_GAME = "Thoát Game\n\n Quay về Menu?";
	
	// reset database
	public static void resetData() {
		conectDataBases.clear();
	}
	
	@Override
	public void create() {
		// initialize for Tween's limit
		Tween.setWaypointsLimit(10);
		Tween.setCombinedAttributesLimit(3);
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.registerAccessor(Button.class, new ButtonAccessor());
		Tween.registerAccessor(Dialog.class, new DialogAccessor());
		
		setScreen(new LoadingScreen(this));
		
	}

	@Override
	public void dispose() {
		super.dispose();
		getScreen().dispose();
	}
	
	// register interface
	public void registerAndroidInterface (AndroidInterface androidInterface) {
		this.androidInterface = androidInterface;
	}
	
}
