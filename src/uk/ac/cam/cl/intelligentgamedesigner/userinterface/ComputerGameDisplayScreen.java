package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.NoMovesFoundException;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.SimulatedPlayerManager;

//defines the functionality specific to the Simulated Player viewer
public class ComputerGameDisplayScreen extends GameDisplayScreen {
    private JLabel           ai_name;
    private JRadioButton     auto_play;
    private JButton          next_move;
    private JButton          solve;
    private boolean          auto_playing;
    private int              ability;

    Timer                    timer;
    private static final int waitspeed          = 500;
    private static int       wait_between_moves = 600;
    SimulatedPlayerManager playerManager;

    public ComputerGameDisplayScreen() {
        super();
        identifier = "Simulated Player";
        auto_playing = false;

        timer = new Timer(waitspeed, this);
        timer.setInitialDelay(waitspeed);
        timer.addActionListener(this);
        timer.setActionCommand("trigger");
    }

    // get the static method for invoking
    public void setAbility(int ability) {
        this.ability = ability;
        String game_mode_text = "High Score";
        if (game_mode == GameMode.JELLY)
            game_mode_text = "Jelly Clear";
        if (game_mode == GameMode.INGREDIENTS)
            game_mode_text = "Ingredients";
        ai_name.setText(game_mode_text + " player, Ability Level " + (ability + 1) + ":");
    }

    @Override
    public void initialiseGame() {
        super.initialiseGame();
        playerManager = new SimulatedPlayerManager();
    }

    @Override
    protected void stopGame() {
        super.stopGame();
        next_move.setEnabled(true);
        quit_button.setEnabled(true);
        auto_play.setSelected(false);
        auto_playing = false;
        timer.stop();
        playerManager = null;
    }

    @Override
    protected GameBoard specificGameBoard() {
        return new ComputerGameBoard(new Design());
    }

    @Override
    protected void makeItems() {
        super.makeItems();
        auto_play = new JRadioButton("Auto-play Simulated Player Moves");
        next_move = new JButton("Play Next Move");
        solve = new JButton("Auto-Complete Level");
        solve.setToolTipText("The player runs through the entire level, stopping in success or failure.");
        ai_name = new JLabel();
    }

    @Override
    protected void setUpItems() {
        super.setUpItems();
        auto_play.setActionCommand("mode");
        auto_play.addActionListener(this);
        next_move.setActionCommand("next");
        next_move.addActionListener(this);
        ;
        solve.setActionCommand("solve");
        solve.addActionListener(this);
    }

    private JPanel controls;

    @Override
    protected void addItems() {
        super.addItems();

        // make a box with all the added simulated player settings
        controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setBorder(BorderFactory.createLineBorder(Color.black));
        controls.add(Box.createRigidArea(new Dimension(0, 20)));
        controls.add(ai_name);
        controls.add(Box.createRigidArea(new Dimension(0, 20)));
        controls.add(auto_play);
        controls.add(Box.createRigidArea(new Dimension(0, 20)));
        controls.add(next_move);
        controls.add(Box.createRigidArea(new Dimension(0, 20)));
        controls.add(solve);
        controls.add(Box.createRigidArea(new Dimension(0, 20)));
        add(controls);

    }

    @Override
    protected void placeItems() {
        super.placeItems();

        // size the fonts
        fontScale(controls, DisplayScreen.FONT_NORMAL);

        // set the locations
        position(controls, 0.75, 0.32, 300, 160);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        // additional button effects
        switch (e.getActionCommand()) {

        case "mode":
            auto_playing = auto_play.isSelected();
            next_move.setEnabled(!auto_playing);
            quit_button.setEnabled(!auto_playing);
            if (auto_playing) {
                timer.setDelay(waitspeed);
                timer.start();
            } else {
                timer.stop();
            }
            nextMove();
            break;
        case "next":
            nextMove();
            break;
        case "trigger":
            if (auto_playing) {
                nextMove();
            }
            timer.setDelay(waitspeed);
            break;
        case "solve":
            allMoves();
            break;
        }
    }

    private void nextMove() {
        if (!playing_move) {
            try {
                Move next = playerManager.calculateBestMove(theGame, ability);

                ((ComputerGameBoard) board).showMove(next);
                Thread.sleep(wait_between_moves);
                ((ComputerGameBoard) board).hideMove();

                playMove(next);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (NoMovesFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void allMoves() {
        if (!playing_move) {
            try {
                playerManager.solve(theGame, ability);
                update();
                endGameCheck();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (NoMovesFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected boolean isHuman() {
        return false;
    }
}
