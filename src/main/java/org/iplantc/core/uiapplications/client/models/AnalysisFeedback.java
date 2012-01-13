package org.iplantc.core.uiapplications.client.models;

import org.iplantc.core.jsonutil.JsonUtil;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;

/**
 * A model class to represent user feedback on an analysis
 * 
 * @author sriram
 *
 */
public class AnalysisFeedback implements Comparable<AnalysisFeedback> {
    public static final String RATING_AVG = "average"; //$NON-NLS-1$
    public static final String RATING_USR = "user"; //$NON-NLS-1$

    private int user_score;
    private double average_score;
    
    public AnalysisFeedback(double avg_score, int score) {
        setAverage_score(avg_score);
        setUser_score(score);
    }

    /**
     * Instantiates from a JSON object.
     * 
     * @param rating JSON representation of the App Feeback, containing keys for "average" and "user".
     */
    public AnalysisFeedback(JSONObject rating) {
        double avgScore = 0;
        int userScore = 0;

        if (rating != null) {
            Number score = JsonUtil.getNumber(rating, RATING_AVG);
            if (score != null) {
                avgScore = score.doubleValue();
            }

            score = JsonUtil.getNumber(rating, RATING_USR);
            if (score != null) {
                userScore = score.intValue();
            }

        }

        setAverage_score(avgScore);
        setUser_score(userScore);
    }

    /**
     * @return the user_score
     */
    public int getUser_score() {
        return user_score;
    }

    /**
     * @param user_score the user_score to set
     */
    public void setUser_score(int user_score) {
        this.user_score = user_score;
    }

    /**
     * @param average_score the average_score to set
     */
    public void setAverage_score(double average_score) {
        this.average_score = average_score;
    }

    /**
     * @return the average_score
     */
    public double getAverage_score() {
        return average_score;
    }

    /**
     * Builds a JSONObject representation of this Feeback model.
     * 
     * @return JSONObject representation of the App Feedback.
     */
    public JSONObject toJson() {
        JSONObject ret = new JSONObject();

        ret.put(RATING_AVG, new JSONNumber(getAverage_score()));
        ret.put(RATING_USR, new JSONNumber(getUser_score()));

        return ret;
    }

    @Override
    public int compareTo(AnalysisFeedback o) {
        if (o == null) {
            return 1;
        }

        double lhs = (user_score != 0) ? user_score : average_score;
        double rhs = (o.getUser_score() != 0) ? o.getUser_score() : o.getAverage_score();

        return Double.compare(lhs, rhs);
    }
     
}
