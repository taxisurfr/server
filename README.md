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
oc rsync data  postgresql-6-srwnn:/var/lib/pgsql
gunzip taxisufr.gz
psql taxisurfr < /var/lib/pgsql/data/taxisufrf.gz

psql -d taxisurfr

select * from price;

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
oc rsh postgresql-3-d4xqs
pg_dump taxisurfr | gzip > tmp/taxisurfr.gz
exit
oc rsync postgresql-2-96tww:/tmp/taxisurfr.dump .
-unpack
psql -U postgres -d taxisurfr <taxisurfr
taxisurfr=# select * from profile;
 id |      monitoremail      | monitormobile | name |                              sendgridkey                              | smspassword |        stripepublishable         |           stripesecret           | taxisurfurl |   taxisurfurlclient   | test
----+------------------------+---------------+------+-----------------------------------------------------------------------+-------------+----------------------------------+----------------------------------+-------------+-----------------------+------
  1 | dispatch@taxisurfr.com |               |      | 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 |             | pk_test_rcKuNpP9OpTri7twmZ77UOI5 | sk_test_TCIbuNPlBRe4VowPhqekTO1L |             | http://localhost:3000 | t

-----------------------------------
dropdb -U postgres taxisurfr
createdb -U postgres -T template0 taxisurfr
psql -U postgres taxisurfr < taxisurfr.dump
psql -U postgres -d taxisurfr
show client_encoding;
SET client_encoding = 'UTF8';

psql -U postgres -d taxisurfrtest -a -f ..\server\src\main\resources\scripts\prices.sql 