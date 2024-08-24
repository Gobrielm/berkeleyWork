package gitlet;

import java.io.File;

/**
 * Represents a gitlet commit object.
 * Takes in all the things that have been staged for adding and puts them to commit
 *
 * @author Gabriel McKay
 */
public class Commit {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    static final File CWD = Main.CWD;
    static final File GITLET = new File(CWD, ".gitlet");
    static final File ADDITION = new File(GITLET, "addition");
    static final File REMOVE = new File(GITLET, "remove");
    static final File COMMITS = new File(GITLET, "commits");
    static final File BLOBS = new File(GITLET, "blobs");

    /**
     * The message of this Commit.
     */
    public static void commit(String message) {
        //File and folder locations
        String commitNumber = Integer.toString(COMMITS.listFiles().length);
        File newCommit = new File(COMMITS, Utils.sha1("commit" + commitNumber));
        String commitFileName = Utils.sha1("commit" + commitNumber);
        newCommit.mkdir();
        File currentHead = Tools.headParser(Tools.currentBranchFinder());
        File headBlobs = new File(currentHead, "blobs");
        File commit = new File(COMMITS, commitFileName);
        File blobFolder = new File(commit, "blobs");
        blobFolder.mkdir(); //Folder for the fileName -> blobs
        Metadata.metadata(commitFileName, message, currentHead, null);



        //Add files from prev commit
        File[] oldtrackedFiles = headBlobs.listFiles();
        for (File x : oldtrackedFiles) {
            File newFile = new File(blobFolder, x.getName());
            Utils.writeContents(newFile, Utils.readContentsAsString(x));
        }
        //Check to see if files are staged for deletion
        File[] filesToNotInclude = REMOVE.listFiles();
        for (File x: filesToNotInclude) {
            File check = new File(blobFolder, x.getName());
            if (check.exists()) {
                check.delete();
            }
            x.delete();
        }

        File[] children = ADDITION.listFiles();
        //Goes through all files in addition and does stuff to them
        for (File x : children) {
            //In each commit with a file called the name of the file with its content being the name of the blob
            File trackedBlob = new File(blobFolder, x.getName());
            Utils.writeContents(trackedBlob, Utils.sha1("blob" + BLOBS.listFiles().length));
            //Blobs storing actual data
            File newBlob = new File(BLOBS, Utils.sha1("blob" + BLOBS.listFiles().length));
            Utils.writeContents(newBlob, Utils.readContents(x));
            x.delete(); //Destructive so be careful
        }
    }
}
