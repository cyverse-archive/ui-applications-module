package org.iplantc.core.uiapps.client.util;

import org.iplantc.core.uiapps.client.Constants;
import org.iplantc.core.uiapps.client.I18N;
import org.iplantc.core.uicommons.client.util.RegExp;

import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.widget.form.TextField;

/**
 * Utility methods related to Apps.
 * 
 * @author psarando
 * 
 */
public class AnalysisUtil {

    /**
     * Sets a regex pattern and RegexText validation message for valid App Names in the given field.
     * 
     * @param field A TextField for App Name input.
     */
    public static void setAppNameRegexValidation(TextField<String> field) {
        if (field == null) {
            return;
        }

        // Build the valid App Name regex pattern.
        String appNameRegex = Format.substitute(
                "[^{0}{1}][^{1}]*", //$NON-NLS-1$
                Constants.CLIENT.appNameRestrictedStartingChars(),
                RegExp.escapeCharacterClassSet(Constants.CLIENT.appNameRestrictedChars()));

        // Set this regex and a validation message in the given field.
        field.setRegex(appNameRegex);
        field.getMessages().setRegexText(
                I18N.ERROR.invalidAppNameMsg(Constants.CLIENT.appNameRestrictedStartingChars(),
                        Constants.CLIENT.appNameRestrictedChars()));
    }
}
