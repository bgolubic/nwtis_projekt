#!/bin/bash
echo "DOCKER STOP:"
docker stop bgolubic_payara_micro
echo "DOCKER REMOVE:"
docker rm bgolubic_payara_micro
echo "DOCKER PRIPREMI:"
./scripts/pripremiSliku.sh
echo "DOCKER POKRENI:"
./scripts/pokreniSliku.sh
