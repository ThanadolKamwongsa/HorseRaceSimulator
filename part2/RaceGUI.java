import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;

public class RaceGUI extends JFrame {
    // Components
    private JComboBox<String> lanesComboBox, shapeComboBox, weatherComboBox, horseComboBox;
    private JTextField distanceField, betField;
    private JButton startButton, viewStatsButton;
    private JPanel racePanel;

    // Race Logic
    private int raceLength;
    private int laneCount;
    private String trackShape;
    private String weather;
    private Horse[] horses;
    private Horse betHorse;
    private double betAmount;
    private double userBalance = 1000.0;
    private Timer raceTimer;
    private long startTime;
    private char[] horseIcons;
    private JLabel balanceLabel;
    
    private RaceStatisticsManager statsManager = new RaceStatisticsManager();

    // Constructor Initialize and set up the GUI
    public RaceGUI() {
        loadBalanceFromFile();
        setTitle("Horse Race");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLayout(null);

        setupUI();
    }

    // Set up all UI components
    private void setupUI() {

        // Label and ComboBox for selecting number of lanes
        JLabel lanesLabel = new JLabel("Select Number of Lanes:");
        lanesLabel.setBounds(30, 20, 200, 25);
        String[] lanesOptions = {"2", "3", "4", "5", "6"};
        lanesComboBox = new JComboBox<>(lanesOptions);
        lanesComboBox.setBounds(220, 20, 100, 25);

        // Label and TextField for entering race distance
        JLabel distanceLabel = new JLabel("Enter Race Distance (units):");
        distanceLabel.setBounds(30, 60, 200, 25);
        distanceField = new JTextField("20");
        distanceField.setBounds(220, 60, 100, 25);

        // Label and ComboBox for selecting track shape
        JLabel shapeLabel = new JLabel("Select Track Shape:");
        shapeLabel.setBounds(30, 100, 200, 25);
        String[] shapeOptions = {"Straight", "Oval", "Figure Eight"};
        shapeComboBox = new JComboBox<>(shapeOptions);
        shapeComboBox.setBounds(220, 100, 100, 25);

        // Label and ComboBox for selecting weather condition
        JLabel weatherLabel = new JLabel("Select Weather Condition:");
        weatherLabel.setBounds(30, 140, 200, 25);
        String[] weatherOptions = {"Dry", "Muddy", "Icy"};
        weatherComboBox = new JComboBox<>(weatherOptions);
        weatherComboBox.setBounds(220, 140, 100, 25);

        // Label and ComboBox for selecting horse to bet on
        JLabel horseLabel = new JLabel("Choose Horse to Bet On:");
        horseLabel.setBounds(30, 180, 200, 25);
        String[] horseOptions = {"Horse 1", "Horse 2", "Horse 3", "Horse 4", "Horse 5", "Horse 6"};
        horseComboBox = new JComboBox<>(horseOptions);
        horseComboBox.setBounds(220, 180, 100, 25);

        // Label and TextField for entering bet amount
        JLabel betLabel = new JLabel("Enter Bet Amount (£):");
        betLabel.setBounds(30, 220, 200, 25);
        betField = new JTextField("100");
        betField.setBounds(220, 220, 100, 25);

        // Label to display current balance
        balanceLabel = new JLabel("Current Balance: £" + userBalance);
        balanceLabel.setBounds(30, 270, 300, 25);
        
        // Buttons for starting the race and viewing statistics
        startButton = new JButton("Start Race");
        viewStatsButton = new JButton("View Statistics");

        // Panel to hold the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBounds(80, 320, 300, 50);
        buttonPanel.setOpaque(false);

        // Panel to display the race track
        racePanel = new RaceTrackPanel();
        racePanel.setBounds(400, 20, 450, 550);
        racePanel.setBackground(Color.WHITE);

        // Add all components to the main frame
        add(lanesLabel);
        add(lanesComboBox);
        add(distanceLabel);
        add(distanceField);
        add(shapeLabel);
        add(shapeComboBox);
        add(weatherLabel);
        add(weatherComboBox);
        add(horseLabel);
        add(horseComboBox);
        add(betLabel);
        add(betField);
        add(racePanel);
        add(balanceLabel);
        buttonPanel.add(startButton);
        buttonPanel.add(viewStatsButton);
        add(buttonPanel);

        // Add action listeners to buttons
        startButton.addActionListener(e -> prepareRace());
        viewStatsButton.addActionListener(e -> {
            showStatisticsFromFile();
        });
        
    }
    
