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
        int rowOfMaximumIndex = 0;
        int columnOfMaximumIndex = 0;
        int maximumNumber = array[0][0];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] > maximumNumber) {
                    maximumNumber = array[i][j];
                    rowOfMaximumIndex = i;
                    columnOfMaximumIndex = j;
                }
            }
        }
        System.out.println(rowOfMaximumIndex + " " + columnOfMaximumIndex);
    }
}