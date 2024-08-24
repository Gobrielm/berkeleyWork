package gitlet;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.Arrays;

public class AddRemove {
    static final File CWD = Main.CWD;
    static final File GITLET = new File(CWD, ".gitlet");
    static final File ADDITION = new File(GITLET, "addition");
    static final File REMOVE = new File(GITLET, "remove");
    static final File BLOBS = new File(GITLET, "blobs");

    public static void add(String fileName) {
        //Check through the directory for the file with the fileName
        String currentBranch = Tools.currentBranchFinder();
        File currentHead = Tools.headParser(currentBranch);
        File savedFilesFolder = new File(currentHead, "blobs");
        File actualFile = new File(CWD, fileName);
        if (actualFile.exists()) {
            //Put it in addition
            File fileBlob = new File(ADDITION, fileName);
            //If the file is staged for deletion remove from remove
            File inRemove = new File(Main.REMOVE, fileName);
            if (inRemove.exists()) {
                inRemove.delete();
            }
            File fileOfSameName = new File(savedFilesFolder, actualFile.getName());
            if (fileBlob.exists() && Arrays.equals(Utils.readContents(fileBlob), Utils.readContents(actualFile))) {
                //If it is the same and exists do not add the file, do nothing
                return;
            }
            //Checking to see if the file is the same, if so don't add
            if (fileOfSameName.exists()) {
                String reference = Utils.readContentsAsString(fileOfSameName);
                File fileInBlobs = new File(BLOBS, reference);
                if (Arrays.equals(Utils.readContents(fileInBlobs), Utils.readContents(actualFile))) {
                    //System.out.println("Commiting file is the same as the one being added");
                    return;
                }
            }
            //Overwrite or create a new file as it is different

            Utils.writeContents(fileBlob, Utils.readContents(actualFile));
            return;

        }
        System.out.println("File does not exist.");
        System.exit(0);
    }

    public static void remove(String fileName) {
        File inAddition = new File(ADDITION, fileName);
        File currentHead = Tools.headParser(Tools.currentBranchFinder());
        File references = new File(currentHead, "blobs" + FileSystems.getDefault().getSeparator() + fileName);
        //If the file is staged for addition
        if (inAddition.exists()) {
            inAddition.delete();
            return;
        }
        //If the file is in the current commit
        if (references.exists()) {
            //Creates a copy in remove
            File toBeDeleted = new File(REMOVE, fileName);
            Utils.writeContents(toBeDeleted, Utils.readContentsAsString(references));
            //Deletes the original file
            File mainFile = new File(CWD, fileName);
            mainFile.delete();
            return;
        }
        System.out.println("No reason to remove the file.");
        System.exit(0);
    }
}
