import org.junit.jupiter.api.*;

import java.util.Comparator;
import deque.Deque61B;
import deque.ArrayDeque61B;
import deque.LinkedListDeque61B;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;


public class ArrayDeque61BTest {
    @Test
    public void test1() {
        Deque61B<Integer> list = new ArrayDeque61B<>();
        list.addFirst(1);
        assertThat(list.toString()).isEqualTo("[1]");
        list.addFirst(2);
        list.addFirst(3);
        list.addFirst(4);
        assertThat(list.toString()).isEqualTo("[4, 3, 2, 1]");
        for (int x: list) {
            System.out.print(x);
        }
        Deque61B<Integer> list1 = new LinkedListDeque61B<>();
        list1.addFirst(1);
        assertThat(list1.toString()).isEqualTo("[1]");
        list1.addFirst(2);
        list1.addFirst(3);
        list1.addFirst(4);
        assertThat(list1.toString()).isEqualTo("[4, 3, 2, 1]");
        assertThat(list.toString()).isEqualTo("[4, 3, 2, 1]");
        assertThat(list1.equals(list)).isTrue();
        assertThat(list.equals(list1)).isTrue();
        assertThat(list1.equals(list1)).isTrue();
        Deque61B<Integer> list2 = new LinkedListDeque61B<>();
        assertThat(list2.equals(list1)).isFalse();
        assertThat(list2.equals(list)).isFalse();
    }
    @Test
    public void test2() {
        Deque61B<Integer> list = new ArrayDeque61B<>();
        Deque61B<Integer> list1 = new ArrayDeque61B<>();
        list1.addFirst(1);
        list1.addFirst(2);
        list1.addFirst(3);
        list1.addFirst(4);
        list.addFirst(1);
        list.addFirst(2);
        list.addFirst(3);
        list.addFirst(4);
        assertThat(list.equals(list1)).isTrue();
    }

    @Test
    public void test3() {
        Deque61B<Integer> list1 = new ArrayDeque61B<>();
        list1.addFirst(1);
        list1.addFirst(2);
        list1.addFirst(3);
        Integer list [] = new Integer[]{3,2,1};
        assertThat(list1.equals(list)).isFalse();
        assertThat(list1.equals(list1)).isTrue();
    }
    @Test
    public void test4() {
        Deque61B<Integer> list1 = new ArrayDeque61B<>();
        list1.addFirst(0);
        list1.addFirst(0);
        list1.addFirst(0);
        list1.addFirst(0);
        list1.addLast(0);
        list1.addLast(0);
        list1.addLast(1);
        assertThat(list1.get(6)).isEqualTo(1);
    }

    @Test
    public void test5() {
        Deque61B<String> list1 = new ArrayDeque61B<>();
        Deque61B<String> list2 = new LinkedListDeque61B<>();
        assertThat(list1.equals(list2)).isTrue();
        assertThat(list2.equals(list1)).isTrue();

        for (int i = 0; i < 100; i++) {
            list1.addLast("ik");
            list1.addFirst("i");
            list2.addLast("ik");
            list2.addFirst("i");
        }
        for (int i = 0; i < 30; i++) {
            list1.removeLast();
            list1.removeFirst();
            list2.removeLast();
            list2.removeFirst();
        }
        assertThat(list1.equals(list2)).isTrue();
        assertThat(list2.equals(list1)).isTrue();
    }
    @Test
    public void test6() {
        Deque61B<Integer> list1 = new ArrayDeque61B<>();
        Deque61B<Integer> list2 = new LinkedListDeque61B<>();
        assertThat(list1.equals(list2)).isTrue();
        assertThat(list2.equals(list1)).isTrue();
        list1.addFirst(1);
        assertThat(list1.equals(list2)).isFalse();
        assertThat(list2.equals(list1)).isFalse();
        list1.removeLast();
        list2.addFirst(1);
        assertThat(list1.equals(list2)).isFalse();
        assertThat(list2.equals(list1)).isFalse();
    }

    @Test
    public void test7() {
        Deque61B<Integer> list1 = new ArrayDeque61B<>();
        Deque61B<Integer> list2 = new ArrayDeque61B<>();
        list1.addFirst(1);
        assertThat(list1.equals(list2)).isFalse();
        assertThat(list2.equals(list1)).isFalse();
        list1.removeLast();
        assertThat(list1.equals(list2)).isTrue();
        assertThat(list2.equals(list1)).isTrue();
    }

    @Test
    public void test8() {
        Deque61B<Character> list1 = new ArrayDeque61B<>();
        Deque61B<Character> list2 = new ArrayDeque61B<>();
        list1.addFirst('a');
        list2.addFirst('A');
        assertThat(list1.equals(list2)).isFalse();
        assertThat(list2.equals(list1)).isFalse();
        list1.removeLast();
        list2.removeLast();
        assertThat(list1.equals(list2)).isTrue();
        assertThat(list2.equals(list1)).isTrue();
    }
}
