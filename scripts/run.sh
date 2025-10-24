#!/usr/bin/env bash

set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
ARTIFACT="middleware-0.0.1-SNAPSHOT.jar"

usage() {
	cat <<'EOF'
Usage: ./scripts/run.sh [--skip-tests]

Builds the middleware application and starts it with the default Spring profile.

Options:
  --skip-tests   Skip running tests during the Maven build.
EOF
}

SKIP_TESTS=false
while [[ $# -gt 0 ]]; do
	case "$1" in
		--skip-tests)
			SKIP_TESTS=true
			shift
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

java -jar "target/${ARTIFACT}"
