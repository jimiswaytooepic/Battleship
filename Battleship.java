public class Battleship
{
  public static void main(String[] args)
  {
    Display display = new Display();
    Strategy strategy1 = new Human(display);
    Strategy strategy2 = new Plebe();
    int numWinsNeeded = 2;
    int winner = Game.play(strategy1, strategy2, display, numWinsNeeded);
    System.out.println("winner = " + winner);
  }
}