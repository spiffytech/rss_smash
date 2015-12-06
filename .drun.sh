#!/bin/bash

set -e

export CONTAINER=spiffytech/clojure-drip

mkdir -p "$(pwd)/container-data/m2"
mkdir -p "$(pwd)/container-data/lein"
mkdir -p "$(pwd)/container-data/drip-runtime-data"

export VOLUMES=(
    "$(pwd)/container-data/m2:/root/.m2"
    "$(pwd)/container-data/lein:/root/.lein"
    "$(pwd)/container-data/drip-runtime-data:/root/.drip"
)

export ENVVARS=(
    "siteBaseDir=/site-source"
    "LEIN_JAVA_CMD=/bin/drip"
    "LEIN_ROOT=true"
)
