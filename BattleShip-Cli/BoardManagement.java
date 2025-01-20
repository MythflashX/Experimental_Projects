public class BoardManagement
{
  private Grid map = new Grid();
  private ConstructShip[] fleet;
  private int[][] coordinates = new int[7][2];
  private int[][] hits = new int[7][2];
  private int[][] miss = new int[18][2];
  private int[] hOm = new int[2];     // 0-hit or 1-miss counts
  
  public void clear ()       // clear terminal screen  ANSI escape codes method
  {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
  
  private int collision_check (ConstructShip[] fleet)
  {
    //System.out.println("COLLISION CHECK");    // to debug..un comment a line in Grid.java update();
    int points[][] = new int[7][2];
    int index = 0;
    
    for (int i = 0; i < 3; i++) {         // extract positions for collision test
      int length;
      if (fleet[i].get_type() == 'd') length = 2;
      else length = 3;
      
      for (int ii = 0; ii < length; ii++) {
        points[index++] = fleet[i].get_part(ii).get_coordinates();
      }
    }
    
    for (int i = 0; i < 7; i++) {     // start checking cell collision
      for (int ii = i + 1; ii < 7; ii++) {
        if ((points[i][0] == points[ii][0]) && (points[i][1] == points[ii][1])) {
          if (ii < 1) return 0;
          else if (ii < 3) return 1;
          else return 2;
        }
      }
    }

    coordinates = points;   // reaching this point means there is no collision

    return 0;
  }  
  
  public void status_check (int pos[])     // check if an attack is hit or miss
  {
    pos = map.grid_sync(pos);
    
    if (hOm[0] == 6) {
      update();
      System.out.println("\n\t\t CONGRATULATIONS!!!");
      System.out.println("\t You sank all the Ships.");
      System.out.println("Press Ctrl + C to Exit");
      while (true);
    }
    else if (hOm[1] == 6) {
      update();
      System.out.println("\n\t\t GAME OVER!!!");
      System.out.println("\t You lost all the chances.");
      System.out.println("Press Ctrl + C to Exit");
      while (true);
    }

    for (int i = 0; i < hOm[0]; i++) {        // reject redundent inputs
      if (pos[0] == hits[i][0] && pos[1] == hits[i][1]) {
        System.out.println("Invalid");
        return;
      }
    }
    
    for (int i = 0; i < hOm[1]; i++) {        // reject redundant inputs
      if (pos[0] == miss[i][0] && pos[1] == miss[i][1]) {
        System.out.println("Invalid");
        return;
      }
    }
    
    for (int i = 0; i < 7; i++) {
      //System.out.println(pos[0] + " " + coordinates[i][0] + "\t" + pos[1] + " " + coordinates[i][1] + " " + i);
      if (pos[0] == coordinates[i][0] && pos[1] == coordinates[i][1]) {
        System.out.println("HIT");
        hits[hOm[0]++] = pos;
        return;
      }
    }
    
    System.out.println("MISS");
    miss[hOm[1]++] = pos;
    return;
  }

  public void deploy_ships ()     // set 3 ship 's coordinates
  {
    char orientation = 'v';
    char type = 'd';
    int[][] points = new int[3][2];
    fleet = new ConstructShip[3];

    for (int i = 0; i < 3; i++) {     // Manage Ships
      if ((((int) (Math.random() * 2)) % 2) == 0)  // 50% chance for either vertical or horizontal
        orientation = 'v';
      else
        orientation = 'h';
      
      if (i == 2) type = 'c';
      points[i] = spawn(orientation, type);

      fleet[i] = new ConstructShip();
      fleet[i].construct(type, orientation, points[i]);
    }

    int infected_cell = collision_check(fleet);

    while (infected_cell != 0) {          // Respawn Infected Ships
      orientation = fleet[infected_cell].get_orientation();
      type = fleet[infected_cell].get_type();
      fleet[infected_cell].construct(type, orientation, spawn(orientation, type));
      infected_cell = collision_check(fleet);
    }
  }

  private int[] spawn (char orientation, char type)     // Generate a starting coordinate on the map
  {
    int[] pos = new int[2];
    
    if (orientation == 'v') {
      if (type == 'd')  // for destroyers
        pos[1] = (int) (Math.random() * 4);   // limit between cell of row to 0 - 3 since destroyers only needs 2 cells
      else
        pos[1] = (int) (Math.random() * 3);   // limit between cell of row to 0 - 2 since crusier requires 3 cells
                                            // 
      pos[0] = (int) (Math.random() * 5);  // cell of col can be whatever between 0 - 4
    }
    else {
      if (type == 'd')  // for destroyers
        pos[0] = (int) (Math.random() * 4);   // limit between cell of row to 0 - 3 since destroyers only needs 2 cells
      else
        pos[0] = (int) (Math.random() * 3);   // limit between cell of row to 0 - 2 since crusier requires 3 cells
                                            // 
      pos[1] = (int) (Math.random() * 5);  // cell of col can be whatever between 0 - 4
    }
    
    pos = map.grid_sync(pos);
    //System.out.print("SPAWNED " + orientation + " " + type + " ");
    //System.out.println(pos[0] + " " + pos[1]);
    
    return pos;
  }

  public void cheat ()
  {
    for (int i = 0; i < 3; i++) {
      map.update_ship(fleet[i]);
    }

    map.display();
  }
  
  public void update ()
  {
    System.out.println("\t\t      BATTLESHIP!\n\n\n");

    System.out.println("Shots: " + (7 - hOm[1]));
    
    for (int i = 0; i < hOm[1]; i++) {
      map.update_miss (miss[i]);
      //System.out.println("miss" + miss[i][0] + " " + miss[i][1]);
    }
    
    int part_number = 0;
    for (int i = 0; i < hOm[0]; i++) {
      //map.update_hits(fleet[i].get_part(fleet[i].search_part(hits[i])));
     
      for (int ship_number = 0; ship_number < 3; ship_number++) {
        part_number = fleet[ship_number].search_part(hits[i]);
        if (part_number != 4) {
          map.update_hits(fleet[ship_number].get_part(part_number));
          break;
        }
      }
      
    }
    
    map.display();
  }

  public void init ()
  {
    System.out.println("\t\t      BATTLESHIP!\n\n\n");
    map.generate();
    map.display();
  }
}
