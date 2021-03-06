#!/usr/bin/env bash

# ==============================================
# Initials
# ----------------------------------------------

### msgs ###
echo "hello, Bya"
echo "happy coding!"

### alias ###
alias l="ls -al"
alias c="clear"
alias bpe="vi ~/.bash_profile"
alias bpg="source ~/.zshrc"
alias s="history | peco"
alias config="vim ~/.ssh/config"
alias cpp="g++ -pipe -O2 -std=c++14"
alias gitlazy="git add . && git commit -m 'updated' && git push"

# ==============================================
# Paths
# ----------------------------------------------

# Anaconda
export PATH="${HOME}/anaconda3/bin:$PATH"

# add Maven bin to PATH
export PATH="${HOME}/apache-maven-3.5.0/bin/:$PATH"

# ==============================================
# Environment Variables
# ----------------------------------------------

# Drone (BLT)
export DRONE_SERVER=""
export DRONE_TOKEN=""

# Java
JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/

# GO
GOPATH=${HOME}/work

# GCP
GOOGLE_APPLICATION_CREDENTIALS=/Users/01014477/.credentials/service-account.json
