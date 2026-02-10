package org.kelvinizer;

import org.kelvinizer.animation.AnimatablePanel;
import org.kelvinizer.params.GeneralParams;
import org.kelvinizer.display.Display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App extends JFrame {
    private AnimatablePanel display;

    public App(){
        boot();
        ScheduledExecutorService gameLoop = Executors.newSingleThreadScheduledExecutor();
        gameLoop.scheduleAtFixedRate(this::runApp, 0, 1000/ GeneralParams.FPS, TimeUnit.MILLISECONDS);
        setVisible(true);
    }

    private void boot(){
        setTitle("Wifi Optimizer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(0, 0,
                GeneralParams.REF_WIN_W + GeneralParams.extraWidth,
                GeneralParams.REF_WIN_H + GeneralParams.extraHeight
        );
        display = new Display();
        add(display);
        addWindowListener(new WindowAdapter() {
            /**
             * Invoked when the window is in the process of being closed.
             * It saves user back, deletes temporary files, and exports user data before exiting.
             *
             * @param e the event to be processed
             */
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void runApp(){
        display.setBounds(0, 0,
                getSize().width - GeneralParams.extraWidth,
                getSize().height - GeneralParams.extraHeight
        );
        display.scale(new Dimension(
                getSize().width - GeneralParams.extraWidth,
                getSize().height - GeneralParams.extraHeight
        ));
    }
}
