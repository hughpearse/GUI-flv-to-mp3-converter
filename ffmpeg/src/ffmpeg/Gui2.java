/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ffmpeg;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;

/**
 *
 * @author goonmaster
 */
public class Gui2 extends JFrame {
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
    String folderName="";
    String destName="";
    File folder;
    File[] listOfFiles;
    String[] listOfFiles2=null;
    JTextArea notice = new JTextArea("");

    public Gui2(){
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
        panel1.add(notice);
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

        public void actionPerformed(ActionEvent e) {


            if (e.getSource() == b1){
                //subRoot.add(panel2);
                int returnVal = fc.showOpenDialog(panel2);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    folderName=fc.getSelectedFile().getAbsolutePath();
                    folderName+="/";
                    System.out.println("Listing FLV files in: " + folderName);
                    getFileNames();
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

        public void getFileNames(){
            int x=0;
            folder = new File(folderName);
            listOfFiles = folder.listFiles();
            String video="";
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    video=listOfFiles[i].getName();
                    if(video.endsWith(".flv")){
                        listOfFiles2[x]=listOfFiles[i].getName();
                    }
                }
            }
        }

        public void convert(){
            String s = null;
            int i=0;
            String newName="";
            try {

                // run the Unix "ps -ef" command
                // using the Runtime exec method:
                Process p = Runtime.getRuntime().exec("date");
                for(i=0; i < listOfFiles2.length ; i++ ){
                    newName=listOfFiles[i].getName().replaceAll(".flv", ".mp3");
                    Runtime.getRuntime().exec("wine " + where() + "ffmpeg.exe -i  " + folderName + listOfFiles[i].getName() + " -f mp3  " + destName + newName);
                    subRoot.removeAll();
                    notice.append("Converting " + listOfFiles[i].getName());
                    subRoot.add(panel1);
                    subRoot.validate();
                    subRoot.repaint();
                }

                BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

                // read the output from the command
                System.out.println("Here is the standard output of the command:\n");
                while ((s = stdInput.readLine()) != null) {
                    System.out.println(s);
                }
                // read any errors from the attempted command
                System.out.println("Here is the standard error of the command (if any):\n");
                while ((s = stdError.readLine()) != null) {
                    System.out.println(s);
                }
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