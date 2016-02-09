package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class BackgroundThreadTest extends JPanel implements ActionListener, PropertyChangeListener {

    private JProgressBar progressBar;
    private JButton startButton;
    private LevelDesignerManager levelDesignerManager;

    public BackgroundThreadTest () {
        super(new BorderLayout());

        startButton = new JButton("Run Level Designer");
        startButton.setActionCommand("start");
        startButton.addActionListener(this);

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        JPanel panel = new JPanel();
        panel.add(startButton);
        panel.add(progressBar);

        add(panel, BorderLayout.PAGE_START);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    public static void main (String[] args) {
        //Create and set up the window.
        JFrame frame = new JFrame("Level Designer Test");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new BackgroundThreadTest();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(PropertyChanges.PROPERTY_CHANGE_DESIGNS)) {
            List<LevelRepresentation> topDesigns = (List<LevelRepresentation>) evt.getNewValue();

            for (LevelRepresentation r : topDesigns) {
                System.out.println("***************************************************");
                System.out.println("***************************************************");
                System.out.println("***************************************************");
                r.printRepresentation();
                System.out.println("***************************************************");
                System.out.println("***************************************************");
                System.out.println("***************************************************");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        startButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        levelDesignerManager = new LevelDesignerManager(new Specification(0.5, GameMode.HIGHSCORE));
        levelDesignerManager.addPropertyChangeListener(this);
        levelDesignerManager.execute();
    }
}
