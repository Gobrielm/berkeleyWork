package gitlet;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.*;

public class Status {
    static final File CWD = Main.CWD;
    static final File GITLET = new File(CWD, ".gitlet");
    static final File COMMITS = new File(GITLET, "commits");
    static final File ADDITION = new File(GITLET, "addition");
    static final File REMOVE = new File(GITLET, "remove");
    static final File BRANCHES = new File(GITLET, "branches");

    public static Object[] getStagedFiles() {
        File currentHead = Tools.headParser(Tools.currentBranchFinder());
        File stagedFiles = new File(currentHead, "blobs");
        File[] children = stagedFiles.listFiles();
        if (children == null || children.length == 0) {
            return null;
        }
        return children;
    }
    public static Object[] checkUntrackedFiles() {
        File[] filesInCWD = CWD.listFiles();
        Deque<File> toReturn = new ArrayDeque<>();
        for (File x: filesInCWD) {
            File check = new File(ADDITION, x.getName());
            if (check.exists()) {
                continue;
            } else {
                File currentHead = Tools.headParser(Tools.currentBranchFinder());
                check = new File(currentHead, "blobs" + FileSystems.getDefault().getSeparator() + x.getName());
                File check1 = new File(REMOVE, x.getName());
                if (check.exists() && !check1.exists()) {
                    continue;
                } else {
                    toReturn.add(x);
                }
            }
        }
        return toReturn.toArray();
    }
    public static String takeOffFileExt(String input) {
        if (input == null || input.isEmpty()) {
            System.out.println("Uh-oh, tried to take off nothing");
            System.exit(0);
        }
        return input.substring(0, input.length() - 4);
    }

    public static void status() {
        Object[] unTrackedFiles = checkUntrackedFiles();

        System.out.println("=== Branches ===");
        File[] branches = BRANCHES.listFiles();
        String current = Tools.currentBranchFinder();
        for (File x: branches) {
            if (x.getName().equalsIgnoreCase("current.txt")) {
                continue;
            } else if (x.getName().equalsIgnoreCase(current)) {
                System.out.println("*" + takeOffFileExt(x.getName()));
            } else {
                System.out.println(takeOffFileExt(x.getName()));
            }
        }
        System.out.print("\n");
        System.out.println("=== Staged Files ===");
        File[] inAddition = ADDITION.listFiles();
        ArrayList<File> inAdd = new ArrayList<>();
        if (inAddition != null && inAddition.length != 0) {
            inAdd.addAll(Arrays.asList(inAddition));
        }
        if (!inAdd.isEmpty()) {
            Collections.sort(inAdd);
            for (File x: inAdd) {
                System.out.println(x.getName());
            }
        }


        System.out.print("\n");
        System.out.println("=== Removed Files ===");
        Object[] toPrint = getStagedFiles();
        if (toPrint != null) {
            for (Object x: toPrint) {
                File check1 = new File(REMOVE, ((File) x).getName());
                if (check1.exists()) {
                    System.out.println(((File) x).getName());
                }
            }
        }
        System.out.print("\n");
        System.out.println("=== Modifications Not Staged For Commit ===");

        System.out.print("\n");
        System.out.println("=== Untracked Files ===");
        for (Object x: unTrackedFiles) {
            //Casting to File as it should only have files
            if (((File) x).getName().equals(".gitlet")) {
                continue;
            }
            System.out.println(((File) x).getName());
        }
    }

}
