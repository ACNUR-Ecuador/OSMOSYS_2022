#!/bin/bash
mvn clean
profiles=("cri" "ecu" "mex" "per" "slv" "test" "ven" "rba")
for profile in "${profiles[@]}"
do
  mvn package -P$profile
done

zip osmosys_war.zip target/osmosys_cri.war target/osmosys_ecu.war target/osmosys_mex.war target/osmosys_per.war target/osmosys_slv.war target/osmosys_test.war target/osmosys_ven.war