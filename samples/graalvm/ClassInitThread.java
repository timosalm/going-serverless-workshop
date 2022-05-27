import java.nio.charset.*;

public class ClassInitThread {
    public static void main(String[] args) {
        First.second.printIt();
    }
}

class First {
    public static Second second = new Second();

    public static Thread t;

    static {
        t = new Thread(()-> {
            try {
                System.out.println("Sleep for 10s...");
                Thread.sleep(10_000);
                System.out.println("Done...");
            } catch (Exception e){}
        });
        t.start();
    } 
}

class Second {
    private static final Charset UTF_32_LE = Charset.forName("UTF-32LE");

    public void printIt() {
        System.out.println("Unicode 32 bit CharSet: " + UTF_32_LE);
    }
}