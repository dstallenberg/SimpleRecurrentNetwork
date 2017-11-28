package com.dimitri.nffnn.example;

        import java.awt.event.KeyEvent;
        import java.awt.event.KeyListener;
        import java.awt.event.MouseEvent;
        import java.awt.event.MouseListener;
        import java.awt.event.MouseMotionListener;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {
    private static boolean[] keys = new boolean[256];
    private static boolean[] keysLast = new boolean[256];
    private static boolean[] buttons = new boolean[5];
    private static boolean[] buttonsLast = new boolean[5];
    private static int mouseX;
    private static int mouseY;
    private static char key;

    public InputHandler() {
    }

    public static void update() {
        keysLast = (boolean[])keys.clone();
        buttonsLast = (boolean[])buttons.clone();
    }

    public static boolean isKey(int keyCode) {
        return keys[keyCode];
    }

    public static boolean isKeyPressed(int keyCode) {
        return keys[keyCode] && !keysLast[keyCode];
    }

    public static boolean isKeyReleased(int keyCode) {
        return !keys[keyCode] && keysLast[keyCode];
    }

    public static boolean isButton(int button) {
        return buttons[button];
    }

    public static boolean isButtonPressed(int button) {
        return buttons[button] && !buttonsLast[button];
    }

    public static boolean isButtonReleased(int button) {
        return !buttons[button] && buttonsLast[button];
    }

    public static int getMouseX() {
        return mouseX;
    }

    public static int getMouseY() {
        return mouseY;
    }

    public static char getKey() {
        return key;
    }

    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX() / 3;
        mouseY = e.getY() / 3;
    }

    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX() / 3;
        mouseY = e.getY() / 3;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        buttons[e.getButton()] = true;
    }

    public void mouseReleased(MouseEvent e) {
        buttons[e.getButton()] = false;
    }

    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    public void keyTyped(KeyEvent e) {
    }
}
