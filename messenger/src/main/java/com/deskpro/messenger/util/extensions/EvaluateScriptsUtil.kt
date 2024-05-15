package com.deskpro.messenger.util.extensions

import com.deskpro.messenger.util.Constants.WEB_INTERFACE_KEY

/**
 * Utility object containing scripts for initializing and interacting with the DeskPro Messenger WebView.
 *
 * The `EvaluateScriptsUtil` object provides JavaScript scripts for initializing the Messenger connection,
 * opening the Messenger, and handling various events.
 */
internal object EvaluateScriptsUtil {
    private fun optionsScript(): String {
        return """
        window.DpMessengerOptions = {
            showLauncherButton: false,
            openOnInit: true,
            userInfo: $WEB_INTERFACE_KEY.getUserInfo(),
            signedUserInfo: undefined,
            launcherButtonConfig: undefined,
            messengerAppConfig: undefined,
            urlCacheableConfig: undefined,
          };
        """
    }

    private fun connectionScript(): String {
        return """ 
        window.DpMessengerConnection = {
           parentMethods: {
            ready: async (messengerId) => {
             const data = await window.DpMessengerConnection.childMethods?.init(messengerId, {
              showLauncherButton: DpMessengerOptions.showLauncherButton,
              userInfo: window.DpMessengerOptions?.userInfo,
              launcherButtonConfig: DpMessengerOptions.launcherButtonConfig,
              messengerAppConfig: DpMessengerOptions.messengerAppConfig,
              parentViewDimensions: "fullscreen",
              open: DpMessengerOptions.openOnInit,
             });
        
             if (data) {
              const { side, offsetBottom, offsetSide, width, height } = data;
             }
             
             const fcmToken = await $WEB_INTERFACE_KEY.getFcmToken();
             
             window.DpMessengerConnection.childMethods?.setDeviceToken(messengerId, {
                token: fcmToken
             });
            },
            getViewDimensions: async (messengerId) => {
             return "fullscreen";
            },
            getSignedUserInfo: async (messengerId) => {
              return $WEB_INTERFACE_KEY.getJwtToken();
            },
            open: async (messengerId, data) => {
              const { width, height } = data;
            },
            close: async (messengerId, data) => {
              const result = await $WEB_INTERFACE_KEY.close();
            },
            appEvent: async (messengerId, event) => {
              var jsonEvent = JSON.stringify(event);
              $WEB_INTERFACE_KEY.appEvent(jsonEvent);
            },
           },
           childMethods: undefined,
          };
        """
    }

    fun initAndOpenScript(): String {
        return optionsScript() + connectionScript()
    }

    fun logoutScript(): String {
        return """
      window.DpMessengerConnection.childMethods.logout(messengerId);
      """
    }
}
