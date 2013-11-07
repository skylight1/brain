@echo off
path c:\Android\android-sdk\platform-tools
adb -d uninstall com.test.helloeeg
adb -d install bin\HelloEEG.apk
adb -d logcat -c
adb -d shell am start -n com.test.helloeeg/com.test.helloeeg.HelloEEGActivity