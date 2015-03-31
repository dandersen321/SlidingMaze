package dylan.andersen.slidingmaze;

import java.util.Random;

import dylan.andersen.slidingmaze.GameTile.TileState;

import android.util.Log;

public class MazeRandomizer
{
	//test class, to be deleted on deployment
	static public class MyTest
	{
		public static int randomSeeder = 3;
	}

	GameTile[][] board;

	int rows;
	int cols;

	int begRow = 1;
	int begCol = 1;

	int atMostMoves;
	TilePosition goal;
	Random myRand;

	long seedNumber;

	public Direction[] winArray;

	class TilePosition
	{
		public int row;
		public int col;

		TilePosition(int _row, int _col)
		{
			row = _row;
			col = _col;
		}

	}

	static enum Direction
	{
		up, down, right, left, NA
	}

	public MazeRandomizer(int rows, int cols, int difficultyLevel, long seedNumber)
	{
		this.rows = rows;
		this.cols = cols;
		this.atMostMoves = difficultyLevel * 5;
		if (seedNumber != -1)
		{
			this.seedNumber = seedNumber;
		}
		else
		{
			this.seedNumber = System.currentTimeMillis();
		}

		winArray = new Direction[atMostMoves];
		board = new GameTile[rows + 2][cols + 2];

	}

	public GameTile[][] generateRandomMaze()
	{
		intializeGameBoard();
		myRand = new Random();
		myRand.setSeed(seedNumber);
		//myRand.setSeed(MyTest.randomSeeder++);
		goal = new TilePosition(myRand.nextInt(rows) + 1, myRand.nextInt(cols) + 1);
		board[goal.row][goal.col].tileState = TileState.NA;
		Direction playerMustPath = generateFirstBlock();
		TilePosition currentPlayerPosition = new TilePosition(goal.row, goal.col);

		for (int i = 0; i < atMostMoves; ++i)
		{
			winArray[winArray.length - 1 - i] = inverseDirection(playerMustPath);
			playerMustPath = generateNextBlock(currentPlayerPosition, playerMustPath);
			if (playerMustPath == Direction.NA)
			{
				break;
			}
		}
		//playerMustPath = generateNextBlock(currentPlayerPosition, playerMustPath);
		board[currentPlayerPosition.row][currentPlayerPosition.col].tileState = TileState.Player;
		board[goal.row][goal.col].tileState = TileState.Goal;

		return board;
	}

	public Direction generateNextBlock(TilePosition currentPosition, Direction playerMustPath)
	{
		int randDepth = getRandDepth(currentPosition, playerMustPath);
		if (randDepth == -1)
			return Direction.NA;

		//constraints stop blocks from being places between new and current position

		addConstraints(currentPosition, playerMustPath, randDepth);

		TilePosition nextPosition = newPosition(playerMustPath, currentPosition, randDepth - 1);
		currentPosition.row = nextPosition.row;
		currentPosition.col = nextPosition.col;

		//the added block should be right next to the nextPosition
		Direction newPlayerPath = chooseNewPlayerPath(currentPosition, playerMustPath);
		if(newPlayerPath!= Direction.NA)
			addBlock(currentPosition, newPlayerPath);

		return newPlayerPath;

	}

	private void addBlock(TilePosition currentPosition, Direction playerMustPath)
	{
		TilePosition PositionToBlock = newPosition(inverseDirection(playerMustPath), currentPosition, 1);
		board[PositionToBlock.row][PositionToBlock.col].tileState = TileState.Blocked;

	}

	public Direction chooseNewPlayerPath(TilePosition currentPosition, Direction dir)
	{
		int randNum = myRand.nextInt(2);

		if (isHorDir(dir))
		{
			if (randNum == 0) //try up first
			{
				if (board[currentPosition.row - 1][currentPosition.col].tileState != TileState.Blocked
						&& board[currentPosition.row + 1][currentPosition.col].tileState != TileState.NA)
				{
					return Direction.up;
				}
				else
				{
					if (board[currentPosition.row + 1][currentPosition.col].tileState != TileState.Blocked
							&& board[currentPosition.row - 1][currentPosition.col].tileState != TileState.NA)
					{
						return Direction.down;
					}
					else
					{
						return Direction.NA;
					}
				}
			}
			else
			{
				if (board[currentPosition.row + 1][currentPosition.col].tileState != TileState.Blocked
						&& board[currentPosition.row - 1][currentPosition.col].tileState != TileState.NA)
				{
					return Direction.down;
				}
				else if (board[currentPosition.row - 1][currentPosition.col].tileState != TileState.Blocked
						&& board[currentPosition.row + 1][currentPosition.col].tileState != TileState.NA)
				{
					return Direction.up;
				}

				else
				{
					return Direction.NA;
				}

			}
		}
		else
		{
			if (randNum == 0) //try right first
			{
				if (board[currentPosition.row][currentPosition.col + 1].tileState != TileState.Blocked
						&& board[currentPosition.row][currentPosition.col - 1].tileState != TileState.NA)
				{
					return Direction.right;
				}
				else
				{
					if (board[currentPosition.row][currentPosition.col - 1].tileState != TileState.Blocked
							&& board[currentPosition.row][currentPosition.col + 1].tileState != TileState.NA)
					{
						return Direction.left;
					}
					else
					{
						return Direction.NA;
					}
				}
			}
			else
			{
				if (board[currentPosition.row][currentPosition.col - 1].tileState != TileState.Blocked
						&& board[currentPosition.row][currentPosition.col + 1].tileState != TileState.NA)
				{
					return Direction.left;
				}
				else if (board[currentPosition.row][currentPosition.col + 1].tileState != TileState.Blocked
						&& board[currentPosition.row][currentPosition.col - 1].tileState != TileState.NA)
				{
					return Direction.right;
				}
				else
				{
					return Direction.NA;
				}

			}
		}
	}

