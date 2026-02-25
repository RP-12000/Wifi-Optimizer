package org.kelvinizer.settings;

import org.kelvinizer.animation.AnimatablePanel;
import org.kelvinizer.params.GeneralParams;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class SettingsPage extends AnimatablePanel {
    private final SettingsPageButtons buttons = new SettingsPageButtons();

    private void bindNumbers(int num){
        addKeyBinding(0x30 + num, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttons.air.updateText((char) (num + '0'));
            }
        });
    }

    public SettingsPage() {
        super(1000, GeneralParams.REF_WIN_W, GeneralParams.REF_WIN_H);
        for(int i=0; i<=9; i++){
            bindNumbers(i);
        }
        addKeyBinding(KeyEvent.VK_BACK_SPACE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttons.air.updateText('\b');
            }
        });
        addKeyBinding(KeyEvent.VK_PERIOD, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttons.air.updateText('.');
            }
        });
    }

    @Override
    public void render(Graphics2D g2d){
        buttons.render(g2d);
    }

    @Override
    public void scale(Dimension d){
        buttons.scale(d);
    }

    @Override
    public void mouseMoved(MouseEvent e){
        buttons.setFocused(e);
    }

    @Override
    public void mouseClicked(MouseEvent e){
        if(buttons.back.isFocused()){
            exit(1000);
        }
        else if(buttons.air.isFocused()){
            buttons.air.select();
        }
        else{
            buttons.air.select(false);
        }
    }

    @Override
    public void toNextPanel(){
        buttons.air.select(false);
        GeneralParams.panelIndex = 0;
    }
}
