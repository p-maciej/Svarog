package svarog.objects;

public interface MouseInteraction {
	public boolean isClickable();			// Returns value for object if object is clickable
	public boolean isMovable();				// Returns value for object if object is movable
	public boolean isOverable();			// Returns value for object shows description after mouse over
	public void setClickable(boolean isClickable);
	public void setMovable(boolean isMovable);
	public void setOverable(boolean isOverable);
}
