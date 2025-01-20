public class BattleShip
{
  public static void main (String[] args)
  {
    BoardManagement board = new BoardManagement();
    Prompt usr_inpt = new Prompt();
    
    board.clear();
    board.init();
    board.deploy_ships();
    //board.cheat();

    while (true)
    {
      int cell[] = new int[2];
      cell = usr_inpt.kill();
      board.clear();
      board.status_check(cell);
      board.update();
    }
  }
}

