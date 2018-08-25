package homo.efficio.scratchpad.java8.springbootcompletablefuture;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author homo.efficio@gmail.com
 * created on 2018-03-30
 */
public class ChainPartners {

    @Test
    public void numbuilder() {
        assertThat(buildNum(new int[]{0, 4, 2, 3, 7})).isEqualTo(4237);
    }

    private int buildNum(int[] digits) {
        int result = 0;
        for (int i = 0, len = digits.length ; i < len ; i++) {
            result = result * 10 + digits[i];
        }
        return result;
    }

    @Test
    public void rotationTest() {
        assertThat(isRotation(new int[]{3, 1, 2, 1, 5}, new int[]{2, 1, 5, 3, 1})).isTrue();
    }

    private boolean isRotation(int[] arr1, int[] arr2) {
        int len = arr1.length;
        if (len == 0) return true;

        for (int i = 0 ; i < len ; i++) {
            int matched = 0;
            for (int j = 0 ; j < len ; j++) {
                if (arr1[j] == arr2[(i + j) % len]) {
                    matched++;
                } else {
                    break;
                }
            }
            if (matched == arr1.length) return true;
        }
        return false;
    }
}
