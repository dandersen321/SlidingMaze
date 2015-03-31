package dylan.andersen.slidingmaze;

import dylan.andersen.slidingmaze.Player.MoveDirection;
import android.os.Bundle;

public class GameBundler
{
	String playerRowKey = "playerX";
	String playerColKey = "playerY";
	String playerMoveDirectionKey = "playerMoveDir";
	String playerStoppedKey = "playerStopped";
	String gameBoardSeedNumberKey = "seedNumber";
	String numOfRowsKey = "numOfRows";
	String numOfColsKey = "numOfCols";
	String difficultyLevelKey = "difficultyLevel";

	void createBundle(Bundle myBundle, ApplicationData myAData)
	{
		myBundle.putLong(gameBoardSeedNumberKey, myAData.gameBoard.seedUsed);
		myBundle.putInt(numOfRowsKey, myAData.playerOptions.numOfRows);
		myBundle.putInt(numOfColsKey, myAData.playerOptions.numOfCols);
		myBundle.putInt(difficultyLevelKey, myAData.playerOptions.difficultyLevel);
		
		GameTile playerTile = myAData.gameBoard.getTileFromPosition( myAData.gameBoard.player.playerAvatar.center);

		myBundle.putInt(playerRowKey, playerTile.rowPosition);
		myBundle.putInt(playerColKey, playerTile.colPosition);
		//myBundle.putInt(playerMoveDirectionKey, myAData.gameBoard.player.currentDirection.ordinal());
		myBundle.putSerializable(playerMoveDirectionKey, myAData.gameBoard.player.currentDirection);
		myBundle.putBoolean(playerStoppedKey, myAData.gameBoard.player.stopped);

	}

	void parseBundle(Bundle myBundle, ApplicationData myAData)
	{
		myAData.playerOptions.randomSeed = myBundle.getLong(gameBoardSeedNumberKey);
		myAData.playerOptions.numOfRows = myBundle.getInt(numOfRowsKey);
		myAData.playerOptions.numOfCols = myBundle.getInt(numOfColsKey);
		myAData.playerOptions.difficultyLevel = myBundle.getInt(difficultyLevelKey);

		MazeRandomizer myMazeRandomizer = new MazeRandomizer(myAData.playerOptions.numOfRows,
				myAData.playerOptions.numOfCols, myAData.playerOptions.difficultyLevel,
				myAData.playerOptions.randomSeed);

		myAData.gameBoard.intializeGameBoard(myMazeRandomizer.generateRandomMaze(), myMazeRandomizer.seedNumber, myMazeRandomizer.winArray);
		
		int playerRow = myBundle.getInt(playerRowKey);
		int playerCol = myBundle.getInt(playerColKey);
		
		myAData.gameBoard.player.setPositionViaRowAndCol(playerRow, playerCol);
		myAData.gameBoard.player.currentDirection = (MoveDirection) myBundle.getSerializable(playerMoveDirectionKey);
		myAData.gameBoard.player.stopped = myBundle.getBoolean(playerStoppedKey);
		
	}

}
