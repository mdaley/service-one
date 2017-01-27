#!/bin/sh
# Perform a release, bumping versions, tagging, creating and pushing a docker image
# Because this runs on deployment server we don't want password entry so can't use lein release directly.

set -euo pipefail
IFS=$'\n\t'

lein vs assert-committed
lein vcs commit
git tag `cat project.clj | grep defproject | cut -d" " -f 3 | tr -d "\""` # OK, just about reasonable!

lein change version leiningen.release/bump-version
lein vcs commit
lein vcs push
