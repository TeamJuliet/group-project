package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.event.ChangeListener;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

public class SelectBoard extends DisplayBoard implements MouseListener{

	private boolean selected;
	private DesigningLevelScreen manager;
	private int id_number;
	
	public SelectBoard(Design design, int n) {
		super(design);
		selected = false;
		id_number = n;
		addMouseListener(this);
	}

	public void setManager(DesigningLevelScreen master){
		manager = master;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		
		if(selected){
			g.setColor(Color.RED);
		    Graphics2D g2d = (Graphics2D)g;
		    int brush_width = 5;
		    g2d.setStroke(new BasicStroke(brush_width));
		    g2d.drawRect(0, 0, (tile_size*width), tile_size*height);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		manager.selectBoard(id_number);
	}

	//UNUSED METHODS
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	
}
