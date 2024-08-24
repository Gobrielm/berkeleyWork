package gitlet;

import java.io.File;

public class Branch {
    static final File CWD = Main.CWD;
    static final File GITLET = new File(CWD, ".gitlet");
    static final File BRANCHES = new File(GITLET, "branches");

    public static void branch(String branchName) {
        branchName = branchName + ".txt";
        //Create the new branch
        File newBranch = new File(BRANCHES, branchName);
        if (newBranch.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        String headCommit = Tools.headParser(Tools.currentBranchFinder()).getName();
        Utils.writeContents(newBranch, headCommit);
    }
    public static void clearStagingArea() {
        File[] inAddition = Main.ADDITION.listFiles();
        File[] inRemove = Main.REMOVE.listFiles();

        for (File x: inAddition) {
            x.delete();
        }
        for (File x: inRemove) {
            x.delete();
        }
    }

    private static void bringFilesFromHeadToCWD() {
        File head = Tools.headParser(Tools.currentBranchFinder());
        File[] storedBlobs = new File(head, "blobs").listFiles();

        for (File x: storedBlobs) {
            File temp = new File(CWD, x.getName());
            File currentBlob = new File(Main.BLOBS, Utils.readContentsAsString(x));
            Utils.writeContents(temp, Utils.readContentsAsString(currentBlob));
        }
    }


    public static void switchBranch(String branchName) {
        branchName = branchName + ".txt";
        File newBranch = new File(BRANCHES, branchName);
        String currentBranch = Tools.currentBranchFinder();
        if (!newBranch.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        } else {
            if (currentBranch.equals(branchName)) {
                System.out.println("No need to switch to the current branch.");
                System.exit(0);
            }

            //Starts the process of switching
            File commitFolder = new File(Main.COMMITS, Utils.readContentsAsString(newBranch));
            Restore.checkForUntracakedFiles(commitFolder);

            File current = new File(BRANCHES, "current.txt");
            Utils.writeContents(current, branchName);
            bringFilesFromHeadToCWD();

        }
    }

    public static void removeBranch(String branchName) {
        branchName = branchName.toLowerCase() + ".txt";
        File branch = new File(BRANCHES, branchName);
        File current = new File(BRANCHES, "current.txt");
        if (!branch.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        } else if (Utils.readContentsAsString(current).equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        branch.delete();
    }
}
