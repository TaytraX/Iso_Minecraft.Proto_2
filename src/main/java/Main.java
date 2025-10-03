import window.Window;

public class Main {
    Window windo = new Window("Test", 800, 600, true);

    public void run() {
        windo.create();
        while(!windo.shouldClose()) {
            windo.update();
        }
        windo.cleanup();
    }

    public static void main(String[] args) {
        Main engine = new Main();
        engine.run();
    }
}