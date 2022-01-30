import java.util.stream.IntStream;

class ArrayOperations {
    public static void reverseElements(int[][] twoDimArray) {

        IntStream.range(0, twoDimArray.length)
                .forEach(i -> {
                    twoDimArray[i] = reverse(twoDimArray[i], twoDimArray[i].length);
                });
    }

    static int[] reverse(int[] a, int n) {
        int[] b = new int[n];
        int j = n;
        for (int i = 0; i < n; i++) {
            b[j - 1] = a[i];
            j--;
        }
        return b;
    }
}
