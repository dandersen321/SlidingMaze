package dylan.andersen.slidingmaze;

import android.widget.GridView;

public class GameBoardPanelInfo
{
	int numOfCols;
	int numOfRows;
	int numOfTiles;
	int width;
	int height;
	
	int colWidth;
	int rowHeight;
	
	GameTile tileArray[];

	public void setNumOfCols(int numOfCols)
	{
		this.numOfCols = numOfCols;
		updateNumOfTiles();
	}

	public int getNumOfCols()
	{
		return numOfCols;
	}

	public int getNumOfRows()
	{
		return numOfRows;
	}

	public void setNumOfRows(int numOfRows)
	{
		this.numOfRows = numOfRows;
		updateNumOfTiles();
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public int getColWidth()
	{
		return colWidth;
	}

	public void setColWidth(int colWidth)
	{
		this.colWidth = colWidth;
	}

	public int getRowHeight()
	{
		return rowHeight;
	}

	public void setRowHeight(int rowHeight)
	{
		this.rowHeight = rowHeight;
	}

	public GameTile[] getTileArray()
	{
		return tileArray;
	}

	public void setTileArray(GameTile[] tileArray)
	{
		this.tileArray = tileArray;
	}
	
	public void updateNumOfTiles()
	{
		this.numOfTiles = this.numOfCols * this.numOfRows;
	}

}
