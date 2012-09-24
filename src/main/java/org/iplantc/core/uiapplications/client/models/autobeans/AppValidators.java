package org.iplantc.core.uiapplications.client.models.autobeans;

import org.iplantc.core.client.widgets.validator.BasicEmailValidator3;
import org.iplantc.core.uiapplications.client.Constants;
import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uicommons.client.util.RegExp;

import com.extjs.gxt.ui.client.util.Format;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;

public class AppValidators {
    private static String appNameRegex = Format.substitute(
            "[^{0}{1}][^{1}]*", //$NON-NLS-1$
            Constants.CLIENT.appNameRestrictedStartingChars(),
            RegExp.escapeCharacterClassSet(Constants.CLIENT.appNameRestrictedChars()));

    public static RegExValidator APP_NAME_VALIDATOR = new RegExValidator(appNameRegex,
            I18N.ERROR.invalidAppNameMsg(Constants.CLIENT.appNameRestrictedStartingChars(),
                    Constants.CLIENT.appNameRestrictedChars()));

    public static RegExValidator APP_WIKI_URL_VALIDATOR = new BasicEmailValidator3();

}
