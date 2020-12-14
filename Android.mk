
LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := \
    $(call all-Iaidl-files-under, app/src/main/java)	\
    $(call all-java-files-under, app/src/main/java)

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/app/src/main/res

LOCAL_MANIFEST_FILE := app/src/main/AndroidManifest.xml

LOCAL_JAVA_LIBRARIES := com.asuka.framework

LOCAL_PACKAGE_NAME := NightMode
LOCAL_CERTIFICATE := platform
LOCAL_PRIVILEGED_MODULE := true

include $(BUILD_PACKAGE)

# To access external JARs in Android Studio
# --------------------------------------------------------------------------------------------------
JAR_INTERMEDIATES_PATH := $(ANDROID_BUILD_TOP)/$(TARGET_OUT_COMMON_INTERMEDIATES)/JAVA_LIBRARIES

SHELL_RESULT := $(shell mkdir $(LOCAL_PATH)/app/syslibs)
SHELL_RESULT := $(shell ln -s -f $(JAR_INTERMEDIATES_PATH)/com.asuka.framework_intermediates/classes.jar $(LOCAL_PATH)/app/syslibs/asuka.jar)
SHELL_RESULT := $(shell ln -s -f $(JAR_INTERMEDIATES_PATH)/framework_intermediates/classes.jar $(LOCAL_PATH)/app/syslibs/framework.jar)