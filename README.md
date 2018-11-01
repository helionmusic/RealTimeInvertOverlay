## This is a work in progress. ##

[ ![Download](https://api.bintray.com/packages/helionmusic/maven/RealTimeInvertOverlay/images/download.svg) ](https://bintray.com/helionmusic/maven/RealTimeInvertOverlay/_latestVersion)

# Invert Overlay for Android

Quick implementation of a color filter that inverts the colors of underlying views. Can be enabled/disabled at will.

![sample_video](assets/sample.gif)

## Usage

Wrap your activity content in a View at the first level after root, then add the InvertOverlay as a layout as the second child of the root view. 

You can always draw more views untouched above the inverted ones by placing them below the invert overlay in the xml file.

```xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true">

    <FrameLayout
        android:id="@+id/content_view_wrapper"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

        <include layout="@layout/content_activity_main" />

    </FrameLayout>

    <com.helionmusic.invertoverlay.InvertOverlay
        android:id="@+id/invert_overlay"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

    <ImageView
        android:id="@+id/normal_colored_image"
        android:layout_width="150dp"
        android:layout_height="150dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</android.support.constraint.ConstraintLayout>

```

In your activity, get a reference to both the wrapper view and the inversion overlay view and call the inversion view's helper method to link it to the wrapper and track updates.

```java

public class ExampleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ...

        View _viewWrapper = findViewById(R.id.content_view_wrapper);
        InvertOverlay _invertOverlay = findViewById(R.id.invert_overlay);
        
        _invertOverlay.attachToView(_viewWrapper);
    }

}

```

Some helper methods
```java

// Toggles the inversion view to enable/disable it with a fade animation
_invertOverlay.toggleInversion(); 

// Toggles the inversion view to enable/disable it with or without an animation
_invertOverlay.toggleInversion(bool withAnim);

// Toggles the inversion view to enable/disable it with or without an animation and a duration
_invertOverlay.toggleInversion(bool withAnim, int durationInMs);


```

## Download

### Step 1. Add the Bintray repository to your build file

Add it in your root build.gradle at the end of repositories:

```groovy

    allprojects {
        repositories {
            ...
            maven { url "https://dl.bintray.com/helionmusic/maven" }
        }
    }
```

### Step 2. Add the dependency

```groovy

    dependencies {
        implementation 'com.helionmusic.library:invertoverlay:0.1.1'
    }
  
```

## License

    MIT License
    
    Copyright (c) 2018 Fares Saidi
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.