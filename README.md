rss-kr

Android Library Project dependencies (for Eclipse ADT users):

Google ActionBar appcompat (android-sdk\extras\android\support\v7\appcompat\)

Building(guide for comrads):<br />
0. Download [Android SDK][sdk_direct_url], install, open SDK Manager and download Android 19 Api, all tools, and extras (Support repository, support library) like on [screen][sdk_manager]<br />
1. download gradle v1.10<br />
2. edit config file (/res/values/config)<br />
``` xml
    <string name="app_name">Rss reader</string><br />
    <string name="url">http://news.tut.by/rss/economics.rss</string><br />
    <color name="actionbar_background">#FF0000</color><br />
    <color name="screen_background_color">#FFFFFF</color><br />
```
3. from command line: gradle build<br />
4. ...<br />
5. PROFIT

PS: result apk in /build/apk/

[sdk_direct_url]: http://dl.google.com/android/installer_r22.6.2-windows.exe
[sdk_manager]: http://c2n.me/i75YZt.png