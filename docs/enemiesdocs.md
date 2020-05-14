# How to write working enemies 101

# xml (quest)

```xml
<?xml version = "1.0"?>
<class>
	<enemy>
		<globalID>0</globalID>
		<texture>textures/avatar.png</texture>
		<posX>46</posX>
		<posY>29</posY>
		<fullBoundingBox>true</fullBoundingBox>
		<minAttack>30</minAttack>
		<maxAttack>50</maxAttack>
		<xpForKilling>15</xpForKilling>
		<hp>150</hp>
		<reward>100</reward>
		<name>Malina</name>
		<howManyItems>0</howManyItems>
		<itemGlobalID>1</itemGlobalID>
		<itemTileID>-1</itemTileID>
	</enemy>
	<enemy>
		<globalID>1</globalID>
		<texture>textures/avatar2.png</texture>
		<posX>46</posX>
		<posY>29</posY>
		<fullBoundingBox>true</fullBoundingBox>
		<minAttack>60</minAttack>
		<maxAttack>120</maxAttack>
		<xpForKilling>150</xpForKilling>
		<hp>300</hp>
		<reward>1400</reward>
		<name>Kikimora</name>
		<howManyItems>0</howManyItems>
	</enemy>
</class>
```

#
First you must declare  ```<enemy>``` 
#
Next you give it global ID for that type of enemy (int) ```<globalID>```
#
Next define picture of that enemy (string) ```<texture>```
#
Add X position (int) ```<posX>```
#
Add Y position (int) ```<posY>```
#
Add is fullBoundingBox (Boolean) for now EVERYTIME use true ```<fullBoundingBox>```
#
Add minimum attack that enemy will give (int) ```<minAttack>```
#
Add maximum attack that enemy will give (int) ```<maxAttack>```
#
Add xp value whoose player will get after killing enemy (int) ```<xpForKilling>```
#
Add enemy hp (int) ```<hp>```
#
Add value in money which player will get after kill enemy (int) ```<reward>```
#
Add how many items enemy will have (it is not showing so for now let it be value of 0 EVERYTIME) (int) ```<howManyItems>```
#
for(how many items enemy will have) {
#
	Add X position (int) ```<itemGlobalID>```
#
	Add Y position (int) ```<itemTileID>```
#
}
#

CONGRATS YOU MADE YOURS FIRST WORKING ENEMY, REMEMBER TO ADD XML FORMAT AT TOP AND DO EVERY THING IN class TAG :)


# Doc ver 0.7

# Author JG