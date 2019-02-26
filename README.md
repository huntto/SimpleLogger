# SimpleLogger
A very simple logger for Android.
## How to use
1. Add `gradle_plugin_android_aspectjx` to build.gradle of project:
```
buildscript {

    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.2.1'

        classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.4'
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```
2. Add dependencies and apply plugin 'com.hujiang.android-aspectjx' for your app:
```
apply plugin: 'com.android.application'
apply plugin: 'com.hujiang.android-aspectjx'

android {
    //
}

dependencies {
    //
    implementation 'com.ihuntto:simplelogger-annotations:1.0.1'
    debugImplementation 'com.ihuntto:simplelogger-aspect:1.0.1'
}
```
3. Add `@DebugTrace` on the method that you want to logcat a simple message:
```
public class MainActivity extends AppCompatActivity {

    @Override
    @DebugTrace
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
```
you will get a logcat like this:
```
2019-02-26 21:11:19.590 27717-27717/com.ihuntto.simpleloggertest D/MainActivity: onCreate
```

## References
[1] [AspectJ](https://www.eclipse.org/aspectj/doc/released/progguide/index.html)

[2] [gradle_plugin_android_aspectjx](https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx)