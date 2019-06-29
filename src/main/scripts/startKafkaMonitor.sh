#!/bin/bash

# _ubeg_
#
# NAME
#    %facility% - Start Kafka monitor
#
# SYNOPSIS
#    %facility%
#    %facility% -h
#    %facility% -p <port>
#
# DESCRIPTION
#   Start Kafka monitor on specified port (4567 by default).
#
# OPTIONS
#    -h : show this help
#    -p : specify application listening port
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

typeset appPort='4567'
typeset mainClass='org.kik.kafka.monitor.Application'

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
#  Function name : startApp
#    Description : Start (nohup) application
#  Parameters IN : $1 - pattern
#         Return : -
# ****************************************************************************
function startApp {
  java "-D$1" -classpath 'lib/*' $mainClass $appPort &
}

# ############################################################################
# main
# ############################################################################

typeset -i iOptH=0
typeset -i iOptP=0
typeset -i iOptCount=0

while getopts ":hp:" opt; do
  case $opt in
    h  )
      iOptH=${iOptH}+1
      iOptCount=${iOptCount}+1
      ;;
    p  )
      iOptP=${iOptP}+1
      iOptCount=${iOptCount}+1
	  appPort=${OPTARG}
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

if [[ $iOptH -eq 1 && $iOptCount -eq 1 && $# -eq 0 ]]; then
  usage
  exit 0
fi

if [[ $iOptCount -gt 1 || $# -ne 0 ]]; then
  usage
  exit 1
fi

# Functional stuff
# ----------------

startApp 'KafkaMonitor'
exit 0

### End of File ###