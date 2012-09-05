package org.iplantc.core.uiapplications.client.presenter;

import com.google.gwt.user.client.ui.HasOneWidget;

public abstract interface Presenter {
    public abstract void go(final HasOneWidget container);
}
