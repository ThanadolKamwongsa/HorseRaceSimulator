public class HorseTest
{
    public static void main(String[] args)
    {
        Horse horse = new Horse('♘', "James", 0.6);

        System.out.println("Name: " + horse.getName());
        System.out.println("Symbol: " + horse.getSymbol());
        System.out.println("Confidence: " + horse.getConfidence());
        System.out.println("Distance Travelled: " + horse.getDistanceTravelled());
        System.out.println("Has Fallen: " + horse.hasFallen());

        horse.moveForward();
        System.out.println("Distance after moving: " + horse.getDistanceTravelled());

        horse.fall();
        System.out.println("Has Fallen after fall(): " + horse.hasFallen());

        horse.goBackToStart();
        System.out.println("Distance after reset: " + horse.getDistanceTravelled());
        System.out.println("Has Fallen after reset: " + horse.hasFallen());
    }
}
