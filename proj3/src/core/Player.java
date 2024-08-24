package core;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import java.awt.*;
import java.util.*;

public class Player {
    private static TETile ON = null;
    private static int PlayerPosX;
    private static int PlayerPosY;
    private static int MonsterPosX;
    private static int MonsterPosY;
    private static ArrayList<Coords> monsterPath;
    private static boolean pathShown;
    public static TETile[][] playerCharacter(TETile[][] world, long seed) {
        pathShown = false;
        Random rand = new Random(seed);
        int randX = rand.nextInt(0, Main.WIDTH);
        int randY = rand.nextInt(0, Main.WIDTH);
        while (true) {
            if (World.checkBounds(world, randX, randY) == Tileset.FLOOR) {
                world = World.checkBounds(world, randX, randY, Tileset.PLAYER);
                PlayerPosX = randX;
                PlayerPosY = randY;
                break;
            }
            randX = rand.nextInt(0, Main.WIDTH);
            randY = rand.nextInt(0, Main.WIDTH);
        }
        return addMonster(world, rand);
    }
    public static int getPlayerX() {
        return PlayerPosX;
    }
    public static int getPlayerY() {
        return PlayerPosY;
    }
    public static void setON(TETile newON) {
        ON = newON;
    }

    private static TETile[][] addMonster(TETile[][] world, Random rand) {
        int randX = rand.nextInt(0, Main.WIDTH);
        int randY = rand.nextInt(0, Main.WIDTH);
        while (true) {
            if (World.checkBounds(world, randX, randY) == Tileset.FLOOR) {
                world = World.checkBounds(world, randX, randY, Tileset.MONSTER);
                MonsterPosX = randX;
                MonsterPosY = randY;
                break;
            }
            randX = rand.nextInt(0, Main.WIDTH);
            randY = rand.nextInt(0, Main.WIDTH);
        }
        return world;
    }

    public static TETile getON() {
        return ON;
    }
    public static void changePlayerPos(int x, int y) {
        PlayerPosX = x;
        PlayerPosY = y;
    }
    public static TETile[][] movementTracker(TETile[][] world, TERenderer ter, TETile on) {
        //Find player
        int x = 0;
        int y = 0;
        for (int i = 0; i < Main.WIDTH; i++) {
            for (int k = 0; k < Main.HEIGHT; k++) {
                if (world[i][k] == Tileset.PLAYER) {
                    x = i;
                    y = k;
                } else if (MonsterPosX == 0 && MonsterPosY == 0 && world[i][k] == Tileset.MONSTER) {
                    MonsterPosX = i;
                    MonsterPosY = k;
                }
            }
        }
        TETile toPlace = Tileset.FLOOR;
        if (on != null) {
            toPlace = on;
        } else if (ON != null) {
            toPlace = ON;
        }
        String currentTarget = "";
        char c;
        //Just to give it a key, so it doesn't cause issues
        char last = 'i';
        while (true) {
            ter.resetFont();
            ter.renderFrame(world);
            //Put stuff on HUD
            currentTarget = showHud(world, currentTarget);
            if (StdDraw.hasNextKeyTyped()) {
                c = StdDraw.nextKeyTyped();
                c = Character.toLowerCase(c);
                if (c == 'w') {
                    world[x][y] = toPlace;
                    if (checkbounds(world, x, y + 1)) {
                        y++;
                        toPlace = nextTile(world, x, y);
                        world = monsterMover(world);
                        changePlayerPos(x, y);
                    }
                    world[x][y] = Tileset.PLAYER;
                } else if (c == 's') {
                    world[x][y] = toPlace;
                    if (checkbounds(world, x, y - 1)) {
                        y--;
                        toPlace = nextTile(world, x, y);
                        world = monsterMover(world);
                        changePlayerPos(x, y);
                    }
                    world[x][y] = Tileset.PLAYER;
                } else if (c == 'd') {
                    world[x][y] = toPlace;
                    if (checkbounds(world, x + 1, y)) {
                        x++;
                        toPlace = nextTile(world, x, y);
                        world = monsterMover(world);
                        changePlayerPos(x, y);
                    }
                    world[x][y] = Tileset.PLAYER;
                } else if (c == 'a') {
                    world[x][y] = toPlace;
                    if (checkbounds(world, x - 1, y)) {
                        x--;
                        toPlace = nextTile(world, x, y);
                        world = monsterMover(world);
                        changePlayerPos(x, y);
                    }
                    world[x][y] = Tileset.PLAYER;
                } else if (c == 'q' && last == ':' || last == 'q' && c == ':') {
                    ON = toPlace;
                    return world;
                } else if (c == 'k') {
                    world = pathHelper(world);
                }
                last = c;
            }
            StdDraw.pause(5);
        }
    }
    private static TETile[][] pathHelper(TETile[][] world) {
        pathShown = !pathShown;
        if (pathShown) {
            world = showMonsterPath(world);
        } else {
            world = hideMonsterPath(world);
        }
        return world;
    }
    public static TETile[][] monsterMover(TETile[][] world) {
        world = hideMonsterPath(world);
        monsterPath = new ArrayList<>();
        int x;
        int y;
        //Using DFS measured by distance
        PriorityQueue<Coords> spaces = new PriorityQueue<>();
        HashSet<Coords> visited = new HashSet<>();
        spaces.add(new Coords(MonsterPosX, MonsterPosY, PlayerPosX, PlayerPosY));
        Coords dest = new Coords(PlayerPosX, PlayerPosY, 0);
        Coords curr = null;

        while (!spaces.isEmpty()) {
            curr = spaces.poll();
            visited.add(curr);
            x = curr.x;
            y = curr.y;
            if (curr.equals(dest)) {
                break;
            }
            if (checkbounds(world, x + 1, y)) {
                Coords newSpace = new Coords(x + 1, y, PlayerPosX, PlayerPosY, curr);
                if (!visited.contains(newSpace)) {
                    spaces.add(newSpace);
                }
            }
            if (checkbounds(world, x - 1, y)) {
                Coords newSpace = new Coords(x - 1, y, PlayerPosX, PlayerPosY, curr);
                if (!visited.contains(newSpace)) {
                    spaces.add(newSpace);
                }
            }
            if (checkbounds(world, x, y + 1)) {
                Coords newSpace = new Coords(x, y + 1, PlayerPosX, PlayerPosY, curr);
                if (!visited.contains(newSpace)) {
                    spaces.add(newSpace);
                }
            }
            if (checkbounds(world, x, y - 1)) {
                Coords newSpace = new Coords(x, y - 1, PlayerPosX, PlayerPosY, curr);
                if (!visited.contains(newSpace)) {
                    spaces.add(newSpace);
                }
            }
        }
        if (curr.prev != null && curr.prev.prev != null && curr.prev.prev.prev != null) {
            while (curr.prev.prev != null) {
                curr = curr.prev;
                if (curr.prev.prev != null) {
                    monsterPath.add(curr);
                }
            }
            world[MonsterPosX][MonsterPosY] = Tileset.FLOOR;
            MonsterPosX = curr.x;
            MonsterPosY = curr.y;
            world[curr.x][curr.y] = Tileset.MONSTER;
        }
        if (pathShown) {
            world = showMonsterPath(world);
        }
        return world;
    }

