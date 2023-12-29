package com.deskpro.messenger.util.extensions

import com.deskpro.messenger.util.Constants.APP_ID

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
