/*
 * TetrisLogic.java
 *
 * Created on April 3, 2007, 9:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eu.somatik.desklet.tetris;



/**
 *
 * @author francisdb
 */
public class TetrisLogic {
    
        
    private int score;
    private int level;
    private int lines;
    
    /** Creates a new instance of TetrisLogic */
    public TetrisLogic() {
        reset();
        
        
        // Tools.submitScore(userId, score);
    }
    
    /**
     * Resets the game
     */
    public void reset(){
        this.score = 0;
        this.level = 0;
        this.lines = 0;
    }
    
    /**
     * 
     * @param lineCount 
     */
    public void linesRemoved(int lineCount){
        switch (lineCount){
        case 0:
            // no new lines
            break;
        case 1:
            score += 40*(level+1);
            break;
        case 2:
            score += 100*(level+1);
            break;
        case 3:
            score += 300*(level+1);
            break;
        case 4:
            score += 1200*(level+1);
            break;
        default:
            throw new AssertionError("Should never come here");
        }
        int previousLines = lines;
        lines+=lineCount;
        // increment level whenever we pass a multiple of 10
        if (lines/10 != previousLines/10){
            level++;
        }
    }
    
    public int getDelay(){
        return 600 - level*40;
    }
    
    /**
     * 
     */
    public void blockDropped(){
        score++;
    }
    
    
    
        
    ////////////////////////GETTERS & SETTERS////////////////////////////////
    
    /**
     * 
     * @param lines 
     */
    public void setLines(int lines) {
        this.lines = lines;
    }

    /**
     * 
     * @return 
     */
    public int getLines() {
        return lines;
    }

    /**
     * 
     * @param level 
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * 
     * @return 
     */
    public int getLevel() {
        return level;
    }

    /**
     * 
     * @param score 
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * 
     * @return 
     */
    public int getScore() {
        return score;
    }


    
    
    
}
