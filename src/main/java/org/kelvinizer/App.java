package org.kelvinizer;

import org.kelvinizer.animation.AnimatablePanel;
import org.kelvinizer.params.GeneralParams;
import org.kelvinizer.display.Display;
import org.kelvinizer.settings.SettingsPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.kelvinizer.params.GeneralParams.REF_WIN_H;
import static org.kelvinizer.params.GeneralParams.REF_WIN_W;

public class App extends JFrame {
    private AnimatablePanel display;

    private int lastPanel = 0;
    public static final Dimension panelSize = new Dimension(REF_WIN_W, REF_WIN_H);

    public App(){
        boot();
        ScheduledExecutorService gameLoop = Executors.newSingleThreadScheduledExecutor();
        gameLoop.scheduleAtFixedRate(this::runApp, 0, 1000/ GeneralParams.FPS, TimeUnit.MILLISECONDS);
        setVisible(true);
    }

    private void boot(){
        setTitle("Wi-Scope");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(0, 0,
                REF_WIN_W + GeneralParams.extraWidth,
                REF_WIN_H + GeneralParams.extraHeight
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
        panelSize.width = getSize().width - GeneralParams.extraWidth;
        panelSize.height = getSize().height - GeneralParams.extraHeight;
        if(lastPanel != GeneralParams.panelIndex) {
            remove(display);
            display = null; // Java Garbage Collector
            if(GeneralParams.panelIndex == 0){
                display = new Display();
            }
            else {
                display = new SettingsPage();
            }
            add(display);
            revalidate();
            lastPanel = GeneralParams.panelIndex;
        }
        display.setBounds(0, 0, panelSize.width, panelSize.height);
        display.scale(panelSize);
    }
}
