import java.util.*;
public class JimPhieffer implements Strategy {
    protected int[][] heatmap = new int[10][10];
    private int[][] shots = new int[10][10];
    private int[][] hits = new int[10][10];
    private int numHits = 0;
    private int total = 0;
    private boolean[] shipsDown = new boolean[5];
    private int[][] opponentShips = null;
    private int winsNeeded = 0;
    private boolean b = false;
    //////MODIFIERS\\\\\\
    private int MOD = 1000;
    private int MOD2 = 0;
    private int MOD3 = 1;
    //////MODIFIERS\\\\\\
    public static final String[] CODES = {"107", "47", "100", "40"};
    public int[][] ships = new int[10][10];
    private Location lastShot = null;
    public JimPhieffer() {
    }
    public JimPhieffer(int x, int y, int z) {
        MOD = x;
        MOD2 = y;
        MOD3 = z;
        b = true;
    }
    public int[][] randomizeShips() {
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
    public void seriesStarted(int numWinsNeeded) {
        winsNeeded = numWinsNeeded;
    }
    public int[][] placeShips() {
        ships = new int[10][10];
        int[][] x = randomizeShips();
        return x;
    }
    public Location getTarget() {
        calculateHeatMap();
        List<Map.Entry<Location, Integer>> l = makeQueue();
        Map.Entry<Location, Integer> last = l.get(l.size()-1);
        lastShot = last.getKey();
        total++;
        return last.getKey();
    }
    public void targetMissed() {
        shots[lastShot.getRow()][lastShot.getCol()]=1;
    }
    public void targetHit(int shipSunk) {
        hits[lastShot.getRow()][lastShot.getCol()]=1;
        if(shipSunk!=0) shipsDown[shipSunk-1] = true;
        numHits++;
    }
    public void opponentShot(int row, int col) {
    }
    public void roundEnded(int outcome, int[][] opponentShips) {
        this.opponentShips = opponentShips;
        heatmap = new int[10][10];
        shots = new int[10][10];
        hits = new int[10][10];
        shipsDown = new boolean[5];
        lastShot = null;
        System.out.println((100*numHits/total)+"% at "+MOD+", "+MOD2+", "+MOD3+(outcome==1?" and won.":"."));
        numHits = 0;
        total = 0;
        if(b)MOD++;
    }
    public void seriesEnded(int numRoundsWon, int numRoundsLost, int numRoundsTied) {
    }
    public void calculateHeatMap() {
        int[][] a = prioritizeHits(MOD);
        int[][] d = prioritizePreviousShips(MOD2);
        int[][] e = new int[10][10];
        int[] b = shipsRemaining();
        for(int k = 0; k < b.length; k++) {
            for(int i = 0; i < 10; i++) {
                for(int j = 0; j < 10; j++) {
                    for(int l = 0; l < b[k]; l++) {
                        int[][] c = new int[10][10];
                        for(int m = -b[k]+1; m <= 0; m++) {
                            if(!isValid(i, j+m+l)) {c = new int[10][10]; break;}
                            c[i][j+m+l]++;
                        }
                        if(!collisions(shots, c)) e = addArrays(e, c);
                    }
                    for(int l = 0; l < b[k]; l++) {
                        int[][] c = new int[10][10];
                        for(int m = -b[k]+1; m <= 0; m++) {
                            if(!isValid(i+m+l, j)) {c = new int[10][10]; break;}
                            c[i+m+l][j]++;
                        }
                        if(!collisions(shots, c)) e = addArrays(e, c);
                    }
                }
            }
        }
        a = addArrays(a, d);
        heatmap = addArrays(a, e);
        heatmap = removeIf(heatmap, shots);
        heatmap = removeIf(heatmap, hits);
    }
    private int[][] removeIf(int[][] a, int[][] b) {
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                a[i][j]=b[i][j]>0?0:a[i][j];
            }
        }
        return a;
    }
    private int longestShipRemaining() {
        if(!shipsDown[4]) return 5;
        if(!shipsDown[3]) return 4;
        if(!shipsDown[2]||!shipsDown[1]) return 3;
        if(!shipsDown[0]) return 2;
        return 0;
    }
    private int[] shipsRemaining() {
        int i = 0;
        for(boolean b : shipsDown) {
            if(!b) i++;
        }
        int[] a = new int[i];
        for(int j = 4; j >=0; j--) {
            if(!shipsDown[j]) {
                i--;
                a[i] = j>=2?j+1:j+2;
            }
        }
        return a;
    }
    private List<Map.Entry<Location, Integer>> makeQueue() {
        List<Map.Entry<Location, Integer>> l = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                l.add(new AbstractMap.SimpleEntry<>(new Location(i, j), heatmap[i][j]));
                //if(i%2==j%2||hitmap[i][j]!=0) 
            }
        }
        Collections.sort(l, new Comparator<Map.Entry<Location, Integer>>() {public int compare(Map.Entry<Location, Integer> a, Map.Entry<Location, Integer> b){return a.getValue().compareTo(b.getValue());}});
        return l;
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
    public static void print2D(int[][] b, boolean c) {
        for(int[] a : b) {
            for(int i : a) {
                if(c) printColored(i, i);
                else System.out.print(i);
            }
            System.out.println();
        }
    }
    public static <E> void printColored(E text, int i) {
        System.out.print("\u001B["+CODES[i/64]+"m"+(String.valueOf(text).length()==2?text+" ":(String.valueOf(text).length()==1?text+"  ":text))+"\u001B[0m");
    }
    public int[][] removeCheckered(int[][] a, int[][] ignore) {
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                if(i%2==j%2&&ignore[i][j]==0) a[i][j]/=MOD3;
            }
        }
        return a;
    }
    public boolean isValid(int i, int j) {
        return i>=0&&i<10&&j>=0&&j<10;
    }
    public int[][] prioritizeHits(int modifier) {
        int[][] a = new int[10][10];
        int[] b = shipsRemaining();
        for(int k = 0; k < b.length; k++) {
            for(int i = 0; i < 10; i++) {
                for(int j = 0; j < 10; j++) {
                    if(hits[i][j]==1) {
                        for(int l = 0; l < b[k]; l++) {
                            int[][] c = new int[10][10];
                            for(int m = -b[k]+1; m <= 0; m++) {
                                if(!isValid(i, j+m+l)) {c = new int[10][10]; break;}
                                c[i][j+m+l]+=modifier;
                            }
                            if(!collisions(shots, c)) a = addArrays(a, c);
                        }
                        for(int l = 0; l < b[k]; l++) {
                            int[][] c = new int[10][10];
                            for(int m = -b[k]+1; m <= 0; m++) {
                                if(!isValid(i+m+l, j)) {c = new int[10][10]; break;}
                                c[i+m+l][j]+=modifier;
                            }
                            if(!collisions(shots, c)) a = addArrays(a, c);
                        }
                        a[i][j] = 0;
                    }
                }
            }
        }
        return a;
    }
    private int[][] prioritizePreviousShips(int modifier) {
        int[][] a = new int[10][10];
        if(opponentShips != null) {
            for(int i = 0; i < 10; i++) {
                for(int j = 0; j < 10; j++) {
                    if(opponentShips[i][j]>0) a[i][j] += modifier;
                }
            }
        }
        return a;
    }
    public static void main(String[] args) {
        Display d = new Display();
        //Game.play(new JimPhieffer(1, 1, 1), new JimPhieffer(12, 3 ,3), d, 1);
        Strategy s1 = new JimPhieffer(1, 1, 1);
        Strategy s2 = new JimPhieffer();
        Game.play(s1, s2, d, 200);
        // JimPhieffer jp = new JimPhieffer();
        // jp.hits[5][5] = 1;
        // jp.calculateHeatMap();
        // print2D(jp.heatmap, true);
    }
}