package dylan.andersen.slidingmaze;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class GameTile
{

	TileState tileState;
	Integer rowPosition;
	Integer colPosition;
	Integer gridPosition;

	Rectangle cellRectangle;

	
	static Paint cellPaint = new Paint();
	static Paint borderPaint = new Paint();
	
	static
	{
		borderPaint.setAntiAlias(true);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setStrokeJoin(Paint.Join.ROUND);
		borderPaint.setStrokeWidth(3);
		
		borderPaint.setColor(Color.LTGRAY);
		
		cellPaint.setAntiAlias(true);
		cellPaint.setStyle(Paint.Style.FILL);
	
	}

	static public enum TileState //NA and Player are for creation
	{
		Open, Blocked, Goal, NA, Player
	}

	static Integer[] backgroundImages =
		{ R.drawable.dark_test, R.drawable.blue_test };

	static Map<TileState, Integer> backgroundImagesMap = new HashMap<TileState, Integer>();

	static
	{
		backgroundImagesMap.put(TileState.Open, R.drawable.blue_test);
		backgroundImagesMap.put(TileState.Blocked, R.drawable.dark_test);
	}

	public GameTile(int _rowPosition, int _colPosition, int rowHeight, int colWidth)
	{
		rowPosition = _rowPosition;
		colPosition = _colPosition;
		tileState = TileState.Open;

		if (rowHeight != -1)
		{

			Position NW = new Position(_colPosition * colWidth, _rowPosition * rowHeight), NE = new Position(
					_colPosition * colWidth + colWidth, _rowPosition * rowHeight), SW = new Position(_colPosition
					* colWidth, _rowPosition * rowHeight + rowHeight), SE = new Position(_colPosition * colWidth
					+ colWidth, _rowPosition * rowHeight + rowHeight);

			cellRectangle = new Rectangle(NW, NE, SW, SE);
			
			//if(cellRectangle.NW.x - cellRectangle.NE.x < 10 || cellRectangle.SE.y - cellRectangle.NE.y < 10)
			//	borderPaint.setStrokeWidth(1);
		}

	}

	public void blockTile()
	{
		tileState = TileState.Blocked;
	}

	public Rectangle getCellRectangle()
	{
		return cellRectangle;
	}

	public void drawCell(Canvas canvas, boolean cheatingEnabled)
	{
		if (this.tileState == TileState.Goal)
			cellPaint.setColor(Color.YELLOW);
		else if (this.tileState == TileState.Blocked)
			cellPaint.setColor(Color.DKGRAY);
		else if (this.tileState == TileState.Open || this.tileState == TileState.NA)
			cellPaint.setColor(Color.CYAN);
		else
			cellPaint.setColor(Color.BLACK); // error if there is a black square
		
		if(this.tileState == TileState.NA && cheatingEnabled)
			cellPaint.setColor(Color.RED);
		
		cellRectangle.drawRect(canvas, cellPaint);
		cellRectangle.drawRect(canvas, borderPaint);
	}

}