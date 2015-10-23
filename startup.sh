#!/bin/bash
cd ~
sudo rm -rf ~/mas-ddos
git clone https://nut-hatch:hall0dud4@github.com/nut-hatch/mas-ddos.git
cd ~/mas-ddos
./run.sh $@

