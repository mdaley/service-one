#!/bin/sh
# Perform a release, bumping versions, tagging, creating and pushing a docker image
# Because this runs on deployment server we don't want password entry so can't use lein release directly.

set -euo pipefail
IFS=$'\n\t'

lein vcs assert-committed
lein change version leiningen.release/bump-version release
lein vcs commit

version=`cat project.clj | grep defproject | cut -d" " -f 3 | tr -d "\""` # OK, just about reasonable!
git tag $version

lein uberjar
docker build -t mdaley/service-one:$version .
docker push  mdaley/service-one:$version

lein change version leiningen.release/bump-version
lein vcs commit
lein vcs push
