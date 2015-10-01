---
output: pdf_document
---

gitStati
============


Design Document
-------------

  The main idea of gitStati is to take a look at a user's git repositories and returning information about their status. To implement this plan, we decided on a tree data structure. At a high-level, the system is easy enough to explain. We begin with a recursive search, going through a person's entire file system. Here, we search for directories containing ".git", denoting them as git-enabled repositories. Upon finding such a thing, we add that directory to our tree structure and continue on. This process essentially recreates user's entire collection of git repositories as a tree, with the tree emulating file system structures- sub-directories appear as a new leaf branching from the main directory, for example. From here, we simply traverse through the tree we have created in order to easily access any given git repository a user wants to check, then perform whatever operation the user requests. The options provided to the user include if they would like to check the status of a specific repository, or if they would like to check the status of every git repository.  Features such as color-coding make the experience as a whole more user-friendly. For a more succinct version of the steps involved in the making of our system, please refer to our design diagram.