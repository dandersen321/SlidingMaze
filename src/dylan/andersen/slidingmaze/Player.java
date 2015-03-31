package dylan.andersen.slidingmaze;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

public class Player
{
	//Position currentPosition;
	//Rectangle currentPosition = new Rectangle();
	Paint myPaint = new Paint();
	Circle playerAvatar = new Circle();
	Integer speed = 10; //pixels per frame

	int rowHeight;
	int colWidth;

	ApplicationData myAData;

	boolean stopped;
	MoveDirection currentDirection;

	static public enum MoveDirection
	{
		up, right, down, left
	}

	public Player(ApplicationData myAData, int rowHeight, int colWidth)
	{
		this.myAData = myAData;
		//myPaint.setColor(Color.GREEN);
		
		myPaint.setColor(Color.rgb(255, 100, 25));
		myPaint.setShader(new LinearGradient(0, 0, 0, rowHeight, Color.RED, Color.GREEN, Shader.TileMode.MIRROR));

		this.rowHeight = rowHeight;
		this.colWidth = colWidth;
		
		this.stopped = true;
	}

	public void setPositionViaRowAndCol(int row, int col)
	{
		//setPosition(new Position(col * colWidth + playerAvatar.radius + (colWidth - playerAvatar.radius)/2,
		//		row * rowHeight + playerAvatar.radius + (rowHeight - playerAvatar.radius)/2));
		setPosition(new Position(col * colWidth + (colWidth)/2,
				row * rowHeight + (rowHeight)/2));

	}

	public void setPosition(Position newPosition)
	{
		//TODO: validate newPosition;

		playerAvatar.setCenter(newPosition);

	}

	public void drawPlayer(Canvas canvas)
	{
		playerAvatar.drawCircle(canvas, myPaint);
	}

	public void startDirection(MoveDirection newDirection)
	{
		currentDirection = newDirection;
		stopped = false;
	}

	public void moveForOneFrame()
	{
		if (currentDirection == MoveDirection.down)
		{
			playerAvatar.addToY(speed);
		}
		else if (currentDirection == MoveDirection.up)
		{
			playerAvatar.addToY(speed * -1);
		}
		else if (currentDirection == MoveDirection.right)
		{
			playerAvatar.addToX(speed);
		}
		else if (currentDirection == MoveDirection.left)
		{
			playerAvatar.addToX(speed * -1);
		}

		if (hitABarrier())
		{
			bounceAfterHittingBarrier();
			stopped = true;
		}
	}

	public void bounceAfterHittingBarrier()
	{
		speed *= -1; //WHY DID I DO THIS? lol :D
		if (currentDirection == MoveDirection.down)
		{
			playerAvatar.addToY(speed);
		}
		else if (currentDirection == MoveDirection.up)
		{
			playerAvatar.addToY(speed * -1);
		}
		else if (currentDirection == MoveDirection.right)
		{
			playerAvatar.addToX(speed);
		}
		else if (currentDirection == MoveDirection.left)
		{
			playerAvatar.addToX(speed * -1);
		}

		if (hitABarrier())
		{
			stopped = true;
		}
		speed *= -1;
	}

	public boolean hitABarrier()
	{
		if (myAData.gameBoard.rectangleTouchingBarrier(playerAvatar.getBoundingRectangle()))
		{
			return true;
		}
		else
			return false;
	}
}
