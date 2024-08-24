# Gitlet Design Document

**Name**: Gabriel P McKay with some general discussion with Julian Robin

## Classes and Data Structures
        
File Structure

    .gitlet
    |
    -commits
        -sha1(commit0)
            -metadata <date,message,head,prev,branch>
            -blobs <references to blobs>
            -prev <reference to prev commit>
        -sha1(commit1)
            -metadata <date,message>
            -blobs <references to blobs>
            -prev <reference to commit0>
    -addition
        -(Any files ready to be added go here)
    -removal
        -(Any files ready to be deleted go here)
    -blobs
        -sha1(fileName)
        ...
    -branchs
        -current <current branchName>
        -main <reference to head commit>
        ...

Classes

    Main: Houses the main switch case function along with
    many helper functions/algorithms like headParser.
    Also houses the init function.
    
    AddRemove: Houses the functions Add and rm.

    Commit: Houses the commit function.

    Log: Houses the global-log, log, and find functions.

    Status: Houses the status function.

    Branch: Deals with the branch, merge, rm branch, and switch.

    Restore: Houses the restore function.

Algorithms/Functions

    headParser: Returns the current Head

    metadata: Creates the metadata for a new commit.

    doesGitletExist: Checks to see if the .gitlet folder
    exists to prevent errors accessing it.

    commitNameToBlobName: What the name suggests, returns the file, takes in strings

    branchNameToBlobName: What the name suggests, returns the file, takes in strings