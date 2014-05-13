rss-kr

Android Library Project dependencies (for Eclipse ADT users):

Google ActionBar appcompat (android-sdk\extras\android\support\v7\appcompat\)

Building(guide for comrads):
0. Download [Android SDK][sdk_direct_url], install, open SDK Manager and download Android 19 Api, all tools, and extras (Support repository, support library) like on [screen][sdk_manager]
1. download gradle v1.10
2. edit config file (/res/values/config)
    <string name="app_name">Rss reader</string>
    <string name="url">http://news.tut.by/rss/economics.rss</string>
    <color name="actionbar_background">#FF0000</color>
    <color name="screen_background_color">#FFFFFF</color>
3. from command line: gradle build
4. ...
5. PROFIT

PS: result apk in /build/apk/

[sdk_direct_url]: http://dl.google.com/android/installer_r22.6.2-windows.exe
[sdk_manager]: http://c2n.me/i75YZt.png