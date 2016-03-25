/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel_project;


import edu.sit.cs.db.CSDbDelegate;

/**
 *
 * @author Chayut
 */
public class Main {

    public static void main(String[] args) {
        MainMenu f1 = new MainMenu();
        f1.setVisible(true);
        //testZone();
    }

    public static void testZone() {
        CSDbDelegate db = new CSDbDelegate("url", "port", "dbname", "user", "pw");
        db.connect();
        for (int i = 1; i < 8; i++) {
            String sql = "INSERT INTO Hotel_room(room_number, room_type, room_cost, room_person) VALUES (21" + i  + ", 'GRAND LUXURY ROOM', 3000, 2)";
            db.executeQuery(sql);
        }
    }
}
