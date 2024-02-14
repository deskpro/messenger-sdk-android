package com.deskpro.messenger.util.extensions

import com.deskpro.messenger.util.Constants.WEB_INTERFACE_KEY

/**
 * Utility object containing scripts for initializing and interacting with the DeskPro Messenger WebView.
 *
 * The `EvaluateScriptsUtil` object provides JavaScript scripts for initializing the Messenger connection,
 * opening the Messenger, and handling various events.
 */
internal object EvaluateScriptsUtil {
    fun initScript(): String {
        return """
        window.DESKPRO_MESSENGER_CONNECTION = {
          parentMethods: {
            ready: async () => {
              const data = await window.DESKPRO_MESSENGER_CONNECTION.childMethods?.init("1", {
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
        window.DESKPRO_MESSENGER_CONNECTION.childMethods.open("1", {
          parentViewHeight: 'fullscreen',
          showLauncherButton: false,
        });
    """
    }

    private fun optionsScript(): String {
        return """
        window.DESKPRO_MESSENGER_OPTIONS = {
            showLauncherButton: false,
            openOnInit: true,
            userInfo: { name: "john" },
            signedUserInfo: undefined,
            launcherButtonConfig: undefined,
            messengerAppConfig: undefined,
            urlCacheableConfig: undefined,
          };
        """
    }

    private fun connectionScript(): String {
        return """ 
        window.DESKPRO_MESSENGER_CONNECTION = {
           parentMethods: {
            ready: async (messengerId) => {
             const data = await window.DESKPRO_MESSENGER_CONNECTION.childMethods?.init(messengerId, {
              showLauncherButton: DESKPRO_MESSENGER_OPTIONS.showLauncherButton,
              user: window.DESKPRO_MESSENGER_OPTIONS?.userInfo,
              launcherButtonConfig: DESKPRO_MESSENGER_OPTIONS.launcherButtonConfig,
              messengerAppConfig: DESKPRO_MESSENGER_OPTIONS.messengerAppConfig,
              parentViewHeight: "fullscreen",
              open: DESKPRO_MESSENGER_OPTIONS.openOnInit,
             });
        
             if (data) {
              const { side, offsetBottom, offsetSide, width, height } = data;
             }
            },
            getViewHeight: async (messengerId) => {
             return "fullscreen";
            },
            getUserInfo: async (messengerId) => {
              return $WEB_INTERFACE_KEY.getUserInfo();
            },
            getUserJwtToken: async (messengerId) => {
              return $WEB_INTERFACE_KEY.getJwtToken();
            },
            open: async (messengerId, data) => {
              const { width, height } = data;
            },
            close: async (messengerId, data) => {
              $WEB_INTERFACE_KEY.close();
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
}
