# zxing_qrcode_demo

  Android two dimensional code scanning tool library.

 ---

 [ ![Bintray](https://img.shields.io/badge/bintray-v1.0.0-brightgreen.svg) ](https://bintray.com/jusenr/maven/qrcode/_latestVersion)
 [ ![Jitpack](https://jitpack.io/v/Jusenr/zxing_qrcode_demo.svg) ](https://jitpack.io/#Jusenr/zxing_qrcode_demo)
 [ ![API](https://img.shields.io/badge/API-19%2B-blue.svg) ](https://developer.android.com/about/versions/android-4.4.html)
 [ ![License](http://img.shields.io/badge/License-Apache%202.0-blue.svg) ](http://www.apache.org/licenses/LICENSE-2.0)


#### To get a Git project into your build: ####

 ---

 >Step 1. Dependent Manner

   >>The First： Add the JitPack repository to your build file

 (1) Add it in your root build.gradle at the end of repositories:

     allprojects {
             repositories {
                 ...
                 maven { url 'https://jitpack.io' }
             }
     }

 (2) Add the dependency

         dependencies {
          ...
         compile 'com.github.Jusenr:qrcode:1.0.0'
         }

 ---

   >>The Second: Add the dependency

           dependencies {
            ...
           compile 'com.jusenr.android.library:qrcode:1.0.0'
           }

 ---

 >Step 3. Add required permissions

     <manifest
         ...
         <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
         <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
         <uses-permission android:name="android.permission.WAKE_LOCK"/>
         <uses-permission android:name="android.permission.INTERNET"/>

          <application
             ...

          </application>

     </manifest>

 ---

 >Step 4. Initialization configuration

     App extends Application{

         @Override
         public void onCreate() {
            super.onCreate();

            //ZXingTools initialise.
            ZXingTools.initDisplayOpinion(getApplicationContext());
        }
     }

 ---

#### It was smashing! Setup Complete!!! ####

 ---

#### Express one's thanks ####

Thanks to Yipianfengye provides the resources.

---

#### Possible solutions to the problem ####

   1. Support packet conflict problem

   Add Build.gradle to the project: The version is the current version of your project.

        subprojects {
            configurations.all {
                resolutionStrategy {
                    force 'com.android.support:support-xx:xx.x.x'
                }
            }
        }

---

#### License ####

[Apache License Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
