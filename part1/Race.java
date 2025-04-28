import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.util.Arrays;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance.
 * 
 * Improved by Thanadol kamwongsa
 * @version 2.0
 */
public class Race
{
    private int raceLength;
    private Horse lane1Horse;
    private Horse lane2Horse;
    private Horse lane3Horse;

    /**
     * Constructor for objects of class Race.
     * Initially, there are no horses in the lanes.
     * 
     * @param distance the length of the racetrack (in meters/yards)
     */
    public Race(int distance)
    {
        raceLength = distance;
        lane1Horse = null;
        lane2Horse = null;
        lane3Horse = null;
    }
    
    /**
     * Adds a horse to a specific lane in the race.
     * 
     * @param theHorse the horse to be added
     * @param laneNumber the lane number (1-3)
     */
    public void addHorse(Horse theHorse, int laneNumber)
    {
        if (laneNumber == 1)
        {
            lane1Horse = theHorse;
        }
        else if (laneNumber == 2)
        {
            lane2Horse = theHorse;
        }
        else if (laneNumber == 3)
        {
            lane3Horse = theHorse;
        }
        else
        {
            System.out.println("Cannot add horse to lane " + laneNumber + " because there is no such lane.");
        }
    }
    
    /**
     * Starts the race.
     * Horses are reset to the start, and move forward until one wins.
     * After the race ends, the winner is displayed.
     */
    public void startRace()
    {
        boolean finished = false;

        lane1Horse.goBackToStart();
        lane2Horse.goBackToStart();
        lane3Horse.goBackToStart();

        while (!finished)
        {
            moveHorse(lane1Horse);
            moveHorse(lane2Horse);
            moveHorse(lane3Horse);

            updateConfidenceByRank();
            printRace();

            if (raceWonBy(lane1Horse) || raceWonBy(lane2Horse) || raceWonBy(lane3Horse))
            {
                finished = true;
            }
            else if (allHorsesFallen()|| allHorsesUnableToMove())
            {
                finished = true;
                System.out.println("No horses can move anymore. Ending the race.");
            }

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (Exception e) {
            }
        }

        // After race is finished
        Horse winner = findWinner();
        if (winner != null) {
            System.out.println("And the winner is... " + winner.getName() + "!");
        } else {
            System.out.println("All horses have fallen. No winner.");
        }
    }

    /**
     * Moves a horse forward based on its confidence, or makes it fall.
     * A fallen horse cannot move further.
     * 
     * @param theHorse the horse to move
     */
    private void moveHorse(Horse theHorse)
    {
        if (theHorse != null && !theHorse.hasFallen())
        {
            if (Math.random() < theHorse.getConfidence())
            {
                theHorse.moveForward();
            }
            
            if (Math.random() < (0.1 * theHorse.getConfidence() * theHorse.getConfidence()))
            {
                theHorse.fall();
            }
        }

        //  Return true if horse can still move
        // return (theHorse != null) && (!theHorse.hasFallen());
    }

        
    /**
     * Determines if a horse has won the race.
     * 
     * @param theHorse the horse to check
     * @return true if the horse reached or passed the race length
     */
    private boolean raceWonBy(Horse theHorse)
    {
        return (theHorse != null) && (theHorse.getDistanceTravelled() >= raceLength);
    }
    
    /**
     * Finds the winning horse after the race.
     * 
     * @return the winning horse, or null if no winner
     */
    private Horse findWinner()
    {
        if (lane1Horse != null && lane1Horse.getDistanceTravelled() >= raceLength && !lane1Horse.hasFallen()) {
            return lane1Horse;
        }
        if (lane2Horse != null && lane2Horse.getDistanceTravelled() >= raceLength && !lane2Horse.hasFallen()) {
            return lane2Horse;
        }
        if (lane3Horse != null && lane3Horse.getDistanceTravelled() >= raceLength && !lane3Horse.hasFallen()) {
            return lane3Horse;
        }
        return null;
    }

    /**
     * Prints the current state of the race track to the terminal.
     */
    private void printRace()
    {
        System.out.print('\u000C');  // Clear the terminal
        
        multiplePrint('=', raceLength + 3); // Top border
        System.out.println();
        
        printLane(lane1Horse);
        System.out.println();
        
        printLane(lane2Horse);
        System.out.println();
        
        printLane(lane3Horse);
        System.out.println();
        
        multiplePrint('=', raceLength + 3); // Bottom border
        System.out.println();    
    }
    
    /**
     * Prints a single horse's lane during the race.
     * Displays the horse's symbol or a fallen marker if the horse has fallen.
     * 
     * @param theHorse the horse to print
     */
    private void printLane(Horse theHorse)
    {
        if (theHorse == null) {
            return; // If no horse in this lane, skip
        }
        
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();
        
        System.out.print('|');
        
        multiplePrint(' ', spacesBefore);
        
        if (theHorse.hasFallen())
        {
            System.out.print('x'); // x fallen horse symbol
        }
        else
        {
            System.out.print(theHorse.getSymbol());
        }
        
        multiplePrint(' ', spacesAfter);
        
        System.out.print('|');
        System.out.printf("%s (Current confidence %.1f)", theHorse.getName(), theHorse.getConfidence());
    }
        
    /**
     * Prints a given character multiple times.
     * 
     * @param aChar the character to print
     * @param times how many times to print the character
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i++;
        }
    }

    // Checks if all horses have fallen.
    private boolean allHorsesFallen()
    {
        boolean lane1Fallen = (lane1Horse == null) || lane1Horse.hasFallen();
        boolean lane2Fallen = (lane2Horse == null) || lane2Horse.hasFallen();
        boolean lane3Fallen = (lane3Horse == null) || lane3Horse.hasFallen();
        
        return lane1Fallen && lane2Fallen && lane3Fallen;
    }

    /**
     * Updates the confidence levels of horses based on their ranking.
     * 
     * Increases the confidence of the leading horse by 0.1 (maximum 1.0),
     * and decreases the confidence of the last horse by 0.1 (minimum 0.5).
     */
    private void updateConfidenceByRank(){
        Horse[] horses = {lane1Horse, lane2Horse, lane3Horse};
        
        // Sort horses by distance travelled in descending order
        Arrays.sort(horses, (h1, h2) -> h2.getDistanceTravelled() - h1.getDistanceTravelled());
        
        // Increase confidence of the horse in 1st place
        if (!horses[0].hasFallen()) {
            horses[0].setConfidence(Math.min(1.0, horses[0].getConfidence() + 0.1));
        }
        
        // Decrease confidence of the horse in last place, but not below 0.1
        if (!horses[2].hasFallen()) {
            double newConfidence = horses[2].getConfidence() - 0.1;
            if (newConfidence < 0.5) {
                newConfidence = 0.5; 
            }
            horses[2].setConfidence(newConfidence);
        }
    }
    // Checks if all horses are unable to move.
    private boolean allHorsesUnableToMove()
    {
        boolean lane1Stopped = (lane1Horse == null) || lane1Horse.hasFallen() || lane1Horse.getConfidence() <= 0;
        boolean lane2Stopped = (lane2Horse == null) || lane2Horse.hasFallen() || lane2Horse.getConfidence() <= 0;
        boolean lane3Stopped = (lane3Horse == null) || lane3Horse.hasFallen() || lane3Horse.getConfidence() <= 0;
        
        return lane1Stopped && lane2Stopped && lane3Stopped;
    }

    

}
