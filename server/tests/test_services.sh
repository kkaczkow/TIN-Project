#!/bin/bash

source test.sh

echo -ne "Services: spawning...\r"
start_server
echo -ne "Services: registering...\r"
netcat_start register_v4.in services.in
netcat_start register_v6.in services.in
netcat_start register_both.in services.in
echo -ne "Services: listing...    \r"
simple_testcase norespawn "Services" list_all_services
netcat_killall
kill_server