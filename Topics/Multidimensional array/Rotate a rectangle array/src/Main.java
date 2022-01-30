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
        int[][] newArray = new int[y][x];

        for (int i = 0; i < y; i++) {
            int k = newArray[i].length;
            for (int j = 0; j < x; j++) {
                newArray[i][j] = array[k - 1][i];
                k--;
            }
        }

        for (int[] ints : newArray) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }
}
