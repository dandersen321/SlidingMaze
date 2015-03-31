package dylan.andersen.slidingmaze;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class ApplicationData extends Application
{
	GameBoardPanelInfo boardInfo;
	GameBoard gameBoard;
	
	PlayerOptions playerOptions;
	
	boolean cheatingEnabled = false;
	boolean playRandom = true;
	
	GameLevels gameLevels = new GameLevels();
	

	Context currentContext;
	
	public ApplicationData()
	{
		boardInfo = new GameBoardPanelInfo();
		playerOptions = new PlayerOptions();
	}
	
	public void createToast(String message)
	{
		createToast(message, 1000);
	}
	
	public void createToast(String message, int miliseconds)
	{
		Toast.makeText(currentContext, message, miliseconds).show();
	}
	
	
}
