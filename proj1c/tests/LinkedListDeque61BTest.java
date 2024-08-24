import org.junit.jupiter.api.*;

import java.util.Comparator;
import deque.Deque61B;
import deque.LinkedListDeque61B;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;


public class LinkedListDeque61BTest {
    @Test
    public void test1() {
        Deque61B<Integer> list = new LinkedListDeque61B<>();
        list.addFirst(1);
        assertThat(list.toString()).isEqualTo("[1]");
        list.addFirst(2);
        list.addFirst(3);
        list.addFirst(4);
        assertThat(list.toString()).isEqualTo("[4, 3, 2, 1]");
        for (int x: list) {
            System.out.print(x);
        }
    }
}
