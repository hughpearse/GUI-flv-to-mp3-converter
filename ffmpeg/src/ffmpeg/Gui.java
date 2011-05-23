/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ffmpeg;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author goonmaster
 */
public class Gui extends JFrame {
    Container subRoot  = new JPanel();
    Container panel1  = new JPanel();
    Container panel2  = new JPanel();
    Container panel3  = new JPanel();
    JTextArea text1 = new JTextArea("Hello");
    JFileChooser fc = new JFileChooser();
    JFileChooser fc2 = new JFileChooser();
    MyHandler hand1 = new MyHandler();
    JButton b1 = new JButton("Select Source Folder");
    JButton b2 = new JButton("Select Destination Folder");
    JButton b3 = new JButton("Convert");

    public Gui(){
        super("Stall Holder Inc");
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setSize( 600, 500 );
        setContentPane(paneSetup());
        setVisible(true);
    }

    Container paneSetup(){
        setupPane1();
        setupPane2();
        setupPane3();
        subRoot.add(panel1);
        return subRoot;
    }

    void setupPane1(){
        b1.addActionListener(hand1);
        b2.addActionListener(hand1);
        b3.addActionListener(hand1);
        panel1.add(b1);
        panel1.add(b2);
        panel1.add(b3);
    }

    void setupPane2(){
        //panel2.add(fc);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.addActionListener(hand1);
    }

    void setupPane3(){
        fc2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc2.addActionListener(hand1);
    }

    public class MyHandler implements ActionListener{
        String folderName="";
        String destName="";
        File folder;
        File[] listOfFiles;

        public void actionPerformed(ActionEvent e) {


            if (e.getSource() == b1){
                //subRoot.add(panel2);
                int returnVal = fc.showOpenDialog(panel2);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    folderName=fc.getSelectedFile().getAbsolutePath();
                    folderName+="/";
                    System.out.println("Listing FLV files in: " + folderName);
                    for(int i=0; i!=fileCount(); i++)
                        System.out.println(getFileName(i));
                }
            }
            if (e.getSource() == b2){
                //subRoot.add(panel2);
                int returnVal = fc2.showOpenDialog(panel3);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    destName=fc2.getSelectedFile().getAbsolutePath();
                    destName+="/";
                    System.out.println("Saving Files to: " + destName);
                }
            }
            if (e.getSource() == b3){
                subRoot.removeAll();
                subRoot.add(panel3);
                subRoot.validate();
                subRoot.repaint();
                convert();
            }
        }

        public String getFileName(int index){
            String fileName = null;
            ArrayList fileArray = new ArrayList();
            folder = new File(folderName);
            listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    if(listOfFiles[i].getName().endsWith(".flv"))
                        fileArray.add(listOfFiles[i].getName());
                }
            }
            fileName=fileArray.get(index).toString();
            return fileName;
        }

        public int fileCount(){
            folder = new File(folderName);
            listOfFiles = folder.listFiles();
            int count=0;
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    if(listOfFiles[i].getName().endsWith(".flv"))
                        count++;
                }
            }
            return count;
        }

        public void convert(){
            String s = null;
            int i=0;
            String newName="";

            folder = new File(folderName);
            listOfFiles = folder.listFiles();
            
            try {
                System.out.println("Currently in: " + where());
                System.out.println("Converting files in: " + folderName + " to folder " + destName);
                for(i=0; i != fileCount(); i++ ){
                    newName=getFileName(i).replaceAll(".flv", ".mp3");
                    System.out.println("Processing: " + i + " " + getFileName(i) + " into " + newName);
                    Runtime.getRuntime().exec("wine " + where() + "ffmpeg.exe -i  " + folderName + getFileName(i) + " -f mp3  " + destName + newName);
                }
                System.exit(0);
            }
            catch (IOException e) {
                System.out.println("exception happened - here's what I know: ");
                e.printStackTrace();
                System.exit(-1);
            }

        }//end method convert

        public String where(){
            File directory = new File (".");
            String appDir="";
            try {
                appDir=directory.getCanonicalPath();
                appDir+="/";
            }catch(Exception e) {
                System.out.println("Exception is ="+e.getMessage());
            }
            return appDir;
        }//end method where
    }
}
