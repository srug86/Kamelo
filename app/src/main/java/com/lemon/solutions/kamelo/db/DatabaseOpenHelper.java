package com.lemon.solutions.kamelo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	// --- CONTANTS ---
	final private static String NAME = "refueling_history_db";
	final private static Integer VERSION = 1;
	// --- ATTRIBUTES ---
	final private Context mContext;
	// --- TABLE ---
	// -> ATTRIBUTES
	public final static String TABLE_NAME = "refuelings";
	public final static String _ID = "_id";
	public final static String DATE = "date";
	public final static String DISTANCE = "distance";
	public final static String PRICE = "price";
	public final static String AMOUNT = "amount";
	public final static String CONSUMPTION = "consumption";
	public final static String AVERAGE = "average";
	public final static String[] fullColumns = { _ID, DATE, PRICE, DISTANCE, CONSUMPTION, AVERAGE };
	public final static String[] reducedColumns = { _ID, DATE, AVERAGE };
	// -> CREATE TABLE
	final private static String CREATE_CMD =

	"CREATE TABLE " + TABLE_NAME + " (" + _ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE
			+ " TEXT NOT NULL, " + DISTANCE + " REAL NOT NULL, " + PRICE
			+ " REAL NOT NULL, " + AMOUNT + " REAL NOT NULL, " + CONSUMPTION
			+ " REAL NOT NULL, " + AVERAGE + " REAL NOT NULL)";

	// --- CONSTRUCTOR ---
	public DatabaseOpenHelper(Context context) {
		super(context, NAME, null, VERSION);
		this.mContext = context;
	}

	// --- OVERRIDEN METHODS ---
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CMD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// N/A
	}

	// --- PUBLIC METHODS ---
	public void deleteDatabase() {
		mContext.deleteDatabase(NAME);
	}
}
