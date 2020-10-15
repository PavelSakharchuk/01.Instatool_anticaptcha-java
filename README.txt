AntiCaptcha lib
======================

### About ###
This is fork reworked of original lib:
https://github.com/AdminAnticaptcha/anticaptcha-java.


### How to use development version of Instagram Java scraper ###
Read more info on [jitpack page of project](https://jitpack.io/#com.github.PavelSakharchuk/anticaptcha-java).
Open "Commit" tab and select revision by commit hash.
Just open Gradle or Maven tab copy artifact info
and place it with dependency management repository in your project build configuration


### Examples ###
You can find examples in the test class:
- tasks: https://github.com/PavelSakharchuk/01.Instatool_anticaptcha-java/blob/master/src/test/java/AntiCaptchaTaskTest.java
- other: https://github.com/PavelSakharchuk/01.Instatool_anticaptcha-java/blob/master/src/test/java/AntiCaptchaBaseTest.java


### Settings ###
Need to add file 'src/main/resources/anticaptcha.properties' with following content:
```
key=51065637323daa35622c25435460b871
```
You can get your key from [anti-captcha.com](https://anti-captcha.com/clients/reports/dashboard)


### Documentation ###
https://api.anti-captcha.com
