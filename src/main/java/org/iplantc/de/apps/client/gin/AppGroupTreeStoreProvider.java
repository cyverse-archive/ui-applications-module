package org.iplantc.de.apps.client.gin;

import com.google.inject.Provider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;

import org.iplantc.de.apps.client.models.autobeans.AppGroup;

public class AppGroupTreeStoreProvider implements Provider<TreeStore<AppGroup>> {

    @Override
    public TreeStore<AppGroup> get() {
       return new TreeStore<AppGroup>(new ModelKeyProvider<AppGroup>() {

        @Override
        public String getKey(AppGroup item) {
                return item.getId();
        }
    });
    }

}
