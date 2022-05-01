/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package courseroom_analyzer;

import com.formdev.flatlaf.FlatDarkLaf;
import frames.CourseRoom_Analyzer_Frame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author LENOVO
 */
public class CourseRoom_Analyzer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            FlatDarkLaf ui = new FlatDarkLaf();
            UIManager.setLookAndFeel(ui);
            CourseRoom_Analyzer_Frame principal_Frame = new CourseRoom_Analyzer_Frame();
            principal_Frame.setVisible(true);
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
}
