package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.Random;

public class World {
    //The specifics of these values could be tweaked by them being related to height/width is usually good enough
    private static final int HALLWAYTURNS1 = Main.WIDTH * Main.HEIGHT / 140;
    private static final int HALLWAYTURNS2 = Main.WIDTH * Main.HEIGHT / 100;
    private static final int HALLWAYLENGTH1 = Main.WIDTH * Main.HEIGHT / 560;
    private static final int HALLWAYLENGTH2 = Main.WIDTH * Main.HEIGHT / 140;
    private static final int CHANCEROOM = Main.WIDTH * Main.HEIGHT / 28 / 4 * 3;
    private static final int HUNDRED = 100;
    private static final int CHANCERIGHT = 30;
    private static final int CHANCELEFT = 60;
    private static final int CHANCEUP = 80;
    public static TETile[][] worldBuilder(long seed) {
        TETile[][] world = createEmpty(Main.WIDTH, Main.HEIGHT);
        Random rand = new Random(seed);
        int totalEmpty = Main.WIDTH * Main.HEIGHT;
        int startX = rand.nextInt(Main.WIDTH / 3, Main.WIDTH * 2 / 3);
        int startY = rand.nextInt(Main.HEIGHT / 3, Main.HEIGHT * 2 / 3);
        //Check to see if the map is at least 1/4 full of flooring

        while (totalEmpty > Main.HEIGHT * Main.WIDTH / 4 * 3) {
            totalEmpty = 0;
            //Creates hallways
            hallwayStraight(world, rand, 1, startX, startY);

            //Creates rooms
            for (int x = 0; x < Main.WIDTH; x++) {
                for (int y = 0; y < Main.HEIGHT; y++) {
                    if (world[x][y] == Tileset.FLOWER) {
                        createRoom(world, x - 1, y - 1, rand.nextInt(4, 6), rand.nextInt(4, 6), rand);
                    } else if (world[x][y] == Tileset.NOTHING) {
                        totalEmpty += 1;
                    }
                }
            }
        }
        //Create walls
        world = wallPlacer(world);
        return world;
    }

    private static TETile[][] createEmpty(int width, int height) {
        TETile[][] toReturn = new TETile[width][height];
        for (int x = 0; x < toReturn.length; x++) {
            for (int y = 0; y < toReturn[0].length; y++) {
                toReturn[x][y] = Tileset.NOTHING;
            }
        }
        return toReturn;
    }

    private static TETile[][] createRoom(TETile[][] world, int x, int y, int width, int height, Random rand) {
        //Random shift to each room(By default the bottom left corner will be at x, y)
        int shiftY = rand.nextInt(0, width / 2);
        int shiftX = rand.nextInt(0, height / 2);
        //Creates rectangular floor plan
        for (int i = x - shiftX; i <= width + x - shiftX; i++) {
            for (int k = y - shiftY; k <= height + y - shiftY; k++) {
                checkBounds(world, i, k, Tileset.FLOOR);
            }
        }
        return world;
    }

    public static TETile[][] checkBounds(TETile[][] arr, int i, int j, TETile type) {
        //Checks if the tile is in bound, if so then set it to type.
        if (!(i < 0) && !(j < 0) && (!(i >= Main.WIDTH) && !(j >= Main.HEIGHT))) {
            arr[i][j] = type;
        }
        return arr;
    }

    public static TETile checkBounds(TETile[][] arr, int i, int j) {
        //Checks and returns the tile at the location
        if (!(i < 0) && !(j < 0) && (!(i >= Main.WIDTH) && !(j >= Main.HEIGHT))) {
            return arr[i][j];
        }
        return null;
    }

