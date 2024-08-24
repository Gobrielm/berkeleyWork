package gitlet;

import java.io.File;
import java.nio.file.FileSystems;

public class Main {

    //File Folders
    static final File CWD = new File(".");
    static final File GITLET = new File(CWD, ".gitlet");
    static final File ADDITION = new File(GITLET, "addition");
    static final File REMOVE = new File(GITLET, "remove");
    static final File BRANCHES = new File(GITLET, "branches");
    static final File COMMITS = new File(GITLET, "commits");
    static final File BLOBS = new File(GITLET, "blobs");
    static final String FILESPACE = FileSystems.getDefault().getSeparator();

    public static void main(String[] args) {
        if (args.length <= 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        if (!args[0].equals("init")) {
            doesGitletExist();
        }
        switch (firstArg) {
            case "init":
                init();
                break;
            case "add":
                if (args.length <= 1) {
                    fileError(args);
                }
                AddRemove.add(args[1]);
                break;
            case "rm":
                if (args.length <= 1) {
                    fileError(args);
                }
                AddRemove.remove(args[1]);
                break;
            case "commit":
                commitError(args);
                Commit.commit(args[1]);
                break;
            case "log":
                Log.log(COMMITS);
                break;
            case "global-log":
                Log.globalLog(COMMITS);
                break;
            case "find":
                if (args.length == 1) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                Log.find(args[1]);
                break;
            case "reset":
                if (args.length == 1) {
                    System.out.println("Please enter a commit ID.");
                    System.exit(0);
                }
                Restore.reset(args[1]);

                break;
            case "branch":
                branchError(args);
                Branch.branch(args[1]);
                break;
            case "merge":
                branchError(args);
                Merge.merge(args[1]);
                break;
            case "switch":
                branchError(args);
                Branch.switchBranch(args[1]);
                break;
            case "rm-branch":
                branchError(args);
                Branch.removeBranch(args[1]);
                break;
            case "status":
                Status.status();
                break;
            case "restore":
                restoreError(args);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }
    public static void doesGitletExist() {
        if (!GITLET.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }
    public static void branchError(String[] args) {
        if (args.length <= 1) {
            System.out.println("Please enter a branch name.");
            System.exit(0);
        }
    }
    public static void fileError(String[] args) {
        System.out.println("Please enter a file name");
        System.exit(0);
    }
    public static void commitError(String[] args) {
        if (args.length <= 1 || args[1].isEmpty()) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        } else if (ADDITION.listFiles().length <= 0 && REMOVE.listFiles().length <= 0) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
    }
    public static void restoreError(String[] args) {
        if (args.length < 3) {
            System.out.println("Please enter a file name.");
            System.exit(0);
        } else if (args.length == 4 && !args[2].equals("--")) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        } else if (args.length == 3) {
            Restore.restore(args[2]);
        } else if (args.length >= 4) {
            Restore.restore(args[1], args[3]);
        }
    }
    public static void init() {
        if (GITLET.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        CWD.mkdir();
        GITLET.mkdir();
        COMMITS.mkdir();
        BLOBS.mkdir();
        ADDITION.mkdir();
        REMOVE.mkdir();
        BRANCHES.mkdir();
        File commit = new File(COMMITS, Utils.sha1("commit0"));
        commit.mkdir();
        File blobs = new File(commit, "blobs");
        blobs.mkdir();
        Metadata.metadata(Utils.sha1("commit0"), "initial commit", null, null);

    }
}
