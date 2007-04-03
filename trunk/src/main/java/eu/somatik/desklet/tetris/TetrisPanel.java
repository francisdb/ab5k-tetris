package eu.somatik.desklet.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JPanel;


/**
 *
 * @author francisdb
 */
public class TetrisPanel extends JPanel implements Runnable, KeyListener {
    
    private static final int WIDTH = 350;
    private static final int HEIGHT = 450;
  
    private static final int BLOCK_HEIGHT = 20;
    private static final int BLOCK_WIDTH = 20;
    

    private static final Color COLOR_BACKGROUND = Color.WHITE;

    private static final int keyInterval=100;

    
    private Image doubleBufferImage;
    private Graphics2D doubleBufferGraphics;
    
    private Field gameField;
    private TetrisLogic logic;

    private Random                      randomizer = new Random();
    
    private Block curBlock;
    private Block                               nextBlock;
    
    private boolean                             paused=false;
    private boolean                             gameover=false;
    private int                                 userId=0;
    private long                                downTime = System.currentTimeMillis(); //timer for blockfall
    private boolean                             leftdown=false;
    private boolean                             rightdown=false;
    private boolean                             downdown=false;
    private boolean                             spaced=false;
    private boolean                             updown=false;
    private long                                keyTimer=System.currentTimeMillis();
    
    
    private Thread thread = null;
    
