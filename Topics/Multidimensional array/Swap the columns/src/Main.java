import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        int[][] array = new int[x][y];

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                array[i][j] = scanner.nextInt();
            }
        }
        int m = scanner.nextInt();
        int n = scanner.nextInt();
        swapArray(m, n, array);

        for (int[] ints : array) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }

    public static void swapArray(int col, int col2, int[][] array) {
        for (int i = 0; i < array.length; i++) {
            int temp = array[i][col];
            array[i][col] = array[i][col2];
            array[i][col2] = temp;
        }
    }
}
