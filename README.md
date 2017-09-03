# server

oc new-app --name=hellots1 wildfly~https://github.com/taxisurfr/server.git
oc expose svc hellots1