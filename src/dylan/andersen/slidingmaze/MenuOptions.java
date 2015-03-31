package dylan.andersen.slidingmaze;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MenuOptions extends ActionBarActivity
{
	ApplicationData myAData = new ApplicationData();

	Button startGame;

	MenuOptions myThis = this;

	SeekBar seekBarNumOfRows;
	SeekBar seekBarNumOfCols;
	SeekBar seekBarDifficultyLevel;
	EditText editTextRandomSeed;

	TextView textViewNumOfRows;
	TextView textViewNumOfCols;
	TextView textViewDifficultyLevel;

	Button buttonSubmit;

	private final int MIN_ROW_VALUE = 5;
	private final int MIN_COL_VALUE = 5;

	OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener()
	{
		@Override
		public void onStopTrackingTouch(SeekBar seekBar)
		{
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar)
		{
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
		{
			if (seekBar.getId() == R.id.seekBarRowNum)
			{
				textViewNumOfRows.setText(Integer.toString(progress + MIN_ROW_VALUE));
			}
			else if (seekBar.getId() == R.id.seekBarColNum)
			{
				textViewNumOfCols.setText(Integer.toString(progress + MIN_COL_VALUE));
			}
			else if (seekBar.getId() == R.id.seekBarDifficultyLevel)
			{
				textViewDifficultyLevel.setText(Integer.toString(progress + 1));
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_options);

		myAData = (ApplicationData) getApplication();

		intializeGUI();

		buttonSubmit = (Button) this.findViewById(R.id.buttonSubmit);
		buttonSubmit.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				try
				{
					int numOfRows = Integer.parseInt(textViewNumOfRows.getText().toString());
					int numOfCols = Integer.parseInt(textViewNumOfCols.getText().toString());
					int difficultyLevel = Integer.parseInt(textViewDifficultyLevel.getText().toString());
					long randomSeed = Long.parseLong(editTextRandomSeed.getText().toString());

					myAData.playerOptions.numOfRows = numOfRows;
					myAData.playerOptions.numOfCols = numOfCols;
					myAData.playerOptions.difficultyLevel = difficultyLevel;
					myAData.playerOptions.randomSeed = randomSeed;

					Intent mainActivityIntent = new Intent(myThis, MainActivity.class);
					startActivity(mainActivityIntent);

				}
				catch (NumberFormatException e)
				{
					createToast("Please enter an integer value for random Seed");
				}
			}

		});
	}

	private void createToast(String message)
	{
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	private void intializeGUI()
	{
		seekBarNumOfRows = (SeekBar) this.findViewById(R.id.seekBarRowNum);
		seekBarNumOfCols = (SeekBar) this.findViewById(R.id.seekBarColNum);
		seekBarDifficultyLevel = (SeekBar) this.findViewById(R.id.seekBarDifficultyLevel);
		editTextRandomSeed = (EditText) this.findViewById(R.id.editTextRandomSeed);

		seekBarNumOfRows.setProgress(myAData.playerOptions.numOfRows - MIN_ROW_VALUE);
		seekBarNumOfCols.setProgress(myAData.playerOptions.numOfCols - MIN_COL_VALUE);
		seekBarDifficultyLevel.setProgress(myAData.playerOptions.difficultyLevel);
		editTextRandomSeed.setText(Long.toString(myAData.playerOptions.randomSeed));

		textViewNumOfRows = (TextView) this.findViewById(R.id.textViewRowNum);
		textViewNumOfCols = (TextView) this.findViewById(R.id.textViewColNum);
		textViewDifficultyLevel = (TextView) this.findViewById(R.id.textViewDifficultyLevel);

		textViewNumOfRows.setText(Integer.toString(myAData.playerOptions.numOfRows));
		textViewNumOfCols.setText(Integer.toString(myAData.playerOptions.numOfCols));
		textViewDifficultyLevel.setText(Integer.toString(myAData.playerOptions.difficultyLevel));

		seekBarNumOfRows.setOnSeekBarChangeListener(seekBarListener);
		seekBarNumOfCols.setOnSeekBarChangeListener(seekBarListener);
		seekBarDifficultyLevel.setOnSeekBarChangeListener(seekBarListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.final_project_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
