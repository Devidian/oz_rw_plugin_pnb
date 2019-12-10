# planksNbeams
A Rising World Java plug-in generating non-standard planks and beams.
A plug-in allowing **any player** to get planks, triangular planks, beams, logs and 4 type of windows textured with **any of 201 built-in RW construction textures** (in other words, the textures of the blocks, which in full creative mode can be obtained wit the console command "item woodplank/beam <qty> <textureId>"), even **outside of full creative mode**.
It is not intended as a way of cheating, but as a way to extend building possibilities. Then, items have a cost in resources: by default, the same cost a block with the same texture would have: stones, sandstones, dirt, iron ingots, copper ingots (but wood lumbers are used instead of logs).
## Features
- Any of the `201` built-in construction textures can be used (there are 4 screens as the one shown above, through the Up and Down arrows on the right).
- Selection of Plank, Beam, Log, Triangle and the 4 type of window frames.
- 1 to 64 quantity.
- "Min" and "Max" button to jump to minimum (1) and maximum (64) quantity.
- ID of textures.
- The cost in resources of the current selection is shown.
- The player must have enough resources in the inventory (any slot) to "buy" all the selected items; if there are not enough resources, the player is notified in the chat and nothing happens.
- On buy, the resources are withdrawn from the inventory and the new items left in it.
- By default, one item costs one resource, but greedy server owners can set a higher cost in the plug-in settings.properties.
- A setting allows to have item for free (no resources needed) for admins and / or while in creative mode, as in the vanilla game.
- All GUI texts can be localised to any other language. Language is determined by `player.getSystemLanguage()`
____________________

# Important - Important - Important - Important:
You need to install `Omega Zirkel Tools` also known as `shared lib` in atleast version `0.6.0` in order to run this Plugin. (it is a drop in replacement for `rwgui`)

Latest Version can be found [here](https://github.com/Devidian/oz_rw_plugin_tools/releases).
____________________

## Commands

|Command|Description|
|-|-|
|/pnb|Open Planks 'N Beams GUI|
|/pnb help|Show available commands|
|/pnb info|Show Plugin description & Credits|
|/pnb status|Show plugin version, language etc.|

## Installation

Extract the files in the ZIP placing the whole PlanksNBeams folder into the plugins folder of RW (if the plugins folder does not exist, just create one). The resulting hierarchy shall be:

```css
    ── RisingWorld
        ├── plugins
        │    ├── PlanksNBeams
        │    │    ├── assets...
        │    │    ├── i18n...
        │    │    ├── COPYING
        │    │    ├── HISTORY.en.md
        │    │    ├── HISTORY.md
        │    │    ├── OZ-PlanksNBeams-Plugin.jar
        │    │    ├── README.en.md
        │    │    ├── README.md
        │    │    └── settings.properties
        │    ├── shared
        │    │    ├── assets...
        │    │    └── lib
        │    │         ├── HISTORY.en.md
        │    │         └── tools.jar
        :    :
```

## Open points and known issues

- As built-in images cannot be accessed, the plug-in has its own, reduced, version of the textures for the GUI and must send them to the client for display; this may affect bandwidth. Only actual practice may tell if and how much. (This only refers to the images used in the GUI, not to the textures actually used to texturise the items, which RW manages internally).

- The GUI images come from RW own textures, scaled to 50% and cropped to 64x64 pixels (to fit a reasonable amount of textures in a screen). I hope this does not result in a copyright infringement. I tried to put the images inside the .jar, as a measure of (rather limited) protection, but I could not find a way (see here for details).
