# Intelligent Game Designer
This is a repository for the group project of team Juliet. It aims to automate the process of level design for a game similar to Candy Crush Saga.

## Branching
To avoid things going horribly wrong, we should make updates for different system components on different branches. Perhaps we could use the following:
* `master` - **ONLY** for production release code (or small changes at the start)
* `develop` - For general development, to which the system component branches are frequently merged (see below).
* `coregame` - Used for changes to the `coregame` package.
* `simulatedplayers` - Used for changes to the `simulatedplayers` package.
* `leveldesigner` - Used for changes to the `leveldesigner` package.
* `gameinterface` - Used for changes to the `gameinterface` package.