    /**
     *
     */
    public TetrisPanel(){
        
        this.logic = new TetrisLogic();
        this.gameField = new Field();
        
        doubleBufferImage=new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        doubleBufferGraphics=(Graphics2D)doubleBufferImage.getGraphics();
        
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                //System.out.println("mousePressed");
                requestFocus();
            }
        };
        
        this.addMouseListener(mouseAdapter);
        this.addKeyListener(this);
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
    }
    
    public void run() {
        
        //init render
        myInit();
        long pauseTime;
        int delay;
        while (thread != null) {
            long sl, sa = System.currentTimeMillis();
            
            checkKeys();
            delay = logic.getDelay();
            if(!paused&&!gameover){
                if ((System.currentTimeMillis()-downTime)>delay){
                    //correctie voor over tijd
                    downTime=downTime+delay;
                    checkMove();
                    
                }
            }
            
            renderAll(doubleBufferGraphics);
            this.repaint();
            
            //timing for paint
            if ((sl = 30l - (System.currentTimeMillis() - sa)) < 10l)
                sl = 10l;
            try { Thread.sleep(sl); } catch (Exception e) { }
            if (paused){
                try { Thread.sleep(100); } catch (Exception e) { }
                //not 100% correct...
                downTime+=System.currentTimeMillis() - sa;
            }
        }
    }
    
    private void checkKeys(){
        long now = System.currentTimeMillis();
        if(rightdown&&(now-keyTimer>keyInterval)){
            if(!paused&&!gameover)
                if (curBlock.getPosition().getX()+curBlock.getWidth()<gameField.getWidth() && !checkOverlap(1,0) )
                    curBlock.getPosition().translate(1, 0);
            keyTimer=now;
        }
        if(leftdown&&(now-keyTimer>keyInterval)){
            if(!paused&&!gameover)
                if (curBlock.getPosition().getX()>0 && !checkOverlap(-1,0) )
                    curBlock.getPosition().translate(-1, 0);
            keyTimer=now;
        }
        if(downdown&&(now-keyTimer>keyInterval)){
            //drop 1
            if(!paused&&!gameover)
                checkMove();
            keyTimer=now;
        }
        if(spaced){
            //fix voor dropDown die niet snel genoeg is als hij in de keybhandler staat
            spaced=false;
            dropDown();
        }
        if(updown){
            //rotate, check for border before rotate
            if (curBlock.getPosition().getX()+curBlock.getHeight()<=gameField.getWidth())
                curBlock.rotate();
            //rotate to original
            if (checkOverlap(0,0)){
                curBlock.rotate();
                curBlock.rotate();
                curBlock.rotate();
            }
            updown=false;
        }
        
    }
    
    private void newGame(){
        logic.reset();
        curBlock = new Block();
        curBlock.setPosition(new Point(3,0));
        nextBlock = new Block();
        gameField.reset();
        gameover=false;
        paused=false;
        this.requestFocus();
        downTime = System.currentTimeMillis();
    }
    
    
    void myInit() {
        newGame();
    }
    
    private void clearField(Graphics g){
        g.setColor(COLOR_BACKGROUND);
        g.fillRect(0,0, doubleBufferImage.getWidth(null), doubleBufferImage.getHeight(null));
    }
    
    private void renderField(Graphics g){
        g.setColor(Color.DARK_GRAY);
        g.drawRect(BLOCK_WIDTH-1,BLOCK_HEIGHT-1,BLOCK_WIDTH*gameField.getWidth()+1,BLOCK_HEIGHT*gameField.getHeight()+1);
        for( int x=0;x<gameField.getWidth();x++){
            for(int y=0;y<gameField.getHeight();y++){
                renderBlock(g, x+1, y+1, gameField.get(x,y));
            }
        }
    }
    
    private void renderBlock(Graphics g, int x, int y, Color color){
        //if (!color.equals(empty)){
        g.setColor(color);
        //BufferedImage bim = new BufferedImage(4,4, BufferedImage.TYPE_INT_ARGB);
        //Graphics2D g2 = bim.createGraphics();
        //g2.setPaint(Color.red);
        //g2.drawLine(1,1, 3, 3);
        //Rectangle r = new Rectangle(0,0,bim.getWidth(),bim.getHeight());
        //TexturePaint tp = new TexturePaint(bim,r);
        //doubleBufferGraphics.setPaint(tp);
        g.fill3DRect(x*BLOCK_WIDTH,y*BLOCK_HEIGHT,BLOCK_WIDTH,BLOCK_HEIGHT,true);
        //}
    }
    
    private void renderNextBlock(Graphics g){
        for( int x=0;x<nextBlock.getWidth();x++){
            for(int y=0;y<nextBlock.getHeight();y++){
                if(nextBlock.getMatrix()[x][y]!=0){
                    renderBlock(g,(int)nextBlock.getPosition().getX()+x+1,(int)nextBlock.getPosition().getY()+y+1,nextBlock.getColor());
                }
            }
        }
    }
    
    private void renderCurentBlock(Graphics g){
        for( int x=0;x<curBlock.getWidth();x++){
            for(int y=0;y<curBlock.getHeight();y++){
                if(curBlock.getMatrix()[x][y]!=0){
                    renderBlock(g, (int)curBlock.getPosition().getX()+x+1,(int)curBlock.getPosition().getY()+y+1,curBlock.getColor());
                }
            }
        }
    }
    
    private void renderInfo(Graphics g, TetrisLogic logic){
        g.setColor(Color.DARK_GRAY);
        g.drawString("Level: " + logic.getLevel(),12*BLOCK_WIDTH,7*BLOCK_WIDTH);
        g.drawString("Lines: "+ logic.getLines(),12*BLOCK_WIDTH,8*BLOCK_WIDTH);
        g.drawString("Score: "+ logic.getScore() ,12*BLOCK_WIDTH,9*BLOCK_WIDTH);
        
        
        if(paused){
            g.setColor(Color.RED);
            g.drawString("PAUSED",12*BLOCK_WIDTH,10*BLOCK_WIDTH);
        }
        
        if(gameover){
            g.setColor(Color.RED);
            g.drawString("GAME OVER",12*BLOCK_WIDTH,11*BLOCK_WIDTH);
            g.drawString("Try again? (press y)",12*BLOCK_WIDTH,15*BLOCK_WIDTH);
        }
    }
    

    
    private void renderAll(Graphics g){
        clearField(g);
        renderField(g);
        renderCurentBlock(g);
        renderInfo(g, logic);
    }
    
    /**
     * Starts the game thread
     */
    public void start() {
        thread = new Thread(this);
        thread.start();
    }
    
    /**
     * Stops the game thread
     */
    public void stop() {
        // causes the thread to stop on the next loop
        thread = null;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        doubleBufferImage.flush();
        g.drawImage(doubleBufferImage,0,0,this);
    }
    
    /**
     *
     */
    public void clearLines(){
        boolean filled;
        int linesRemoved = 0;
        
        for( int y=0;y<gameField.getHeight();y++){
            filled=true;
            for(int x=0;x<gameField.getWidth();x++){
                if (gameField.isEmpty(x, y)){
                    filled = false;
                }
            }
            
            //line found
            if (filled){
                linesRemoved++;
                //move all lines above down
                for(int y2=y;y2>0;y2--){
                    for(int x=0;x<gameField.getWidth();x++){
                        gameField.set(x, y2, gameField.get(x, y2-1));
                    }
                }
                for(int x=0;x<gameField.getWidth();x++){
                    gameField.setEmpty(x, 0);
                }
            }
        }
        
        logic.linesRemoved(linesRemoved);
    }
    

    
    /**
     *
     * @param dirX
     * @param dirY
     * @return
     */
    public boolean checkOverlap(int dirX,int dirY){
        boolean overlap= false;
        int curBLOCK_WIDTH=curBlock.getMatrix().length;
        int curBLOCK_HEIGHT=curBlock.getMatrix()[0].length;
        
        int curX, curY;
        
        //check onderkant bij geroteerd komt hij er ander onder
        if(curBLOCK_HEIGHT+curBlock.getPosition().getY()<=gameField.getHeight()){
            for( int x=0;x<curBLOCK_WIDTH;x++){
                for(int y=0;y<curBLOCK_HEIGHT;y++){
                    if(curBlock.getMatrix()[x][y]!=0){
                        curX = (int)curBlock.getPosition().getX()+x+dirX;
                        curY = (int)curBlock.getPosition().getY()+y+dirY;
                        if(!gameField.isEmpty(curX, curY)){
                            overlap =true;
                        }
                    }
                }
            }
        } else {
            overlap=true;
        }
        return overlap;
    }
    
    
    /**
     *
     */
    public void checkMove(){
        
        Color curBlockCol = curBlock.getColor();
        boolean blocked = false;
        
        //check onder
        if(curBlock.getPosition().getY()+curBlock.getHeight() >= gameField.getHeight()){
            blocked = true;
        }
        
        //check veld
        if(!blocked)
            if (checkOverlap(0,1))
                blocked=true;
        
        int curX, curY;
        
        //vast
        if(blocked){
            for( int x=0;x<curBlock.getWidth();x++){
                for(int y=0;y<curBlock.getHeight();y++){
                    if(curBlock.getMatrix()[x][y]!=0){
                        curX = (int)curBlock.getPosition().getX()+x;
                        curY = (int)curBlock.getPosition().getY()+y;
                        gameField.set(curX, curY, curBlockCol);
                    }
                }
            }
            curBlock = nextBlock;
            curBlock.setPosition(new Point(3,0));
            
            //game over???
            if(checkOverlap(0,0)){
                gameover=true;
                
            } else{
                nextBlock = new Block();
                logic.blockDropped();
            }
            //moved here as a fix to slow removes
            clearLines();
        } else
            curBlock.getPosition().translate(0,1);
    }
    
    /**
     *
     */
    public void dropDown(){
        long oldscore = logic.getScore();
        while ((logic.getScore() == oldscore)&&(!gameover)){
            checkMove();
        }
    }
    
    
    public void keyPressed(java.awt.event.KeyEvent e) {
        switch(e.getKeyCode()){
        case KeyEvent.VK_RIGHT:
            rightdown=true;
            break;
        case KeyEvent.VK_LEFT:
            leftdown=true;
            break;
        case KeyEvent.VK_UP:
            if(!paused&&!gameover){
                updown=true;
            }
            break;
        case KeyEvent.VK_DOWN:
            downdown=true;
            break;
        case KeyEvent.VK_SPACE:
            if(!paused&&!gameover)
                spaced=true;
            break;
        case KeyEvent.VK_P:
            paused = !paused;
            break;
        case KeyEvent.VK_Y:
            if (gameover){
                newGame();
            }
            break;
        }
        //System.out.println("keyPressed "+e.getKeyCode());
    }//System.out.println("keyReleased "+e.getKeyCode());
    
    public void keyReleased(java.awt.event.KeyEvent e) {
        //System.out.println("keyReleased "+e.getKeyCode());
        switch(e.getKeyCode()){
        case KeyEvent.VK_RIGHT:
            rightdown=false;
            break;
        case KeyEvent.VK_LEFT:
            leftdown=false;
            break;
        case KeyEvent.VK_DOWN:
            downdown=false;
            break;
        }
    }
    
    public void keyTyped(java.awt.event.KeyEvent e) {
        
    }
}
