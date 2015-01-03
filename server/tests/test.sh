#!/bin/bash

NETCAT="nc"
SERVER_PID=0
SERVER_BIN="../src/server"
SERVER_HOST="localhost"
SERVER_PORT="12345"

declare -a NETCAT_PIDS

function kill_server() {
  if [ "$SERVER_PID" -gt 0 ]; then
    kill "$SERVER_PID"
    wait "$SERVER_PID" 2>/dev/null
    SERVER_PID=0
  fi
}

function start_server() {
  "$SERVER_BIN" >/dev/null 2>&1 &
  SERVER_PID=$!
}

function respawn_server() {
  kill_server
  start_server
}

function simple_testcase() {
  echo -n "$1: "
  if [ "x$1" != "xnorespawn" ]; then
    echo -ne "\r$1: spawning..."
    respawn_server
  else
    shift
  fi
  echo -ne "\r$1: request... "
  cat "$2.in" | $NETCAT -w 1 "$SERVER_HOST" "$SERVER_PORT" > "$2.actual"
  echo -ne "\r$1: "
  if diff "$2.out" "$2.actual"; then
    rm "$2.actual"
    echo "PASSED     "
  else
    echo "FAILED     "
  fi
}

function netcat_start() {
  cat "$@" | $NETCAT "$SERVER_HOST" "$SERVER_PORT" >/dev/null 2>&1 &
  NETCAT_PIDS+=($!) 
  sleep 0.1
}

function netcat_waitall() {
  wait ${NETCAT_PIDS[@]} 2>/dev/null
  unset -v NETCAT_PIDS
  declare -a NETCAT_PIDS
}

function netcat_killall() {
  kill ${NETCAT_PIDS[@]}
  netcat_waitall
}

if [ ! -f "$SERVER_BIN" ]; then
  echo "Wrong server binary path"
  exit 1
fi