package dylan.andersen.slidingmaze;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Circle
{
	int radius;
	Position center;
	
	public void drawCircle(Canvas canvas, Paint myPaint)
	{
		canvas.drawCircle(center.x, center.y, radius, myPaint);
	}

	public float getRadius()
	{
		return radius;
	}

	public void setRadius(int radius)
	{
		this.radius = radius;
	}

	public Position getCenter()
	{
		return center;
	}

	public void setCenter(Position center)
	{
		this.center = center;
	}
	
	public void addToY(int pixelsMoved)
	{
		center.y += pixelsMoved;
	}
	
	public void addToX(int pixelsMoved)
	{
		center.x += pixelsMoved;
	}
	
	public Rectangle getBoundingRectangle()
	{
		Position NE = new Position(center.x - radius, center.y - radius);
		Position NW = new Position(center.x + radius, center.y - radius);
		Position SE = new Position(center.x - radius, center.y + radius);
		Position SW = new Position(center.x + radius, center.y + radius);
		Rectangle boundingRec = new Rectangle();
		boundingRec.NE = NE;
		boundingRec.NW = NW;
		boundingRec.SE = SE;
		boundingRec.SW = SW;
		
		return boundingRec;		
	}
	

}
