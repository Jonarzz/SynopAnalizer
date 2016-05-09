# Synop Analizer
Java application made to analize and save data read from Synop reports.

![synop](http://i.imgur.com/RceFpY8.png)

Synop report examples:
```
AAXX 14004 78948 11578 60711 10263 20237 30107 40115 60001 70522 83801 333 02207 10307 20254 59010 83824 84079
AAXX 14004 78970 11463 71003 10265 20241 30078 40103 6///1 71582 81802 333 02209 10336 20242 59011 81818 86078 90920
AAXX 14004 17607 NIL
AAXX 14004 17609 12970 73507 10126 20079 30208 40212 58012 60001 80006 333 87275
BBXX CGBY 14004 99487 71235 46/// /0601 10046 21001 40173 55003 7//// 8//// 22200 0//// 2////
AAXX 14004 64500 32460 72010 10270 20241 30096 40109 875// 333 0//00 59001 84610 87640
OOXX FRP31 13221 99052 70527 ///// 00091 26/// /0000 10249 29095 30169 92237 333 60000
OOXX FRP31 13221 99052 70527 ///// 00091 26/// /0000 10248 29095 30166 92247 333 60000
```

Data is saved in PostgreSQL database. The queries are sent directly from the application.

---
##Treeware License
Basically MIT License, but if you use the code (learning or project purposes), you have to plant at least one tree at some future time.
