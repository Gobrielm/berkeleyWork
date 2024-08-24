package gitlet;

import java.io.File;
import java.nio.file.FileSystems;

public class Log {


    public static void globalLog(File commitLocation) {
        File[] commits = commitLocation.listFiles();
        for (File x: commits) {
            printer(x);
        }
    }

    public static void log(File commitLocation) {
        File current = Tools.headParser(Tools.currentBranchFinder());
        while (current.exists()) {
            printer(current);
            File prev2 = new File(current, "metadata" + FileSystems.getDefault().getSeparator() + "prev2.txt");
            if (prev2.exists()) {
                File prev = new File(current, "metadata" + FileSystems.getDefault().getSeparator() + "prev.txt");
                String nextPlace = Utils.readContentsAsString(prev);
                current = new File(commitLocation, nextPlace);
                printer(current);
                break;
            }
            File prev = new File(current, "metadata" + FileSystems.getDefault().getSeparator() + "prev.txt");
            String nextPlace = Utils.readContentsAsString(prev);
            current = new File(commitLocation, nextPlace);
        }
    }

    private static void printer(File current) {
        File time = new File(current, "metadata" + Main.FILESPACE + "timestamp.txt");
        File message = new File(current, "metadata" + Main.FILESPACE + "message.txt");
        File prev1 = new File(current, "metadata" + Main.FILESPACE + "prev.txt");
        File prev2 = new File(current, "metadata" + Main.FILESPACE + "prev2.txt");
        System.out.println("===");
        System.out.println("commit " + current.getName());
        if (prev2.exists()) {
            System.out.println("Merge: " + Utils.readContentsAsString(prev1).substring(0, 7) + " "
                    + Utils.readContentsAsString(prev2).substring(0, 7));
        }
        System.out.println("Date: " + Utils.readContentsAsString(time));
        System.out.println(Utils.readContentsAsString(message));
        System.out.print("\n");
    }

    public static void find(String message) {
        File[] commits = Main.COMMITS.listFiles();
        int i = 0;
        for (File x: commits) {
            File temp = new File(Main.COMMITS, x.getName() + Main.FILESPACE + "metadata"
                    + Main.FILESPACE + "message.txt");
            if (Utils.readContentsAsString(temp).equals(message)) {
                System.out.println(x.getName());
                i++;
            }
        }
        if (i == 0) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
    }
}
