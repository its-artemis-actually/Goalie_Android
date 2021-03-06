package com.github.q115.goalie_android.https;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.github.q115.goalie_android.Constants.ASYNC_CONNECTION_NORMAL_TIMEOUT;
import static com.github.q115.goalie_android.Constants.URL;

/*
 * Copyright 2017 Qi Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class RESTRemind extends RESTBase<String> {
    private RESTRemind.Listener mListener;
    private String mToUsername;
    private String mGuid;
    private boolean isRemindingRef;

    public RESTRemind(String fromUsername, String toUsername, String guid, boolean isRemindingRef) {
        this.mUsername = fromUsername;
        this.mToUsername = toUsername;
        this.mGuid = guid;
        this.isRemindingRef = isRemindingRef;
    }

    public interface Listener extends RESTBaseListener {
        void onSuccess();

        void onFailure(String errMsg);
    }

    public void setListener(RESTRemind.Listener mList) {
        super.setListener(mList);
        this.mListener = mList;
    }

    public void execute() {
        final String url = URL + "/remind";
        StringRequest req = new StringRequest(Request.Method.POST, url, this, this) {
            @Override
            public HashMap<String, String> getHeaders() {
                return getDefaultHeaders();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fromUsername", mUsername);
                params.put("toUsername", mToUsername);
                params.put("guid", mGuid);
                params.put("isRemindingRef", isRemindingRef ? "1" : "0");
                return new JSONObject(params).toString().getBytes();
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(
                ASYNC_CONNECTION_NORMAL_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                0));
        VolleyRequestQueue.getInstance().addToRequestQueue(req);
    }

    @Override
    public void onResponse(String response) {
        if (mListener != null)
            mListener.onSuccess();
    }
}