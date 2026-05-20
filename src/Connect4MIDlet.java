import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class Connect4MIDlet extends MIDlet {
    private Connect4Canvas gameCanvas;
    private Display display;
    
    protected void destroyApp(boolean unconditional) {}
    
    protected void pauseApp() {}

    protected void startApp() {
        if (display == null) {
            gameCanvas = new Connect4Canvas();
        }
        
        display = Display.getDisplay(this);
        display.setCurrent(gameCanvas);
    }

}
