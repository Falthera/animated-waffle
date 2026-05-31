# Release Process

1. The repository tracks the current release number in `mod_version` inside `gradle.properties`.
2. When a commit lands on `main`, the prepare-release workflow creates a `vMAJOR.MINOR.PATCH` tag from the current version.
3. The same workflow bumps `gradle.properties` to the next patch version and commits that change back to `main`.
4. The tag push triggers the release workflow, which builds the project, runs verification, and uploads the main and sources JARs.
5. GitHub Release notes are generated automatically from the tag event.
