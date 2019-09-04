package com.sport.want.Post_Get.LinkForm;

import org.json.JSONObject;

public class GetLinkForm {

    private LinkFormListener linkFormListener;

    public void setListener(LinkFormListener mLinkFormListener){
        linkFormListener = mLinkFormListener;
    }

    void toLink(JSONObject responseJson) {
        if (linkFormListener != null && responseJson != null) {
            linkFormListener.tolink(responseJson);
        }
    }
}
