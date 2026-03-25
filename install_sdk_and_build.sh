#!/bin/bash

echo "========================================="
echo "  Andro-Burp SDK Installation & Build"
echo "========================================="
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Set variables
ANDROID_HOME="/data/data/com.termux/files/home/android-sdk"
PROJECT_DIR="/data/data/com.termux/files/home/andro-burp"

echo -e "${BLUE}[1/8]${NC} Creating SDK directory..."
mkdir -p $ANDROID_HOME
cd $ANDROID_HOME

echo -e "${BLUE}[2/8]${NC} Downloading Android Command Line Tools..."
if [ ! -f "commandlinetools-linux-11076708_latest.zip" ]; then
    wget -q --show-progress https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip
else
    echo -e "${GREEN}✓${NC} Tools already downloaded"
fi

echo -e "${BLUE}[3/8]${NC} Extracting tools..."
unzip -q -o commandlinetools-linux-11076708_latest.zip

echo -e "${BLUE}[4/8]${NC} Setting up directory structure..."
# Clean up and set correct structure
rm -rf cmdline-tools
mkdir -p cmdline-tools
unzip -q commandlinetools-linux-11076708_latest.zip -d cmdline-tools/
if [ -d "cmdline-tools/cmdline-tools" ]; then
    mv cmdline-tools/cmdline-tools/* cmdline-tools/
    rmdir cmdline-tools/cmdline-tools
fi

# Create a symlink for latest for easier access
if [ -d "cmdline-tools/bin" ]; then
    ln -sf cmdline-tools latest
else
    mkdir -p latest
    cp -r cmdline-tools/* latest/ 2>/dev/null || true
fi

echo -e "${BLUE}[5/8]${NC} Setting up environment variables..."
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin
export PATH=$PATH:$ANDROID_HOME/platform-tools

# Add to .bashrc permanently
grep -q "ANDROID_HOME" ~/.bashrc || {
    echo "export ANDROID_HOME=/data/data/com.termux/files/home/android-sdk" >> ~/.bashrc
    echo "export PATH=\$PATH:\$ANDROID_HOME/cmdline-tools/latest/bin" >> ~/.bashrc
    echo "export PATH=\$PATH:\$ANDROID_HOME/platform-tools" >> ~/.bashrc
    echo "export PATH=\$PATH:\$ANDROID_HOME/build-tools/34.0.0" >> ~/.bashrc
}

echo -e "${BLUE}[6/8]${NC} Installing SDK components..."
# Accept licenses
echo -e "${YELLOW}Accepting Android licenses...${NC}"
yes | $ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager --licenses > /dev/null 2>&1

# Install components
echo -e "${YELLOW}Installing platform tools...${NC}"
$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager "platform-tools" > /dev/null 2>&1

echo -e "${YELLOW}Installing Android 34 platform...${NC}"
$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager "platforms;android-34" > /dev/null 2>&1

echo -e "${YELLOW}Installing build tools...${NC}"
$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager "build-tools;34.0.0" > /dev/null 2>&1

echo -e "${GREEN}✓${NC} SDK components installed"

echo -e "${BLUE}[7/8]${NC} Creating local.properties..."
cd $PROJECT_DIR
echo "sdk.dir=$ANDROID_HOME" > local.properties
echo -e "${GREEN}✓${NC} Created local.properties"

echo -e "${BLUE}[8/8]${NC} Building the app..."
# Set gradle memory
export GRADLE_OPTS="-Xmx2048m"

# Make gradlew executable
if [ -f "gradlew" ]; then
    chmod +x gradlew
fi

# Clean previous builds
echo -e "${YELLOW}Cleaning...${NC}"
./gradlew clean 2>/dev/null || true

# Build the APK
echo -e "${YELLOW}Building APK (this may take 5-10 minutes)...${NC}"
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}✅ BUILD SUCCESSFUL!${NC}"
    echo -e "${GREEN}========================================${NC}"
    echo ""
    
    # Find the APK
    APK=$(find app/build/outputs/apk -name "*.apk" 2>/dev/null | head -1)
    if [ -f "$APK" ]; then
        echo -e "${GREEN}📱 APK Location: $APK${NC}"
        
        # Copy to downloads
        mkdir -p $HOME/storage/downloads 2>/dev/null
        cp "$APK" $HOME/storage/downloads/andro-burp.apk 2>/dev/null
        echo -e "${GREEN}📁 Also copied to: $HOME/storage/downloads/andro-burp.apk${NC}"
        
        echo ""
        echo "To install the app:"
        echo "  termux-open $APK"
        echo "  or"
        echo "  cp $APK /sdcard/Download/ && open from file manager"
    fi
else
    echo ""
    echo -e "${RED}========================================${NC}"
    echo -e "${RED}❌ BUILD FAILED${NC}"
    echo -e "${RED}========================================${NC}"
    echo ""
    echo "Troubleshooting steps:"
    echo "1. Check disk space: df -h"
    echo "2. Check memory: free -h"
    echo "3. Try building with: ./gradlew assembleDebug --stacktrace"
    echo "4. Check Java: java -version (should be 17+)"
fi
