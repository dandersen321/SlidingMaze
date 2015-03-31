package dylan.andersen.slidingmaze;

import java.util.ArrayList;
import java.util.List;

public class GameLevels
{
	
	
	static List<Level> myLevels;
	static int currentLevel = -1;
	
	static
	{
		myLevels = new ArrayList<Level>();
		//way easy
		//myLevels.add(new Level(25, 25, 5, 1417984638135L));
		
		
		//easy
		myLevels.add(new Level(25, 25, 5, 1417989191810L));
		myLevels.add(new Level(25, 25, 5, 1417837857007L));
		myLevels.add(new Level(25, 25, 5, 1417838798451L));
		myLevels.add(new Level(25, 25, 5, 1417989386151L));
		
		//moderate
		myLevels.add(new Level(25, 25, 5, 1417828148498L));
		myLevels.add(new Level(25, 25, 5, 1417989252471L));
		myLevels.add(new Level(25, 25, 5, 1416738316197L));
		myLevels.add(new Level(25, 25, 5, 1417988831347L));
		myLevels.add(new Level(25, 25, 5, 1417988183646L));
		
		//good
		myLevels.add(new Level(25, 25, 5, 1417841627844L));
		myLevels.add(new Level(25, 25, 5, 1417827453578L));
		
		//hard
		myLevels.add(new Level(38, 33, 5, 1417989556379L));
		myLevels.add(new Level(41, 35, 5, 1417990484259L));
		myLevels.add(new Level(45, 48, 5, 1417990079138L));
		myLevels.add(new Level(45, 48, 5, 1417989955217L));
		
	}
	
	public MazeRandomizer getNextLevel()
	{
		++currentLevel;
		if(currentLevel >= myLevels.size())
			return null;
		else
			return new MazeRandomizer(myLevels.get(currentLevel).rows,
				myLevels.get(currentLevel).cols, myLevels.get(currentLevel).difficultyLevel,
				myLevels.get(currentLevel).seedNumber);
	}
	
	
}
