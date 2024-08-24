package core;
import org.antlr.v4.runtime.misc.Utils;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

public class Main {
    static final int WIDTH = 70;
    static final int HEIGHT = 40;
    static final int HUDLENGTH = 2;
    static final File CWD = new File(".");
    static final File SAVEGAME = new File(CWD, "saves");
    static TETile ON = null;
    //ids
    static final int NOTHINGID = 3;
    static final int FLOORID = 2;
    static final int WALLID = 1;
    static final int PLAYERID = 13;
    static final int MONSTERID = 14;
    static final int MONSTERPATHID = 15;
    static final int PATHPLAYERID = 132;

    public static void main(String[] args) throws IOException {
        TETile[][] array = null;
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        GraphicalInterface.drawMainMenu();
        String seedS = GraphicalInterface.waitForInput();
        if (seedS == null || seedS.isEmpty()) {
            //default seed
            seedS = "123";
            long seed = Long.parseLong(seedS);
            array = World.worldBuilder(seed);
            array = Player.playerCharacter(array, seed);
        } else if (seedS.equals("load")) {
            File saveGame = new File(SAVEGAME, "save" + ".txt");
            if (!saveGame.exists()) {
                System.exit(0);
            }
            char[] toRead = Utils.readFile(SAVEGAME.getName()
                    + FileSystems.getDefault().getSeparator() + saveGame.getName());
            int[][] tileIDs = new int[WIDTH][HEIGHT];
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
            //An array containing the ids for each tile
            array = constructFromFile(tileIDs);

        } else {
            long seed = Long.parseLong(seedS);
            array = World.worldBuilder(seed);
            array = Player.playerCharacter(array, seed);
        }

        //Game starts
        ter.resetFont();
        ter.initialize(WIDTH, HEIGHT + HUDLENGTH, 0, 0);
        ter.renderFrame(array);
        array = Player.movementTracker(array, ter, ON);
        //The player has quit the game save the game
        if (!SAVEGAME.exists()) {
            SAVEGAME.mkdir();
        }
        String name = new File(SAVEGAME, "save" + ".txt").getName();

        String input = "";
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                input += array[x][y].id();
                if (array[x][y].id() == Tileset.PLAYER.id()) {
                    input += Player.getON().id();
                }
                input += " ";
            }
            input += "\n";
        }
        Utils.writeFile(SAVEGAME.getName() + FileSystems.getDefault().getSeparator() + name, input);
        System.exit(0);
    }

    public static TETile[][] constructFromFile(int[][] tileIDs) {
        TETile[][] array = new TETile[Main.WIDTH][Main.HEIGHT];
        for (int k = 0; k < Main.WIDTH; k++) {
            for (int i = 0; i < Main.HEIGHT; i++) {
                switch (tileIDs[k][i]) {
                    case NOTHINGID:
                        array[k][i] = Tileset.NOTHING;
                        break;
                    case FLOORID:
                    case MONSTERPATHID:
                        array[k][i] = Tileset.FLOOR;
                        break;
                    case WALLID:
                        array[k][i] = Tileset.WALL;
                        break;
                    case PLAYERID:
                        array[k][i] = Tileset.PLAYER;
                        break;
                    case PATHPLAYERID:
                        array[k][i] = Tileset.PLAYER;
                        ON = Tileset.FLOOR;
                        break;
                    case MONSTERID:
                        array[k][i] = Tileset.MONSTER;
                        break;
                    default:
                        break;
                }
            }
        }
        return array;
    }
}
