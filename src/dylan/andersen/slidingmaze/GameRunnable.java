package dylan.andersen.slidingmaze;

import java.util.Random;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GameRunnable implements Runnable
{
	Handler myHandler; //to communicate with the GUI thread
	public boolean stopThread = false; //used to stop the run loop
	public boolean pauseThread;
	ApplicationData myAData;
	
	public boolean shouldCheckPlayerStatus = true;
	
	static final long oneFrameTimeInMS = 1000/60;

	public GameRunnable(Handler aHandler, ApplicationData myAData)
	{
		myHandler = aHandler;
		this.myAData = myAData;
		
		this.pauseThread = false;
	}

	//runs until stopThread is true, will send handler a random id to change image src to
	//during a set interval of time
	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				if (stopThread)
				{
					return;
				}
				
				Message msg = new Message();
				GameRunnableMessage myMessage = new GameRunnableMessage();
				
				if(pauseThread || myAData.gameBoard == null || myAData.gameBoard.player== null) //needed in case user goes back to options menu
				{
					Thread.sleep(oneFrameTimeInMS);
					continue;
				}
				
				//Log.i("final", "running game");
				
				
				if(myAData.gameBoard.player.stopped)
				{
					if(shouldCheckPlayerStatus)
					{
						myAData.gameBoard.recenterPlayer();						
						if(myAData.gameBoard.playerHasWon())
						{
							myMessage.playerWon = true;
							pauseThread = true;
						}
						shouldCheckPlayerStatus = false;
						msg.obj = myMessage;
						myHandler.dispatchMessage(msg);
					}
					
				}
				else
				{
					myAData.gameBoard.player.moveForOneFrame();
					msg.obj = myMessage;
					myHandler.dispatchMessage(msg);
					shouldCheckPlayerStatus = true;
				}
				
				Thread.sleep(oneFrameTimeInMS);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

	}

}
