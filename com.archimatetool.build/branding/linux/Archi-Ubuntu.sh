#!/bin/bash
export UBUNTU_MENUPROXY=0
dir=$(dirname $(readlink -m $0))
if  [[ "$HOSTTYPE" = "i586" ]]; then
    $dir/Archi32
else
    $dir/Archi64
fi
