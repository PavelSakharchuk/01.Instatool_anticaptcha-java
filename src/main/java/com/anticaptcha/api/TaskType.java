package com.anticaptcha.api;

public enum TaskType {
    IMAGE_TO_TEXT_TASK("ImageToTextTask"),
    NO_CAPTCHA_TASK("NoCaptchaTask"),
    NO_CAPTCHA_TASK_PROXYLESS("NoCaptchaTaskProxyless"),
    RECAPTCHA_V3_TASK_PROXYLESS("RecaptchaV3TaskProxyless"),
    FUN_CAPTCHA_TASK("FunCaptchaTask"),
    FUN_CAPTCHA_TASK_PROXYLESS("FunCaptchaTaskProxyless"),
    SQUARE_NET_TASK("SquareNetTask"),
    GEE_TEST_TASK("GeeTestTask"),
    GEE_TEST_TASK_PROXYLESS("GeeTestTaskProxyless"),
    H_CAPTCHA_TASK("HCaptchaTask"),
    H_CAPTCHA_TASK_PROXYLESS("HCaptchaTaskProxyless"),
    CUSTOM_CAPTCHA_TASK("CustomCaptchaTask");

    private String type;

    TaskType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
