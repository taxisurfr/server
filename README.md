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
psql postgresql < /var/lib/pgsql/data/taxisufrf.gz

psql -d taxisurfr


GRANT USAGE ON SCHEMA public to readonly;


Username: userB2K
       Password: IP7l2njiVidYDRhl
  Database Name: taxisurfr
 Connection URL: postgresql://postgresql:5432/