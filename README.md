# server

oc new-app --name=taxisurfr-server wildfly~https://github.com/taxisurfr/server.git
oc expose svc taxisurfr-server

v2
 /cygdrive/c/SWTOOLS/Ruby21/bin/rhc.bat ssh -a taxisurfr
pg_dump taxisurfr | gzip > ~/app-root/data/tmp/taxisurfr.gz

local
/cygdrive/c/SWTOOLS/Ruby21/bin/rhc.bat scp -a taxisurfr download . app-root/data/tmp/taxisurfr.gz


oc
oc login --config .kube/config
oc project xxx


oc new-app postgresql-persistent -p POSTGRESQL_DATABASE=taxisurfr -p POSTGRESQL_PASSWORD=adminrpbs8pt -p POSTGRESQL_USER=cRNYjds4y8M
oc get pods
oc rsync data postgresql-1-8cj93:/var/lib/pgsql
gunzip taxisufr.gz
psql taxisurfr < /var/lib/pgsql/data/taxisufrf.gz

psql -d taxisurfr


GRANT USAGE ON SCHEMA public to readonly;


Username: userB2K
       Password: IP7l2njiVidYDRhl
  Database Name: taxisurfr
 Connection URL: postgresql://postgresql:5432/
 
 
 client-taxisurfr.rhcloud.com
 
 C:\taxisurfr\openshift
 
 Generate a key and a csr
 -----------------------
 openssl req -new -newkey rsa:2048 -nodes -keyout server.key -out server.csr
 
 Upload csr to godaddy and download crt.
 ---------------------------------------
 
 install simple cert and key in openshift
 
 cat a97a53706f480a26.crt gd_bundle-g2-g1.crt  > combined.pem
 
 Setup default-host
/subsystem=undertow/server=default-server/host=default-host:write-attribute(name=default-web-module,value=taxisurfr-1.war)

See
https://docs.jboss.org/author/display/WFLY/Command+Line+Interface

-----------------------------------
db backup
cd c:\dev\taxisurfr\db_dump
oc rsh postgresql-2-96tww
pg_dump taxisurfr | gzip > tmp/taxisurfr.gz
exit
oc rsync postgresql-2-96tww:/tmp/taxisurfr.dump .

-----------------------------------
createdb -U postgres -T template0 taxisurfrtest
psql -U postgres taxisurfrtest < taxisurfr.dump
psql -U postgres -d taxisurfrtest
show client_encoding;
SET client_encoding = 'UTF8';

psql -U postgres -d taxisurfrtest -a -f ..\server\src\main\resources\scripts\prices.sql 