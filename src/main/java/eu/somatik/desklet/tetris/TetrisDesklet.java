/*
 * TetrisDesklet.java
 *
 * Created on April 3, 2007, 8:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eu.somatik.desklet.tetris;

import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import org.glossitope.desklet.Desklet;
import org.glossitope.desklet.DeskletContainer;
import org.glossitope.desklet.test.DeskletTester;

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
        runOnEDT( new Runnable(){
            public void run() {
                tetrisPanel = new TetrisPanel();
                DeskletContainer container = getContext().getContainer();
                getContext().getContainer().setBackgroundDraggable(true);
                container.setContent(tetrisPanel);
                container.setResizable(false);
                container.setShaped(true);    
                container.setVisible(true);
            }
        });
    }
    
    private void runOnEDT(Runnable runnable){
        if(SwingUtilities.isEventDispatchThread()){
            System.out.println("ON EDT");
            runnable.run();
        }else{
            System.out.println("MOVING TO EDT");
            try         {
                javax.swing.SwingUtilities.invokeAndWait(runnable);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            }
}
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
