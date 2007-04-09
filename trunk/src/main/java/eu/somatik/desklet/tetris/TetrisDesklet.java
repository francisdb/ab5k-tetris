/*
 * TetrisDesklet.java
 *
 * Created on April 3, 2007, 8:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eu.somatik.desklet.tetris;

import ab5k.desklet.Desklet;
import ab5k.desklet.DeskletContainer;
import ab5k.desklet.test.DeskletTester;

/**
 *
 * @author francisdb
 */
public class TetrisDesklet extends Desklet{
    
    private TetrisPanel tetrisPanel;
    
    /**
     * 
     * @param s 
     */
    public static void main(String s[]) {
        DeskletTester.start(TetrisDesklet.class);
    }
    
    /**
     * Sets up the desklet
     * @throws java.lang.Exception 
     */
    public void init() throws Exception {
        tetrisPanel = new TetrisPanel();
        DeskletContainer container = getContext().getContainer();
        getContext().getContainer().setBackgroundDraggable(true);
        container.setContent(tetrisPanel);
        container.setResizable(false);
        container.setShaped(true);    
        container.setVisible(true);
    }

    public void start() throws Exception {
        tetrisPanel.start();
    }

    public void stop() throws Exception {
        tetrisPanel.stop();
        getContext().notifyStopped();
    }

    public void destroy() throws Exception {
        System.out.println("Destroy");
    }
}
