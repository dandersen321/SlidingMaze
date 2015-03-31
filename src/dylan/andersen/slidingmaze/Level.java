package dylan.andersen.slidingmaze;

public class Level
{
	int rows;
	int cols;
	int difficultyLevel;
	long seedNumber;
	
	Level(int rows, int cols, int difficultyLevel, long seedNumber)
	{
		this.rows = rows;
		this.cols = cols;
		this.difficultyLevel = difficultyLevel;
		this.seedNumber = seedNumber;
	}
}