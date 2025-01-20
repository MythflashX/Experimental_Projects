public class Grid
{
  private char[][] map;

  public void update_ship (ConstructShip ship)   // update the map for entire ship
  {
    int cells;
    if (ship.get_type() == 'd')
      cells = 2;
    else
      cells = 3;

    for (int i = 0; i < cells; i++)
    {
      int x = ship.get_part(i).get_coordinates()[0];
      int y = ship.get_part(i).get_coordinates()[1];
      
      //System.out.println(x + " " + y);
      char[] piece = ship.get_part(i).pieces_pos();
      int index = 0;
      
      for (int row = y; row < (y + 2); row++) {
        for (int col = x; col < (x + 3); col++) {
          map[row][col] = piece[index++];
          //System.out.println(row + " " + col + " " + piece[index -  1]);
        }
      }
    }
  }
  
  public int[] grid_sync (int[] pos)
  {
    //System.out.println("SYNCED");
    int cell_row = 4;
    int cell_col = 3;
    int x_len = pos[0];
    int y_len = pos[1];
    pos[0] = 1; 
    pos[1] = 1;
    
    for (int i = 1; i < x_len; i++) {
      pos[0] += cell_row;
    }
    for (int i = 1; i < y_len; i++) {
      pos[1] += cell_col;
    }

    return pos;
  }
  
  public void update_hits (ShipPiece cell)   // edit cell by cell for each part
  {
    //grid_sync(cell);
    int x = cell.get_coordinates()[0];
    int y = cell.get_coordinates()[1];
    
    //System.out.println(x + " " + y);
    
    int index = 0;
    
    for (int row = y; row < (y + 2); row++) {
      for (int col = x; col < (x + 3); col++) {
        map[row][col] = cell.pieces_pos()[index++];
        //System.out.println(row + " " + col + " " + piece[index -  1]);
      }
    }
  }
  
  public void update_miss (int[] cell)   // edit cell by cell for each part
  {
    //grid_sync(cell);
    int x = cell[0];
    int y = cell[1];
    
    //System.out.println(x + " " + y);
    char[] miss = new char[] {' ', 'X', ' ', ' ', 'X', ' '};
    int index = 0;
    
    for (int row = y; row < (y + 2); row++) {
      for (int col = x; col < (x + 3); col++) {
        map[row][col] = miss[index++];
        //System.out.println(row + " " + col + " " + piece[index -  1]);
      }
    }
  }
  
  public void display ()
  {
    int pos_y = 0;
    char pos_x = 'A';
    int row = 16;
    int col = 22;
    
    System.out.print("\t       ");
    for (int i = 0; i < 5; i++ ) {          // Index ABCDE X-axis
      System.out.print("   " + pos_x++);
    }
    
    System.out.println("\n");
    
    for (int r = 0; r < row; r++) {
      if ((r % 3) == 0 && (r != 15))        // Index 12345 Y-axis
        System.out.print( "\t" + ++pos_y + "\t");
      else
        System.out.print("\t\t");
      
      for (int c = 0; c < col; c++) {   // draw the grid data
        System.out.print(map[r][c]);
      }
    }
  }

  public void generate ()
  {
    int rows = 16;
    int cols = 22;
    map = new char[rows][cols];

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        if ((c % 4) == 0)
          map[r][c] = '|';
        else {
          if ((r % 3) == 0 && (c % 4) != 0)
            map[r][c] = '-';
          else
            map[r][c] = ' ';
        }
      }
      map[r][cols - 2] = '|';
      map[r][cols - 1] = '\n';
    }
  }
  
}
