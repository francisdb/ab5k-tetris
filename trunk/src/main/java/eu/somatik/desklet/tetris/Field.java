/*
 * Field.java
 *
 * Created on April 3, 2007, 9:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eu.somatik.desklet.tetris;

import java.awt.Color;

/**
 *
 * @author francisdb
 */
public class Field {
    
    /**
     * The Color for an empty square
     */
    public static final Color COLOR_EMPTY = new Color(30,30,30,255);
    
    private static final int GAME_WIDTH=10;
    private static final int GAME_HEIGHT=20;
    
    private Color[][] gameField;
    
    /** Creates a new instance of Field */
    public Field() {
        gameField= new Color[GAME_WIDTH][GAME_HEIGHT];
        reset();
    }
    
    /**
     * Gets square info
     * @param x 
     * @param y 
     * @return 
     */
    protected Color get(int x, int y){
        return gameField[x][y];
    }
    
    /**
     * Checks if a square is empty
     * @param x 
     * @param y 
     * @return true if empty
     */
    public boolean isEmpty(int x, int y){
        return gameField[x][y].equals(COLOR_EMPTY);
    }
    
    /**
     * Sets a square empty;
     * @param x 
     * @param y 
     */
    public void setEmpty(int x, int y){
        gameField[x][y] = COLOR_EMPTY;
    }
    
    /**
     * Sets square info
     * @param x 
     * @param y 
     * @param value 
     */
    protected void set(int x, int y, Color value){
        gameField[x][y] = value;
    }
    
    /**
     * 
     * @return the game width
     */
    public int getWidth(){
        return GAME_WIDTH;
    }
    
    /**
     * 
     * @return the game height
     */
    public int getHeight(){
        return GAME_HEIGHT;
    }
    
    /**
     * Makes the game field empty
     */
    protected void reset(){
        for( int x=0;x<GAME_WIDTH;x++){
            for(int y=0;y<GAME_HEIGHT;y++){
                gameField[x][y] = COLOR_EMPTY;
            }
        }
    }
}
