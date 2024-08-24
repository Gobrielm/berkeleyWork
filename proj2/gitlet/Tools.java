package gitlet;

import java.io.File;

public class Tools {

    public static File headParser(String branch) {
        File main = new File(Main.BRANCHES, branch);
        String headReference = Utils.readContentsAsString(main);
        File head = new File(Main.COMMITS, headReference);
        if (head.exists()) {
            return head; //returns the file that sits in commit
        }
        System.out.println("Uh-oh Something went wrong");
        System.exit(0);
        return null;
    }

    public static String currentBranchFinder() {
        File currentBranch = new File(Main.BRANCHES, "current.txt");
        return Utils.readContentsAsString(currentBranch);
    }

    public static void setHead(String newHead, String branchName) {
        File branch = new File(Main.BRANCHES, branchName);
        if (branch.exists()) {
            Utils.writeContents(branch, newHead);
            return;
        }
        System.out.println("A branch with that name does not exist.");
        System.exit(0);
    }
}
