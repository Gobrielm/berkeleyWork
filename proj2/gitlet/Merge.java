package gitlet;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class Merge {
    public static void merge(String branchName) {
        branchName += ".txt";
        branchChecker(branchName);
        //Find the common ancestor, first for branchName
        File headCommit = Tools.headParser(Tools.currentBranchFinder());
        File prevFile = new File(headCommit, "metadata" + Main.FILESPACE + "prev.txt");
        File otherHeadCommit = Tools.headParser(branchName);
        File otherPrevFile = new File(otherHeadCommit, "metadata" + Main.FILESPACE + "prev.txt");
        File commonDescendant = null;
        //Contains all the files that are children of branchName
        HashSet<File> check = new HashSet<>(); //Following prev
        HashSet<File> check1 = new HashSet<>(); //Following prev2 if it exists
        while (true) {
            check.add(headCommit);
            if (Utils.readContentsAsString(prevFile).equals("null")) {
                break;
            }
            headCommit = new File(Main.COMMITS, Utils.readContentsAsString(prevFile));
            prevFile = new File(headCommit, "metadata" + Main.FILESPACE + "prev.txt");
        }
        File secondHeadCommit = Tools.headParser(Tools.currentBranchFinder());
        File prevFile2 = new File(secondHeadCommit, "metadata" + Main.FILESPACE + "prev2.txt");
        File secondOtherHeadCommit = Tools.headParser(branchName);
        File otherPrevFile2 = new File(secondOtherHeadCommit, "metadata" + Main.FILESPACE + "prev2.txt");

        if (prevFile2.exists()) {
            while (true) {
                check1.add(secondHeadCommit);
                if (!prevFile2.exists()) {
                    break;
                }
                secondHeadCommit = new File(Main.COMMITS, Utils.readContentsAsString(prevFile2));
                prevFile2 = new File(secondHeadCommit, "metadata" + Main.FILESPACE + "prev2.txt");
            }
        }
        //Then compare to current Branch and find first common descendant
        //2 sets, 2 ptrs
        if (!check1.isEmpty() && otherPrevFile2.exists()) {
            while (true) {
                if (check.contains(otherHeadCommit)) {
                    commonDescendant = otherHeadCommit;
                    break;
                } else if (check1.contains(secondOtherHeadCommit)) {
                    commonDescendant = secondOtherHeadCommit;
                    break;
                }
                otherHeadCommit = new File(Main.COMMITS, Utils.readContentsAsString(otherPrevFile));
                otherPrevFile = new File(otherHeadCommit, "metadata" + Main.FILESPACE + "prev.txt");
                if (otherPrevFile2.exists()) {
                    secondOtherHeadCommit = new File(Main.COMMITS, Utils.readContentsAsString(otherPrevFile2));
                    otherPrevFile2 = new File(secondOtherHeadCommit, "metadata" + Main.FILESPACE + "prev2.txt");
                }
            }
            //2 sets, 1 ptr
        } else if (!check1.isEmpty()) {
            while (true) {
                if (check.contains(otherHeadCommit)) {
                    commonDescendant = otherHeadCommit;
                    break;
                } else if (check1.contains(secondHeadCommit)) {
                    commonDescendant = secondHeadCommit;
                    break;
                }
                otherHeadCommit = new File(Main.COMMITS, Utils.readContentsAsString(otherPrevFile));
                otherPrevFile = new File(otherHeadCommit, "metadata" + Main.FILESPACE + "prev.txt");
            }
            //1 set, 1 ptr
        } else {
            while (true) {
                if (check.contains(otherHeadCommit)) {
                    commonDescendant = otherHeadCommit;
                    break;
                }
                otherHeadCommit = new File(Main.COMMITS, Utils.readContentsAsString(otherPrevFile));
                otherPrevFile = new File(otherHeadCommit, "metadata" + Main.FILESPACE + "prev.txt");
            }
        }
        errorCatching(headCommit, branchName, otherHeadCommit, commonDescendant);

    }
    private static void branchChecker(String branchName) {
        if (branchName.equals(Tools.currentBranchFinder())) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        File branch = new File(Main.BRANCHES, branchName);
        if (!branch.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
    }
    private static void errorCatching(File headCommit, String branchName, File otherHeadCommit, File commonDescendant) {
        //Reset head commits back to their original
        headCommit = Tools.headParser(branchName);
        otherHeadCommit = Tools.headParser(Tools.currentBranchFinder());
        if (commonDescendant == null) {
            System.out.println("An error has occurred, branches are somehow completely disjoint");
            System.exit(0);
        } else if (commonDescendant.equals(headCommit)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        } else if (commonDescendant.equals(otherHeadCommit)) {
            Branch.switchBranch(
                    branchName.substring(0, branchName.length() - 4));
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
        createFiles(branchName, commonDescendant);
    }
    private static void createFiles(String branchName, File commonDescendant) {
        File commit = new File(Main.COMMITS, Utils.readContentsAsString(new File(Main.BRANCHES, branchName)));
        File currCommit = Tools.headParser(Tools.currentBranchFinder());
        File otherCommitFolder = new File(Main.COMMITS, Utils.readContentsAsString(new File(Main.BRANCHES, branchName))
                + Main.FILESPACE + "blobs");
        File currBranch = new File(Main.BRANCHES, Tools.currentBranchFinder());
        File currCommitFolder = new File(Main.COMMITS, Utils.readContentsAsString(currBranch)
                + Main.FILESPACE + "blobs");
        File descendantFolder = new File(commonDescendant, "blobs");
        checkForUncommitedChanges();
        Restore.checkForUntracakedFiles(commit);
        ArrayList<File> toBeMerged = new ArrayList<>();
        File[] filesInTargetCommit = otherCommitFolder.listFiles();
        File[] filesInCurrCommit = currCommitFolder.listFiles();
        File[] filesInSplit = new File(commonDescendant, "blobs").listFiles();
        for (File x: filesInSplit) {
            File curr = new File(currCommitFolder, x.getName());
            File target = new File(otherCommitFolder, x.getName());
            if (target.exists() && curr.exists()) {
                String tar = Utils.readContentsAsString(target);
                String cur = Utils.readContentsAsString(curr);
                String temp = Utils.readContentsAsString(x);
                if (tar.equals(cur)) {
                    toBeMerged.add(x);
                } else if (!tar.equals(temp) && cur.equals(temp)) {
                    toBeMerged.add(target);
                } else if (tar.equals(temp)) {
                    toBeMerged.add(curr);
                } else {
                    System.out.println("Encountered a merge conflict.");
                    toBeMerged.add(mergeError(curr, target));
                }
            } else if (target.exists()) {
                if (!Utils.readContentsAsString(target).equals(Utils.readContentsAsString(x))) {
                    System.out.println("Encountered a merge conflict.");
                    toBeMerged.add(mergeError(curr, target));
                }
            } else if (curr.exists()) {
                if (!Utils.readContentsAsString(curr).equals(Utils.readContentsAsString(x))) {
                    System.out.println("Encountered a merge conflict.");
                    toBeMerged.add(mergeError(curr, target));
                }
            }
        }
        for (File x: filesInTargetCommit) {
            File curr = new File(currCommitFolder, x.getName());
            File split = new File(descendantFolder, x.getName());
            if (!split.exists()) {
                if (!curr.exists()) {
                    toBeMerged.add(x);
                }
            }
        }
        for (File x: filesInCurrCommit) {
            File other = new File(otherCommitFolder, x.getName());
            File split = new File(descendantFolder, x.getName());
            if (!split.exists()) {
                if (!other.exists()) {
                    toBeMerged.add(x);
                } else if (other.exists() && Utils.readContentsAsString(other).equals(Utils.readContentsAsString(x))) {
                    toBeMerged.add(x);
                } else {
                    System.out.println("Encountered a merge conflict.");
                    toBeMerged.add(mergeError(x, other));
                }
            }
        }
        createNewHeadCommit(branchName, currCommit, commit);
        File mergeCommit = Tools.headParser(Tools.currentBranchFinder());
        File mergeBlobs = new File(mergeCommit, "blobs");
        for (File x: toBeMerged) {
            File temp = new File(mergeBlobs, x.getName());
            Utils.writeContents(temp, Utils.readContentsAsString(x));
        }
        for (File x: mergeBlobs.listFiles()) {
            File actualFile = new File(Main.BLOBS, Utils.readContentsAsString(x));
            File newFile = new File(Main.CWD, x.getName());
            Utils.writeContents(newFile, Utils.readContentsAsString(actualFile));
        }
    }

    private static void createNewHeadCommit(String otherBranch, File currCommit, File otherCommit) {
        String commitNumber = Integer.toString(Main.COMMITS.listFiles().length);
        File newCommit = new File(Main.COMMITS, Utils.sha1("commit" + commitNumber));
        String commitFileName = Utils.sha1("commit" + commitNumber);
        newCommit.mkdir();
        Metadata.metadata(commitFileName, "Merged " + otherBranch.substring(0, otherBranch.length() - 4)
                        + " into " + Tools.currentBranchFinder().
                        substring(0, Tools.currentBranchFinder().length() - 4) + ".",
                currCommit, otherCommit);
        File newBlobs = new File(newCommit, "blobs");
        newBlobs.mkdir();

        Tools.setHead(commitFileName, Tools.currentBranchFinder());
    }
    private static void checkForUncommitedChanges() {
        if (Main.ADDITION.listFiles().length != 0 || Main.REMOVE.listFiles().length != 0) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
    }
    private static File mergeError(File input1, File input2) {
        File blob1 = new File(Main.BLOBS, Utils.readContentsAsString(input1));
        File blob2 = null;
        String contents2 = "";
        if (input2.exists()) {
            blob2 = new File(Main.BLOBS, Utils.readContentsAsString(input2));
            contents2 = (Utils.readContentsAsString(blob2));
        }
        String contents1 = Utils.readContentsAsString(blob1);
        //Create new file in blobs
        File extra = new File(Main.BLOBS, Utils.sha1(input1.getName()));
        //Create a new file in CWD to return
        File toReturn = new File(Main.CWD, (input1.getName()));
        Utils.writeContents(toReturn, Utils.sha1(input1.getName()));
        String toWrite = "<<<<<<< HEAD\n" + contents1
                + "=======\n" + contents2 + ">>>>>>>\n";
        Utils.writeContents(extra, toWrite);
        return toReturn;
    }
}
