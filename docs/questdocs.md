# How to write good quests 101

# xml (quest)

```xml
<?xml version = "1.0"?>
<class>
   <dialog id = "0">
      <content>Chcesz questa?</content>
      <ans>2</ans>
      <answer>TAK</answer>
      <id>0</id>
      <leadsTo>1</leadsTo>
      <answer>NIE</answer>
      <id>1</id>
      <leadsTo>2</leadsTo>
      <q>0</q>
      <t>0</t>
   </dialog>
   <dialog id = "1">
      <content>Bierz questa</content>
      <ans>1</ans>
      <answer>Bywaj</answer>
      <id>0</id>
      <leadsTo>1</leadsTo>
      <q>1</q>
	  <questID>1</questID>
	  <title>Pokonaj zbojcow</title>
	  <description>Pod miastem kryja sie grozni zbuje, mozesz ich pokonac przy pomocy miecza</description>
	  <t>2</t>
	  <taskID>0</taskID>
      <toDo>5</toDo>
      <doItemID>6</doItemID>
      <state>kill</state>
	  <taskID>1</taskID>
	  <toDo>6</toDo>
      <doItemID>12</doItemID>
      <state>collect</state>
   </dialog>
   <dialog id = "2">
      <content>To nie dostaniesz</content>
      <ans>1</ans>
      <answer>Bywaj</answer>
      <id>0</id>
      <leadsTo>1</leadsTo>
      <q>0</q>
      <t>0</t>
   </dialog>
</class>
```

#
First you must declare  ```<dialog>``` 
i=with its ```id``` (its local number so start from 0 and go on for every entity)

Next you give it a, main thing the Entity will say to you ```<content>```
#
Next define how many answers will be there? ```<ans>```
#
Add Answer contents ```<answer>```
#
answer id ```<id>``` -- remember to start from 0
#
where answer leads (to which dialog) ```<leadsTo>```
#
If it will be any quest? ```<q>``` -- 0 if no, 1 if yes, remember that every dialog should have ONLY ONE QUEST WITH SOME TASK LIKE KILL BADGUYS AND COLLECT FLOWERS
#
number of tasks in quest ```<t>```
#
Quest global ID ```<questID>```
#
Quest title ```<title>```
#
Quest description ```<description>```
#
task ID ```<taskID>```
#
How many things player have to do (ex. how many enemy should he kill?) ```<toDo>```
#
item/entity global ID, which player had to interact with (which type of enemy he should kill/ which flower he should collect) ```<doItemID>```
#
doState enum for now there is 2 states 'kill' and 'collect' if you have more ideas msg me ```<state>```
#
CONGRATS YOU MADE YOURS FIRST WORKING DIALOG, REMEMBER TO ADD XML FORMAT AT TOP AND DO EVERY THING IN class TAG :)


# Doc ver 0.7

# Author JG
