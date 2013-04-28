package com.smartreply.DatabaseHandling;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class DataProvider extends ContentProvider {

	private DatabaseCreator mDB;

	private static final String AUTHORITY = "com.smartreply.DatabaseHandling.DataProvider";
	public static final int TEMPLATES = 100;
	public static final int TEMPLATE_ID = 110;
	public static final int GROUP_TEMPLATES = 200;
	public static final int GROUP_TEMPLATE_ID = 210;

	private static final String TEMPLATES_BASE_PATH = DatabaseCreator.TABLE_TEMPLATE;
	private static final String TEMPLATES_GROUPS_BASE_PATH = DatabaseCreator.TABLE_GROUP_TEMPLATE;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TEMPLATES_BASE_PATH);

	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/mt-template";
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/mt-template";

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, TEMPLATES_BASE_PATH, TEMPLATES);
		sURIMatcher.addURI(AUTHORITY, TEMPLATES_BASE_PATH + "/#", TEMPLATE_ID);
		sURIMatcher.addURI(AUTHORITY, TEMPLATES_GROUPS_BASE_PATH,
				GROUP_TEMPLATES);
		sURIMatcher.addURI(AUTHORITY, TEMPLATES_GROUPS_BASE_PATH + "/#",
				GROUP_TEMPLATE_ID);
	}

	@Override
	public boolean onCreate() {
		mDB = new DatabaseCreator(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case TEMPLATE_ID:
			queryBuilder.appendWhere(DatabaseCreator.COL_ID + "="
					+ uri.getLastPathSegment());
			Log.d("QUERY:", "TEMPLATE_ID");
		case TEMPLATES:
			// no filter
			queryBuilder.setTables(DatabaseCreator.TABLE_TEMPLATE);
			Log.d("QUERY:", "TEMPLATES");
			break;
		case GROUP_TEMPLATE_ID:
			queryBuilder
					.appendWhere(DatabaseCreator.COL_GROUP_TEMPLATE_TEMPLATE_ID
							+ "=" + uri.getLastPathSegment());
			Log.d("QUERY:", "GROUP_TEMPLATE_ID");
		case GROUP_TEMPLATES:
			queryBuilder.setTables(DatabaseCreator.TABLE_GROUP_TEMPLATE);
			Log.d("QUERY:", "GROUP_TEMPLATES");
			break;

		default:
			throw new IllegalArgumentException("Unknown URI");
		}

		Cursor cursor = queryBuilder.query(mDB.getReadableDatabase(),
				projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = mDB.getWritableDatabase();
		int rowsAffected = 0;
		switch (uriType) {
		case TEMPLATES:
			rowsAffected = sqlDB.delete(DatabaseCreator.TABLE_TEMPLATE,
					selection, selectionArgs);
			break;
		case TEMPLATE_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsAffected = sqlDB.delete(DatabaseCreator.TABLE_TEMPLATE,
						DatabaseCreator.COL_ID + "=" + id, null);
			} else {
				rowsAffected = sqlDB
						.delete(DatabaseCreator.TABLE_TEMPLATE, selection
								+ " and " + DatabaseCreator.COL_ID + "=" + id,
								selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsAffected;
	}

	@Override
	public String getType(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case TEMPLATES:
			return CONTENT_TYPE;
		case TEMPLATE_ID:
			return CONTENT_ITEM_TYPE;
		default:
			return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		if (uriType != TEMPLATES || uriType != GROUP_TEMPLATES) {
			throw new IllegalArgumentException("Invalid URI for insert");
		}

		SQLiteDatabase sqlDB = mDB.getWritableDatabase();
		long newID = 0;
		switch (uriType) {
		case TEMPLATES:
			newID = sqlDB.insert(DatabaseCreator.TABLE_TEMPLATE, null, values);
			break;
		case GROUP_TEMPLATES:
			newID = sqlDB.insert(DatabaseCreator.TABLE_GROUP_TEMPLATE, null,
					values);
			break;
		default:
			break;
		}

		if (newID > 0) {
			Uri newUri = ContentUris.withAppendedId(uri, newID);
			getContext().getContentResolver().notifyChange(uri, null);
			return newUri;
		} else {
			throw new SQLException("Failed to insert row into " + uri);
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = mDB.getWritableDatabase();

		int rowsAffected;

		switch (uriType) {
		case TEMPLATE_ID:
			String id = uri.getLastPathSegment();
			StringBuilder modSelection = new StringBuilder(
					DatabaseCreator.COL_ID + "=" + id);

			if (!TextUtils.isEmpty(selection)) {
				modSelection.append(" AND " + selection);
			}

			rowsAffected = sqlDB.update(DatabaseCreator.TABLE_TEMPLATE, values,
					modSelection.toString(), null);
			break;
		case TEMPLATES:
			rowsAffected = sqlDB.update(DatabaseCreator.TABLE_TEMPLATE, values,
					selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsAffected;
	}
}
