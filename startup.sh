#!/bin/bash
cd ~
sudo rm -rf ~/mas-ddos
git clone https://nut-hatch@github.com/nut-hatch/mas-ddos.git
cd ~/mas-ddos
./run.sh $@

