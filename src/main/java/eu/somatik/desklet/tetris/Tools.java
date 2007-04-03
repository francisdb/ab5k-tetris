/*
 * Tools.java
 *
 * Created on November 12, 2006, 8:37 PM
 */

package eu.somatik.desklet.tetris;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;

/**
 *
 * @author francisdb
 */
public final class Tools {
    
    /** Private constructor, utility class */
    private Tools() {
        super();
    }
    
    
    
    /**
     * 
     * @param userId 
     * @param score 
     */
    public static void submitScore(int userId, long score){
        
        //nickname from param
        //String urlName = java.net.URLEncoder.encode("Somatik");
        
        // Encrypt username and password
        String md5 = String.valueOf(userId + score);// "username:password";
        //System.out.println("APPLET MD5 string='"+md5+"'");
        byte[] raw = md5.getBytes();  // take care to use the right encoding
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            raw = md.digest(raw);
        } catch (Exception e){
            e.printStackTrace();
            //no md5?????
        }
        
        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<raw.length;i++) {
            if(Integer.toHexString(0xFF & raw[i]).length() ==1) {
                hexString.append("0");
                hexString.append(Integer.toHexString(0xFF & raw[i]));
            } else
                hexString.append(Integer.toHexString(0xFF & raw[i]));
            
        }
        String code = hexString.toString();
        //System.out.println(code);
        
        
        try{
            URL url = new URL("http://www.somatik.be/tetris.php?user_id=" + userId + "&score="+score+"&code="+code);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            while (in.ready()){
                System.out.println(in.readLine());
            }
            in.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    
}
