package gitlet;


import java.io.File;

public class Restore {
    static final File CWD = Main.CWD;
    static final int LENGTHOFCOMMIT = 40;
    private static File fileNameToFileInCommit(String fileName) {
        File head = Tools.headParser(Tools.currentBranchFinder());
        File storedBlobs = new File(head, "blobs");
        //The file that contains the number to the blob that is to be commited to fileName
        File returnFile = null;
        File[] filesToCheck = storedBlobs.listFiles();
        for (File x: filesToCheck) {
            if (x.getName().equals(fileName)) {
                returnFile = x;

            }
        }
        return returnFile;
    }
    //Put in the File reference from inside commit and receive the blob file
    private static File fileReferenceToBlob(File file) {
        if (file != null && file.exists()) {
            String reference = Utils.readContentsAsString(file);
            return new File(Main.BLOBS, reference);
        }
        return null;
    }

    public static void restore(String fileName) {
        File file = new File(CWD, fileName); //Original
        File fileToCheck = fileNameToFileInCommit(fileName); //File in Commit
        File theBlob = fileReferenceToBlob(fileToCheck);
        //Has the file been found
        if (theBlob.exists()) {
            Utils.writeContents(file, Utils.readContentsAsString(theBlob));
            File ifInDelete = new File(Main.REMOVE, fileName);
            if (ifInDelete.exists()) {
                ifInDelete.delete();
            }
            //Successfully copied the file over
            return;
        }
        //If not does not exist, exit
        System.out.println("File does not exist in that commit.");
        System.exit(0);
    }

    public static void restore(String commitID, String fileName) {
        File file = new File(CWD, fileName);
        File commitFolder = null;
        if (commitID.length() < LENGTHOFCOMMIT) {
            File[] commits = Main.COMMITS.listFiles();
            for (File x: commits) {
                if (x.getName().startsWith(commitID)) {
                    commitFolder = x;
                }
            }
        } else {
            commitFolder = new File(Main.COMMITS, commitID);
        }
        if (commitFolder == null || !commitFolder.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        File blobsFolder = new File(commitFolder, "blobs");
        File referenceFile = new File(blobsFolder, fileName);
        File blob = fileReferenceToBlob(referenceFile);

        if (blob != null && blob.exists() && file.exists()) {
            Utils.writeContents(file, Utils.readContentsAsString(blob));
            File ifInDelete = new File(Main.REMOVE, fileName);
            if (ifInDelete.exists()) {
                ifInDelete.delete();
            }
            //Successfully copied the file over
            return;
        }
        //If not does not exist, exit
        System.out.println("File does not exist in that commit.");
        System.exit(0);
    }

    public static void reset(String commitId) {
        File commitFolder = null;
        if (commitId.length() < LENGTHOFCOMMIT) {
            File[] commits = Main.COMMITS.listFiles();
            for (File x: commits) {
                if (x.getName().startsWith(commitId)) {
                    commitFolder = x;
                    commitId = commitFolder.getName();
                }
            }
        } else {
            commitFolder = new File(Main.COMMITS, commitId);
        }
        if (commitFolder == null || !commitFolder.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        //All set to go
        //Clearing CWD before
        checkForUntracakedFiles(commitFolder);
        File[] filesToRestore = new File(commitFolder, "blobs").listFiles();
        for (File x: filesToRestore) {
            File temp = new File(CWD, x.getName());
            File actualFile = new File(Main.BLOBS, Utils.readContentsAsString(x));
            Utils.writeContents(temp, Utils.readContentsAsString(actualFile));
        }
        //After porting all the files to CWD, clear addition and remove as well
        Branch.clearStagingArea();
        Tools.setHead(commitId, Tools.currentBranchFinder());
    }

    public static File commitNameToBlobName(File commitName, String fileName) {
        File fileStorage = new File(commitName, "blobs");
        File curr = new File(fileStorage, fileName);
        if (!curr.exists()) {
            //The file is in the CWD but not tracked and is being deleted
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            System.exit(0);
        }
        File blob = new File(Main.BLOBS, Utils.readContentsAsString(curr));
        return blob;
    }


    public static void checkForUntracakedFiles(File commitFolder) {
        String commit = Utils.readContentsAsString(new File(Main.BRANCHES, Tools.currentBranchFinder()));
        File commitFile = new File(Main.COMMITS, commit);
        File blobs = new File(commitFile,  "blobs");

        File[] targetBlobs = new File(commitFolder,  "blobs").listFiles();
        File[] checks = CWD.listFiles();

        for (File x: targetBlobs) {
            File temp = new File(CWD, x.getName());
            if (temp.exists()) {
                File temp1 = new File(blobs, x.getName());
                File inBlobs = commitNameToBlobName(commitFile, x.getName());
                if (temp1.exists() && !(Utils.readContentsAsString(inBlobs)).equals(Utils.readContentsAsString(temp))) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
        }
        //All files are okay to delete
        for (File x: checks) {
            if (x.getName().equals(".gitlet")) {
                continue;
            }
            x.delete();
        }
    }
}

