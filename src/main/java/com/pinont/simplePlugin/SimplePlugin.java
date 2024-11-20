package com.pinont.simplePlugin;

import com.pinont.experiences.plugin.ExpPlugin;

public final class SimplePlugin extends ExpPlugin {

    @Override
    public void onPluginStart() {

    }

    @Override
    public void onPluginStop() {

    }

    public static SimplePlugin getInstance() {
        return (SimplePlugin) ExpPlugin.getPlugin();
    }
}
