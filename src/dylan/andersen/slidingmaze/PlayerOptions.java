package dylan.andersen.slidingmaze;

public class PlayerOptions
{
	public int numOfRows;
	public int numOfCols;
	public int difficultyLevel;
	public long randomSeed;
	
	public PlayerOptions()
	{
		numOfRows = 25;
		numOfCols = 25;
		difficultyLevel = 5;
		randomSeed = -1;
//		numOfRows = 4;
//		numOfCols = 3;
//		difficultyLevel = 5;
//		//randomSeed = 1417844242673L;
//		randomSeed = 1417848776043L;
	}

}
