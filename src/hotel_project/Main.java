/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel_project;


import edu.sit.cs.db.CSDbDelegate;
import hotel_project.view.MainFrame;

/**
 *
 * @author Chayut
 */
public class Main {

    public static void main(String[] args) {
//        MainMenu f1 = new MainMenu();
//        f1.setVisible(true);

        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);


//        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("Exit");
//            }
//        }, "Shutdown-thread"));
    }


}
