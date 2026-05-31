# Testing

## Singleplayer

- Load a world and verify the mod activates without any setup.
- Take melee damage from a hostile mob or test target and confirm one jump reset occurs.

## Multiplayer

- Join a Fabric-compatible server.
- Verify the mod triggers on valid combat hits and does not interfere with chat, inventory, or menus.

## Dedicated Servers

- Test on a dedicated server with standard PvP combat.
- Confirm the client behavior remains local and does not require server-side installation.

## Combat Tests

- Sword hits
- Axe hits
- Fist hits
- Combo hits
- High CPS opponents
- Low CPS opponents

## Cooldown Tests

- Confirm repeated damage in the same hurt window does not trigger duplicate jumps.
- Confirm the next legitimate combat hit can still trigger after the cooldown window expires.

## Release Tests

- Build from a `vMAJOR.MINOR.PATCH` tag.
- Verify the JAR version, Fabric metadata version, and release version all match.
