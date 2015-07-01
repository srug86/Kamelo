package com.lemon.solutions.kamelo.gui;

import java.util.Calendar;

import com.lemon.solutions.kamelo.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class RefuelingActivity extends Activity {

	/* --- CONSTANTS --- */
	static final int DATE_DIALOG_ID = 0;
	public final static String EXTRA_DATE = "com.lemon.solutions.kamelo.refueling.date";
	public final static String EXTRA_DISTANCE = "com.lemon.solutions.kamelo.refueling.distance";
	public final static String EXTRA_PRICE = "com.lemon.solutions.kamelo.refueling.price";
	public final static String EXTRA_AMOUNT = "com.lemon.solutions.kamelo.refueling.amount";
	public final static String EXTRA_CONSUMPTION = "com.lemon.solutions.kamelo.refueling.consumption";
	public final static String EXTRA_AVERAGE = "com.lemon.solutions.kamelo.refueling.average";

	/* --- ATTRIBUTES --- */
	// Views
	private RadioButton radioAmount;
	private RadioButton radioConsumption;
	private EditText editDistance;
	private EditText editPrice;
	private EditText editAmount;
	private EditText editConsumption;
	private TextView textDateDisplay;
	private Button buttonChangeDate;
	// Primitive values
	private int cYear;
	private int cMonth;
	private int cDay;

	// The callback received when the user "sets" the date in the Dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			cYear = year;
			cMonth = monthOfYear + 1; // Month is 0 based so add 1
			cDay = dayOfMonth;
			updateDateDisplayed();
		}
	};

	/* --- OVERRIDEN METHODS --- */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refueling);

		initializeEditTexts();
		initializeDatePicker();
		initializeRadioGroup();
	}

	// Create and return DatePickerDialog
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, cYear,
					cMonth - 1, cDay);
		}
		return null;
	}

	// Create Options Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.top_menu, menu);
		return true;
	}

	// Process clicks on Options Menu items
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.option_historical:
			openHistoricalActivity();
			return true;
		default:
			return false;
		}
	}

	/* PUBLIC METHODS */
	public void calculateRefuelingSummary(View view) {
		if (!checkCurrentValues())
			return;

		double distance = Double.parseDouble(editDistance.getText().toString());
		double price = Double.parseDouble(editPrice.getText().toString());
		double amount, consumption;
		if (radioAmount.isChecked()) {
			amount = Double.parseDouble(editAmount.getText().toString());
			consumption = amount / price;
		} else {
			consumption = Double.parseDouble(editConsumption.getText()
					.toString());
			amount = consumption * price;
		}
		double average = (consumption * 100) / distance;

		Intent summaryIntent = new Intent(this, SummaryActivity.class);
		summaryIntent.putExtra(EXTRA_DATE, getCurrentDate());
		summaryIntent.putExtra(EXTRA_DISTANCE, distance);
		summaryIntent.putExtra(EXTRA_PRICE, price);
		summaryIntent.putExtra(EXTRA_AMOUNT, amount);
		summaryIntent.putExtra(EXTRA_CONSUMPTION, consumption);
		summaryIntent.putExtra(EXTRA_AVERAGE, average);
		startActivity(summaryIntent);
	}

	/* PRIVATE METHODS */
	private void openHistoricalActivity() {
		Intent historicalIntent = new Intent(this, HistoricalActivity.class);
		startActivity(historicalIntent);
	}

	private void initializeEditTexts() {
		editDistance = (EditText) findViewById(R.id.edit_distance);
		editPrice = (EditText) findViewById(R.id.edit_price);
		editAmount = (EditText) findViewById(R.id.edit_amount);
		editConsumption = (EditText) findViewById(R.id.edit_consumption);
	}

	private void initializeRadioGroup() {
		final OnClickListener radioListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				RadioButton rb = (RadioButton) v;
				switch (rb.getId()) {
				case R.id.radio_amount:
					editAmount.setEnabled(true);
					editConsumption.setEnabled(false);
					break;
				case R.id.radio_consumption:
					editConsumption.setEnabled(true);
					editAmount.setEnabled(false);
					break;
				}
			}
		};

		radioAmount = (RadioButton) findViewById(R.id.radio_amount);
		radioAmount.setOnClickListener(radioListener);

		radioConsumption = (RadioButton) findViewById(R.id.radio_consumption);
		radioConsumption.setOnClickListener(radioListener);
	}

	private void initializeDatePicker() {
		textDateDisplay = (TextView) findViewById(R.id.text_date_display);
		buttonChangeDate = (Button) findViewById(R.id.button_pick_date);

		// Set an OnClickListener on the Change The Date Button
		buttonChangeDate.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		// Get the current date
		final Calendar c = Calendar.getInstance();
		cYear = c.get(Calendar.YEAR);
		cMonth = c.get(Calendar.MONTH) + 1; // Month is 0 based so add 1
		cDay = c.get(Calendar.DAY_OF_MONTH);

		// Display the current date
		updateDateDisplayed();
	}

	// Update the date in the TextView
	private void updateDateDisplayed() {
		textDateDisplay.setText(getCurrentDate());
	}

	private String getCurrentDate() {
		return cDay + "-" + cMonth + "-" + cYear;
	}

	private Boolean checkCurrentValues() {
		if (!checkCurrentValue(editDistance, "Distancia"))
			return false;
		if (!checkCurrentValue(editPrice, "Precio"))
			return false;

		if (radioAmount.isChecked()) {
			return checkCurrentValue(editAmount, "Importe total");
		} else if (radioConsumption.isChecked()) {
			return checkCurrentValue(editConsumption, "Consumo total");
		} else
			return false;
	}

	private Boolean checkCurrentValue(EditText editText, String id) {
		try {
			double distance = Double.parseDouble(editText.getText().toString());
			if (distance > 0) {
				return true;
			}
			Toast.makeText(getApplicationContext(),
					"'" + id + "' tiene que ser mayor que 0 Km.",
					Toast.LENGTH_LONG).show();
			return false;
		} catch (NumberFormatException nfe) {
			Toast.makeText(getApplicationContext(),
					"'" + id + "' no contiene un valor válido.",
					Toast.LENGTH_LONG).show();
			return false;
		}
	}
}
