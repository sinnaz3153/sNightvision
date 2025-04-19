# sNightvision Plugin
![title](https://cdn.modrinth.com/data/cached_images/6a373af47ec72460c309748e60028734cedd2d7a.png)

A simple yet powerful night vision plugin for Paper servers.

**Last Version:** 1.21.5-3.2  
**Author:** sinnaz3153  
**API Version:** 1.21
**Java:** 21+

## What is sNightVision
This plugin allows players to toggle night vision effect for themselves and others. Features hex color support and configurable messages.

## Features
- Toggle night vision effect
- Configurable duration (infinite or timed)
- Hex color support in messages
- Permission-based access control
- Admin reload functionality
- Tab completion support
- Automatic config backup
- Config version management

## Commands
- `/nv` - Toggle night vision for yourself
- `/nv [player]` - Toggle night vision for another player
- `/nv help` - Show help menu
- `/nvadmin reload` - Reload plugin configuration
- `/nvadmin help` - Show admin help menu

## Permissions
- `nightvision.use` - Use night vision command (default: op)
- `nightvision.use.other` - Toggle night vision for others (default: op)
- `nightvision.admin` - Access admin commands (default: op)

## Configuration
```yaml
config-version: "1.2"
prefix: "&#62b5eaNightVision&7 >>&f "
effect:
  duration: -1  # -1 for infinite, or time in seconds

messages:
  plugin-enabled: "&aplugin has been enabled!"
  plugin-disabled: "&cplugin has been disabled!"
  nightvision-enabled: "&bNightvision has been enabled!"
  nightvision-disabled: "&cNightvision has been disabled!"
  no-permission: "&cYou don't have permission to use this command!"
  plugin-reloaded: "&bPlugin configuration reloaded!"
  player-not-found: "&cPlayer not found!"
  nightvision-enabled-other: "&bNightvision has been enabled for &f%player%&a!"
  nightvision-disabled-other: "&cNightvision has been disabled for &f%player%&c!"
  help:
    header: "&8====== &#62b5eaNightVision Help &8======"
    nightvision: "&b/nightvision &8- &7Toggle nightvision for yourself"
    nightvision-player: "&b/nightvision <player> &8- &7Toggle nightvision for another player"
    admin-reload: "&b/nightvisionadmin reload &8- &7Reload the plugin configuration"
    footer: "&8=========================="
```
