# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#---------------------------------基本指令区----------------------------------
-ignorewarnings  # 忽略警告
-dontskipnonpubliclibraryclasses     # 不去忽略非公共的库类
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志
-printmapping proguardMapping.txt
-optimizationpasses 5   # 指定代码的压缩级别
-dontskipnonpubliclibraryclassmembers

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*    # 混淆时所采用的算法
-keepattributes Signature # 避免混淆泛型
-keepattributes EnclosingMethod
-keepattributes SourceFile,LineNumberTable # 运行抛出异常时保留代码行号
-keepattributes Exceptions # 解决AGPBI警告
-keepattributes *Annotation*
-keepattributes InnerClasses

-dump class_files.txt   # apk包内所有class的内部结构
-printseeds seeds.txt   # 未混淆的类和成员
-printusage unused.txt  # 列出从apk中删除的代码


#继承自activity,fragment,application,service,broadcastReceiver,contentprovider....不进行混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.AppCompatActivity
-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.support.multidex.MultiDexApplication
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

# 所有View的子类及其子类的get、set方法都不进行混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 这个主要是在layout 中写的onclick方法android:onclick="onClick"，不进行混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers class * extends android.app.AppCompatActivity {
   public void *(android.view.View);
}

# 对于带有回调函数onXXEvent的，不能被混淆
-keepclassmembers class * {
    void *(*Event);
}

# 枚举类不能被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# natvie 方法不混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#不混淆Serializable接口的子类中指定的某些成员变量和方法
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

# 不混淆R类里及其所有内部static类中的所有static变量字段，$是用来分割内嵌类与其母体的标志
-keep public class **.R$*{
   public static final int *;
   public static <fields>;
}

#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
   public static *** e(...);
 }

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}

-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}
-keepattributes *JavascriptInterface*
-keepclassmembers class **.ClassName$H5_Object{ *; }

#---------------------------------业务组件实体类---------------------------------
#model配置
-keep class com.putao.**.model.** {
    public void set*(***);
    public *** get*();
    public *** is*();
    public *** get();
    public void clear();
    public *** compareTo(***);
    public *** toString();
}
-keep class com.putao.**.bean.** {
    public void set*(***);
    public *** get*();
    public *** is*();
    public *** toString();
}

-keep class com.putao.**.qrcode {*;}
-keep class com.jusenr.qrcode.** {*;}
-keep class com.jusenr.qrcode.ZXingTools {*;}

-dontwarn javax.annotation.**

#保持注解类不被混淆
-keepattributes *Annotation*

#---------------------------------第三方库及jar包-------------------------------

#litepal数据库不能被混淆
-keep class org.litepal.** {*;}
-keep class * extends org.litepal.crud.DataSupport {*;}

#Glide不能被混淆
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# Dagger2
-dontwarn com.google.errorprone.annotations.*

# Facebook
-keep class com.facebook.** {*;}
-keep interface com.facebook.** {*;}
-keep enum com.facebook.** {*;}
# facebook fresco
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Fresco
-keep class com.facebook.fresco.** {*;}
-keep interface com.facebook.fresco.** {*;}
-keep enum com.facebook.fresco.** {*;}

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-dontnote rx.internal.util.PlatformDependent


#PersistentCookieJar
-dontwarn com.franmontiel.persistentcookiejar.**
-keep class com.franmontiel.persistentcookiejar.**

#activityrouter
-keep class com.github.mzule.activityrouter.router.** { *; }

# 微信支付
-dontwarn com.tencent.mm.**
-dontwarn com.tencent.wxop.stat.**
-keep class com.tencent.mm.** {*;}
-keep class com.tencent.wxop.stat.**{*;}

# 支付宝钱包
-dontwarn com.alipay.**
-dontwarn HttpUtils.HttpFetcher
-dontwarn com.ta.utdid2.**
-dontwarn com.ut.device.**
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.mobilesecuritysdk.*
-keep class com.ut.*

# 银联
-dontwarn com.unionpay.**
-keep class com.unionpay.** { *; }

# 信鸽
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep class com.tencent.android.tpush.**  {* ;}
-keep class com.tencent.mid.**  {* ;}
-keepattributes *Annotation*

# 新浪微博
-keep class com.sina.weibo.sdk.* { *; }
-keep class android.support.v4.* { *; }
-keep class com.tencent.* { *; }
-keep class com.baidu.* { *; }
-keep class lombok.ast.ecj.* { *; }
-dontwarn android.support.v4.**
-dontwarn com.tencent.**s
-dontwarn com.baidu.**

# fastjson配置
-keep class com.alibaba.fastjson.** { *; }
-dontwarn com.alibaba.fastjson.**
-keepattributes Signature

# LeakCanary配置
-keep class org.eclipse.mat.** { *; }
-keep class com.squareup.leakcanary.** { *; }

# GreenDao配置
-keep class de.greenrobot.dao.** { *; }
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties

#alibaba
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}

