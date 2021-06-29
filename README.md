# Empire Minecraft Utilities

[![](https://waffle.coffee/modrinth/emcutils/downloads?style=flat-square)](https://modrinth.com/mod/emcutils)
![](https://tokei.rs/b1/github/wafflecoffee/Empire-Minecraft-Utilities?category=code)
![](https://tokei.rs/b1/github/wafflecoffee/Empire-Minecraft-Utilities?category=files)
![](https://img.shields.io/badge/environment-client-1976d2)

Empire Minecraft Utilities, aka emcutils, is a 1.16 and 1.17 mod containing a collection of small utilities for players of [Empire Minecraft](https://ref.emc.gs/GreenMeanie).  
Feature idea credits go to [Giselbaer](https://u.emc.gs/Giselbaer) and [wafflecoffee](https://u.emc.gs/wafflecoffee).

Made by [MrFrydae](https://u.emc.gs/GreenMeanie), and available under the MIT License.

Modrinth page for downloads: https://modrinth.com/mod/emcutils

**This mod requires [Mod Menu](https://modrinth.com/mod/modmenu)**. In addition, [Not Enough Crashes](https://www.curseforge.com/minecraft/mc-mods/not-enough-crashes) and [VoxelMap](https://www.curseforge.com/minecraft/mc-mods/voxelmap) are recommended.

## Feature List

* Chat channel buttons above the chat input bar
* Automatic teleportation to a Residence on another server
* Easier viewing of usable Custom Items such as [Pot of Gold](https://wiki.emc.gs/pot-of-gold)
* Customizable tab list sorting (configurable from Mod Menu)
* Vault buttons
* Integration with VoxelMap
  * Automatic world detection and map confirmation/creation
  * Clicking on a residence will teleport you to that residence
* And more to come :)

## Maven/Development

Technically, emcutils is on a Maven. Why you would ever want to use it, I'm not sure, but here it is documented nonetheless:

```groovy
repositories {
  maven { url "https://api.modrinth.com/maven" }
}

dependencies {
  modImplementation "maven.modrinth:emcutils:${project.emcutils_version}"
}
```

## Disclaimer

This mod is not sponsored by nor affiliated with Empire Minecraft, Starlis LLC, or Mojang Studios. It has been approved for use on Empire Minecraft in accordance with its [approved mod](https://mods.emc.gs) policies.
