package com.lemon.solutions.kamelo.gui;

import com.lemon.solutions.kamelo.R;
import com.lemon.solutions.kamelo.db.DatabaseOpenHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SummaryActivity extends Activity {

	/* --- ATTRIBUTES --- */
	private String cDate;
	private double cDistance;
	private double cPrice;
	private double cAmount;
	private double cConsumption;
	private double cAverage;
	private DatabaseOpenHelper mDbHelper;

	/* --- OVERRIDEN METHODS --- */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary);
		initializeValues();
	}

	// Close database
	@Override
	protected void onDestroy() {
		if (mDbHelper != null) {
			mDbHelper.getWritableDatabase().close();
		}
		super.onDestroy();
	}

	/* --- PUBLIC METHODS --- */
	public void sendRegistry(View view) {
		Boolean success = insertRefuelingRecord();
		showToastResult(success);
		if (success) {
			openHistoricalActivity();
		}
	}

	/* --- PRIVATE METHODS --- */
	private void initializeValues() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		cDate = bundle.getString(RefuelingActivity.EXTRA_DATE);
		cDistance = bundle.getDouble(RefuelingActivity.EXTRA_DISTANCE);
		cPrice = bundle.getDouble(RefuelingActivity.EXTRA_PRICE);
		cAmount = bundle.getDouble(RefuelingActivity.EXTRA_AMOUNT);
		cConsumption = bundle.getDouble(RefuelingActivity.EXTRA_CONSUMPTION);
		cAverage = bundle.getDouble(RefuelingActivity.EXTRA_AVERAGE);

		TextView viewDate = (TextView) findViewById(R.id.refueling_date);
		viewDate.setText(cDate);
		TextView viewDistance = (TextView) findViewById(R.id.refueling_distance);
		viewDistance.setText(Double.toString(cDistance));
		TextView viewPrice = (TextView) findViewById(R.id.refueling_price);
		viewPrice.setText(Double.toString(cPrice));
		TextView viewAmount = (TextView) findViewById(R.id.refueling_amount);
		viewAmount.setText(Double.toString(cAmount));
		TextView viewConsumption = (TextView) findViewById(R.id.refueling_consumption);
		viewConsumption.setText(Double.toString(cConsumption));
		TextView viewConsumptionAvg = (TextView) findViewById(R.id.refueling_avg_consumption);
		viewConsumptionAvg.setText(Double.toString(cAverage));
	}

	private Boolean insertRefuelingRecord() {
		try {
			ContentValues values = new ContentValues();

			values.put(DatabaseOpenHelper.DATE, cDate);
			values.put(DatabaseOpenHelper.DISTANCE, cDistance);
			values.put(DatabaseOpenHelper.PRICE, cPrice);
			values.put(DatabaseOpenHelper.AMOUNT, cAmount);
			values.put(DatabaseOpenHelper.CONSUMPTION, cConsumption);
			values.put(DatabaseOpenHelper.AVERAGE, cAverage);

			// Create a new DatabaseHelper
			mDbHelper = new DatabaseOpenHelper(this);
			mDbHelper.getWritableDatabase().insert(
					DatabaseOpenHelper.TABLE_NAME, null, values);

			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	private void showToastResult(Boolean success) {
		Toast.makeText(getApplicationContext(),
				success ? "Registro efectuado" : "Registro fallido",
				Toast.LENGTH_LONG).show();
	}

	private void openHistoricalActivity() {
		Intent historicalActivity = new Intent(this, HistoricalActivity.class);
		startActivity(historicalActivity);
	}
}
