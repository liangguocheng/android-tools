package com.lange.tools.model;

/**
 * Created by liangguocheng on 2018/12/24.
 */

public class PgyUpgradeModel {
    private int code;

    private String message;

    private Data data;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return this.data;
    }

    public class Data {
        private String buildBuildVersion;

        private String downloadURL;

        private boolean buildHaveNewVersion;

        private String buildVersionNo;

        private String buildVersion;

        private String buildShortcutUrl;

        private String buildUpdateDescription;

        public void setBuildBuildVersion(String buildBuildVersion) {
            this.buildBuildVersion = buildBuildVersion;
        }

        public String getBuildBuildVersion() {
            return this.buildBuildVersion;
        }

        public void setDownloadURL(String downloadURL) {
            this.downloadURL = downloadURL;
        }

        public String getDownloadURL() {
            return this.downloadURL;
        }

        public void setBuildHaveNewVersion(boolean buildHaveNewVersion) {
            this.buildHaveNewVersion = buildHaveNewVersion;
        }

        public boolean getBuildHaveNewVersion() {
            return this.buildHaveNewVersion;
        }

        public void setBuildVersionNo(String buildVersionNo) {
            this.buildVersionNo = buildVersionNo;
        }

        public String getBuildVersionNo() {
            return this.buildVersionNo;
        }

        public void setBuildVersion(String buildVersion) {
            this.buildVersion = buildVersion;
        }

        public String getBuildVersion() {
            return this.buildVersion;
        }

        public void setBuildShortcutUrl(String buildShortcutUrl) {
            this.buildShortcutUrl = buildShortcutUrl;
        }

        public String getBuildShortcutUrl() {
            return this.buildShortcutUrl;
        }

        public void setBuildUpdateDescription(String buildUpdateDescription) {
            this.buildUpdateDescription = buildUpdateDescription;
        }

        public String getBuildUpdateDescription() {
            return this.buildUpdateDescription;
        }

    }
}
