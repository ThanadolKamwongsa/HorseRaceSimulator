Horse Racing Simulation (Console + GUI Edition)
Description
This is a Java project that simulates horse racing, available in both console-based and graphical user interface (GUI) versions.
Features include: Dynamic horse movement based on confidence levels.
Randomized falling mechanism.
GUI betting system with customized horses.
Race statistics recording.
Persistent balance management (across sessions).

Part 1 (Console Mode)
├── Horse.java           # Horse class (basic model)
├── Race.java            # Race manager (console output)
├── HorseTest.java       # Unit test for Horse
├── RaceTest.java        # Unit test and race starter

Part 2 (GUI Mode)
├── Horse.java           # Horse class (extended with breed, coat color, etc.)
├── RaceGUI.java         # Main GUI application
├── RaceStatisticsManager.java # Manages and saves race statistics
├── balance.txt          # Saves user betting balance
├── race_statistics.txt  # Race history and horse speed stats

How to Run (Console Version)
1.Compile: javac Horse.java Race.java RaceTest.java HorseTest.java
2.Run: java RaceTest
3.Run test: java HorseTest

How to Run (GUI Version)
1.Compile: javac Horse.java RaceGUI.java RaceStatisticsManager.java
2.Run: java RaceGUI

Features:
    Console Mode:
        Simple console visualization.
        Three horses compete.
        Confidence rating dynamically changes based on race ranking.

    GUI Mode
        Interactive betting system: Choose a horse and place your bets.
        Horse customization: Pick breed, coat color, saddle, and horseshoe types.
        Dynamic track and weather effects: Conditions affect horse performance.
        Balance management: Persistent balance (balance.txt).
        Detailed statistics: Race history saved in race_statistics.txt including finish time and average speed.
        
Author: Thanadol kamwongsa