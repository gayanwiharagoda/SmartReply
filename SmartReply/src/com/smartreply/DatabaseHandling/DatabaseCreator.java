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
	public static final String COL_GROUP_TEMPLATE_GROUP_ID = "group_id";

	private static final String CREATE_TABLE_GROUP_TEMPLATE = "create table "
			+ TABLE_GROUP_TEMPLATE + " (" + COL_GROUP_TEMPLATE_TEMPLATE_ID
			+ " integer, " + COL_GROUP_TEMPLATE_GROUP_ID + " integer);";


	public DatabaseCreator(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		//context.deleteDatabase(DB_NAME);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_TEMPLATE);
		db.execSQL(CREATE_TABLE_GROUP_TEMPLATE);
		seedData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DEBUG_TAG,
				"Upgrading database. Existing contents will be lost. ["
						+ oldVersion + "]->[" + newVersion + "]");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEMPLATE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP_TEMPLATE);
		onCreate(db);
	}

	/**
	 * Create sample data to use
	 * 
	 * @param db
	 *            The open database
	 */
	private void seedData(SQLiteDatabase db) {
		 db.execSQL("insert into message_template (title, message) values ('Family', 'I will call you later. Now I am at a work');");
		 db.execSQL("insert into message_template (title, message) values ('Client', 'I am buzy now. Would you mind calling me late');");
		 db.execSQL("insert into message_template (title, message) values ('Girl', 'I will call you later sweety');");
		 db.execSQL("insert into message_template (title, message) values ('Boss', 'Sorry Im not here to lend an ear because Im busy right now.ill call you as soon as possible.');");
		 db.execSQL("insert into message_template (title, message) values ('Other', 'Message');");
		 db.execSQL("insert into message_template (title, message) values ('Other', 'Message');");
		 
		 //Movini
		 //Boss÷ Sorry Im not here to lend an ear because Im busy right now.ill call you as soon as possible.
		 //Family ÷ So long as phones can ring and eyes can see,leave a message and ill get back to thee.im buzzzyyy....
		 //client Hey, it/'s me Gayan.Sorry you cant get through. Leave a message and ill get back to you
		 //Girl÷ roses are red ,violets are blue.sugar is sweet and so are you.ill call you as soon as possible my Juliet as you call your Romeo bcoz im busy right now.
		 //Frndz÷ Hello?Hello?Hellooo? Im sorry , you are gonna have to speak up,I cant hear u.That's bcoz Im busy right now.Leave message guys.
	}
}
