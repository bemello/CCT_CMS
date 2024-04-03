/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cct_cms.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bemello
 */
public class FileUtils {
    
    public void generateCSV( 
            String[] header, ArrayList<String> values, String filename ) {        
        try {
            File file = new File( filename + ".csv" );
            FileWriter fw = new FileWriter( file );
            BufferedWriter bw = new BufferedWriter( fw );
            
            String tempHeader = "";
            for ( int i = 0; i < header.length; i++ ) {
                tempHeader += header[i];
                if ( i < header.length - 1 ) {
                    tempHeader += ",";
                }
            }
            bw.write( tempHeader );
            bw.newLine();
            
            for ( Iterator<String> iterator = values.iterator(); iterator.hasNext(); ) {
                String next = iterator.next();
                bw.write(next);
                bw.newLine();
            }
            
            bw.close();
            System.out.println( "File " + filename + ".csv criado com sucesso!" );
        } catch ( IOException ex ) {
            Logger.getLogger( 
                    FileUtils.class.getName() ).log( 
                            Level.SEVERE, null, ex );
        }
        
    }
    
    public void generateTXT( 
            String[] header, ArrayList<String[]> values, 
            String formatter, String filename, boolean consoleOnly ) {
        
        StringBuilder sb = new StringBuilder();

        sb.append( String.format( formatter.concat( "%n" ), header ) );

        char[] charArray = new char[sb.length()];
        Arrays.fill(charArray, '=');
        sb.append( new String( charArray ) );

        for ( String[] value : values ) {
            sb.append( String.format( "%n".concat( formatter ), value ) );
        }
        
        if ( !consoleOnly ) {
            try {
                File file = new File( filename + ".txt" );
                FileWriter fw = new FileWriter( file );
                BufferedWriter bw = new BufferedWriter( fw );
                
                bw.write( sb.toString() );
                bw.close();
                System.out.println( "File " + filename + ".txt criado com sucesso!" );
            } catch ( IOException ex ) {
                Logger.getLogger( 
                        FileUtils.class.getName() ).log( 
                                Level.SEVERE, null, ex );
            }
        } else {
            System.out.println(sb.toString());
        }
    }
    
}
