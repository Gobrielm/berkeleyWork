package gitlet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Metadata {
    //Used to fill out metadata in commits and change branches
    public static void metadata(String path/*also name*/, String newMessage, File prev, File prev2) {
        File currentCommit = new File(Main.COMMITS, path);
        File metadata = new File(currentCommit, "metadata");
        File timestamp = new File(metadata, "timestamp.txt");
        File message = new File(metadata, "message.txt");
        File prevTxt = new File(metadata, "prev.txt");
        File prevTxt2 = new File(metadata, "prev2.txt");
        String prevFile;
        String prevFile2 = null;
        if (prev == null) {
            prevFile = "null";
        } else if (prev2 != null) {
            prevFile = prev.getName();
            prevFile2 = prev2.getName();
        } else {
            prevFile = prev.getName();
        }
        metadata.mkdir();
        //Current Time
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
        format.setTimeZone(TimeZone.getDefault());
        Date now = new Date();
        Date unix = new Date(0);
        Utils.writeContents(timestamp, format.format(now));
        //Message
        Utils.writeContents(message, newMessage);
        Utils.writeContents(prevTxt, prevFile);
        if (prevFile2 != null) {
            Utils.writeContents(prevTxt2, prevFile2);
        }
        if (newMessage.equals("initial commit")) {
            File main = new File(Main.BRANCHES, "main.txt");
            File current = new File(Main.BRANCHES, "current.txt");
            Utils.writeContents(main, path);
            Utils.writeContents(current, "main.txt");
            Utils.writeContents(timestamp, format.format(unix));
        } else {
            String currentBranch = Tools.currentBranchFinder();
            File branchToChange = new File(Main.BRANCHES, currentBranch);
            Utils.writeContents(branchToChange, path);
        }
    }
}
