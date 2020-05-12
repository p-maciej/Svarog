# How to write working items 101

# xml (quest)

```xml
<?xml version = "1.0"?>
<class>
	<item>
		<TextureFileName>textures/item.png</TextureFileName>
   		<globalID>0</globalID>
   		<defense>0</defense>
   		<hpRegeneration>25</hpRegeneration>
   		<attackBonus>0</attackBonus>
   		<lvlRequired>0</lvlRequired>
   		<name>Potion</name>
   		<description>It will icrease your hp</description>
   		<itemType>consumable</itemType>
	</item>
	<item>
		<TextureFileName>textures/helmet.png</TextureFileName>
   		<globalID>1</globalID>
   		<defense>10</defense>
   		<hpRegeneration>0</hpRegeneration>
   		<attackBonus>5</attackBonus>
   		<lvlRequired>0</lvlRequired>
   		<name>helmet</name>
   		<description>It will defense yours head</description>
   		<itemType>helm</itemType>
	</item>
</class>
```
#
REMEMBER: EVERYTAG IS REQUIRED 
#
First you must declare  ```<item>``` 
#
Next step is to set (string) ```<TextureFileName>``` for texture for item like -> textures/item.png
#
Next add globalID ```WARNING```
it SHOULD BE UNIQUE (int) ```<globalID>```
#
Next step should be adding (int) ```<defense>``` tag 
#
Next step should be adding (int) ```<hpRegeneration>``` tag 
#
Next step should be adding (int) ```<attackBonus>``` tag 
#
Next step should be adding (int) ```<lvlRequired>``` tag 
#
Next step should be adding (string) ```<name>``` tag
#
Next step should be adding (string) ```<description>``` tag 
#
Next step should be adding (int) ```<itemType>``` tag 
#
Last step is to remember to close ```</item>``` tag 
#
CONGRATS YOURS FIRST ITEM IS COMPLETED.

# Doc ver 0.2

# Author JG
