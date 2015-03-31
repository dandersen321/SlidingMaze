package dylan.andersen.slidingmaze;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Rectangle
{
	Position NW = new Position();
	Position NE = new Position();
	Position SW = new Position();
	Position SE = new Position();

	Rectangle()
	{

	}

	Rectangle(Position _NW, Position _NE, Position _SW, Position _SE)
	{
		NW = _NW;
		NE = _NE;
		SW = _SW;
		SE = _SE;

		testRectangleParameters();
	}

	public void drawRect(Canvas canvas, Paint myPaint)
	{
		canvas.drawRect(NW.x, NW.y, SE.x, SE.y, myPaint);
	}

	public void testRectangleParameters()
	{
		boolean error = false;

		if (NW.x > NE.x)
			error = true;
		if (NW.y != NE.y)
			error = true;
		if (SW.x > SE.x)
			error = true;
		if (SW.y != SW.y)
			error = true;

		if (SW.y < NW.y)
			error = true;
		if (SW.x != NW.x)
			error = true;

		if (SE.y < NE.y)
			error = true;
		if (SE.x != NE.x)
			error = true;

		if (error)
		{
			Log.e("Final", "bad rectangle parameters");
		}
	}

	public void setRectangle(Position _NW, Position _NE, Position _SW, Position _SE)
	{
		NW = _NW;
		NE = _NE;
		SW = _SW;
		SE = _SE;
		testRectangleParameters();
	}

	public void addToY(Integer speed)
	{
		NW.y += speed;
		NE.y += speed;
		SW.y += speed;
		SE.y += speed;

	}

	public void addToX(Integer speed)
	{
		NW.x += speed;
		NE.x += speed;
		SW.x += speed;
		SE.x += speed;
	}
}
