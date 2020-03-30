package svarog.objects;

public interface Movable {
	float getPositionX();				// Position X of object center
	float getPositionY();				// Position T of object center
	void setPositionX(float X);				// Position X setter
	void setPositionY(float Y);				// Position Y setter
	
	//int belongsTo();					// Returns belonging to another object eg. tile
	//void setBelonging(int objectId);	// Sets belonging to another object
}
