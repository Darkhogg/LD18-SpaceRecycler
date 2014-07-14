Space Recycler
==============

Space Recycler is a game originally created for [Ludum Dare 18][ld18]. The game
was created from scratch and entirely by me in less than 48 hours.

  [ld18]: http://www.ludumdare.com/compo/ludum-dare-18/

Your objective is to kill as many monsters as you can. Surviving is actually not
productive (you lose points for being alive without killing), but will spawn
more enemies wich give you more and more points.

The whole game revolves around the theme *Enemies as Weapons*. In Space Recycler
this becomes apparent by using the rests of dead enemies to kill other enemies.
There is a single energy bar that lowers when you receive damage, shoot, attract
debris or have much weight already attracted.

### Controls

| Key | Action |
|----:|:-------|
| `←` | Turns the sip counter-clockwise
| `→` | Turns the sip clockwise
| `↑` | Advances the ship
| `V` | Shoots some weak energy
| `C` | Propels your attracted matter forward
| `X` | Attracts matter nearby or directly in front of you
| `K` | Kills yourself


Building/Running the Game
-------------------------

In the [releases](releases) page you can find pre-compiled versions of the game
in JAR format. In most systems you should be able to double-click the file to
run it if you have Java installed. If you have problems, the Internet is full of
advice on how to execute a runnable JAR.

If you want to build the game from source, the easiest way is to install Ant and
run the `ant` command inside of the game directory. Running `ant run` will
have the same effect, in addition to immediately run the game.