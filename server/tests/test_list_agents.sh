#!/bin/bash

source test.sh

echo -ne "Agents: spawning...\r"
start_server
echo -ne "Agents: registering...\r"
netcat_start register_v4.in
netcat_start register_v6.in
netcat_start register_both.in
echo -ne "Agents: listing...    \r"
simple_testcase norespawn "Agents" list_agents
netcat_killall
kill_server