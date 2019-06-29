#!/bin/bash

# _ubeg_
#
# NAME
#    %facility% - Stop Kafka Monitor
#
# SYNOPSIS
#    %facility%
#    %facility% -h
#
# DESCRIPTION
#   Stop Kafka Monitor.
#
# OPTIONS
#    -h : show this help
#
# EXIT STATUS
#    0: OK
#
# _uend_

# ############################################################################
# const
# ############################################################################

typeset Facility=$0
typeset OriginDir=$(dirname $Facility)
typeset facility=$(basename $Facility)

# ############################################################################
# var
# ############################################################################


# ############################################################################
# func
# ############################################################################

# FUNCTION BEGINNING *********************************************************
#  Function name : usage
#    Description : show usage
#  Parameters IN : -
#         Return : -
# ****************************************************************************
function usage {
  sed "/^# _ubeg_/,/^# _uend_/!d;/_ubeg_/d;/_uend_/d;s/^#//g;s/%faci[^%]*%/$facility/g" $Facility
}

# FUNCTION BEGINNING *********************************************************
#  Function name : stopApp
#    Description : Kill all process with specified pattern in its command line
#  Parameters IN : $1 - pattern
#         Return : -
# ****************************************************************************
function stopApp {
  ps auxwww | grep -E "D$1" | grep -v 'grep' | while read sUsr sPid sFoo; do
    echo "Process $sPid detected"
    kill -9 $sPid
    echo "Process $sPid killed"
  done
}

# ############################################################################
# main
# ############################################################################

typeset -i lv_optionH=0
typeset -i lv_optionCount=0

while getopts ":h" opt; do
  case $opt in
    h  )
      lv_optionH=${lv_optionH}+1
      lv_optionCount=${lv_optionCount}+1
      ;;
    \? )
      usage
      exit 1
      ;;
  esac
done
shift $(($OPTIND - 1))

# Check arguments
# ---------------

if [[ ${lv_optionH} -eq 1 && ${lv_optionCount} -eq 1 && $# -eq 0 ]]; then
  usage
  exit 0
fi

if [[ ${lv_optionCount} -ne 0 ]]; then
  usage
  exit 1
fi

# Functional stuff
# ----------------

stopApp 'KafkaMonitor'
exit 0

### End of File ###