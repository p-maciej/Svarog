package svarog.objects;

public interface Movable {
	float getPositionX();				// Position X of object center
	float getPositionY();				// Position T of object center
	float setPositionX();				// Position X setter
	float setPositionY();				// Position Y setter
	int belongsTo();					// Returns belonging to another object eg. tile
	void setBelonging(int objectId);	// Sets belonging to another object
}
