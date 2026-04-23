#!/bin/sh

set -e

ROOT_DIR="$(cd "$(dirname "$0")/.."; pwd)"
LIB_DIR="$ROOT_DIR/lib"

mkdir -p "$LIB_DIR"

MAVEN_URL="https://repo1.maven.org/maven2"

download_jar() {
    group_id="$1"
    artifact="$2"
    version="$3"
    target="$LIB_DIR/${artifact}-${version}.jar"
    if [ ! -f "$target" ]; then
        group_path="$(echo "$group_id" | tr '.' '/')"
        url="$MAVEN_URL/${group_path}/${artifact}/${version}/${artifact}-${version}.jar"
        echo "Downloading $url..."
        curl -fsSL -o "$target" "$url"
    fi
}

download_jar "commons-codec" "commons-codec" "1.17.0"
download_jar "org.apache.commons" "commons-lang3" "3.18.0"
download_jar "org.mnode.ical4j" "ical4j" "4.2.5"
download_jar "org.slf4j" "slf4j-api" "2.0.12"
download_jar "org.threeten" "threeten-extra" "1.8.0"

echo ""
echo "To compile: javac -cp \"$LIB_DIR/*:.\" *.java"
echo "To run:     java -cp \"$LIB_DIR/*:.\" MiniLMSApp"

exit 0