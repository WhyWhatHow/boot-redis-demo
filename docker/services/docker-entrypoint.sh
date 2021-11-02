#!/bin/sh
# docker-entrypoint.sh
# link: https://stackoverflow.com/questions/69718900/how-can-i-change-heap-size-when-i-use-docker-compose-and-dockerfile-to-create-a


#########################################################
# Set JVM memory options if set as environment variables.
#########################################################
if [ -n "$JVM_XMS" ]; then
  JAVA_OPTS="$JAVA_OPTS -Xms$JVM_XMS"
fi
if [ -n "$JVM_XMX" ]; then
  JAVA_OPTS="$JAVA_OPTS -Xmx$JVM_XMX"
fi
if [ -n "$JVM_GC"]; then
  JAVA_OPTS="$JAVA_OPTS -XX:+$JVM_GC"
fi
#########################################################
# set params
#########################################################

### print output message
echo "$@"
# Then run the main container command.
exec "$@"