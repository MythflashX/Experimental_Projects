public class ShipPiece
{
  private int[] indices;   // start row & col pos
  private char[] pieces = new char[6];   // ships' components

  public char[] pieces_pos ()
  {
    return pieces;
  }

  public int[] get_coordinates ()
  {
    return indices;
  }

  public void set_coordinates (int x, int y)
  {
    indices = new int[2];
    indices[0] = x;
    indices[1] = y;
  }
  
  public void define_pieces (char[] arg_pieces)    // ship's orientation can be either vertical or horizontal, thus assigning what type will be done after spawn
  {
    pieces = arg_pieces;
  }
    
}
