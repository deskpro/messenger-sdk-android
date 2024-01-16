package com.deskpro.messenger.util.extensions

import com.deskpro.messenger.util.Constants.APP_ID
import com.deskpro.messenger.util.Constants.WEB_INTERFACE_KEY

internal object EvaluateScriptsUtil {
    fun initScript(): String {
        return """
        window.DESKPRO_MESSENGER_CONNECTION = {
          parentMethods: {
            ready: async () => {
              const data = await window.DESKPRO_MESSENGER_CONNECTION.childMethods?.init("$APP_ID", {
                showLauncherButton: false,
                user: window.DESKPRO_MESSENGER_OPTIONS?.userInfo,
                launcherButtonConfig: undefined,
                messengerAppConfig: undefined,
                parentViewHeight: "fullscreen",
              });

              if (data) {
                const { side, offsetBottom, offsetSide, width, height } = data;
                // setViewportPosition({ side, offsetBottom, offsetSide });
                // setViewportSize({ width, height });
              }
            },
            open: async () => {
              // setViewportSize({ width, height });
            },
            close: async () => {
              // setViewportSize({ width, height });
              androidApp.close();
            },
            getViewHeight: async () => {
              return 'fullscreen';
            },
          },
          childMethods: undefined,
        };
    """
    }

    fun openScript(): String {
        return """
        window.DESKPRO_MESSENGER_CONNECTION.childMethods.open("$APP_ID", {
          parentViewHeight: 'fullscreen',
          showLauncherButton: false,
        });
    """
    }

    fun initAndOpenScript(): String {
        return """
 window.DESKPRO_MESSENGER_CONNECTION = {
   parentMethods: {
     ready: async () => {
       const data = await window.DESKPRO_MESSENGER_CONNECTION.childMethods?.init('$APP_ID', {
         showLauncherButton: false,
         user: window.DESKPRO_MESSENGER_OPTIONS?.userInfo,
         launcherButtonConfig: undefined, // Optional,
         messengerAppConfig: undefined,
         parentViewHeight: "fullscreen",
         open: true
       });

       if (data) {
         const { side, offsetBottom, offsetSide, width, height } = data;
         // setViewportPosition({ side, offsetBottom, offsetSide });
         // setViewportSize({ width, height });
       }
     },
     open: async () => {
       // setViewportSize({ width, height });
     },
     close: async () => {
        $WEB_INTERFACE_KEY.close();
        //alert('close.') //triggers `runJavaScriptAlertPanelWithMessage`
     },
     getViewHeight: async () => {
        return 'fullscreen'
     },
   },
     // This object will be assigned by the messenger app on ready, so you can call the childMethods from the parent.
   childMethods: undefined,
 };
"""
    }
}
