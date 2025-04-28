public class RaceTest
{
    public static void main(String[] args)
    {
        // Create a race of length 20 units
        Race race = new Race(20);

        // Create horses
        Horse horse1 = new Horse('♘', "Nathan", 0.6);
        Horse horse2 = new Horse('♞', "Alex", 0.6);
        Horse horse3 = new Horse('♛', "James", 0.4);

        // Add horses to lanes
        race.addHorse(horse1, 1);
        race.addHorse(horse2, 2);
        race.addHorse(horse3, 3);

        // Start the race
        race.startRace();
    }
}
