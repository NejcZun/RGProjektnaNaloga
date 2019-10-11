package renderTest;

import org.lwjgl.opengl.Display;
import renderEngine.DisplayManager;

public class Main {
    public static void main(String[] args){
        DisplayManager.createDisplay();

        while(!Display.isCloseRequested()){
            DisplayManager.updateDisplay();
        }
        DisplayManager.closeDisplay();
    }
}
