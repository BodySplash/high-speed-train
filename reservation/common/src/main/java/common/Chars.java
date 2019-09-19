package common;

public final class Chars {

    private Chars() {
    }

    public static char toChar(int number) {
        return (char)(number+64);
    }
}
