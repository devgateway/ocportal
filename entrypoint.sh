#!/bin/sh

COMMON_JAVA_ARGS="$(tr '\n' ' ' <<-EOF
  -server
  -DjwtSecret=$JWT_SECRET
  -Dserver.address=0.0.0.0
  -Dwicket.configuration=deployment
  -Dfile.encoding=UTF-8
  -DserverURL=$SERVER_URL
  -Xms512m
  -Xmx4096m
  --add-opens=java.naming/javax.naming=ALL-UNNAMED
  --add-opens=java.base/java.lang.reflect=ALL-UNNAMED
  --add-opens=java.base/java.lang.ref=ALL-UNNAMED
  --add-opens=java.base/java.lang=ALL-UNNAMED
  --add-exports=java.naming/com.sun.jndi.ldap=ALL-UNNAMED
  --add-opens=java.base/java.lang.invoke=ALL-UNNAMED
  --add-opens=java.base/java.io=ALL-UNNAMED
  --add-opens=java.base/java.security=ALL-UNNAMED
  --add-opens=java.base/java.util=ALL-UNNAMED
  --add-opens=java.management/javax.management=ALL-UNNAMED
  -Dspring.mail.port=$SMTP_PORT
  -Dspring.mail.host=$SMTP_HOST
  -XX:MaxMetaspaceSize=512m
  -XX:ReservedCodeCacheSize=256m
  -Dspring.jmx.enabled=true
  -Dspring.datasource.username=$POSTGRES_USER
  -Dspring.datasource.password=$POSTGRES_PASSWORD
  -DJava.awt.headless=true
  -XX:+UseG1GC
  -Dspring.data.mongodb.uri=mongodb://$MONGO_INITDB_ROOT_USERNAME:$MONGO_INITDB_ROOT_PASSWORD@mongo:27017/ocportal?authSource=admin
  -Dspring.datasource.url=jdbc:postgresql://db/ocportal
  -Dgoogle.recaptcha.secret=$RECAPTCHA_SECRET
  -Dinfobip.key=$INFOBIP_KEY
  -Dsmsgateway.key=$SMSGATEWAY_KEY
EOF
)"

case "$1" in
admin)
  JAVA_ARGS="${COMMON_JAVA_ARGS} $(tr '\n' ' ' <<-EOF
			-cp .:lib/*
			org.devgateway.toolkit.forms.wicket.FormsWebApplication
		EOF
  )"
  exec java $JAVA_ARGS $@
  ;;
admin-dev)
  JAVA_ARGS="${COMMON_JAVA_ARGS} $(tr '\n' ' ' <<-EOF
			-Dspring.devtools.restart.additional-exclude=logs/**,META-INF/**,ehcache-disk-store/**
			-Dspring.devtools.restart.poll-interval=3s
			-Dspring.devtools.restart.quiet-period=2s
			-XX:+TieredCompilation
			-XX:TieredStopAtLevel=1
			-noverify
			-cp forms/classes:persistence-mongodb/classes:web/classes:persistence/classes:lib/*
			org.devgateway.toolkit.forms.wicket.FormsWebApplication
		EOF
  )"
  exec java $JAVA_ARGS $@
  ;;
*)
  exec "$@"
  ;;
esac
