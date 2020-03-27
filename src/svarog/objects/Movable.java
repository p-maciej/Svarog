package svarog.objects;

public interface Movable {
	float getPositionX();
	float getPositionY();
	float setPositionX();
	float setPositionY();
	int belongsTo();
	void setBelonging(int objectId);
}
