# Postal-System
Object oriented design in Java (View readme as raw)

4.1) Post office file
  The name of this file is offices.txt. This file contains the information about the post offices
  in the system. You should read this file and use the contents to set up your program for the
  simulation.
  The first data point in the file will be the integer count of post offices in the system.
  Following the post office count integer and for each post office the following data will be
  provided, in order:
  • A string naming the post office.
  • An integer transit time for items leaving this post office.
  • An integer postage required for sending packages out of this post office.
  • An integer capacity of this post office.
  • An integer persuasion amount of this post office.
  • An integer maximum package length for this post office.
  Example: Berlin 4 10 1000 10000 300 – A post office called Berlin which sends out mail
  that arrives in four days of transit, where packages cost 1000 units of currency, able to store
  1000 items, nearly impossible to convince into shipping oversize mail and rejecting packages
  over 300 units of length.
  
4.2) Wanted criminals file
  The name of this file is wanted.txt. This file contains the names of criminals which may be
  using the postal system for their nefarious plans to take over the world. You should read this
  file at program startup and thwart their evil plans by rejecting their packages or arresting them
  whenever they happen to be foolish enough to attempt a package pickup in person.
  The first data point in the file will be the integer count of criminals at large.
  Following the criminal count integer and for each criminal the following data item will be
  provided:
  • A string naming the criminal.

4.3) Command file
  The name of this file is commands.txt. This file contains instructions for your program to
  execute.
  This file will begin with the integer count of commands within. Following will be the
  commands.
  A command can be any of the following.
  • DAY – Which signifies the end of the day.
  • PICKUP – A pickup attempt. This command will be followed by the following:
  – A string naming the post office of pickup.
  – A string naming the person attempting a pickup.
  Example: PICKUP New_York Timothy – A person named Timothy is picking up mail
  in New_York.
  Example: PICKUP Vancouver Karol – A person named Karol is picking up mail in Vancouver.
  7
  • LETTER – A new letter being brought to an initiating office. This command will be
  followed by the following:
  – A string naming the post office to which the letter was brought.
  – A string naming the person who is the letter’s recipient.
  – A string naming the destination post office.
  – A string naming the person who can pick up the letter if it gets returned to sender.
  NONE will mean that there was no return address.
  Example: LETTER Boston Khan Ulan_Baator NONE – An unknown sender in Boston
  is trying to send a letter to a person named Khan, to be picked up in the post office
  called Ulan_Baator.
  Example: LETTER Moscow Sam Washington Bill – A letter to be mailed in Moscow,
  with the recipient being Sam in Washington. If the letter gets returned to sender
  then it will be available for pickup by Bill at the post office in Moscow.
  • PACKAGE – A new package being brought to an initiating office. This command will be
  followed by the following:
  – A string naming the post office to which the package was brought.
  – A string naming the person who is the package’s recipient.
  – A string naming the destination post office.
  – An integer specifying the money accompanying this package, to be spent both on
  postage and on persuasion.
  – An integer specifying the length of the package.
  Example: PACKAGE Paris Pierre Toulouse 200 99999 – A package to be sent in Paris,
  with the destination being Toulouse and the recipient being Pierre. The package is 99999
  units long and has 200 units of currency accompanying it.
