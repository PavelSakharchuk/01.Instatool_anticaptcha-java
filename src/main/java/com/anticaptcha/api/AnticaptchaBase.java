package com.anticaptcha.api;

import com.anticaptcha.apiresponse.BalanceResponse;
import com.anticaptcha.helper.DebugHelper;
import org.json.JSONException;
import org.json.JSONObject;

public class AnticaptchaBase extends AnticaptchaAbstract {

    @SuppressWarnings("WeakerAccess")
    public Double getBalance() {
        JSONObject jsonPostData = new JSONObject();

        try {
            jsonPostData.put("clientKey", clientKey);
        } catch (JSONException e) {
            errorMessage = e.getMessage();
            DebugHelper.out("JSON compilation error: " + e.getMessage(), DebugHelper.Type.ERROR);

            return null;
        }

        JSONObject postResult = jsonPostRequest(ApiMethod.GET_BALANCE, jsonPostData);

        if (postResult == null) {
            DebugHelper.out("API error", DebugHelper.Type.ERROR);

            return null;
        }

        BalanceResponse balanceResponse = new BalanceResponse(postResult);

        if (balanceResponse.getErrorId() == null || !balanceResponse.getErrorId().equals(0)) {
            errorMessage = balanceResponse.getErrorDescription();
            String errorId = balanceResponse.getErrorId() == null ? "" : balanceResponse.getErrorId().toString();

            DebugHelper.out(
                    "API error " + errorId + ": " + balanceResponse.getErrorDescription(),
                    DebugHelper.Type.ERROR
            );

            return null;
        }

        return balanceResponse.getBalance();
    }

    @Override
    public JSONObject getPostData() {
        // TODO if needed
        return null;
    }
}
