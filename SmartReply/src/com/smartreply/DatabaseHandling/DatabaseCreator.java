package com.smartreply.DatabaseHandling;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseCreator extends SQLiteOpenHelper {
	private static final String DEBUG_TAG = "SmartReplyDatabase";
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "smartreply_db";

	public static final String TABLE_TEMPLATE = "message_template";
	public static final String COL_ID = "_id";
	public static final String COL_TEMPLATE_TITLE = "title";
	public static final String COL_TEMPLATE_MESSAGE = "message";

	private static final String CREATE_TABLE_TEMPLATE = "create table "
			+ TABLE_TEMPLATE + " (" + COL_ID
			+ " integer primary key autoincrement, " + COL_TEMPLATE_TITLE
			+ " text not null, " + COL_TEMPLATE_MESSAGE + " text not null);";

	// create group template table
	// group can have one template
	public static final String TABLE_GROUP_TEMPLATE = "group_message_template";
	public static final String COL_GROUP_TEMPLATE_TEMPLATE_ID = "template_id";
	public static final String COL_ROUP_TEMPLATE_GROUP_ID = "group_id";

	private static final String CREATE_TABLE_GROUP_TEMPLATE = "create table "
			+ TABLE_GROUP_TEMPLATE + " (" + COL_GROUP_TEMPLATE_TEMPLATE_ID
			+ " text not null, " + COL_ROUP_TEMPLATE_GROUP_ID
			+ " text not null);";

	private static final String DB_SCHEMA = CREATE_TABLE_TEMPLATE + " "
			+ CREATE_TABLE_GROUP_TEMPLATE;

	public DatabaseCreator(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DB_SCHEMA);
		seedData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DEBUG_TAG,
				"Upgrading database. Existing contents will be lost. ["
						+ oldVersion + "]->[" + newVersion + "]");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEMPLATE);
		onCreate(db);
	}

	/**
	 * Create sample data to use
	 * 
	 * @param db
	 *            The open database
	 */
	private void seedData(SQLiteDatabase db) {
		db.execSQL("insert into message_template (title, message) values ('Best of Tuts+ in February 2011', 'http://mobile.tutsplus.com/articles/news/best-of-tuts-in-february-2011/');");
		db.execSQL("insert into message_template (title, message) values ('Design & Build a 1980s iOS Phone App: Design Comp Slicing', 'http://mobile.tutsplus.com/message_template/mobile-design-message_template/80s-phone-app-slicing/');");
		db.execSQL("insert into message_template (title, message) values ('Create a Brick Breaker Game with the Corona SDK: Game Controls', 'http://mobile.tutsplus.com/message_template/corona/create-a-brick-breaker-game-with-the-corona-sdk-game-controls/');");
		db.execSQL("insert into message_template (title, message) values ('Exporting Graphics for Mobile Apps: PNG or JPEG?', 'http://mobile.tutsplus.com/message_template/mobile-design-message_template/mobile-design_png-or-jpg/');");
		db.execSQL("insert into message_template (title, message) values ('Android Tablet Design', 'http://mobile.tutsplus.com/message_template/android/android-tablet-design/');");
		db.execSQL("insert into message_template (title, message) values ('Build a Titanium Mobile Pizza Ordering App: Order Form Setup', 'http://mobile.tutsplus.com/message_template/appcelerator/build-a-titanium-mobile-pizza-ordering-app-order-form-setup/');");
		db.execSQL("insert into message_template (title, message) values ('Create a Brick Breaker Game with the Corona SDK: Application Setup', 'http://mobile.tutsplus.com/message_template/corona/corona-sdk_brick-breaker/');");
		db.execSQL("insert into message_template (title, message) values ('Android Tablet Virtual Device Configurations', 'http://mobile.tutsplus.com/message_template/android/android-sdk_tablet_virtual-device-configuration/');");
		db.execSQL("insert into message_template (title, message) values ('Build a Titanium Mobile Pizza Ordering App: Topping Selection', 'http://mobile.tutsplus.com/message_template/appcelerator/pizza-ordering-app-part-2/');");
		db.execSQL("insert into message_template (title, message) values ('Design & Build a 1980s iOS Phone App: Interface Builder Setup', 'http://mobile.tutsplus.com/message_template/iphone/1980s-phone-app_interface-builder-setup/');");
	}
}
