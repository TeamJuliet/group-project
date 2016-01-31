package uk.ac.cam.cl.intelligentgamedesigner.testing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class TestRunner extends JPanel implements ActionListener, PropertyChangeListener {

    private JProgressBar progressBar;
    private JButton startButton;
    private JTextArea taskOutput;
    private TestWorker task;

    // For keeping track of test data
    private String mostRecentTestDescription;
    private int numTests;
    private int numPassed;

    public TestRunner () {
        super(new BorderLayout());

        UIManager.put("ProgressBar.selectionForeground", Color.BLACK);
        
        startButton = new JButton("Start");
        startButton.setActionCommand("start");
        startButton.addActionListener(this);

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);

        JPanel panel = new JPanel();
        panel.add(startButton);
        panel.add(progressBar);

        add(panel, BorderLayout.PAGE_START);
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    public static void main (String[] args) {
        //Create and set up the window.
        JFrame frame = new JFrame("Unit Tests");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new TestRunner();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    class TestWorker extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() {

            ArrayList<TestCase> testCases = TestLibrary.getTests();
            numTests = testCases.size();

            setProgress(0);

            numPassed = 0;
            for (int t = 0; t < numTests; t++) {
                boolean result = testCases.get(t).run();
                if (result) {
                    numPassed++;
                    mostRecentTestDescription = "PASSED: " + testCases.get(t).description;
                } else {
                    mostRecentTestDescription = "FAILED: " + testCases.get(t).description;
                }

                setProgress((int) (((t + 1) / (double) numTests) * 100));

                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignore) {}
            }
            return null;
        }

        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            startButton.setEnabled(true);
            setCursor(null); //turn off the wait cursor
            taskOutput.append(numPassed + "/" + numTests + " tests passed.\n");
        }
    }

    public void actionPerformed(ActionEvent evt) {
        startButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        task = new TestWorker();
        task.addPropertyChangeListener(this);
        task.execute();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
            taskOutput.append(mostRecentTestDescription + "\n");
        }
    }
}
