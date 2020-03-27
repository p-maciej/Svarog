package svarog.objects;

public interface MouseInteraction {
	public boolean isClickable();			// Returns value for object if object is clickable
	public boolean isMovable();				// Returns value for object if object is movable
	public boolean isOverAllowed();			// Returns value for object shows description after mouse over
	public String mouseOverDescription();	// Returns description after mouse over
}
