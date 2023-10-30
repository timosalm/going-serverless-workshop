import java.nio.charset.*;

public class ClassInit {
    public static void main(String[] args) {
        First.second.printIt();
    }
}

class First {
    public static Second second = new Second();
}

class Second {
    private static final Charset UTF_32_LE = Charset.forName("UTF-32LE");

    public void printIt() {
        System.out.println("Unicode 32 bit CharSet: " + UTF_32_LE);
    }
}