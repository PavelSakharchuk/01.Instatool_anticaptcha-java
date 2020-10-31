package com.anticaptcha.api;

import com.anticaptcha.apiresponse.TaskResultResponse;
import org.json.JSONObject;

public interface IAnticaptchaTaskProtocol {
    JSONObject getPostData();

    // TODO: 20.10.2020: p.sakharchuk: Need to add catching Exception
    TaskResultResponse.SolutionData getTaskSolution() throws InterruptedException;
}
