# How to write working npc 101

# xml (quest)

```xml
<?xml version = "1.0"?>
<?xml version = "1.0"?>
<class>
	<npc>
		<globalID>0</globalID>
		<texturePath>textures/player.png</texturePath>
		<posX>42</posX>
		<posY>26</posY>
		<fullBoundingBox>true</fullBoundingBox>
		<name>Dummy</name>
		<interactionsFile></interactionsFile>
		<howManyItems>1</howManyItems>
		<itemGlobalID>1</itemGlobalID>
		<itemTileID>-1</itemTileID>
	</npc>
	<npc>
		<globalID>1</globalID>
		<texturePath>textures/npc01.png</texturePath>
		<posX>46</posX>
		<posY>25</posY>
		<fullBoundingBox>true</fullBoundingBox>
		<name>Sklepikarz</name>
		<interactionsFile>quest01.quest</interactionsFile>
		<howManyItems>0</howManyItems>
	</npc>
	<npc>
		<globalID>2</globalID>
		<texturePath>textures/rozenna.png</texturePath>
		<posX>48</posX>
		<posY>25</posY>
		<fullBoundingBox>true</fullBoundingBox>
		<name>Rozenna</name>
		<interactionsFile>Rozenna01.quest</interactionsFile>
		<howManyItems>0</howManyItems>
	</npc>
	<npc>
		<globalID>3</globalID>
		<texturePath></texturePath>
		<posX>29</posX>
		<posY>28</posY>
		<fullBoundingBox>true</fullBoundingBox>
		<name>Ninja</name>
		<interactionsFile></interactionsFile>
		<howManyItems>0</howManyItems>
	</npc>
</class>
```
#
#
# **YOU CAN LEAVE SOME TAGS EMPTY ONLY WHERE I WRITE ABOUT IT !!!**
#
#
First you must declare  ```<npc>``` 
#
Next you give it global ID for that type of npc (int) ```<globalID>```
#
Next define picture path of that enemy (you can leave that empty) (string) ```<texturePath>```
#
Add X position (int) ```<posX>```
#
Add Y position (int) ```<posY>```
#
Add is fullBoundingBox (Boolean) for now EVERYTIME use true ```<fullBoundingBox>```
#
Add name for out hero (String) ```<name>```
#
Add is interactions File (String) (you can leave that empty if npc doesnt have anything to say to out hero) ```<interactionsFile>```
#
Add how many items npc will have (it is not showing so for now let it be o EVERYTIME) (int) ```<howManyItems>```
#
for(how many items enemy will have) {
#
	Add X position (int) ```<itemGlobalID>```
#
	Add Y position (int) ```<itemTileID>```
#
}
#

# CONGRATS YOU MADE YOURS FIRST WORKING NPC, REMEMBER TO ADD XML FORMAT AT TOP AND DO EVERY THING IN class TAG ;)


# Doc ver 0.1

# Author JG
