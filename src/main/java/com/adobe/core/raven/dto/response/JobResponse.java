package com.adobe.core.raven.dto.response;

import com.adobe.core.raven.dto.ResponseError;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public @Data
class JobResponse {

    ResponseError error;
    private ArrayList<ResponseError> errors;
    private ArrayList<ResponseError> warnings;

    private List<String> msgIds;
    private Boolean jobCreated;
    private Boolean jobUpdated;
    private Boolean validData;
    private String jobId;
    public ResponseError getError() {
        return error;
    }

    public void setError(ResponseError error) {
        this.error = error;
    }

    public ArrayList<ResponseError> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<ResponseError> errors) {
        this.errors = errors;
    }

    public ArrayList<ResponseError> getWarnings() {
        return warnings;
    }

    public void setWarnings(ArrayList<ResponseError> warnings) {
        this.warnings = warnings;
    }

    public Boolean getJobCreated() {
        return jobCreated;
    }

    public void setJobCreated(Boolean jobCreated) {
        this.jobCreated = jobCreated;
    }

    public Boolean getJobUpdated() {
        return jobUpdated;
    }

    public void setJobUpdated(Boolean jobUpdated) {
        this.jobUpdated = jobUpdated;
    }

    public Boolean getValidData() {
        return validData;
    }

    public void setValidData(Boolean validData) {
        this.validData = validData;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
    //private List<MasterJob> result;

    // for job details
    //private MasterJob jobResult;
}
