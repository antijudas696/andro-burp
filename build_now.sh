#!/bin/bash

echo "========================================="
echo "  Building Andro-Burp"
echo "========================================="

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Set paths
export ANDROID_HOME=/data/data/com.termux/files/home/android-sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/bin
export PATH=$PATH:$ANDROID_HOME/platform-tools
export GRADLE_OPTS="-Xmx2048m"

cd ~/andro-burp

# Create local.properties
echo "sdk.dir=$ANDROID_HOME" > local.properties
echo -e "${GREEN}✓${NC} local.properties created"

# Check if we have the necessary files
echo -e "${YELLOW}Checking project structure...${NC}"
if [ ! -f "settings.gradle.kts" ]; then
    echo -e "${RED}❌ settings.gradle.kts not found${NC}"
    exit 1
fi

if [ ! -f "build.gradle.kts" ]; then
    echo -e "${RED}❌ build.gradle.kts not found${NC}"
    exit 1
fi

echo -e "${GREEN}✓${NC} Project files found"

# Make gradlew executable
chmod +x gradlew 2>/dev/null || true

# Try to build
echo ""
echo -e "${YELLOW}Starting build...${NC}"
echo ""

./gradlew assembleDebug --stacktrace

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}✅ BUILD SUCCESSFUL!${NC}"
    echo -e "${GREEN}========================================${NC}"
    
    # Find the APK
    APK=$(find app/build/outputs/apk -name "*.apk" 2>/dev/null | head -1)
    
    if [ -f "$APK" ]; then
        echo -e "${GREEN}📱 APK created at: $APK${NC}"
        
        # Get file size
        SIZE=$(du -h "$APK" | cut -f1)
        echo -e "${GREEN}📦 Size: $SIZE${NC}"
        
        # Copy to downloads for easy access
        mkdir -p ~/storage/downloads 2>/dev/null
        cp "$APK" ~/storage/downloads/andro-burp.apk 2>/dev/null
        echo -e "${GREEN}📁 Copied to: ~/storage/downloads/andro-burp.apk${NC}"
        
        echo ""
        echo "To install the app, run:"
        echo "  termux-open $APK"
        echo ""
        echo "Or if termux-open is not available:"
        echo "  cp $APK /sdcard/Download/"
        echo "  Then open from file manager"
    else
        echo -e "${RED}⚠️ APK not found in expected location${NC}"
        echo "Checking for APK files..."
        find . -name "*.apk" 2>/dev/null
    fi
else
    echo ""
    echo -e "${RED}========================================${NC}"
    echo -e "${RED}❌ BUILD FAILED${NC}"
    echo -e "${RED}========================================${NC}"
    echo ""
    echo "Common issues and solutions:"
    echo "1. Check if Java 17 is installed: java -version"
    echo "2. Check disk space: df -h"
    echo "3. Check memory: free -h"
    echo "4. Try building with: ./gradlew assembleDebug --info"
fi
