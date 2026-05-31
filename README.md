    # PVP Optimizer

    PVP Optimizer is a client-side Fabric mod that automatically performs a jump reset after the local player takes valid combat damage. It is built for low overhead, immediate activation, and tag-driven releases.

    ## Features

    - Always-on client-side jump reset behavior.
    - Filters for combat-relevant damage sources.
    - One-shot scheduling with a strict cooldown.
    - Minimal debug logging disabled by default.
    - No config files, no settings screen, and no user controls.

    ## Installation

    1. Install Java 21.
    2. Download the release JAR from GitHub Releases.
    3. Place the JAR in your Minecraft `mods` folder.
    4. Launch Minecraft with Fabric Loader.

    ## Troubleshooting

    - Ensure you are using a Fabric client profile, not a dedicated server profile.
    - Confirm the mod version matches your Minecraft version.
    - If the jump does not trigger, verify that debug logging is enabled in code for local testing.

    ## FAQ

    ### Does this need configuration?

    No. The mod is always enabled and uses built-in timing.

    ### Does this work on multiplayer servers?

    Yes, as a client-side mod. Server rules still apply.

    ### Does it collect data?

    No telemetry, analytics, tracking, or external service calls are included.

    ## Contributing

    Contributions should keep the mod lightweight, deterministic, and free of user-facing settings. Use Java 21 and preserve the client-only architecture.

    ## Development Setup

    1. Install Java 21.
    2. Open the repository in VS Code or IntelliJ IDEA.
    3. Run `./gradlew build`.
    4. The launcher downloads Gradle automatically the first time it runs.
    5. Use the generated Fabric run configuration from Gradle/Loom if your IDE imports it automatically.

    ## Release Workflow

    Releases are created from `vMAJOR.MINOR.PATCH` tags. The prepare-release workflow tags the current version from `gradle.properties` and then bumps the repository to the next patch version automatically. The tag-driven release workflow builds the project, verifies the output, and publishes GitHub Release assets automatically.
