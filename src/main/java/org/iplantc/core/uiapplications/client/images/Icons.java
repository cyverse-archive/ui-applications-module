package org.iplantc.core.uiapplications.client.images;

import com.google.gwt.resources.client.ImageResource;

public interface Icons extends org.iplantc.core.uicommons.client.images.Icons {

    @Override
    @Source("new.gif")
    ImageResource add();

    @Source("delete.gif")
    ImageResource delete();

    @Source("new.gif")
    ImageResource requestTool();

    @Source("file_copy.gif")
    ImageResource copy();

    @Source("edit.gif")
    ImageResource edit();

    @Source("go_public.png")
    ImageResource submitForPublic();

    @Source("information.png")
    ImageResource info();
}
