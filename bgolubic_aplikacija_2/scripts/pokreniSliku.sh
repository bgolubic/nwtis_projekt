#!/bin/bash
NETWORK=bgolubic_mreza_1

docker run -it -d \
  -p 8070:8080 \
  --network=$NETWORK \
  --ip 200.20.0.4 \
  --name=bgolubic_payara_micro \
  --hostname=bgolubic_payara_micro \
  bgolubic_payara_micro:6.2023.4 \
  --deploy /opt/payara/deployments/bgolubic_aplikacija_2-1.0.0.war \
  --contextroot bgolubic_aplikacija_2 \
  --noCluster &

wait