#mob
-keep class com.mob.** {*;}
-dontwarn com.mob.**

#bugly配置
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#shareSDK配置
-keep class cn.sharesdk.** { *; }
-keep class com.sina.** { *; }
-keep class **.R$.* { *; }
-keep class **.R { *; }
-dontwarn cn.sharesdk.**
-dontwarn **.R$*
-keep class m.framework.** { *; }
-keep class android.support.v8.renderscript.** { *; }
-keep class in.srain.**

#photoview-library
-dontwarn uk.co.senab.photoview.**
-keep class uk.co.senab.photoview.** {*;}


-dontwarn com.viewpagerindicator.**

#友盟统计
-keep class com.umeng.analytics.** {*;}
-dontwarn com.umeng.analytics.**
-keepclassmembers class * { public <init>(org.json.JSONObject); }
-keepclassmembers enum com.umeng.analytics.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#友盟推送
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**
-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}
-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}


#换肤框架的混淆文件
-keep class solid.ren.skinlibrary.** {*;}
-dontwarn solid.ren.skinlibrary.**

# Jackson
-dontwarn org.codehaus.jackson.**
-dontwarn com.fasterxml.jackson.databind.**
-keep class org.codehaus.jackson.** { *;}
-keep class com.fasterxml.jackson.** { *; }

#高德相关混淆文件
#3D 地图
-keep class com.amap.api.** {*;}
-keep class com.autonavi.** {*;}
-keep class com.a.a.** {*;}
-keep class com.loc.** {*;}
-dontwarn com.amap.api.**
-dontwarn com.autonavi.**
-dontwarn com.a.a.**
-dontwarn com.loc.**


# simple-xml-core的SDK
-keep class org.simpleframework.xml.** {*;}
-dontwarn org.simpleframework.xml.**

# acra的 SDK
-keep class org.acra.** {*;}
-dontwarn org.acra.**

# 网络请求库async-http
-keep class com.loopj.android.http.** {*;}
-dontwarn com.loopj.android.http.**


#pinyin4j
-dontwarn net.soureceforge.pinyin4j.**
-dontwarn demo.**
-keep class net.sourceforge.pinyin4j.** { *;}
-keep class demo.** { *;}
-keep class com.hp.** { *;}

#httpclient (org.apache.http.legacy.jar)
-dontwarn android.net.compatibility.**
-dontwarn android.net.http.**
-dontwarn com.android.internal.http.multipart.**
-dontwarn org.apache.commons.**
-dontwarn org.apache.http.**
-dontwarn org.apache.http.protocol.**
-keep class android.net.compatibility.**{*;}
-keep class android.net.http.**{*;}
-keep class com.android.internal.http.multipart.**{*;}
-keep class org.apache.commons.**{*;}
-keep class org.apache.org.**{*;}
-keep class org.apache.harmony.**{*;}

#图表库
-keep class com.github.mikephil.charting.** {*;}
-dontwarn com.github.mikephil.charting.**

# 讯飞语音
-keep class com.chinaMobile.** {*;}
-keep class com.iflytek.** {*;}
-keep class com.iflytek.sunflower.** {*;}
-dontwarn com.iflytek.sunflower.**
-dontwarn com.chinaMobile.**
-dontwarn com.iflytek.**

# greenDao混淆
-keep class de.greenrobot.dao.** {*;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static Java.lang.String TABLENAME;
}
-keep class **$Properties

# gson
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.antew.redditinpictures.library.imgur.** { *; }
-keep class com.antew.redditinpictures.library.reddit.** { *; }

# zxing
-keep class com.google.zxing.** {*;}
-dontwarn com.google.zxing.**

##百度定位
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**

## okhttp
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.{*;}

# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }

#okhttp3.x
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

#retrofit
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-dontwarn okio.**

-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-dontwarn okio.**

# Realm
-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class * { *; }
-dontwarn javax.**
-dontwarn io.realm.**

#recyclerview-animators
-keep class jp.wasabeef.** {*;}
-dontwarn jp.wasabeef.*

#multistateview
-keep class com.kennyc.view.** { *; }
-dontwarn com.kennyc.view.*

# universal-image-loader 混淆
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** { *; }


#-ButterKnife 7.0
 -keep class butterknife.** { *; }
 -dontwarn butterknife.internal.**
 -keep class **$$ViewBinder { *; }
 -keepclasseswithmembernames class * {
  @butterknife.* <fields>;
 }
 -keepclasseswithmembernames class * {
 @butterknife.* <methods>;
 }

#eventbus 3.0
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#EventBus
-keepclassmembers class ** {
    void onEvent*(**);
    void *(**On*Listener);
}
-keepclassmembers class ** {
public void xxxxxx(**);
}

-dontwarn android.support.**

# support-v4
-dontwarn android.support.v4.**
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }


# support-v7
-dontwarn android.support.v7.**
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
-keep class android.support.v7.** { *; }

# support design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

