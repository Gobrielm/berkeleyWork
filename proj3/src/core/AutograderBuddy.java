package core;

import edu.princeton.cs.algs4.StdDraw;
import org.antlr.v4.runtime.misc.Utils;
import tileengine.TETile;
import tileengine.Tileset;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

public class AutograderBuddy {

    /**
     * Simulates a game, but doesn't render anything or call any StdDraw
     * methods. Instead, returns the world that would result if the input string
     * had been typed on the keyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quit and
     * save. To "quit" in this method, save the game to a file, then just return
     * the TETile[][]. Do not call System.exit(0) in this method.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public static TETile[][] getWorldFromInput(String input) throws IOException {
        input = input.toLowerCase();
        String seed = "";
        TETile[][] world = null;
        int endOfSeed = 0;
        if (input.charAt(0) == 'n') {
            for (int i = 1; input.charAt(i) != 's'; i++) {
                seed += input.charAt(i);
                endOfSeed = i + 1;
            }
            //Generate the world off the seed
            world = World.worldBuilder(Long.parseLong(seed));
            if (world == null) {
                return null;
            }
            //Place player
            Player.playerCharacter(world, Long.parseLong(seed));
        }
        //If there are movements parse them out
        String movements = "";
        if (endOfSeed != 0 && endOfSeed != input.length()) {
            for (int i = endOfSeed + 1; i < input.length(); i++) {
                movements += input.charAt(i);
            }
        }
        //Takes care of any character inputs
        world = mover(world, movements.toCharArray());
        return world;
    }

    private static TETile[][] mover(TETile[][] world, char[] movements) throws IOException {
        TETile toPlace = Tileset.FLOOR;
        int playX = Player.getPlayerX();
        int playY = Player.getPlayerY();
        for (char c: movements) {
            if (StdDraw.hasNextKeyTyped()) {
                if (c == 'w') {
                    world[playX][playY] = toPlace;
                    if (Player.checkbounds(world, playX, playY + 1)) {
                        playY++;
                        toPlace = Player.nextTile(world, playX, playY);
                        world = Player.monsterMover(world);
                        Player.changePlayerPos(playX, playY);
                    }
                    world[playX][playY] = Tileset.PLAYER;
                } else if (c == 's') {
                    world[playX][playY] = toPlace;
                    if (Player.checkbounds(world, playX, playY - 1)) {
                        playY--;
                        toPlace = Player.nextTile(world, playX, playY);
                        world = Player.monsterMover(world);
                        Player.changePlayerPos(playX, playY);
                    }
                    world[playX][playY] = Tileset.PLAYER;
                } else if (c == 'd') {
                    world[playX][playY] = toPlace;
                    if (Player.checkbounds(world, playX + 1, playY)) {
                        playX++;
                        toPlace = Player.nextTile(world, playX, playY);
                        world = Player.monsterMover(world);
                        Player.changePlayerPos(playX, playY);
                    }
                    world[playX][playY] = Tileset.PLAYER;
                } else if (c == 'a') {
                    world[playX][playY] = toPlace;
                    if (Player.checkbounds(world, playX - 1, playY)) {
                        playX--;
                        toPlace = Player.nextTile(world, playX, playY);
                        world = Player.monsterMover(world);
                        Player.changePlayerPos(playX, playY);
                    }
                    world[playX][playY] = Tileset.PLAYER;
                } else if (c == 'q') {
                    Player.setON(toPlace);
                    return world;
                } else if (c == 'l') {
                    File saveGame = new File(Main.SAVEGAME, "save" + ".txt");
                    if (!saveGame.exists()) {
                        System.exit(0);
                    }
                    char[] toRead = Utils.readFile(Main.SAVEGAME.getName()
                            + FileSystems.getDefault().getSeparator() + saveGame.getName());
                    int[][] tileIDs = new int[Main.WIDTH][Main.HEIGHT];
                    int x = 0;
                    int y = 0;
                    for (char i: toRead) {
                        if (i == '\n') {
                            x++;
                            y = 0;
                        } else if (i == ' ') {
                            y++;
                        } else {
                            if (tileIDs[x][y] == 0) {
                                tileIDs[x][y] = Integer.parseInt(String.valueOf(i));
                            } else {
                                tileIDs[x][y] = Integer.parseInt(Integer.toString(tileIDs[x][y]) + i);
                            }
                        }
                    }
                    world = Main.constructFromFile(tileIDs);
                }
            }
        }

        return world;
    }

    /**
     * Used to tell the autograder which tiles are the floor/ground (including
     * any lights/items resting on the ground). Change this
     * method if you add additional tiles.
     */
    public static boolean isGroundTile(TETile t) {
        return t.character() == Tileset.FLOOR.character()
                || t.character() == Tileset.AVATAR.character()
                || t.character() == Tileset.FLOWER.character();
    }

    /**
     * Used to tell the autograder while tiles are the walls/boundaries. Change
     * this method if you add additional tiles.
     */
    public static boolean isBoundaryTile(TETile t) {
        return t.character() == Tileset.WALL.character()
                || t.character() == Tileset.LOCKED_DOOR.character()
                || t.character() == Tileset.UNLOCKED_DOOR.character();
    }
}
