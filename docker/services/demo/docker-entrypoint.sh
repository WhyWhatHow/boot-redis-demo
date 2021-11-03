#!/bin/sh
# docker-entrypoint.sh
# link: https://stackoverflow.com/questions/69718900/how-can-i-change-heap-size-when-i-use-docker-compose-and-dockerfile-to-create-a

set -x

#########################################################
# Set JVM memory options if set as environment variables.

#########################################################
if [ -n "$JVM_XMS" ]; then
  JAVA_OPTS="$JAVA_OPTS -Xms$JVM_XMS "
fi
if [ -n "$JVM_XMX" ]; then
  JAVA_OPTS="$JAVA_OPTS -Xmx$JVM_XMX "
fi
if [ -n "$JVM_GC"]; then
  JAVA_OPTS="$JAVA_OPTS -XX:$JVM_GC"
fi
if [-n  "$JAVA_RANDOM"];then
  JAVA_OPTS="$JAVA_OPTS $JAVA_RANDOM"
fi
#########################################################
# set params
#########################################################

if [-n  "$SERVER_PORT"];then
    PARAMS="$PARAMS --server.port=$SERVER_PORT"
fi
if [-n  "$SPRING_PROFILES_ACTIVE"];then
    PARAMS="$PARAMS --spring.profiles.active=$SPRING_PROFILES_ACTIVE"
fi
if [-n  "$NACOS_ADDR"];then
    PARAMS="$PARAMS --spring.cloud.nacos.server-addr=${NACOS_ADDR}"
fi
if [-n "$OTHER_PARAMS"];then
    PARAMS="$PARAMS $OTHER_PARAMS"
fi
### print output message
echo "****************end**************************"
echo $JAVA_OPTS
echo "****************done*************************"

echo "****************end**************************"
echo $PARAMS
echo "****************done*************************"
# Then run the main container command.
exec  java $JAVA_OPTS -jar  app.jar $PARAMS