	public void addConstraints(TilePosition currentPosition, Direction dir, int randDepth)
	{
		if (dir == Direction.up)
		{
			for (int i = currentPosition.row - 1; i > currentPosition.row - randDepth; i--)
			{
				//lastIteration
				//				if(i == currentPosition.row - randDepth)
				//				{
				//					board[i][currentPosition.col].tileState = TileState.Blocked;
				//				}else
				if (board[i][currentPosition.col].tileState != TileState.Blocked)
				{
					board[i][currentPosition.col].tileState = TileState.NA;
				}
				else
				{
					Log.e("final", "error in addblockandconstraints up");
				}
			}
		}

		if (dir == Direction.down)
		{
			for (int i = currentPosition.row + 1; i < currentPosition.row + randDepth; i++)
			{
				//				if(i == currentPosition.row + randDepth)
				//				{
				//					board[i][currentPosition.col].tileState = TileState.Blocked;
				//				}else
				if (board[i][currentPosition.col].tileState != TileState.Blocked)
				{
					board[i][currentPosition.col].tileState = TileState.NA;
				}
				else
				{
					Log.e("final", "error in addblockandconstraints down");
				}
			}
		}

		if (dir == Direction.right)
		{
			for (int j = currentPosition.col + 1; j < currentPosition.col + randDepth; j++)
			{
				//				if(j == currentPosition.col + randDepth)
				//				{
				//					board[currentPosition.row][j].tileState = TileState.Blocked;
				//				} else 
				if (board[currentPosition.row][j].tileState != TileState.Blocked)
				{
					board[currentPosition.row][j].tileState = TileState.NA;
				}
				else
				{
					Log.e("final", "error in addblockandconstraints right");
				}
			}
		}

		if (dir == Direction.left)
		{
			for (int j = currentPosition.col - 1; j > currentPosition.col - randDepth; j--)
			{
				//				if(j == currentPosition.col - randDepth)
				//				{
				//					board[currentPosition.row][j].tileState = TileState.Blocked;
				//				} else if
				if (board[currentPosition.row][j].tileState != TileState.Blocked)
				{
					board[currentPosition.row][j].tileState = TileState.NA;
				}
				else
				{
					Log.e("final", "error in addblockandconstraints left");
				}
			}
		}
	}

	public Direction generateFirstBlock()
	{
		Direction blockDirection;
		//do
		//{
		blockDirection = getRandomDirection();

		//used if choosing something like row: 1 col:2 where the above tile is a block
		//in which case it uses the above tile instead
		TilePosition oppositePos = newPosition(inverseDirection(blockDirection), goal, 1);
		if (board[oppositePos.row][oppositePos.col].tileState == TileState.Blocked)
		{
			return blockDirection;
		}
		//} while (!validPlacement(blockDirection, goal.row, goal.col, 1));

		TilePosition newP = newPosition(blockDirection, goal, 1);
		setBoardPosition(newP, TileState.Blocked);

		return inverseDirection(blockDirection);

	}

	public TilePosition newPosition(Direction dir, TilePosition p, int depth)
	{
		int newRow = -1, newCol = -1;
		switch (dir)
		{
			case up:
				newRow = p.row - depth;
				newCol = p.col;
				break;
			case right:
				newCol = p.col + depth;
				newRow = p.row;
				break;
			case down:
				newRow = p.row + depth;
				newCol = p.col;
				break;
			case left:
				newCol = p.col - depth;
				newRow = p.row;
				break;

		}

		return new TilePosition(newRow, newCol);
	}

	//	public boolean validPlacement(Direction dir, int row, int col, int depth)
	//	{
	//		int newRow = -1, newCol = -1;
	//		switch (dir)
	//		{
	//			case up:
	//				newRow = row - depth;
	//				break;
	//			case right:
	//				newCol = col + depth;
	//				break;
	//			case down:
	//				newRow = row + depth;
	//				break;
	//			case left:
	//				newCol = col - depth;
	//				break;
	//
	//		}
	//
	//		if (IsVerDir(dir))
	//		{
	////			if (Math.min(row, newRow) < begRow || Math.max(row, newRow) >= rows)
	////			{
	////				return false;
	////			}
	//
	//			for (int i = Math.min(row, newRow); i <= Math.max(row, newRow); ++i)
	//			{
	//				if (board[i][col].tileState == TileState.Blocked)
	//					return false;
	//			}
	//		}
	//
	//		if (isHorDir(dir))
	//		{
	////			if (Math.min(col, newCol) < begCol || Math.max(col, newCol) >= rows)
	////			{
	////				return false;
	////			}
	//
	//			for (int i = Math.min(col, newCol); i <= Math.max(col, newCol); ++i)
	//			{
	//				if (board[i][col].tileState == TileState.Blocked)
	//					return false;
	//			}
	//		}
	//
	//		return true;
	//
	//	}