    private static TETile[][] hallwayStraight(TETile[][] world, Random rand, int amount, int ogCurrX, int ogCurrY) {
        int currX = ogCurrX;
        int currY = ogCurrY;
        char dir = directionChanger(rand.nextInt(0, HUNDRED), 'p', rand);
        for (int p = 0; p < amount; p++) {
            //Amount of hallways
            for (int i = 0; i < rand.nextInt(HALLWAYTURNS1, HALLWAYTURNS2); i++) {
                //Amount of turns in each hallway
                for (int x = 0; x < rand.nextInt(HALLWAYLENGTH1, HALLWAYLENGTH2); x++) {
                    //Length of each segment of hallway
                    if (rand.nextInt(0, CHANCEROOM) == CHANCEROOM - 1) {
                        //Placeholder for room
                        checkBounds(world, currX, currY, Tileset.FLOWER);
                    } else {
                        checkBounds(world, currX, currY, Tileset.FLOOR);
                    }
                    if (dir == 'r') {
                        currX++;
                        //rightCount++;
                    } else if (dir == 'l') {
                        currX--;
                        //leftCount++;
                    } else if (dir == 'u') {
                        currY++;
                        //upCount++;
                    } else if (dir == 'd') {
                        currY--;
                        //downCount++;
                    }
                    //If its within 3 tiles of side, that specific so it generates a room with size at least 3
                    if (currX == 2 || currY == 2 || currX == Main.WIDTH - 3 || currY == Main.HEIGHT - 3) {
                        //If reached the edge spawn flower then break
                        checkBounds(world, currX, currY, Tileset.FLOWER);
                        break;
                    }
                }
                int chance = rand.nextInt(0, HUNDRED);
                dir = directionChanger(chance, dir, rand);
                if (currX == 2 || currY == 2 || currX == Main.WIDTH - 3 || currY == Main.HEIGHT - 3) {
                    //If reached the edge, end this hallway
                    break;
                }
            }
            if (currX != 2 && currY != 2 && currX != Main.WIDTH - 3 && currY != Main.HEIGHT - 3) {
                checkBounds(world, currX, currY, Tileset.FLOWER);
            }
            currX = ogCurrX;
            currY = ogCurrY;
        }
        return world;
    }

    private static char directionChanger(int chance, char dir, Random rand) {
        while (true) {
            //Chosen for a tendency to move left/right more as map is usually wider than tall
            //Can be tweaked later
            if (chance < CHANCERIGHT && dir != 'l') {
                return 'r';
            } else if (chance < CHANCELEFT && dir != 'r') {
                return 'l';
            } else if (chance < CHANCEUP && dir != 'd') {
                return 'u';
            } else if (dir != 'u') {
                return 'd';
            } else {
                chance = rand.nextInt(0, HUNDRED);
            }
        }
    }

    private static TETile[][] wallPlacer(TETile[][] world) {
        for (int x = 0; x < Main.WIDTH; x++) {
            for (int y = 0; y < Main.HEIGHT; y++) {
                if (world[x][y] == Tileset.NOTHING
                        && ((checkBounds(world, x - 1, y) == Tileset.FLOOR)
                        || (checkBounds(world, x + 1, y) == Tileset.FLOOR)
                        || (checkBounds(world, x, y - 1) == Tileset.FLOOR)
                        || (checkBounds(world, x, y + 1) == Tileset.FLOOR))) {
                    checkBounds(world, x, y, Tileset.WALL);
                }
            }
        }
        for (int x = 0; x < Main.WIDTH; x++) {
            if (checkBounds(world, x, 0) == Tileset.FLOOR) {
                checkBounds(world, x, 0, Tileset.WALL);
            }
        }
        for (int x = 0; x < Main.WIDTH; x++) {
            if (checkBounds(world, x, Main.HEIGHT - 1) == Tileset.FLOOR) {
                checkBounds(world, x, Main.HEIGHT - 1, Tileset.WALL);
            }
        }
        for (int x = 0; x < Main.HEIGHT; x++) {
            if (checkBounds(world, 0, x) == Tileset.FLOOR) {
                checkBounds(world, 0, x, Tileset.WALL);
            }
        }
        for (int x = 0; x < Main.HEIGHT; x++) {
            if (checkBounds(world, Main.WIDTH - 1, x) == Tileset.FLOOR) {
                checkBounds(world, Main.WIDTH - 1, x, Tileset.WALL);
            }
        }
        return world;
    }
}
