# My-Small-Projects

Here are some of my project codes.

15 puzzle: research algorithm and heuristic function
Paxos: client server and paxos simulation
Recursion tutor: visualization of recursion of tree structure and stacks
Sorts: implementation of basic sort algorithms

Hex Game: 
This is a two person board chess game that allows PVP and PVE. An agent is designed to play this game. This is done in a 9*9 board and can also be used in smaller or larger size.

It uses MCTS to find the most promising move for each step. A specific amount of random plays are done to collect the evaluatoin data.
To speedup the search algorithm, a patternengine is designed to detect some specific patterns, from which some cells could be ignored, either considered as dead cell(place a piece there is useless) or captured cell(one player dominants this cell). 

A winning state check algorithm is also used to check whether a player has won or is guaranteed to win(AI will give up if opponent is guaranteed to win). A helper array is used for each player to detect winning state, and avoid useless computation when it is obvious that no one may win.

Evaluation function is design to improve agents selection, especially for beginning game(end game is easy to handle with MCTS). Each move in the random play is weighted by the amount of steps to end the game and the total winning rate of all random plays. 

PVP and PVE test. 
In PVP tests, agents of same parameter have almost 50% winnning rate. Who plays first does not effect the game very much. Agents with more random play trials are more likely to win. And the evaluation function also greatly increases the winning rate.
In PVE tests, the agent plays very good from mid game to the end. Once it has advantages, it never loses because it does no mistakes. And once in disadvantage state, the agent could keep blocking the opponent and wait until his/her mistake. However in the beginning game, because of the larger branching factor and more confusing state, Agent is easy to make some mistakes(play useless moves or ignore opponent's dangerous moves). Adjustment is done to the beginning game with some restrictions so that a fair beginning can be done.


