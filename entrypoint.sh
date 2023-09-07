#!/bin/sh

COMMON_JAVA_ARGS="$(tr '\n' ' ' <<-EOF
  -server
  -DjwtSecret=1321323232
  -Dserver.address=127.0.0.1
  -Dwicket.configuration=deployment
  -Dfile.encoding=UTF-8
  -Xms512m
  -Xmx4096m
  -XX:MaxMetaspaceSize=512m
  -XX:ReservedCodeCacheSize=256m
  -Dspring.datasource.username=postgres
  -Dspring.datasource.password=1234
  -DJava.awt.headless=true
  -XX:+UseG1GC
  -Dspring.data.mongodb.uri=mongodb://root:1234@localhost:27017/ocportal?authSource=admin
  -Dspring.datasource.url=jdbc:postgresql://localhost/ocportal
  -Dgoogle.recaptcha.secret=0
  -Dinfobip.key=a
  -Dsmsgateway.key=b
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
admin-dev-local)
  JAVA_ARGS="${COMMON_JAVA_ARGS} $(tr '\n' ' ' <<-EOF
			-Dspring.devtools.restart.additional-exclude=logs/**,META-INF/**,ehcache-disk-store/**
			-Dspring.devtools.restart.poll-interval=3s
			-Dspring.devtools.restart.quiet-period=2s
			-XX:+TieredCompilation
			-XX:TieredStopAtLevel=1
			-noverify
			-cp forms/classes:web/classes:persistence/classes:lib/*
			org.devgateway.toolkit.forms.wicket.FormsWebApplication
		EOF
  )"
  exec java $JAVA_ARGS $@
  ;;
*)
  exec "$@"
  ;;
esac
