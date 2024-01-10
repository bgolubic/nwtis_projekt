#!/bin/bash

NETWORK=bgolubic_mreza_1

docker run -d \
  -p 9001:9001 \
  --network=$NETWORK \
  --ip 200.20.0.3 \
  --name=nwtishsqldb_projekt \
  --hostname=nwtishsqldb_projekt \
  --mount type=bind,source=/opt/hsqldb-2.7.1/hsqldb/data,target=/opt/data \
  nwtishsqldb_projekt:1.0.0 &

wait
