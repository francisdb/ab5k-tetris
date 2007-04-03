/*
 * TetrisDesklet.java
 *
 * Created on April 3, 2007, 8:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eu.somatik.desklet.tetris;

import ab5k.desklet.AbstractDesklet;
import ab5k.desklet.DeskletContext;
import ab5k.desklet.test.DeskletTester;

/**
 *
 * @author francisdb
 */
public class TetrisDesklet extends AbstractDesklet{
    
    private TetrisPanel tetrisPanel;
    
    /**
     * 
     * @param s 
     */
    public static void main(String s[]) {
        DeskletTester.start(TetrisDesklet.class);
    }

    public void init(DeskletContext context) throws Exception {
        this.context = context;
        tetrisPanel = new TetrisPanel();
        context.getContainer().setContent(tetrisPanel);
        context.getContainer().setResizable(false);
        context.getContainer().setVisible(true);       
    }

    public void start() throws Exception {
        tetrisPanel.start();
    }

    public void stop() throws Exception {
        tetrisPanel.stop();
        this.context.notifyStopped();
    }

    public void destroy() throws Exception {
        System.out.println("Destroy");
    }
}
