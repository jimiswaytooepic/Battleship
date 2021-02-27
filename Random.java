public class Random implements Strategy {
    public int[][] ships = new int[10][10];
    @Override
    public void seriesStarted(int numWinsNeeded) {
        // TODO Auto-generated method stub

    }

    @Override
    public int[][] placeShips() {
        ships = new int[10][10];
        for(int i = 1; i <= 5; i++) {
            int[][] b = new int[10][10];
            int j = (int)(Math.random()*10);
            int k = (int)(Math.random()*10);
            int l = (int)(Math.random()*2);
            int o = i;
            if(o < 3) o++;
            if(l==0) {
                if(j+i+1<0||j+i+1>=10) {
                    i--;
                    continue;
                }
                else {
                    for(int n = 0; n < o; n++) {
                        b[j+n][k] = i;
                    }
                }
            }
            else {
                if(k+i+1<0||k+i+1>=10) {
                    i--;
                    continue;
                }
                else {
                    for(int n = 0; n < o; n++) {
                        b[j][k+n] = i;
                    }
                }
            }
            if(collisions(ships, b)) {
                i--;
                continue;
            }
            else {
                ships = addArrays(ships, b);
            }
        }
        return ships;
    }

    @Override
    public Location getTarget() {
        return new Location((int)(Math.random()*10), (int)(Math.random()*10));
    }

    @Override
    public void targetMissed() {
        // TODO Auto-generated method stub

    }

    @Override
    public void targetHit(int shipSunk) {
        // TODO Auto-generated method stub

    }

    @Override
    public void opponentShot(int row, int col) {
        // TODO Auto-generated method stub

    }

    @Override
    public void roundEnded(int outcome, int[][] opponentShips) {
        // TODO Auto-generated method stub

    }

    @Override
    public void seriesEnded(int numRoundsWon, int numRoundsLost, int numRoundsTied) {
        // TODO Auto-generated method stub

    }
    public static int[][] addArrays(int[][] a, int[][] b) {
        if(a.length!=b.length||a[0].length!=b[0].length) return null;
        for(int i = 0; i < a.length; i++) {
            for(int j = 0; j < a[0].length; j++) {
                a[i][j] += b[i][j];
            }
        }
        return a;
    }
    public static boolean collisions(int[][] a, int[][] b) {
        if(a.length!=b.length||a[0].length!=b[0].length) return true;
        for(int i = 0; i < a.length; i++) {
            for(int j = 0; j < a[0].length; j++) {
                if(a[i][j]>0&&b[i][j]>0) return true;
            }
        }
        return false;
    }
    
}
