package org.kelvinizer.animation;

import org.kelvinizer.misc.objects.Triple;
import org.kelvinizer.misc.interfaces.Drawable;
import org.kelvinizer.misc.interfaces.Scalable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.kelvinizer.params.GeneralParams.*;

/**
 * The {@code AnimatablePanel} class is an extension of {@link JPanel} that implements
 * {@link Animatable}, {@link MouseMotionListener}, {@link MouseWheelListener}, {@link MouseListener},
 * {@link Scalable}, and {@link Drawable}.
 *
 * <p>This class provides support for animations during the panel's appearance and disappearance,
 * along with custom key bindings and mouse event handling.</p>
 * @author Boyan Hu
 */
public class AnimatablePanel extends JPanel implements Animatable, MouseMotionListener, MouseWheelListener, MouseListener, Scalable, Drawable {

    private boolean is_start = true, is_end = false;
    private boolean has_start = false, has_end = false;
    protected long start_time, end_time;
    protected final long start_duration;
    protected long end_duration;
    private final HashMap<Triple<Integer, Boolean, Integer>, Action> bindings = new HashMap<>();
    protected Dimension panelSize = new Dimension(REF_WIN_W, REF_WIN_H);

    /**
     * Creates an {@code AnimatablePanel} with a specified start duration for the appearance animation.
     *
     * @param start_duration_in_ms the duration of the start animation in milliseconds
     */
    public AnimatablePanel(long start_duration_in_ms, int REF_WIN_W, int REF_WIN_H) {
        start_duration = (long) (start_duration_in_ms * 1e6);
        setSize(REF_WIN_W, REF_WIN_H);
        ScheduledExecutorService e = Executors.newSingleThreadScheduledExecutor();
        e.scheduleAtFixedRate(this::repaint, 0, 1000 / FPS, TimeUnit.MILLISECONDS);
    }

    /**
     * Paints the component and handles animations during appearance, disappearance, or steady state.
     *
     * @param g the {@code Graphics} object used for drawing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.scale((double) getWidth() / REF_WIN_W, (double) getHeight() / REF_WIN_H);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, REF_WIN_W, REF_WIN_H);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1.0f));

        if (is_start) {
            if (!has_start) {
                has_start = true;
                start_time = System.nanoTime();
            }
            if (System.nanoTime() - start_time <= start_duration) {
                onAppearance(g2d);
            } else {
                is_start = false;
                addListeners();
            }
        } else if (is_end) {
            if (!has_end) {
                has_end = true;
                end_time = System.nanoTime();
            }
            if (System.nanoTime() - end_time <= end_duration) {
                onDisappearance(g2d);
            } else {
                is_start = true;
                is_end = false;
                has_start = false;
                has_end = false;
                toNextPanel();
            }
        } else {
            render(g2d);
        }
    }

    /**
     * Adds mouse and key bindings listeners to the panel.
     */
    private void addListeners() {
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        if (!bindings.isEmpty()) {
            for (Map.Entry<Triple<Integer, Boolean, Integer>, Action> entry : bindings.entrySet()) {
                try {
                    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                            KeyStroke.getKeyStroke(
                                    entry.getKey().first,
                                    entry.getKey().third,
                                    entry.getKey().second
                            ),
                            entry.getKey()
                    );
                } catch (IllegalStateException e) {
                    throw new RuntimeException(e);
                }
                getActionMap().put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Triggers the panel's disappearance animation.
     *
     * @param end_duration_in_ms the duration of the disappearance animation in milliseconds
     */
    protected void exit(long end_duration_in_ms) {
        end_duration = (long) (end_duration_in_ms * 1e6);
        is_end = true;
        removeMouseListener(this);
        removeMouseMotionListener(this);
        removeMouseWheelListener(this);

        if (!bindings.isEmpty()) {
            for (Map.Entry<Triple<Integer, Boolean, Integer>, Action> entry : bindings.entrySet()) {
                try {
                    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).remove(
                            KeyStroke.getKeyStroke(
                                    entry.getKey().first,
                                    entry.getKey().third,
                                    entry.getKey().second
                            )
                    );
                } catch (IllegalStateException e) {
                    throw new RuntimeException(e);
                }
                getActionMap().remove(entry.getKey());
            }
        }
    }

    /**
     * Triggers the panel's disappearance animation with a default duration of 500 milliseconds.
     */
    protected void exit() {
        exit(500);
    }

    /**
     * Sets the opacity for the appearance animation.
     *
     * @param g2d the {@code Graphics2D} object used for drawing
     */
    protected void setAppearingOpacity(Graphics2D g2d) {
        setGlobalOpacity(g2d, 1.0 * (System.nanoTime() - start_time) / start_duration);
    }

    /**
     * Sets the opacity for the disappearance animation.
     *
     * @param g2d the {@code Graphics2D} object used for drawing
     */
    protected void setDisappearingOpacity(Graphics2D g2d) {
        setGlobalOpacity(g2d, 1.0 - 1.0 * (System.nanoTime() - end_time) / end_duration);
    }

    /**
     * Sets the global opacity for the panel.
     *
     * @param g2d   the {@code Graphics2D} object used for drawing
     * @param ratio the opacity ratio (0.0 to 1.0)
     */
    protected void setGlobalOpacity(Graphics2D g2d, double ratio) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.clamp((float) ratio, 0.0f, 1.0f)));
    }

    @Override
    public void render(Graphics2D g2d) {}

    /**
     * Adds a key binding to the panel.
     *
     * @param VK_Code   the key code of the key
     * @param onRelease true if the action should occur on key release, false for key press
     * @param a         the {@code Action} to be performed
     */
    public void addKeyBinding(int VK_Code, boolean onRelease, Action a) {
        addKeyBinding(VK_Code, onRelease, 0, a);
    }

    /**
     * Adds a key binding to the panel with a specified modifier key state.
     *
     * @param VK_Code   the key code of the key
     * @param onRelease true if the action should occur on key release, false for key press
     * @param VK_State  the modifier key state
     * @param a         the {@code Action} to be performed
     */
    public void addKeyBinding(int VK_Code, boolean onRelease, int VK_State, Action a) {
        bindings.put(new Triple<>(VK_Code, onRelease, VK_State), a);
    }

    /**
     * Adds a key binding to the panel that triggers on key release.
     *
     * @param VK_Code the key code of the key
     * @param a       the {@code Action} to be performed
     */
    public void addKeyBinding(int VK_Code, Action a) {
        addKeyBinding(VK_Code, true, a);
    }

    @Override
    public void onAppearance(Graphics2D g2d) {
        setAppearingOpacity(g2d);
        render(g2d);
    }

    @Override
    public void onDisappearance(Graphics2D g2d) {
        setDisappearingOpacity(g2d);
        render(g2d);
    }

    /**
     * Transitions to the next panel. Can be overridden by subclasses.
     */
    protected void toNextPanel() {}

    @Override
    public void scale(Dimension d) {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseClicked(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void mousePressed(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseReleased(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseEntered(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseExited(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseDragged(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseMoved(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {}
}
