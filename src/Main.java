import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.Timer;

public class Main {
	/* Labels */
	private static final String TYPE_OF_CAUCUS = "Type of caucus";
	private static final String TITLE_OF_CAUCUS = "Title of caucus";
	private static final String SPEAKER_TIME = "Speaker time";
	private static final String MASTER_LIST = "Master list";
	private static final String MASTER_LIST_FILE_TEXT = "Choose master list file";
	private static final String SPEAKER_LIST = "Speaker list";
	private static final String TOTAL_TIME = "Total time";
	private static String DEBATE_TEXT = "Debate!";
	
	private static Timer timer;
	/* These represent the input fields corresponding 
	 * to the fileds of the details page
	 */
	private static JComboBox typeOfCaucusField;
	private static JTextField titleOfCaucusField;
	
	private static JButton debateButton;
	private static JTextArea speakerSelectionArea;
	private static JFrame mainFrame;
	
	/* These represent the two different views of the app */
	private static JPanel detailsPage;
	private static JPanel counterPage;
	
	private static final JFileChooser fc = new JFileChooser();
	private static List<String> speakerList;
	
	/* Specify the types of caucuses here */
	private static String[] CAUCUS_TYPE_CHOICES = {"A", "B"};
	private static String TOTAL_TIME_REMAINING = "Total Time Remaining";
	private static String TOTAL_TIME_OF_CAUCUS = "Total Time of Caucus";
	private static String SPEAKER_TIME_REMAINING = "Speaker Time Remaining";
	private static String CURRENT_SPEAKER = "Current Speaker";
	private static JTextField totalTimeBox;
	private static JTextField speakerTimeBox;
	
