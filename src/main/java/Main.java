import javax.swing.*;
class Main  {
    public Main() {
        Application application = new Application();
    }

    public static void main(String[] args) {
        SwingUtilities. invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        } ) ;
    }
}
