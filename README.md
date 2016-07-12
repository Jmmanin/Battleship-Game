# Battleship-Game
A multiplayer battleship game playable over a network connection

Resources- folder containing all image files used
</br>Attack- object used to communicate attacks between players
</br>BSClientThread- thread created to handle communications with server
</br>BSGrid- creates battleship grid
</br>BattleShipUI- main game UI, starts after player has connected and finished ship placement
</br><b>BattleshipClient</b>- Open to start client! Creates window for user to establish server connection and start game 
</br><b>BattleshipServer</b>- Open to start server! Creates server UI and a seperate thread to handle communications b/t players 
</br>PlaceShips- allows users to place their ships to start a game
</br>Ship- object used to store data related to players' ships
