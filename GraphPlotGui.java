import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/**
* This class reads from a <i>.csv</i> file and produces a plot of graph between fitness values and number of iterations.
* Also, it extends JPanel class and uses its paintComponent method to make the complete plot the graph.
* @author Ashish Rana
* @version 1.0
* @see JPanel
* @since 2018-01-09
*/

public class GraphPlotGui extends JPanel{
	/**
	* Width of the Gui Frame by default.	
	* @since 1.0
	*/
	private int width = 1000;
    /**
	* Height of the Gui Frame by default.	
	* @since 1.0
	*/
	private int height = 500;
    /**
	* Padding of inner content from the Gui Frame by default.	
	* @since 1.0
	*/
	private int padding = 25;
    /**
	* Padding of labels from the Gui Frame by default. Also, if inner padding gets added on.	
	* @since 1.0
	*/
	private int labelPadding = 25;
    /**
	* Width of points in Gui Frame by default.	
	* @since 1.0
	*/
    private final static int pointWidth = 3;
    /**
	* Y-axis divisions in Gui graph by default.	
	* @since 1.0
	*/
    private final static int numberYDivisions = 10;
    /**
	* Data line color in Gui by default.	
	* @since 1.0
	*/
    private Color lineColor = new Color(44, 102, 230, 180);
    /**
	* Color of points in Gui by default.	
	* @since 1.0
	*/
    private Color pointColor = new Color(100, 100, 100, 180);
    /**
	* Color of graph grid in Gui by default.	
	* @since 1.0
	*/
    private Color gridColor = new Color(200, 200, 200, 200);
    /**
	* Stroke in graph Gui by default.	
	* @since 1.0
	*/
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    /**
	*fitness value plot list Gui by default.	
	* @since 1.0
	*/
    private List<Double> fitnessValues;
	
	
	/**
	* This constructor of class sets the fitnessValues list which will be plotted along y-axis of graph.
	* @param fitnessValues a list that passed as arguement to be assigned to member of class
	* @since 1.0
	*/
	public GraphPlotGui(List<Double> fitnessValues) {
        this.fitnessValues = fitnessValues;
    }
	
	/**
	* This method overrides super class method and returns the dimension of the GUI.
	* Also this method initializes a Dimension object for which contains values of height and width.
	* @return Dimension This object carries information about dimensions of the GUI
	* @since 1.0
	*/ 
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
	
	/**
	* This method overrides super class method and also calls it for pre-setting(i.e. base setting of the panel) all the things in GUI.
	* Main task of this method is to draw everything from graph, its background grid, plot line and label along with all the axis values.
	* @param g This Graphics class object will be containing and manipulating all information about the graph GUI plot
	* @exception NullPointerException This exception will thrown when g is null
	* @exception IndexOutOfBoundsException This exception thrown index higher or lower than permitted value is accessed
	* @since 1.0
	*/ 
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (fitnessValues.size() - 1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxValue() - getMinValue());

        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < fitnessValues.size(); i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxValue() - fitnessValues.get(i)) * yScale + padding);
            graphPoints.add(new Point(x1, y1));
        }

        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;
            if (fitnessValues.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = ((int) ((getMinValue() + (getMaxValue() - getMinValue()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // and for x axis
        for (int i = 0; i < fitnessValues.size(); i++) {
            if (fitnessValues.size() > 1) {
                int x0 = i * (getWidth() - padding * 2 - labelPadding) / (fitnessValues.size() - 1) + padding + labelPadding;
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - pointWidth;
                if ((i % ((int) ((fitnessValues.size() / 20.0)) + 1)) == 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                    g2.setColor(Color.BLACK);
                    String xLabel = i + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }
                g2.drawLine(x0, y0, x1, y1);
            }
        }

        // create x and y axes 
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        for (int i = 0; i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - pointWidth / 2;
            int y = graphPoints.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }
    }
	
	/**
	* This method returns the minimum value from the list of fitness values.
	* @return double This contains minValue from the list that will be plotted in the graph
	* @since 1.0
	*/	
	
	public double getMinValue() {
        double minValue = Double.MAX_VALUE;
        for (Double value : fitnessValues) {
            minValue = Math.min(minValue, value);
        }
        return minValue;
    }

	
	/**
	* This method returns the maximum value from the list of fitness values.
	* @return double This contains maxValue from the list that will be plotted in the graph
	* @since 1.0
	*/
		
	public double getMaxValue() {
        double maxValue = Double.MIN_VALUE;
        for (Double value : fitnessValues) {
            maxValue = Math.max(maxValue, value);
        }
        return maxValue;
    }
	
	/**
	* This method is setter method that is used for assinging fitness values to the member of this class.
	* @param fitnessValues This list contains fitness values to corresponding fitness function
	* @since 1.0
	*/
	
    public void setValue(List<Double> fitnessValues) {
        this.fitnessValues = fitnessValues;
        invalidate();
        this.repaint();
    }
	
	/**
	* This method is getter method that is used for getting fitness values from the member of this class.
	* @return List This returned list contains double type fitness values to corresponding fitness function that are to be plotted
	* @since 1.0
	*/
	
    public List<Double> getfintnessValues() {
        return fitnessValues;
    }
	
	/**
	* This method is called by main method and does all the work in the program. Reads the <i>.csv</i> file, load the Gui content and displays it. 
	* @see JFrame
	* @since 1.0
	*/
	
	private static void createAndShowGui() {
        List<Double> fitnessValues = new ArrayList<>();
        
        String csvFile = "ResultGeniticAlgorithm.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            int i=0;
            while ((line = br.readLine()) != null) {
                String[] csvData = line.split(cvsSplitBy);
				if(i>0)
				fitnessValues.add(Double.parseDouble(csvData[1]));

            	i++;
			}

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        GraphPlotGui mainPanel = new GraphPlotGui(fitnessValues);
        mainPanel.setPreferredSize(new Dimension(600, 300));
        JFrame frame = new JFrame("DrawGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
    }
	
	
	/**
	* This is the main method which is having purpose of invoking a private method that will initialize and draw the GUI.Also it is responsible for running the GUI.  
	* @param args Unused
	* @since 1.0
	* @exception NullPointerException This exception can get thrown with empty list in file or in absence of the file 
	* @exception IndexOutOfBoundsException This exception thrown index higher or lower than permitted value is accessed
	*/
	
	public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            createAndShowGui();
         }
      });
   }
}
