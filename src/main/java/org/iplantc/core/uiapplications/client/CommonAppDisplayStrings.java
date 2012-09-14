package org.iplantc.core.uiapplications.client;

import org.iplantc.core.uicommons.client.CommonUIDisplayStrings;

/** Display strings that are shared between applications */
public interface CommonAppDisplayStrings extends CommonUIDisplayStrings {

    /**
     * Rating option: 1 out of 5 stars
     * 
     * @return a string representing the text
     */
    String hateIt();

    /**
     * Rating option: 2 out of 5 stars
     * 
     * @return a string representing the text
     */
    String didNotLike();

    /**
     * Rating option: 3 out of 5 stars
     * 
     * @return a string representing the text
     */
    String likedIt();

    /**
     * Rating option: 4 out of 5 stars
     * 
     * @return a string representing the text
     */
    String reallyLikedIt();

    /**
     * Rating option: 5 out of 5 stars
     * 
     * @return a string representing the text
     */
    String lovedIt();

    /**
     * Localized display text for a Links label.
     * 
     * @return a string representing the localized text.
     */
    String links();

    /**
     * Localized display text for an Integrator label.
     * 
     * @return a string representing the localized text.
     */
    String integrator();

    /**
     * Localized display text for an Avg. Community Rating label.
     * 
     * @return a string representing the localized text.
     */
    String avgCommunityRating();

    /**
     * Localized display text for an Avg. Rating label.
     * 
     * @return a string representing the localized text.
     */
    String avgRating();

    /**
     * Localized display text for a Rating label.
     * 
     * @return a string representing the localized text.
     */
    String rating();

    /**
     * Localized display text for a rating "x out of 5" label.
     * 
     * @return a string representing the localized text.
     */
    String ratingOutOfTotal();

    /**
     * Localized text for app unavailable
     * 
     * @return @return a string representing the localized text.
     */
    String appUnavailable();

    /**
     * Localized display text for an empty App Search field.
     * 
     * @return a string representing the localized text.
     */
    String searchApps();

    /**
     * Localized text for the App Comment dialog title
     * 
     * @return a string representing the localized text.
     */
    String appCommentDialogTitle();

    /**
     * Localized text for the text appearing the App Comment dialog before the text area.
     * 
     * @param appName name of the app
     * @return a string representing the localized text.
     */
    String appCommentExplanation(String appName);

    /**
     * Localized text for the subject line in rating notifications.
     * 
     * @param appName the name of the app
     * @return a string representing the localized text.
     */
    String ratingEmailSubject(String appName);

    /**
     * Localized text for the email body in rating notifications.
     * 
     * @param appName the name of the app
     * @return a string representing the localized text.
     */
    String ratingEmailText(String appName);

    /**
     * Localized text for the "App Info" toolbar button.
     * 
     * @return a string representing the localized text.
     */
    String appInfo();

    /**
     * Localized text for the "Request Tool" toolbar button.
     * 
     * @return a string representing the localized text.
     */
    String requestTool();

    /**
     * @return
     */
    String newApp();

    /**
     * @return
     */
    String workflow();
}
