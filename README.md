
# KLOCE - programming game written in Java

Simple programming game where you give instructions to robot that moves on 2d board.

Robot program is created by drag-n-dropping blocks (hence the name) with possible operations.
The programming langauge for robot is inspired by brainfuck (in other words a simple Turing machine).

Written by me while I was coming up with ideas for job fair attraction. 
(in the end we ended up with different idea, but I finished the game just for fun).

Graphics library is javax.swing.

Project contains game and level editor tool.


## Project contents

  * example-levels/ - folder contains example levels (and cheat sheet with example solutions), 'CD' to this folder from game window to open these levels;
  * experiments/ - python script of game langage simulator used by me when developing game;
  * gfx/ - SVG project of vector graphics (created using Inkscape);
  * kloce/ - Eclipse project with sources and all that jazz; 


## Building/Running

Eclipse project is part of the repository. Easiest way of building/running game is to open it via eclipse.
But the dependencies are simple enough, that I recon, you can easily configure build with whatever you want.

Application was written in Java and Swing graphics library (native Java GUI - should work with most JREs without need of installing additional libraries).

There are two applications in the project:

  * pl.maszynatrurla.Game - launches game window;
  * pl.maszynatrurla.Editor - launches level editor.


## Gameplay

KLOCE uses the most cliche of programming game mechanics - but it has the twist of having wierd and unfriendly programming language - at least unless
player has some CS background.


![Gameplay window](/game.png)

Game loads levels from directory containing "*.kloc" files. Upon starting the game you need to select this directory using "CD" (like change directory) button.
Project contains example levels in "example-levels" directory. You will find suggested solutions for them in example-levels/solutions.txt (if you hate fun).

To complete level, robot has to visit all the fields with battery (upon touching last battery, level is considered won).

Create code by dragging command blocks onto program line. Then run robot by clicking "Play". You can also "debug" your code by using "Step", "Pause" and
"Reset" buttons how many times you want. 


### Language

Movement commands:

 * go - moves robot one tile forward;
 * lt - turns robot left 90 degrees;
 * rt - turns robot right 90 degrees

Memory operations:

 * +1, +2, +4, +8 - add (1, 2, 4 or 8) to value in current memory position;
 * -1 - subtract 1 from current memory position;
 * M> - move memory position forward;
 * <M - move memory position backward;

Jumps:

 * \[ - IF value in current memory position is zero, THEN jump to operation after matching \]
 * \] - IF value in current memory position is NOT zero, THEN jump to matching \[
 
Input/output:

 * in - moves user value (integer number) into current memory position
 * out - moves value from current memory position onto output register
 
Output does not currently have any purpose in interacting with robot. I just added it for "symmetry" to input command and possible
future use.


## Creating levels

Project contains level editor. Levels are simple text files, so you may also edit them by hand (if you are hackerman).

![Gameplay window](/editor.png)

