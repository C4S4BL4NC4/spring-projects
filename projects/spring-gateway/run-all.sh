#!/usr/bin/env bash
# Starts the backend services in the background, then runs the gateway in the foreground.
# Stopping the gateway (Ctrl+C) also stops every service this script started.
# Databases (MySQL, MongoDB) are not started here; run your Docker containers first.

set -u

GATEWAY_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECTS_DIR="$(dirname "$GATEWAY_DIR")"
LOG_DIR="$GATEWAY_DIR/logs"
SERVICES=(spring-auth-server spring-rest-mvc spring-reactive spring-reactive-mongo)
PIDS=()

mkdir -p "$LOG_DIR"

stop_services() {
    echo "Stopping services..."
    for pid in "${PIDS[@]}"; do
        # Each service runs in its own process group, so this also stops the forked JVM
        kill -TERM -- "-$pid" 2>/dev/null
    done
    wait 2>/dev/null
    echo "All services stopped."
}
trap stop_services EXIT
trap 'exit 130' INT TERM

for service in "${SERVICES[@]}"; do
    echo "Starting $service (log: $LOG_DIR/$service.log)"
    (cd "$PROJECTS_DIR/$service" && exec setsid ./mvnw spring-boot:run) > "$LOG_DIR/$service.log" 2>&1 &
    PIDS+=($!)
done

echo "Starting spring-gateway..."
cd "$GATEWAY_DIR" && ./mvnw spring-boot:run
