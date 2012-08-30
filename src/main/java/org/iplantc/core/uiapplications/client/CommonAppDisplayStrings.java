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
}
