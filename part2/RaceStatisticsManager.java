
import javax.swing.*;
import java.io.*;
import java.util.*;

// Class responsible for managing race statistics, including saving race results to a file
public class RaceStatisticsManager
{
    private List<String> history;

    /**
     * Constructor for RaceStatisticsManager.
     * Initializes the history list.
     */
    public RaceStatisticsManager()
    {
        history = new ArrayList<String>();
    }

    /**
     * Records the result of a race and saves it to a file.
     *
     * @param winnerName The name of the winning horse.
     * @param finishTimeMillis The time taken to finish the race in milliseconds.
     * @param horses Array of horses that participated in the race.
     */
    public void recordRace(String winnerName, int finishTimeMillis, Horse[] horses)
    {
        try
        {
            // Create a FileWriter and BufferedWriter to append data to "race_statistics.txt"
            FileWriter fw = new FileWriter("race_statistics.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);

            // Build a detailed race result
            StringBuilder raceResult = new StringBuilder();
            raceResult.append("Winner: ").append(winnerName).append("\n");
            raceResult.append("Finish Time: ").append(finishTimeMillis / 1000.0).append(" seconds\n");

            // For each horse, calculate and record average speed
            for (Horse h : horses)
            {
                double averageSpeed = (double) h.getDistanceTravelled() / (finishTimeMillis / 1000.0);
                raceResult.append(h.getName()).append(" - Avg Speed: ")
                          .append(String.format("%.2f", averageSpeed)).append(" units/sec\n");
            }

            raceResult.append("-------------------------\n");

            String record = raceResult.toString();
            // Write the record to file
            bw.write(record);
            bw.close();
            
            // Also store the record in memory for in-session access
            history.add(record);
        }
        catch (IOException e){
            // Print the stack trace if an error occurs during file writing
            e.printStackTrace();
        }
    }

    /**
     * Displays the saved race statistics in a popup window.
     *
     * @param parentFrame The parent JFrame to attach the popup.
     */
    public void showStatistics(JFrame parentFrame)
    {
        // Check if there are any recorded statistics
        if (history.isEmpty())
        {
            // Show a message if no statistics have been recorded
            JOptionPane.showMessageDialog(parentFrame, "No statistics recorded yet.");
        }
        else
        {
            // Create a text area to display the statistics
            JTextArea textArea = new JTextArea();
            textArea.setText(String.join("\n", history));
            textArea.setEditable(false);
            // Put the text area inside a scroll pane
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));

            // Display the statistics in a dialog attached to the parent frame
            JOptionPane.showMessageDialog(parentFrame, scrollPane, "Race Statistics", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}