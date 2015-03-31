package dylan.andersen.slidingmaze;
import dylan.andersen.slidingmaze.R;
import android.support.v7.app.ActionBarActivity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity
{
	ApplicationData myAData = new ApplicationData();
	
	Button startLevels;
	Button startRandom;
	Button menuOptions;
	
	MainActivity myThis = this;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myAData = (ApplicationData) getApplication();
		
		intializeButtons();
	}

	private void intializeButtons()
	{
		startLevels = (Button) this.findViewById(R.id.buttonStartLevels);
		startLevels.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				myAData.playRandom = false;
				Intent gameIntent = new Intent(myThis, GameActivity.class);
				startActivity(gameIntent);	
			}
			
		});
		
		startRandom = (Button) this.findViewById(R.id.buttonStartRandom);
		startRandom.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				myAData.playRandom = true;
				Intent gameIntent = new Intent(myThis, GameActivity.class);
				startActivity(gameIntent);
				
			}
			
		});
		
		menuOptions = (Button) this.findViewById(R.id.menuOptions);
		menuOptions.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent menuOptionsIntent = new Intent(myThis, MenuOptions.class);
				startActivity(menuOptionsIntent);
				
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.final_project_main, menu);
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
