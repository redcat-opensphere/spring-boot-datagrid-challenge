oc login --token=sha256~GmYwWtiFG3olFEECa55idUJdsY_2pcsVZhf9iPWA83M --server=https://api.cluster-gbsmc.gbsmc.sandbox13.opentlc.com:6443
oc project user7-challenge3
mvn clean package
oc apply -f target/classes/META-INF/dekorate/openshift.yml
