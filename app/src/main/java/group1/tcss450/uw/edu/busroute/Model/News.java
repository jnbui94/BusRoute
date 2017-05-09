package group1.tcss450.uw.edu.busroute.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jnbui on 5/8/2017.
 */

public class News {
    /**
     * ID from JSON
     */
    private String mID;
    /**
     * Language from JSON
     */
    private String mLanguage;
    /**
     * Kind from JSON
     */
    private String mKind;
    /**
     * Specifications from JSON
     */
    private String mSpecs;
    /**
     * Implementation from JSON
     */
    private String mImpl;
    /**
     * takes in a jsonobject.
     * @param json
     * @throws JSONException
     */
    public News(JSONObject json)  throws JSONException {
        create(json);
    }

    /**
     * parse the jsonobject.
     * include name, url, description, image url.
     * @param json
     * @throws JSONException
     */
    private void create(JSONObject json) throws JSONException{
        mID = json.getString("id");
        mLanguage = json.getString("languages");
        mKind = json.getString("kind");
        mSpecs = json.getString("specification");
        mImpl = json.getString("implementation");
    }

    /**
     * return id
     */
    public String getId(){return mID;}
    /**
     * return language
     */
    public String getLanguage(){return mLanguage;}
    /**
     * return kind
     */
    public String getmKind(){return mKind;}
    /**
     * return Specification
     */
    public String getSpecs() {return mSpecs;}
    /**
     * return implemetation
     */
    public String getImp(){return mImpl;}
}
