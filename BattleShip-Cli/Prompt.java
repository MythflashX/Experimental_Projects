import java.util.Scanner;
public class Prompt
{
  public int[] kill ()
  {
    int pos[] = new int[] {6, 6};
    Scanner scanner = new Scanner(System.in);

    while ((pos[0] > 5 || pos[0] < 0) || (pos[1] > 5 || pos[0] < 0)) {
      System.out.print("ATTACK >> ");
      String cell = scanner.nextLine();

      pos[0] = (int) ((cell.toCharArray())[0] - 64);  // convert the string to array of two character and convert them to integers 
      pos[1] = (int) ((cell.toCharArray())[1] - 48);  // 'A' = 64 in ascii and '0' = 48 in ascii
    }
    //System.out.println(pos[0] + " " + pos[1]);

    return pos;
  }
}
