package com.tmobile.ct.tep.qapi.service;

import com.tmobile.ct.tep.qapi.config.ApplicationProperties;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

;

@Service
public class RepoService {

    @Autowired
    private ApplicationProperties applicationProperties;

    private String repoDir = "";
    private String branch = "";
    private String repoLocation = "";
    static Logger logger = LoggerFactory.getLogger(RepoService.class);
    public RepoService(){}
    public static final long timeInterval = 300000;

    @Scheduled(fixedRate = timeInterval)
    public void scan(){
        if (applicationProperties.getQueryConfig().getLocation().toLowerCase().trim().equals("repository")){
            deleteFiles(repoDir);
            getFiles();
            logger.info("Repo scan finished.");
        }
    }

    public void getFiles(){
        if (repoLocation == null || repoLocation.equals("")){
            repoLocation = applicationProperties.getQueryConfig().getRepository();
        }
        if (this.branch == null || branch.equals("")){
            branch = applicationProperties.getQueryConfig().getBranch();
        }
        File dir = new File("repository");
        if (!dir.exists()){
            if(!dir.mkdirs()){
                logger.error("Could not make directory");
            }
        }
        try {
            this.repoDir = dir.getAbsolutePath();
            Process p = Runtime.getRuntime().exec("git -c http.sslVerify=false clone -b " +branch + " " + repoLocation, null, new File(this.repoDir));
            p.waitFor();
        }catch (Exception e){
            logger.error("Could not clone repo");
        }
    }

    public void deleteFiles(String dir){
        try {
            FileUtils.forceDelete(new File(dir));
        } catch(IOException e){
            logger.debug("ERROR: Unable to delete: [{}]", e.getMessage());
        }

    }

    public String getRepoDir() {
        return repoDir;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getRepoLocation() {
        return repoLocation;
    }

    public void setRepoLocation(String repoLocation) {
        this.repoLocation = repoLocation;
    }

    public void setRepoDir(String repoDir) {
        this.repoDir = repoDir;
    }
}
