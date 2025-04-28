/**
 * The Horse class represents a horse participating in the race.
 * Each horse has a name, a symbol, a distance travelled, a fallen status, and a confidence rating.
 * 
 * @author Thanadol kamwongsa
 * @version 2
 */
public class Horse
{
    // Fields of class Horse
    private String name;
    private char symbol;
    private int distanceTravelled;
    private boolean hasFallen;
    private double confidence;
    private String breed;
    private String coatColor;
    private String saddleType;
    private String horseshoeType;
    
    
    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
    public String getCoatColor() { return coatColor; }
    public void setCoatColor(String coatColor) { this.coatColor = coatColor; }
    public String getSaddleType() { return saddleType; }
    public void setSaddleType(String saddleType) { this.saddleType = saddleType; }
    public String getHorseshoeType() { return horseshoeType; }
    public void setHorseshoeType(String horseshoeType) { this.horseshoeType = horseshoeType;}

    // Constructor of class Horse
    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
        this.symbol = horseSymbol;
        this.name = horseName;
        setConfidence(horseConfidence);
        this.distanceTravelled = 0;
        this.hasFallen = false;
    }

    // Mark the horse as fallen.
    public void fall()
    {
        hasFallen = true;
        confidence = Math.max(0.0, confidence - 0.1);
    }

    // Return the horse's confidence rating.
    public double getConfidence()
    {
        return confidence;
    }

    // Return the distance the horse has travelled.
    public int getDistanceTravelled()
    {
        return distanceTravelled;
    }

    // Return the name of the horse.
    public String getName()
    {
        return name;
    }

    // Return the symbol representing the horse.
    public char getSymbol()
    {
        return symbol;
    }


    // Reset the horse's distance and fallen status.
    public void goBackToStart()
    {
        distanceTravelled = 0;
        hasFallen = false;
    }

    
    // Check if the horse has fallen.
    public boolean hasFallen()
    {
        return hasFallen;
    }

    // Move the horse forward by 1 unit.
    public void moveForward()
    {
        if (!hasFallen) {
            distanceTravelled += 1;
        }
    }
    
    // Set the horse's confidence rating, ensuring it remains between 0 and 1.
    public void setConfidence(double newConfidence)
    {
        if (newConfidence < 0) {
            confidence = 0;
        } else if (newConfidence > 1) {
            confidence = 1;
        } else {
            confidence = newConfidence;
        }
    }

    // Set the horse's symbol.
    public void setSymbol(char newSymbol)
    {
        this.symbol = newSymbol;
    }

    
}
