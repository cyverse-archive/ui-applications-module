package org.iplantc.core.uiapps.client.models.autobeans;

import org.iplantc.core.uiapps.client.Constants;
import org.iplantc.core.uiapps.client.I18N;
import org.iplantc.core.uicommons.client.util.RegExp;
import org.iplantc.core.uicommons.client.validators.BasicEmailValidator3;

import com.extjs.gxt.ui.client.util.Format;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;

/**
 * @author jstroot
 * @deprecated Class needs to be deleted or ported to GXT3
 */
@Deprecated
public class AppValidators {
    private static String appNameRegex = Format.substitute("[^{0}{1}][^{1}]*", //$NON-NLS-1$
            Constants.CLIENT.appNameRestrictedStartingChars(), RegExp.escapeCharacterClassSet(Constants.CLIENT.appNameRestrictedChars()));

    public static RegExValidator APP_NAME_VALIDATOR = new RegExValidator(appNameRegex, I18N.ERROR.invalidAppNameMsg(Constants.CLIENT.appNameRestrictedStartingChars(),
            Constants.CLIENT.appNameRestrictedChars()));

    public static RegExValidator APP_WIKI_URL_VALIDATOR = new BasicEmailValidator3();

}
