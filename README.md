# SimpleLogger
A very simple logger for Android.
## How to use

1. Add dependencies for your Android module:
```
dependencies {
    //
    implementation 'com.ihuntto:log-annotation:1.0.3'
    annotationProcessor 'com.ihuntto:log-compiler:1.0.5'
}
```
2. Add `@SimpleLog` on the class that you want to logcat a simple message when call the method:
```
@SimpleLog
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
```
you will get a logcat like this:
```
2019-05-01 14:30:59.038 7466-7466/? D/MainActivity: <init>()
2019-05-01 14:30:59.045 7466-7466/? D/MainActivity: onCreate(savedInstanceState = null)
```

## References
[1] [Android Annotation Processing Tutorial](https://blog.mindorks.com/android-annotation-processing-tutorial-part-1-a-practical-approach)

[2] [Javac黑客指南](http://developer.51cto.com/art/201305/392858.htm)