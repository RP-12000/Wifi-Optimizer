package org.kelvinizer;

import org.kelvinizer.params.GeneralParams;

import javax.swing.*;

/**
 * Main class of the program
 * just calls the creating of a new App
 * @author Boyan Hu
 */
public class Main {
    public static void main(String[] args) {
        GeneralParams.init(60,1080,720);
        SwingUtilities.invokeLater(App::new);
    }
}