	private static void initializeCountdownPanel() {
		
		mainFrame.remove(detailsPage);
		
		/* Create all the labels */
		JLabel caucusHeader = new JLabel(typeOfCaucusField.getSelectedItem().toString() + " : " +
		titleOfCaucusField.getText());
		
		/* This is a stand-in for now */
		JLabel currentSpeakerLabel = new JLabel(CURRENT_SPEAKER );
		JLabel currentSpeaker = new JLabel(speakerList.get(0));
		
		JLabel totalTimeRemainingLabel = new JLabel(TOTAL_TIME_REMAINING );
		final JLabel totalTimeRemaining = new JLabel(totalTimeBox.getText());
		
		JLabel totalTimeOfCaucusLabel = new JLabel(TOTAL_TIME_OF_CAUCUS );
		JLabel totalTimeOfCaucus = new JLabel(totalTimeBox.getText());
		
		JLabel speakerTimeLabel = new JLabel(SPEAKER_TIME);
		JLabel speakerTime = new JLabel(speakerTimeBox.getText());
		
		JLabel speakerTimeRemainingLabel = new JLabel(SPEAKER_TIME_REMAINING );
		final JLabel speakerTimeRemaining = new JLabel(speakerTimeBox.getText());

		counterPage.add(caucusHeader);

		counterPage.add(currentSpeakerLabel);
		counterPage.add(currentSpeaker);
		
		counterPage.add(totalTimeRemainingLabel);
		counterPage.add(totalTimeRemaining);
		
		counterPage.add(totalTimeOfCaucusLabel);
		counterPage.add(totalTimeOfCaucus);
		
		counterPage.add(speakerTimeLabel);
		counterPage.add(speakerTime);
		
		counterPage.add(speakerTimeRemainingLabel);
		counterPage.add(speakerTimeRemaining);
		

		mainFrame.add(counterPage);
		mainFrame.pack();
		
		timer = new Timer(Integer.parseInt(totalTimeBox.getText()), new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		         updateTimeLabels(totalTimeRemaining, speakerTimeRemaining);
		      }
		   });
		timer.start();
		
	}
	
	private static void updateTimeLabels(JLabel totalTimeRemaining, JLabel speakerTimeRemaining){
		int totalTimeRemainingCount = Integer.parseInt(totalTimeRemaining.getText())-1;
        int speakerTimeRemainingCount = Integer.parseInt(speakerTimeRemaining.getText())-1;
        
        if (speakerTimeRemainingCount == 0){
        	timer.stop();
			//Change buttons and interface to reflect
			//that speaker has finished
        	return;
        }
        else if (totalTimeRemainingCount == 0){
        	timer.stop();
			//Change buttons and interface to reflect
			//that entire debate has finished
        	return;
        }
        
       totalTimeRemaining.setText(totalTimeRemainingCount+"");
       speakerTimeRemaining.setText(speakerTimeRemainingCount+"");
	}
	
	private static void createAndShowGUI(){
		counterPage = new JPanel();
		counterPage.setLayout(new BoxLayout(counterPage, BoxLayout.Y_AXIS));
		
        String[] labels = {TYPE_OF_CAUCUS, TITLE_OF_CAUCUS, SPEAKER_TIME, 
        		MASTER_LIST, SPEAKER_LIST, TOTAL_TIME};
        int numPairs = labels.length;
 
        //Create and populate the panel.
        detailsPage = new JPanel(new SpringLayout());
        for (int i = 0; i < numPairs; i++) {
            JLabel l = new JLabel(labels[i], JLabel.TRAILING);
            detailsPage.add(l);
            
            JComponent inputBox;
            
            if (labels[i] == TYPE_OF_CAUCUS){
            	typeOfCaucusField = new JComboBox(CAUCUS_TYPE_CHOICES);
            	inputBox = typeOfCaucusField;
            }
            else if (labels[i] == TITLE_OF_CAUCUS){
            	titleOfCaucusField = new JTextField();
            	inputBox = titleOfCaucusField;
            }
            else if (labels[i] == MASTER_LIST){
            	JButton button = new JButton(MASTER_LIST_FILE_TEXT);
            	button.addActionListener(new ActionListener(){
            		public void actionPerformed(ActionEvent event){
            			int returnValue = fc.showOpenDialog(detailsPage);
            			if (returnValue == JFileChooser.APPROVE_OPTION){
            				try {
								BufferedReader br = new BufferedReader(new FileReader(fc.getSelectedFile()));
								
								speakerList = new ArrayList<String>();
								String temp;
								while ((temp = br.readLine()) != null){
									speakerList.add(temp);
								}
								speakerSelectionArea.setRows(speakerList.size());
								
								for (String name : speakerList){
									speakerSelectionArea.append(name + "\n");
								}
            				} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
            			}
            		}
            	});
            	inputBox = button;
            }
            else if (labels[i] == SPEAKER_LIST){
            	speakerSelectionArea = new JTextArea();
            	inputBox = speakerSelectionArea;
            }
            else if (labels[i] == TOTAL_TIME){
            	totalTimeBox = new JTextField(10);
            	inputBox = totalTimeBox;
            }
            else if (labels[i] == SPEAKER_TIME){
            	speakerTimeBox = new JTextField(10);
            	inputBox = speakerTimeBox;
            }
            else {
            	inputBox = new JTextField(10);
            }
            
            l.setLabelFor(inputBox);
            detailsPage.add(inputBox);
        }
 
        //Lay out the panel.
        SpringUtilities.makeCompactGrid(detailsPage,
                                        numPairs, 2, //rows, cols
                                        6, 6,        //initX, initY
                                        6, 6);       //xPad, yPad
        debateButton = new JButton(DEBATE_TEXT);
        debateButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent event){
        		initializeCountdownPanel();
        	}
        });
        
        detailsPage.add(debateButton);
        
        //Create and set up the window.
        mainFrame = new JFrame("SpringForm");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Set up the content pane.
        detailsPage.setOpaque(true);  //content panes must be opaque
        mainFrame.setContentPane(new JPanel());
        mainFrame.add(detailsPage);
 
        //Display the window.
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
	

}
