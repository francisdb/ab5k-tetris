package eu.somatik.desklet.tetris; 

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

/**
 * Block.java
 *
 * Created on November 8, 2004, 3:40 PM
 *
 * @author  francisdb
 */
public class Block {
    private int[][] matrix;
    private Color   color;
    private Point   pos;
    private Random randomizer= new Random();
    
    private final static int blocks[][][]= {
        {
            {1,0},
            {1,1},
            {1,0}
        },{
            {1},
            {1},
            {1},
            {1}
        },{
            {1,1},
            {1,1}
        },{
            {1,0},
            {1,1},
            {0,1}
        },{
            {0,1},
            {1,1},
            {1,0}
        },{
            {1,0},
            {1,0},
            {1,1}
        },{
            {0,1},
            {0,1},
            {1,1}
        }
    };
    
    private static final Color[] COLORS={
        new Color(70,70,255),
        new Color(255,70,70),
        new Color(0,150,100),
        new Color(255,255,70),
        new Color(200,70,200),
        new Color(70,200,250),
        new Color(255,150,70)
    };
    
    /** 
     * Creates a new instance of Block 
     */
    public Block() {
        randomizer.setSeed((long)(Math.random()*1000));
        int blockNum =randomizer.nextInt(blocks.length) ;
        matrix=blocks[blockNum];
        pos = new Point(11,2);
        this.color = COLORS[blockNum];
    }
    
    public int[][] getMatrix(){
        return matrix;
    }
    
    public Point getPosition(){
        return pos;
    }
    
    public void setPosition(Point pos){
        this.pos = pos;
    }
    
    public Color getColor(){
        return this.color;
    }
    
    public int getWidth(){
        return this.matrix.length;
    }
    
    public int getHeight(){
        return this.matrix[0].length;
    }
    
    public void rotate(){
            int oldWidth=matrix.length;
            int oldHeight=matrix[0].length;
            int newWidth=oldHeight;
            int newHeight=oldWidth;
            int[][] newMatrix = new int[newWidth][newHeight];
            
            for( int x=0;x<newWidth;x++)
                for(int y=0;y<newHeight;y++)
                    newMatrix[x][y]=matrix[y][newWidth-x-1];
            matrix = newMatrix;
    }
    
}