    public static boolean checkbounds(TETile[][] world, int x, int y) {
        if (x >= 0 && y >= 0 && x < Main.WIDTH && y < Main.HEIGHT) {
            return world[x][y].id() == Main.FLOORID || world[x][y].id() == Main.MONSTERPATHID;
        }
        return false;
    }

    public static boolean checkboundsMouse(TETile[][] world, double x, double y) {
        if (x >= 0 && y >= 0 && x < Main.WIDTH && y < Main.HEIGHT) {
            return true;
        }
        return false;
    }

    public static TETile nextTile(TETile[][] world, int x, int y) {
        return world[x][y];
    }
    private static TETile[][] showMonsterPath(TETile[][] world) {
        if (monsterPath == null) {
            return world;
        }
        for (Coords x: monsterPath) {
            world[x.x][x.y] = Tileset.MONSTERPATH;
        }

        return world;
    }
    private static TETile[][] hideMonsterPath(TETile[][] world) {
        if (monsterPath == null) {
            return world;
        }
        for (Coords x: monsterPath) {
            world[x.x][x.y] = Tileset.FLOOR;
        }
        return world;
    }
    private static String showHud(TETile[][] world, String currentTarget) {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, GraphicalInterface.SMALLFONT + 5));
        StdDraw.line(0, Main.HEIGHT, Main.WIDTH, Main.HEIGHT);
        String controls = "Move(WASD)    Show Path(K)    Quit(Q)";
        StdDraw.text(GraphicalInterface.SMALLFONT, Main.HEIGHT + Main.HUDLENGTH / 2, controls);
        double mouseX = StdDraw.mouseX();
        double mouseY = StdDraw.mouseY();
        if (checkboundsMouse(world, mouseX, mouseY)) {
            currentTarget = world[(int) Math.floor(mouseX)][(int) Math.floor(mouseY)].description();
        }
        StdDraw.text(Main.WIDTH - 7, Main.HEIGHT + Main.HUDLENGTH / 2, currentTarget);
        StdDraw.show();
        return currentTarget;
    }

}