	public boolean IsVerDir(Direction dir)
	{
		return dir == Direction.up || dir == Direction.down;
	}

	public boolean isHorDir(Direction dir)
	{
		return dir == Direction.left || dir == Direction.right;
	}

	public void setBoardPosition(TilePosition newPosition, TileState newState)
	{
		board[newPosition.row][newPosition.col].tileState = newState;
	}

	public Direction inverseDirection(Direction dir)
	{
		switch (dir)
		{
			case up:
				return Direction.down;
			case right:
				return Direction.left;
			case down:
				return Direction.up;
			case left:
				return Direction.right;
			default:
				return Direction.NA;

		}
	}

	public Direction getRandomDirection()
	{
		int randNum = myRand.nextInt(4);
		switch (randNum)
		{
			case 0:
				return Direction.up;
			case 1:
				return Direction.right;
			case 2:
				return Direction.down;
			default:
				return Direction.left;
		}
	}

	public int getRandDepth(TilePosition currentPosition, Direction dir)
	{
		int validMovesAvailable = 0;
		if (dir == Direction.up)
		{
			for (int i = currentPosition.row - 1; i >= begRow; i--)
			{
				if (board[i][currentPosition.col].tileState != TileState.Blocked)
				{
					validMovesAvailable++;
				}
				else
				{
					break;
				}
			}
		}

		if (dir == Direction.down)
		{
			for (int i = currentPosition.row + 1; i <= rows; i++)
			{
				if (board[i][currentPosition.col].tileState != TileState.Blocked)
				{
					validMovesAvailable++;
				}
				else
				{
					break;
				}
			}
		}

		if (dir == Direction.right)
		{
			for (int j = currentPosition.col + 1; j <= cols; j++)
			{
				if (board[currentPosition.row][j].tileState != TileState.Blocked)
				{
					validMovesAvailable++;
				}
				else
				{
					break;
				}
			}
		}

		if (dir == Direction.left)
		{
			for (int j = currentPosition.col - 1; j >= begCol; j--)
			{
				if (board[currentPosition.row][j].tileState != TileState.Blocked)
				{
					validMovesAvailable++;
				}
				else
				{
					break;
				}
			}
		}

		if (validMovesAvailable == 0)
			return -1;

		//desiredDepth is where to put the block, hence we add 2, one since we can't have it be 0, and 1 for the player
		int desiredDepth = myRand.nextInt(validMovesAvailable) + 2;
		int orgDesiredDepth = desiredDepth;

		while (!isValidDepth(currentPosition, dir, desiredDepth))
		{
			desiredDepth++;
			if (desiredDepth > validMovesAvailable + 1)
			{
				desiredDepth = 2;
			}
			//not an else if (think if desired is 2)
			if (desiredDepth == orgDesiredDepth)
			{
				return -1;
			}

		}

		return desiredDepth;

	}

	boolean isValidDepth(TilePosition currentPosition, Direction dir, int desiredDepth)
	{
		TilePosition newP = newPosition(dir, currentPosition, desiredDepth);

		if (IsVerDir(dir))
		{
			if ((newP.row < rows && board[newP.row][newP.col].tileState != TileState.NA)
					|| (newP.row >= begRow && board[newP.row][newP.col].tileState != TileState.NA))
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		else
		{
			if ((newP.col < cols && board[newP.row][newP.col].tileState != TileState.NA)
					|| (newP.col > begCol && board[newP.row][newP.col].tileState != TileState.NA))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	public void intializeGameBoard()
	{
		for (int j = 0; j <= cols + 1; ++j)
		{
			board[0][j] = new GameTile(0, j, -1, -1);
			board[0][j].tileState = TileState.Blocked;
			board[rows + 1][j] = new GameTile(rows + 1, j, -1, -1);
			board[rows + 1][j].tileState = TileState.Blocked;
		}

		for (int i = 0; i <= rows + 1; ++i)
		{
			board[i][0] = new GameTile(i, 0, 1, 1);
			board[i][0].tileState = TileState.Blocked;
			board[i][cols + 1] = new GameTile(i, cols + 1, -1, -1);
			board[i][cols + 1].tileState = TileState.Blocked;
		}

		for (int i = begRow; i <= rows; ++i)
		{
			for (int j = begCol; j <= cols; ++j)
			{
				board[i][j] = new GameTile(i, j, -1, -1);
				board[i][j].tileState = TileState.Open;
			}
		}
	}

}
