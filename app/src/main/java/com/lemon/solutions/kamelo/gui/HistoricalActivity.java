package com.lemon.solutions.kamelo.gui;

import com.lemon.solutions.kamelo.R;
import com.lemon.solutions.kamelo.db.DatabaseOpenHelper;

import android.app.ListActivity;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class HistoricalActivity extends ListActivity {

	// --- ATTRIBUTES ---
	private DatabaseOpenHelper mDbHelper;
	private SimpleCursorAdapter mAdapter;
	private Cursor cursor;

	// --- OVERRIDEN METHODS ---
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_historical);
		// Create a new DatabaseHelper
		mDbHelper = new DatabaseOpenHelper(this);

		if (getResources().getConfiguration().getLayoutDirection() == Configuration.ORIENTATION_PORTRAIT) {
			cursor = readReducedRefuelings();
			mAdapter = new SimpleCursorAdapter(this, R.layout.historical_item,
					cursor, DatabaseOpenHelper.reducedColumns, new int[] {
					R.id.historical_id, R.id.historical_date,
					R.id.historical_average }, 0);
		}
		else {
			cursor = readFullRefuelings();
			mAdapter = new SimpleCursorAdapter(this, R.layout.historical_item,
					cursor, DatabaseOpenHelper.fullColumns, new int[] {
					R.id.historical_id, R.id.historical_date,
					R.id.historical_price, R.id.historical_distance,
					R.id.historical_consumption, R.id.historical_average }, 0);
		}

		setListAdapter(mAdapter);
	}

	// Close database
	@Override
	protected void onDestroy() {
		mDbHelper.getWritableDatabase().close();
		super.onDestroy();
	}

	// --- PRIVATE METHODS ---
	// Returns all refueling records in the database
	private Cursor readReducedRefuelings() {
		return mDbHelper.getWritableDatabase().query(
				DatabaseOpenHelper.TABLE_NAME,
				DatabaseOpenHelper.reducedColumns, null, new String[] {}, null,
				null, DatabaseOpenHelper._ID + " DESC");
	}

	private Cursor readFullRefuelings() {
		return mDbHelper.getWritableDatabase().query(
				DatabaseOpenHelper.TABLE_NAME,
				DatabaseOpenHelper.fullColumns, null, new String[] {}, null,
				null, DatabaseOpenHelper._ID + " DESC");
	}
}
