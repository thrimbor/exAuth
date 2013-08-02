exAuth
======

#### What is exAuth? ####
exAuth is an easy to use password-based authentication plugin for bukkit which uses a MySQL-table to authenticate the users. This comes in handy if you don't run your server in online-mode. A player which joins your server is unable to take any action (move, chat, use inventory...) unless he logs in with the /login command. 

#### Does it store the passwords in plain text? ####
No, it uses a hashing algorithm. The user can decide between MD5, SHA1, SHA-256 and SHA-512.

#### Is salting supported? ####
Currently not, but if I find time I might implement it.
