# How to write good quests 101

# xml (quest)

```xml
<?xml version = "1.0"?>
<class>
   <dialog id = "0">
      <content>Chcesz questa?</content>
      <ans>2</ans>
      <q>0</q>
      <answer>TAK</answer>
      <id>0</id>
      <leadsTo>1</leadsTo>
      <answer>NIE</answer>
      <id>1</id>
      <leadsTo>2</leadsTo>
   </dialog>
   <dialog id = "1">
      <content>Bierz questa</content>
      <ans>1</ans>
      <q>1</q>
      <answer>Bywaj</answer>
      <id>0</id>
      <leadsTo>1</leadsTo>
	  <questID>1</questID>
	  <t>2</t>
	  <taskID>0</taskID>
	  <title>Pokonaj zbojcow</title>
	  <description>Pod miastem kryja sie grozni zbuje, mozesz ich pokonac przy pomocy miecza</description>
      <toDo>5</toDo>
      <doItemID>6</doItemID>
      <state>kill</state>
	  <taskID>1</taskID>
	  <title>Zbierz ziiola</title>
	  <description>Kazdy bohater potrzebuje potionow leczniczych</description>
      <toDo>6</toDo>
      <doItemID>12</doItemID>
      <state>collect</state>
   </dialog>
   <dialog id = "2">
      <content>To nie dostaniesz</content>
      <ans>1</ans>
      <q>0</q>
      <answer>Bywaj</answer>
      <id>0</id>
      <leadsTo>1</leadsTo>
      <q>0</q>
   </dialog>
</class>
```


First you must declare  ```<dialog>``` i=with its id (its local number so start from 0 and go on)

Next you give it a ```<content>```
#
Next how many answers will be ? ```<ans>```
#
If it will be any quest? ```<q>``` -- 0 if no, 1 if yes
#
Answer content ```<answer>```
#
answer id ```<id>``` -- remember to start from 0
#
where answer leads (to which dialog) ```<leadsTo>```
#
Quest global ID ```<questID>```
#
number of tasks in quest ```<t>```
#
task ID ```<taskID>```
#
title of task ```<title>```
#
description of task ```<description>```
#
How many things player have to do ```<toDo>```
#
item/entity global ID, which player had to interact with ```<doItemID>```
#
doState enum for now there is 2 states kill and collect if you have more ideas msg me ```<state>```

# Doc ver 0.7

# Author JG
