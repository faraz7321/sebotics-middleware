#!/usr/bin/env bash

set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
ARTIFACT="middleware-0.0.1-SNAPSHOT.jar"

usage() {
	cat <<'EOF'
Usage: ./scripts/run.sh [--skip-tests] [--profile <name>]

Builds the middleware application and starts it with the default Spring profile.

Options:
  --skip-tests       Skip running tests during the Maven build.
  --profile <name>   Activate the given Spring profile when starting the app.
EOF
}

SKIP_TESTS=false
PROFILE=""
while [[ $# -gt 0 ]]; do
	case "$1" in
		--skip-tests)
			SKIP_TESTS=true
			shift
			;;
		--profile)
			if [[ $# -lt 2 ]]; then
				echo "--profile requires a value" >&2
				usage
				exit 1
			fi
			PROFILE="$2"
			shift 2
			;;
		-h|--help)
			usage
			exit 0
			;;
		*)
			echo "Unknown option: $1" >&2
			usage
			exit 1
			;;
	esac
done

cd "$PROJECT_ROOT"

BUILD_ARGS=(clean package)
if [[ "$SKIP_TESTS" == true ]]; then
	BUILD_ARGS+=(-DskipTests)
fi

./mvnw "${BUILD_ARGS[@]}"

APP_ARGS=()
if [[ -n "$PROFILE" ]]; then
	APP_ARGS+=(--spring.profiles.active="$PROFILE")
fi

java -jar "target/${ARTIFACT}" "${APP_ARGS[@]}"
