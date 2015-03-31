package dylan.andersen.slidingmaze;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import dylan.andersen.slidingmaze.GameTile.TileState;
import dylan.andersen.slidingmaze.MazeRandomizer.Direction;
import dylan.andersen.slidingmaze.Player.MoveDirection;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBarActivity;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;

public class GameActivity extends ActionBarActivity implements RecognitionListener
{
	ApplicationData myAData;
	Button testButton;
	Button messageButton;

	private static final int REQUEST_CODE = 1234;

	float startX, startY;
	long startTime;

	final float xMoveMax = 100;
	final float yMoveMax = 100;

	static GameRunnable gameRunnable;
	static Thread gameThread;
	
	SpeechRecognizer recognizer;
	Intent recognizerIntent;
	GameActivity thisActivity = this;
	
	
	@SuppressLint("HandlerLeak")
	Handler gameHandler = new Handler(new Handler.Callback()
	{
		@Override
		public boolean handleMessage(Message msg)
		{

			GameRunnableMessage myMessage = (GameRunnableMessage) msg.obj;

			if (myMessage.playerWon)
			{
				Log.i("final", "playerJustWonPrev");
				//playerJustWon();
				Runnable task = new Runnable()
				{

					@Override
					public void run()
					{
						playerJustWon();
					}

				};
				runOnUiThread(task);
				Log.i("final", "playerJustWonAfter");
				return true;
				//thisActivity.newGame();	
			}
			//Log.i("final", "invaldating");
			myAData.gameBoard.postInvalidate(); //repaint
			//Log.i("final", "invaldating2");
			return true;

		}
	});

	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		myAData = (ApplicationData) getApplication();

		myAData.currentContext = this.getApplicationContext();
		
		createButtons();
		createGameBoard();
		//Intent intent = getIntent();

		

		recognizer = SpeechRecognizer.createSpeechRecognizer(this);
		SpeechRecognizer.createSpeechRecognizer(this);
		recognizer.setRecognitionListener(this);

		recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());

		recognizer.startListening(recognizerIntent);
		
		

	}

	private void createButtons()
	{
		testButton = (Button) this.findViewById(R.id.testButton);

		testButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				gameRunnable.pauseThread = true;
				newGame();
				myAData.gameBoard.postInvalidate();

				//				PackageManager pm = getPackageManager();
				//				List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				//						RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
				//				if (activities.size() == 0)
				//				{
				//					myAData.createToast("no speak!");
				//				}
				//
				//				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				//				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				//				intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
				//				startActivityForResult(intent, REQUEST_CODE);

			}

		});

		messageButton = (Button) this.findViewById(R.id.seedNumber);

		messageButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				//gameRunnable.pauseThread = true;
				//newGame();
				//myAData.gameBoard.postInvalidate();
				
				
				recognizer.startListening(recognizerIntent);
			}

		});
		
	}

	public void playerJustWon()
	{
		final String winMessage = "Nice Job!";

		messageButton.setText(winMessage);
		messageButton.setTextColor(Color.parseColor("#ff0000"));
		messageButton.getBackground().setColorFilter(new LightingColorFilter(0x00FF00, 0x00FF00));

		ObjectAnimator fadeOut = ObjectAnimator.ofFloat(messageButton, "alpha", 1f, .3f);
		fadeOut.setDuration(0);
		ObjectAnimator fadeIn = ObjectAnimator.ofFloat(messageButton, "alpha", .3f, 1f);
		fadeIn.setDuration(4000);

		final AnimatorSet mAnimationSet = new AnimatorSet();

		mAnimationSet.play(fadeIn).after(fadeOut);

		mAnimationSet.addListener(new AnimatorListenerAdapter()
		{
			@Override
			public void onAnimationEnd(Animator animation)
			{
				super.onAnimationEnd(animation);
				//mAnimationSet.start();
				if (messageButton.getText().equals(winMessage)) //just in another message is on the screen
					messageButton.setText("");

				messageButton.setTextColor(Color.parseColor("#000000"));
				messageButton.getBackground().setColorFilter(null);
			}
		});
		mAnimationSet.start();

		if (myAData.playerOptions.randomSeed > 0) //if using a manual seed, set to random once board beaten
			myAData.playerOptions.randomSeed = -1;

		Log.i("final", "called new game from just won Prev");
		newGame();
		Log.i("final", "called new game from just won After");
	}

	//	public void createAlertDialog()
	//	{
	//		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(thisActivity);
	//
	//		alertDialogBuilder.setTitle("You won!");
	//
	//		alertDialogBuilder.setMessage("Click yes to continue!").setCancelable(false)
	//				.setPositiveButton("Yes", new DialogInterface.OnClickListener()
	//				{
	//					public void onClick(DialogInterface dialog, int id)
	//					{
	//						newGame();
	//						dialog.cancel();
	//					}
	//				}).setNegativeButton("Exit", new DialogInterface.OnClickListener()
	//				{
	//					public void onClick(DialogInterface dialog, int id)
	//					{
	//						thisActivity.finish();
	//						
	//					}
	//				});
	//
	//		// create alert dialog
	//		AlertDialog alertDialog = alertDialogBuilder.create();
	//
	//		// show it
	//		alertDialog.show();
	//	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		myAData.createToast("speaking you is");
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
		{
			// Populate the wordsList with the String values the recognition engine thought it heard
			ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			for (String s : matches)
			{
				myAData.createToast(s);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void intializeViewsAndDimensions()
	{
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
		int buttonRowHeight = 150; //xml button height
		
		Log.i("final", "buttonRowHeight:" + Integer.toString(buttonRowHeight));
		
		int width = size.x;
		int height = size.y - buttonRowHeight;

		Log.i("final", "width:" + Integer.toString(width));
		Log.i("final", "height:" + Integer.toString(height));

		myAData.gameBoard = (GameBoard) findViewById(R.id.gameBoardView);
		myAData.gameBoard.width = width;
		myAData.gameBoard.height = height;
	}

	public void createGameBoard()
	{
		intializeViewsAndDimensions();
		gameRunnable = new GameRunnable(gameHandler, myAData);
		gameThread = new Thread(gameRunnable);

		newGame();
		gameThread.start();
	}

	public void newGame()
	{
		myAData.cheatingEnabled = false;
		MazeRandomizer myMazeRandomizer;
		if (myAData.playRandom)
		{
			myMazeRandomizer = new MazeRandomizer(myAData.playerOptions.numOfRows, myAData.playerOptions.numOfCols,
					myAData.playerOptions.difficultyLevel, myAData.playerOptions.randomSeed);
		}
		else
		{
			myMazeRandomizer = myAData.gameLevels.getNextLevel();
			if (myMazeRandomizer == null)
			{
				//player has done all the levels
				this.finish();
				return;
			}
		}

		myAData.gameBoard.intializeGameBoard(myMazeRandomizer.generateRandomMaze(), myMazeRandomizer.seedNumber,
				myMazeRandomizer.winArray);

		myAData.gameBoard.postInvalidate();
		gameRunnable.pauseThread = false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{

		int action = MotionEventCompat.getActionMasked(event);

		if (action == MotionEvent.ACTION_DOWN)
		{
			startX = event.getX();
			startY = event.getY();
			startTime = event.getEventTime();
			Log.d("hw9", "down");
			return false;
		}

		if (action == MotionEvent.ACTION_UP)
		{
			float difX = event.getX() - startX;
			float difY = event.getY() - startY;

			long endTime = event.getEventTime() - startTime;

			if (Math.abs(difX) > xMoveMax && Math.abs(difY) > yMoveMax) //checks to see if the user moved significantly more in the x dir
			{
				if(Math.abs(difX) > Math.abs(difY) + yMoveMax)
				{
					if (difX > xMoveMax)
					{
						Log.i("hw9", "right");
						right();
					}
					else if(Math.abs(difX) > xMoveMax)
					{
						Log.i("hw9", "left");
						left();
					}
				}
				else if(Math.abs(difY) > Math.abs(difX) + xMoveMax) //checks to see if the user moved significantly more in the y dir
				{
					if (difY > yMoveMax)
					{
						Log.i("hw9", "down");
						down();
					}
					else if(Math.abs(difY) > yMoveMax)
					{
						Log.i("hw9", "up");
						up();
					}
				}
				else
				{
					myAData.createToast("Too much in both X and Y direction");
					Log.i("hw9", "too much in both directions");
				}		
				
			}
			else if (difX > xMoveMax)
			{
				Log.i("hw9", "right");
				right();
			}
			else if (Math.abs(difX) > xMoveMax)
			{
				Log.i("hw9", "left");
				left();
			}
			else if (difY > yMoveMax)
			{

				Log.i("hw9", "down");
				down();
			}
			else if (Math.abs(difY) > yMoveMax)
			{

				Log.i("hw9", "up");
				up();
			}

			else
			{
				if (endTime > 750)
				{
					Log.i("hw9", "Held down");

					held();
				}
				else
				{
					myAData.createToast("Not enough in either X or Y direction");
				}
			}
			return false;
		}

		return true;

	}

	public void up()
	{
		if (myAData.gameBoard.player.stopped == false)
			return;

		myAData.gameBoard.player.startDirection(MoveDirection.up);

	}

	public void down()
	{
		if (myAData.gameBoard.player.stopped == false)
			return;
		myAData.gameBoard.player.startDirection(MoveDirection.down);

	}

	public void left()
	{
		if (myAData.gameBoard.player.stopped == false)
			return;

		myAData.gameBoard.player.startDirection(MoveDirection.left);

	}

	public void right()
	{
		if (myAData.gameBoard.player.stopped == false)
			return;
		myAData.gameBoard.player.startDirection(MoveDirection.right);

	}

	public void held()
	{
		myAData.createToast("held");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_activity_options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.itemRestart)
		{
			myAData.gameBoard.resetPlayer();
			return true;
		}
		else if (id == R.id.itemGetSeedNumber)
		{
			TextView textViewSeedNumber = (TextView) this.findViewById(R.id.seedNumber);
			textViewSeedNumber.setText(Long.toString(myAData.gameBoard.seedUsed));
			return true;
		}
		else if (id == R.id.itemShowPath)
		{
			Direction[] winArray = myAData.gameBoard.winArray;
			String instructions = "";
			for (int i = 0; i < winArray.length; ++i)
			{
				if (winArray[i] == null) //not every elem in winArray will have a value
					continue;
				Log.d("final", winArray[i].toString());
				instructions += winArray[i].toString() + "->";
			}
			instructions += "GOAL";
			myAData.cheatingEnabled = true;
			myAData.gameBoard.postInvalidate();
			TextView textViewSeedNumber = (TextView) this.findViewById(R.id.seedNumber);
			textViewSeedNumber.setText(instructions);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		intializeViewsAndDimensions();

		super.onRestoreInstanceState(savedInstanceState);
		GameBundler myBundler = new GameBundler();
		myBundler.parseBundle(savedInstanceState, myAData);
		Toast.makeText(this, "restoring", 1000).show();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		// Save the values you need from your textview into "outState"-object
		GameBundler myBundler = new GameBundler();
		myBundler.createBundle(outState, myAData);
		Toast.makeText(this, "saving", 1000).show();
		gameRunnable.stopThread = true;
		try
		{
			gameThread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		gameRunnable.stopThread = true;
		try
		{
			gameThread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onReadyForSpeech(Bundle params)
	{
		Log.i("final", "readyForSpeech");
	}

	@Override
	public void onBeginningOfSpeech()
	{
		Log.i("final", "startingSpeech");
	}

	@Override
	public void onRmsChanged(float rmsdB)
	{

	}

	@Override
	public void onBufferReceived(byte[] buffer)
	{

	}

	@Override
	public void onEndOfSpeech()
	{
		Log.i("final", "endOfSpeech");
	}

	@Override
	public void onError(int error)
	{
		Log.e("final", "some some of error on speech probably");
		Log.e("final", "error code: " + Integer.toString(error));

		String mError = "";
		switch (error)
		{
			case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
				mError = " network timeout";
				recognizer.startListening(recognizerIntent);
				break;
			case SpeechRecognizer.ERROR_NETWORK:
				mError = " network";
				//toast("Please check data bundle or network settings");
				return;
			case SpeechRecognizer.ERROR_AUDIO:
				mError = " audio";
				break;
			case SpeechRecognizer.ERROR_SERVER:
				mError = " server";
				recognizer.startListening(recognizerIntent);
				break;
			case SpeechRecognizer.ERROR_CLIENT:
				mError = " client";
				break;
			case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
				mError = " speech time out";
				recognizer.startListening(recognizerIntent);
				break;
			case SpeechRecognizer.ERROR_NO_MATCH:
				mError = " no match";
				recognizer.startListening(recognizerIntent);

				break;
			case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
				recognizer.destroy();
				recognizer.setRecognitionListener(this);
				mError = " recogniser busy";
				recognizer.startListening(recognizerIntent);
				break;
			case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
				mError = " insufficient permissions";
				break;
			default:
				mError = "unknown error";
				break;

		}
		Log.i("final", "Error: " + error + " - " + mError);
	}

	@Override
	public void onResults(Bundle resultsBundle)
	{
		Log.i("final", "onResults");
		if (resultsBundle == null)
		{
			Log.i("final", "null resultsBundle");
			recognizer.startListening(recognizerIntent);
			return;
		}
		ArrayList<String> matches = resultsBundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		
		boolean resultFound = false;

		for (String elem : matches)
		{
			if (elem.toLowerCase().contains("up"))
			{
				up();
				resultFound = true;
				break;
			}
			else if (elem.toLowerCase().contains("right"))
			{
				right();
				resultFound = true;
				break;
			}
			else if (elem.toLowerCase().contains("down"))
			{
				down();
				resultFound = true;
				break;
			}
			else if (elem.toLowerCase().contains("left"))
			{
				left();
				resultFound = true;
				break;
			}
		}
		
		if(!resultFound)
		{
			Log.i("final", "no direction recognized, words were:");
			for (String elem : matches)
			{
				Log.i("final", elem.toLowerCase());
			}
		}

		Log.i("final", "end of onResults");

		recognizer.startListening(recognizerIntent);

		Log.i("final", "end of onResults after start listening");
	}

	@Override
	public void onPartialResults(Bundle partialResults)
	{

	}

	@Override
	public void onEvent(int eventType, Bundle params)
	{
		Log.i("final", "onEvent");

	}
}