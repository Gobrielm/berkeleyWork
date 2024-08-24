import org.junit.jupiter.api.*;

import java.util.Comparator;
import deque.MaxArrayDeque61B;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class MaxArrayDeque61BTest {
    private static class StringLengthComparator implements Comparator<String> {
        public int compare(String a, String b) {
            return a.length() - b.length();
        }
    }

    @Test
    public void basicTest() {
        MaxArrayDeque61B<String> mad = new MaxArrayDeque61B<>(new StringLengthComparator());
        mad.addFirst("");
        mad.addFirst("2");
        mad.addFirst("fury road");
        assertThat(mad.max()).isEqualTo("fury road");
    }

    @Test
    public void test1() {
        MaxArrayDeque61B<Integer> m = new MaxArrayDeque61B<Integer>(Comparator.naturalOrder());
        m.addFirst(1);
        m.addFirst(-1);
        m.addFirst(15);
        m.addFirst(-100);
        assertThat(m.max()).isEqualTo(15);
        assertThat(m.max(Comparator.naturalOrder())).isEqualTo(15);
        assertThat(m.max(Comparator.reverseOrder())).isEqualTo(-100);
    }

    @Test
    public void test2() {
        MaxArrayDeque61B<Character> m = new MaxArrayDeque61B<Character>(Comparator.naturalOrder());
        m.addFirst('a');
        m.addFirst('b');
        m.addFirst('g');
        m.addFirst('y');
        m.addFirst('z');
        assertThat(m.max()).isEqualTo('z');
        assertThat(m.max(Comparator.naturalOrder())).isEqualTo('z');
        assertThat(m.max(Comparator.reverseOrder())).isEqualTo('a');
    }

    @Test
    public void test3() {
        MaxArrayDeque61B<Double> m = new MaxArrayDeque61B<Double>(Comparator.naturalOrder());
        m.addFirst(0.1);
        m.addFirst(0.0);
        m.addFirst(0.0);
        m.addFirst(0.0);
        m.addFirst(0.0);
        assertThat(m.max(Comparator.naturalOrder())).isEqualTo(0.1);
        assertThat(m.max(Comparator.reverseOrder())).isEqualTo(0);

    }
}
