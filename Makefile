# Makefile is intended to automate some developmnet houskeeping
# eg updateing the ./sbt script from github and re-checking it in etc
# to prevent wierdness it will also just kick-off sbt if no target is specified and do a publishLocal.

SBT = https://raw.githubusercontent.com/paulp/sbt-extras/master/sbt

# Before we start test that we have the manditory executables avilable
EXECUTABLES = git
K := $(foreach exec,$(EXECUTABLES),\
	$(if $(shell which $(exec)),some string,$(error "No $(exec) in PATH, consider apt-get install $(exec)")))

GIT = $(shell which git)

CURL = $(shell which curl)
WGET = $(shell which wget)

ifneq ($(CURL),)
  FETCH = $(CURL)
  FETCH-SBT = $(FETCH) -s $(SBT) > ./sbt
else ifneq ($(WGET),)
  FETCH = $(WGET)
  FETCH-SBT = $(FETCH) -O ./sbt $(SBT)
else
  $(error "No fetch executable like curl or wget was found. Please install     one")
endif

################################################

.PHONY: all
all: sbt
	echo doing sbt...
	./sbt clean test compile publishLocal

sbt:
	echo git is $(GIT)
	echo doing $(FETCH-SBT)
	$(FETCH-SBT)
	chmod 0755 ./sbt
	$(GIT) add ./sbt
	$(GIT) commit -m "Bump sbt-extras"