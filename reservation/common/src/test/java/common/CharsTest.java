package common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CharsTest {

    @Test
    void returns_letter_from_int() {
        char letter = Chars.toChar(1);

        assertThat(letter).isEqualTo('A');
    }
}