import java.util.ArrayList;
public class ConstructShip
{
  private char type;
  private char orientation;
  private ShipPiece[] ship;
  private char[][] horizontal = new char [][] {
            { '|', '\\', '_', '|', 'X', '_' },   // horizontal back shippiece      ____
            { '[', ']', '_', '_', 'X', '_' },   // horizontal midd shippiece      |   \
            { '_', '_', 'o', 'X','_', '/' },   // horizontal front shippiece     |___/
          };

  private char[][] vertical = new char[][] {
            { ' ', '_', ' ', '|', 'X', '|' },   // vertical back shippiece  v
            { '|', 'O', '|', '|', 'X', '|' },   // vertical midd shippiece  | |   
            { '|', 'X', '|', ' ', 'v', ' ' }     // vertical front shippiece  ___
          };

  public char get_type ()
  {
    return type;
  }

  public char get_orientation ()
  {
    return orientation;
  }

  public ShipPiece get_part (int index)
  {
    return ship[index];
  }

  public int search_part(int[] pos)
  {
    int length = 0;
    if (type == 'd') length = 2;
    else length = 3;
    
    for (int i = 0; i < length; i++) {
      if (pos[0] == ship[i].get_coordinates()[0] && pos[1] == ship[i].get_coordinates()[1]) {
        return i;
      }
    }

    return 4;
  }

  private int[] next_cell (char orientation, int x, int y)
  {
    int one_cell_row = 3;
    int one_cell_col = 4;

    if (orientation == 'v') {
      y += one_cell_row;    // if horizontal go down one cell
    }
    else if (orientation == 'h') {
      x += one_cell_col;    // if vertical go right one cell
    }

    int[] ret_val = new int[] {x, y};
    return ret_val;
  }
  
  public void construct(char arg_type, char arg_orientation, int[] arg_pos)
  {
    int cells = 0;              // ship's size
    int x = arg_pos[0];         // col of first piece
    int y = arg_pos[1];         // row of first piece
    
    type = arg_type;
    orientation = arg_orientation;
    
    if (type == 'd') cells = 2;
    else cells = 3;
    ship = new ShipPiece[cells];    // Determine Ship's size
    
    char[][] body_parts = horizontal;     // check if ship 's orientation is either vertical or horizontal, default is vertical
    if (orientation == 'v') {
      body_parts = vertical;
    }

    for (int i = 0; i < cells; i++) {       // Assemble Ships
      ship[i] = new ShipPiece();            // Allocate memory
      ship[i].define_pieces(body_parts[i]); // Assign body parts ascii to each pieces
      ship[i].set_coordinates(x, y);        // Set starting coordinates on the map
      int[] nex_pos = next_cell(orientation, x, y);   // Calculate next cell's coordinates
      x = nex_pos[0]; 
      y = nex_pos[1];
    }

    if (type == 'd') {                        // overwrite destroyer 's midd cell with front cell...temporary solution
      ship[1].define_pieces(body_parts[2]);
    }
  }
}
