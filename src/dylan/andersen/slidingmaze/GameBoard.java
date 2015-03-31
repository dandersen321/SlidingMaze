package dylan.andersen.slidingmaze;

import dylan.andersen.slidingmaze.GameTile.TileState;
import dylan.andersen.slidingmaze.MazeRandomizer.Direction;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

public class GameBoard extends View
{
	Player player;
	ApplicationData myAData;
	int rows;
	int cols;
	GameTile[][] tileArray;

	int rowHeight;
	int colWidth;

	int width;
	int height;
	
	int startingPlayerRow, startingPlayerCol;
	long seedUsed;
	
	boolean ready = false;
	
	Direction[] winArray;
	
	//static final int minRowHeight = 28;
	//static final int minColWidth = 28;
	
	public void intializeGameBoard(GameTile[][] randomMaze, long seedUsed, Direction[] winArray)
	{
		this.seedUsed = seedUsed;
		this.winArray = winArray;
		
		rows = randomMaze.length - 2;
		cols = randomMaze[0].length - 2;

		setRowAndColSize();

		//tileArray = randomMaze;
		tileArray = new GameTile[rows + 2][cols + 2];

		for (int i = 0; i <= rows + 1; ++i)
		{
			for (int j = 0; j <= cols + 1; ++j)
			{
				tileArray[i][j] = new GameTile(i, j, rowHeight, colWidth);
				tileArray[i][j].tileState = randomMaze[i][j].tileState;

				if (tileArray[i][j].tileState == TileState.Player)
				{
					tileArray[i][j].tileState = TileState.Open;
					intializePlayer();
					startingPlayerRow = i;
					startingPlayerCol = j;
					player.setPositionViaRowAndCol(i, j);
				}

			}
		}
		
		ready = true;
		

	}

	public void intializePlayer()
	{

		player = new Player(myAData, rowHeight, colWidth);

		player.setPosition(new Position(rowHeight / 2, colWidth / 2));
		player.playerAvatar.radius = rowHeight / 2 - 2;
		
		//if(rows > 50 || cols > 50)
		//	player.playerAvatar.radius = rowHeight / 2 - 3;
	}

	public void setRowAndColSize()
	{
		int maxWidth = width / (cols + 2);
		int maxHeight = (int) ((height * 0.90) / (rows + 2));

		int newRowHeight, newColWidth;

		if (maxHeight < maxWidth)
		{
			newRowHeight = maxHeight;
			newColWidth = maxHeight;
		}
		else
		{
			newRowHeight = maxWidth;
			newColWidth = maxWidth;
		}
		
//		if(newRowHeight < minRowHeight)
//			newRowHeight = minRowHeight*2; //it will overflow the screen, so zoom in
//		
//		if(newColWidth < minColWidth)
//			newColWidth = minColWidth*2;


		rowHeight = newRowHeight;
		colWidth = newColWidth;
	}

	public void setTileArray()
	{
		ready = false;
		tileArray = new GameTile[rows + 2][cols + 2];

		for (int i = 0; i <= rows + 1; ++i)
		{
			for (int j = 0; j <= cols + 1; ++j)
			{
				Position NW = new Position(j * colWidth, i * rowHeight), NE = new Position(j * colWidth + colWidth, i
						* rowHeight), SW = new Position(j * colWidth, i * rowHeight + rowHeight), SE = new Position(j
						* colWidth + colWidth, i * rowHeight + rowHeight);

				tileArray[i][j] = new GameTile(i, j, rowHeight, colWidth);
			}
		}
		ready = true;
	}

	public GameBoard(Context context, AttributeSet attrs, int defStyle)
	{

		super(context, attrs, defStyle);
		myAData = (ApplicationData) context.getApplicationContext();
	}

	public GameBoard(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		myAData = (ApplicationData) context.getApplicationContext();
	}

	public GameBoard(Context context)
	{
		super(context);
		myAData = (ApplicationData) context.getApplicationContext();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		for (int i = 0; i <= rows + 1; ++i)
		{
			for (int j = 0; j <= cols + 1; ++j)
			{
				if(this.ready == false)
					return;
				try
				{
					tileArray[i][j].drawCell(canvas, myAData.cheatingEnabled);
				}
				catch(Exception e)
				{
					Log.e("final", e.toString());
				}
				
			}
		}

		player.drawPlayer(canvas);
	}

	public int getRows()
	{
		return rows;
	}

	public void setRows(int rows)
	{
		this.rows = rows;
	}

	public int getCols()
	{
		return cols;
	}

	public void setCols(int cols)
	{
		this.cols = cols;
	}

	public GameTile[][] getTileArray()
	{
		return tileArray;
	}

	public void setTileArray(GameTile[][] tileArray)
	{
		this.tileArray = tileArray;
	}

	public int getRowHeight()
	{
		return rowHeight;
	}

	public void setRowHeight(int rowHeight)
	{
		this.rowHeight = rowHeight;
	}

	public int getColWidth()
	{
		return colWidth;
	}

	public void setColWidth(int colWidth)
	{
		this.colWidth = colWidth;
	}

	

	public boolean rectangleTouchingBarrier(Rectangle myRec)
	{
		if (getTileFromPosition(myRec.NE).tileState == TileState.Blocked
				|| getTileFromPosition(myRec.NW).tileState == TileState.Blocked
				|| getTileFromPosition(myRec.SE).tileState == TileState.Blocked
				|| getTileFromPosition(myRec.SW).tileState == TileState.Blocked)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public GameTile getTileFromPosition(Position p)
	{
		int r = p.y / rowHeight;
		int c = p.x / colWidth;

		return tileArray[r][c];
	}

	public boolean playerHasWon()
	{
		if(player.stopped && getTileFromPosition(player.playerAvatar.center).tileState == TileState.Goal)
		{
			return true;
		}
		else
			return false;
	}

	public void recenterPlayer()
	{
		GameTile currentPlayerTile = getTileFromPosition(player.playerAvatar.center);
		player.setPositionViaRowAndCol(currentPlayerTile.rowPosition, currentPlayerTile.colPosition);
		
	}

	public void resetPlayer()
	{
		player.setPositionViaRowAndCol(startingPlayerRow, startingPlayerCol);
		this.invalidate();
		
	}

}
