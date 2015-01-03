#!/bin/bash

source test.sh

simple_testcase "Register IPv4" register_v4
simple_testcase "Register IPv6" register_v6
simple_testcase "Register both" register_both

kill_server