    // Display race statistics from the "race_statistics.txt" file
    private void showStatisticsFromFile() {
        try {
            // Create a File object pointing to the statistics file
            java.io.File file = new java.io.File("race_statistics.txt");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, "No statistics available yet.");
                return;
            }
            // Read the file contents using a Scanner
            java.util.Scanner scanner = new java.util.Scanner(file);
            StringBuilder stats = new StringBuilder();
            while (scanner.hasNextLine()) {
                stats.append(scanner.nextLine()).append("\n");
            }
            scanner.close();

            // Display the statistics in a non-editable text area inside a scroll pane
            JTextArea textArea = new JTextArea(stats.toString());
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
    
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            
            // Show the statistics in a dialog box
            JOptionPane.showMessageDialog(this, scrollPane, "Race Statistics", JOptionPane.INFORMATION_MESSAGE);
    
        } catch (Exception e) {
            // Handle any exceptions by printing the stack trace and showing an error message
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading statistics.");
        }
    }

    
    // Prepare the race settings based on user input
    private void prepareRace() {
        try {
            // Parse user inputs from UI components
            laneCount = Integer.parseInt((String) lanesComboBox.getSelectedItem());
            raceLength = Integer.parseInt(distanceField.getText());
            trackShape = (String) shapeComboBox.getSelectedItem();
            weather = (String) weatherComboBox.getSelectedItem();
            betAmount = Double.parseDouble(betField.getText());

            // Get the selected horse index for betting
            int betHorseIndex = horseComboBox.getSelectedIndex();

            // Check if the bet amount exceeds the user's balance
            if (betAmount > userBalance) {
                JOptionPane.showMessageDialog(this, "Insufficient balance!");
                return;
            }
            // Show the effects of track shape and weather conditions
            // Allow the user to customize horses
            // Display betting odds based on customized horses
            // Set the horse the user bet on
            // Start the race
            showTrackAndWeatherEffects();
            customizeHorses(); 
            showOdds();
            betHorse = horses[betHorseIndex];
            startRace();
        } catch (NumberFormatException ex) {
            // Handle invalid inputs
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter numbers only.");
        }
    }
    // Display how track shape and weather conditions affect horse movement
    private void showTrackAndWeatherEffects() {
        double modifier = 1.0;
        StringBuilder message = new StringBuilder("Track and Weather Effects:\n");
        
        // Apply track shape effects
        if (trackShape.equals("Oval") || trackShape.equals("Figure Eight")) {
            modifier *= 0.9;
            message.append("- Track Shape (" + trackShape + ") reduces movement by 10%.\n");
        } else {
            message.append("- Track Shape (" + trackShape + ") has no effect.\n");
        }
        
        // Apply weather condition effects
        if (weather.equals("Muddy")) {
            modifier *= 0.8;
            message.append("- Weather (" + weather + ") reduces movement by 20%.\n");
        } else if (weather.equals("Icy")) {
            modifier *= 0.7;
            message.append("- Weather (" + weather + ") reduces movement by 30%.\n");
        } else {
            message.append("- Weather (" + weather + ") has no effect.\n");
        }
        // Append total movement modifier to the message
        message.append(String.format("\nTotal Movement Modifier: %.2f", modifier));
        
        // Display the full effects in a dialog box
        JOptionPane.showMessageDialog(this, message.toString(), "Track and Weather Effects", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Start the horse race and handle race progression
    private void startRace() {
        // Reset all horses to starting position
        for (Horse h : horses) {
            h.goBackToStart();
        }
        // Record the race start time
        startTime = System.currentTimeMillis();

        // Create a timer to update the race every 200 milliseconds
        raceTimer = new Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean raceFinished = false;
                boolean allFallen = true;

                // Update each horse's position
                // Only update horses that have not fallen
                for (Horse h : horses) {
                    if (!h.hasFallen()) {
                        allFallen = false;
                        // Move horse forward based on modified confidence
                        if (Math.random() < modifiedConfidence(h)) {
                            h.moveForward();
                        }
                        // Determine if the horse falls
                        if (Math.random() < fallProbability(h)) {
                            h.fall();
                        }
                        // Check if horse has finished the race
                        if (h.getDistanceTravelled() >= raceLength) {
                            raceFinished = true;
                        }
                    }
                }
                // Redraw the race panel to reflect horse movements
                racePanel.repaint();

                // Stop the race if a horse has finished or all horses have fallen
                if (raceFinished || allFallen) {
                    raceTimer.stop();
                    endRace();
                }
            }
        });
        // Start the timer to begin the race updates
        raceTimer.start();
    }
    // Calculate the horse's adjusted confidence based on track shape and weather conditions
    private double modifiedConfidence(Horse h) {
        double modifier = 1.0;
        // Apply a 10% reduction if the track shape is Oval or Figure Eight
        if (trackShape.equals("Oval") || trackShape.equals("Figure Eight")) {
            modifier *= 0.9;
        }
        // Apply weather-related reductions
        // Reduce movement by 20% if the track is muddy
        // Reduce movement by 30% if the track is icy
        if (weather.equals("Muddy")) {
            modifier *= 0.8;
        } else if (weather.equals("Icy")) {
            modifier *= 0.7;
        }
        // Return the horse's base confidence multiplied by the final modifier
        return h.getConfidence() * modifier;
    }
    // Calculate the probability of a horse falling during the race
    private double fallProbability(Horse h) {
        // Base fall probability is proportional to the square of the horse's confidence
        double baseFall = 0.1 * h.getConfidence() * h.getConfidence();

        // If the weather is icy, double the fall probability
        if (weather.equals("Icy")) {
            baseFall *= 2.0;
        }
        // Return the final calculated fall probability
        return baseFall;
    }
    // Handle the end of the race, determine winner, update balance, and show results
    private void endRace() {
        // Calculate total race duration
        long finishTime = System.currentTimeMillis() - startTime;
        Horse winner = findWinner();

        // Find the winning horse
        if (winner != null) {
            JOptionPane.showMessageDialog(this, "Winner: " + winner.getName());

            // Record the race results in the statistics manager
            statsManager.recordRace(winner.getName(), (int) finishTime, horses);

            // Update user's balance based on betting result
            if (winner == betHorse) {
                userBalance += betAmount;
                JOptionPane.showMessageDialog(this, "You won! New Balance: " + userBalance);
                saveBalanceToFile();
                balanceLabel.setText("Current Balance: £" + userBalance);
            } else {
                userBalance -= betAmount;
                JOptionPane.showMessageDialog(this, "You lost! New Balance: " + userBalance);
                saveBalanceToFile();
                balanceLabel.setText("Current Balance: £" + userBalance);
            }

        // If all horses fell, there is no winner
        } else {
            userBalance -= betAmount;
            JOptionPane.showMessageDialog(this, "No winner, all horses fell!\nYou lost! New Balance: " + userBalance);
            // Save the updated balance to file
            saveBalanceToFile();
            // Update the balance display on the GUI
            balanceLabel.setText("Current Balance: £" + userBalance);
        }
    }

    // Find and return the first horse that finishes the race without falling
    private Horse findWinner() {
        for (Horse h : horses) {
            if (h.getDistanceTravelled() >= raceLength && !h.hasFallen()) {
                // Return the winning horse
                return h;
            }
        }
        // No winner
        return null;
    }

    // Custom JPanel subclass to visually display the race track and horse positions
    class RaceTrackPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Set background color to white
            setBackground(Color.WHITE);

            // If horses array is not initialized, do nothing
            if (horses == null) return;
            // Draw the finish line
            int finishX = (getWidth() - 100);
            g.setColor(Color.BLACK);
            g.drawLine(finishX, 0, finishX, getHeight());
            // Draw horses and track lanes
            for (int i = 0; i < horses.length; i++) {
                // Calculate horse's x position based on distance traveled
                int x = horses[i].getDistanceTravelled() * (getWidth() - 100) / raceLength;
                int y = (i + 1) * 80;

                // Draw the lane line
                g.setColor(Color.BLACK);
                g.drawLine(0, y + 10, getWidth(), y + 10);

                // Draw the horse or indicate if it has fallen
                if (horses[i].hasFallen()) {
                    g.setColor(Color.RED);
                    g.drawString("X", x, y);
                } else {
                    g.setColor(Color.BLUE);
                    g.drawString(Character.toString(horses[i].getSymbol()), x, y);
                }
            }
        }
    }

    // Save the user's current balance to a text file
    private void saveBalanceToFile() {
        try {
            // Create a FileWriter to write to "balance.txt"
            // Write the user's balance as a string
            // Close the writer to save changes
            java.io.FileWriter writer = new java.io.FileWriter("balance.txt");
            writer.write(String.valueOf(userBalance));
            writer.close();
        } catch (Exception e) {
            // Print the stack trace if an error occurs
            e.printStackTrace();
        }
    }

    // Load the user's balance from the text file if it exists
    private void loadBalanceFromFile() {
        try {
            // Create a File object pointing to "balance.txt"
            java.io.File file = new java.io.File("balance.txt");
            // If the file exists, read the balance
            if (file.exists()) {
                java.util.Scanner scanner = new java.util.Scanner(file);
                // If the file contains a double value, update userBalance
                if (scanner.hasNextDouble()) {
                    userBalance = scanner.nextDouble();
                }
                // Close the scanner after reading
                scanner.close();
            }
        } catch (Exception e) {
            // Print the stack trace if an error occurs
            e.printStackTrace();
        }
    }
    
    // Allow the user to customize horses' attributes before the race
    private void customizeHorses() {
        // Define available options for customization
        String[] icons = {"♞", "♘", "♕", "♔", "♖", "♗"};
        String[] breeds = {"Thoroughbred", "Arabian", "Quarter Horse", "Mustang", "Clydesdale"};
        String[] coats = {"Black", "Brown", "White", "Grey", "Chestnut"};
        String[] saddles = {"Standard", "Racing"};
        String[] horseshoes = {"Regular", "Lightweight", "Heavyweight"};
    
        // Initialize arrays to store horse icons and horse objects
        horseIcons = new char[laneCount];
        horses = new Horse[laneCount];
    
        // Create a panel with a grid layout for customization UI
        JPanel panel = new JPanel(new GridLayout(laneCount + 1, 6, 10, 10));
    
        // Add headers for each customization attribute
        panel.add(new JLabel("Horse"));
        panel.add(new JLabel("Icon"));
        panel.add(new JLabel("Breed"));
        panel.add(new JLabel("Coat Color"));
        panel.add(new JLabel("Saddle"));
        panel.add(new JLabel("Horseshoes"));
    
        // Create arrays of combo boxes for user selections
        JComboBox<String>[] iconBoxes = new JComboBox[laneCount];
        JComboBox<String>[] breedBoxes = new JComboBox[laneCount];
        JComboBox<String>[] coatBoxes = new JComboBox[laneCount];
        JComboBox<String>[] saddleBoxes = new JComboBox[laneCount];
        JComboBox<String>[] horseshoeBoxes = new JComboBox[laneCount];
    
        // Populate the panel with input options for each horse
        for (int i = 0; i < laneCount; i++) {
            panel.add(new JLabel("Horse " + (i + 1)));
    
            iconBoxes[i] = new JComboBox<>(icons);
            breedBoxes[i] = new JComboBox<>(breeds);
            coatBoxes[i] = new JComboBox<>(coats);
            saddleBoxes[i] = new JComboBox<>(saddles);
            horseshoeBoxes[i] = new JComboBox<>(horseshoes);
    
            panel.add(iconBoxes[i]);
            panel.add(breedBoxes[i]);
            panel.add(coatBoxes[i]);
            panel.add(saddleBoxes[i]);
            panel.add(horseshoeBoxes[i]);
        }
    
        // Show the customization dialog and wait for user input
        int result = JOptionPane.showConfirmDialog(this, panel, "Customize Horses",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    
        if (result == JOptionPane.OK_OPTION) {
            // If user confirms, apply their choices
            StringBuilder horseInfo = new StringBuilder();
    
            for (int i = 0; i < laneCount; i++) {
                // Set horse properties based on user selections
                horseIcons[i] = ((String) iconBoxes[i].getSelectedItem()).charAt(0);
                horses[i] = new Horse(horseIcons[i], "Horse " + (i + 1), 0.6);
                horses[i].setBreed((String) breedBoxes[i].getSelectedItem());
                horses[i].setCoatColor((String) coatBoxes[i].getSelectedItem());
                horses[i].setSaddleType((String) saddleBoxes[i].getSelectedItem());
                horses[i].setHorseshoeType((String) horseshoeBoxes[i].getSelectedItem());
    
                // Calculate adjusted attributes based on breed, saddle, and horseshoe
                double baseSpeed = 1.0;
                double fallResistance = 1.0;
                double confidence = 0.6;
    
                // Adjust speed and confidence based on breed
                switch (horses[i].getBreed()) {
                    case "Thoroughbred": baseSpeed += 0.1; break;
                    case "Arabian": confidence += 0.1; break;
                    case "Quarter Horse": baseSpeed += 0.05; break;
                    case "Clydesdale": baseSpeed -= 0.1; break;
                    case "Mustang": /* balance */ break;
                }
    
                // Adjust speed based on saddle type
                if (horses[i].getSaddleType().equals("Racing")) {
                    baseSpeed += 0.05;
                }
    
                // Adjust speed or fall resistance based on horseshoe type
                if (horses[i].getHorseshoeType().equals("Lightweight")) {
                    baseSpeed += 0.05;
                } else if (horses[i].getHorseshoeType().equals("Heavyweight")) {
                    fallResistance += 0.2;
                }
    
                // Build a summary of each horse's configuration
                horseInfo.append(String.format("Horse %d: %s | Breed: %s, Coat: %s, Saddle: %s, Horseshoe: %s\n",
                        (i + 1),
                        horses[i].getSymbol(),
                        horses[i].getBreed(),
                        horses[i].getCoatColor(),
                        horses[i].getSaddleType(),
                        horses[i].getHorseshoeType()));
                horseInfo.append(String.format("- Speed Bonus: %.2f%% | Fall Resistance: %.2f%% | Confidence: %.2f\n\n",
                        (baseSpeed - 1.0) * 100,
                        fallResistance * 100,
                        confidence));
            }
    
            // Show a summary dialog of all horse configurations
            JTextArea textArea = new JTextArea(horseInfo.toString());
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
    
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
    
            JOptionPane.showMessageDialog(this, scrollPane, "Horse compare status", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // If user cancels, set default horses
            for (int i = 0; i < laneCount; i++) {
                horseIcons[i] = '♞';
                horses[i] = new Horse(horseIcons[i], "Horse " + (i + 1), 0.6);
                horses[i].setBreed("Thoroughbred");
                horses[i].setCoatColor("Black");
                horses[i].setSaddleType("Standard");
                horses[i].setHorseshoeType("Regular");
            }
        }
    }
    
    // Calculate the betting odds for each horse based on their attributes
    private double[] calculateHorseOdds() {
        double[] scores = new double[horses.length];
    
        // Calculate a score for each horse based on speed and confidence
        for (int i = 0; i < horses.length; i++) {
            double baseSpeed = 1.0;
            double confidence = horses[i].getConfidence();
    
            // Adjust base speed or confidence depending on horse breed
            switch (horses[i].getBreed()) {
                case "Thoroughbred": baseSpeed += 0.1; break;
                case "Arabian": confidence += 0.1; break;
                case "Quarter Horse": baseSpeed += 0.05; break;
                case "Clydesdale": baseSpeed -= 0.1; break;
                case "Mustang": break;
            }
    
            // Adjust speed based on saddle type
            if (horses[i].getSaddleType().equals("Racing")) {
                baseSpeed += 0.05;
            }
    
            // Adjust speed based on horseshoe type
            if (horses[i].getHorseshoeType().equals("Lightweight")) {
                baseSpeed += 0.05;
            }
            // Calculate the final score combining speed and confidence
            double score = (baseSpeed - 1.0) + (confidence * 0.5);
            scores[i] = score;
        }
    
        // Sum of all scores to calculate relative odds
        double sumScore = 0;
        for (double score : scores) {
            sumScore += score;
        }
    
        double[] odds = new double[horses.length];
        // Calculate odds for each horse (lower score → higher odds)
        for (int i = 0; i < horses.length; i++) {
            odds[i] = sumScore / scores[i];
            odds[i] = Math.round(odds[i] * 100.0) / 100.0;
        }
        // Return the final odds
        return odds;
    }

    // Display the calculated betting odds for all horses in a dialog box
    private void showOdds() {
        // Calculate odds based on horse attributes
        double[] odds = calculateHorseOdds();
        StringBuilder sb = new StringBuilder();
        
        // Build a formatted string showing odds for each horse
        for (int i = 0; i < horses.length; i++) {
            sb.append("Horse ").append(i + 1)
              .append(" (").append(horses[i].getSymbol()).append(") - Odds: ")
              .append(odds[i]).append("\n");
        }
    
        // Create a text area to display the odds neatly
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        // Add the text area to a scroll pane for better readability
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        // Show the odds in a dialog box
        JOptionPane.showMessageDialog(this, scrollPane, "Horse Betting Odds", JOptionPane.INFORMATION_MESSAGE);
    }

// Main method Entry point of the program
    public static void main(String[] args) {
        // Launch the GUI safely on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new RaceGUI().setVisible(true);
        });
    }
}
