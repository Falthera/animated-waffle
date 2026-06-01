#!/usr/bin/env sh

set -eu

APP_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
GRADLE_VERSION="8.3"
GRADLE_DIST_NAME="gradle-${GRADLE_VERSION}-bin"
GRADLE_EXTRACTED_NAME="gradle-${GRADLE_VERSION}"
GRADLE_DIST_URL="https://services.gradle.org/distributions/${GRADLE_DIST_NAME}.zip"
GRADLE_CACHE_DIR="${GRADLE_USER_HOME:-$HOME/.gradle}/pvp-optimizer"
GRADLE_DIST_DIR="$GRADLE_CACHE_DIR/$GRADLE_EXTRACTED_NAME"
GRADLE_ZIP="$GRADLE_CACHE_DIR/$GRADLE_DIST_NAME.zip"

if [ ! -x "$GRADLE_DIST_DIR/bin/gradle" ]; then
	mkdir -p "$GRADLE_CACHE_DIR"
	if command -v curl >/dev/null 2>&1; then
		curl -fsSL "$GRADLE_DIST_URL" -o "$GRADLE_ZIP"
	elif command -v wget >/dev/null 2>&1; then
		wget -qO "$GRADLE_ZIP" "$GRADLE_DIST_URL"
	else
		echo "curl or wget is required to bootstrap Gradle." >&2
		exit 1
	fi
	rm -rf "$GRADLE_DIST_DIR"
	unzip -q "$GRADLE_ZIP" -d "$GRADLE_CACHE_DIR"
fi

exec "$GRADLE_DIST_DIR/bin/gradle" -Dorg.gradle.appname=gradlew -p "$APP_DIR" "$@"
