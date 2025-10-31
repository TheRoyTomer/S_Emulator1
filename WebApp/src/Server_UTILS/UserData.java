package Server_UTILS;

import EngineObject.StatisticDTO;
import Out.UserInfoDTO;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    private final String userName;
    private int uploadedProgramCount;
    private int uploadedFunctionCount;
    private int currentCredits;
    private int usedCredits;
    private int executionCount;

    List<StatisticDTO> userHistory;

    public UserData(String userName)
    {
        this.userName = userName;
        this.uploadedProgramCount = 0;
        this.uploadedFunctionCount = 0;
        this.currentCredits = 0;
        this.usedCredits = 0;
        this.executionCount = 0;
        this.userHistory = new ArrayList<>();
    }

    // Getters
    public String getUserName() { return userName; }
    public int getUploadedProgramCount() { return uploadedProgramCount; }
    public int getUploadedFunctionCount() { return uploadedFunctionCount; }
    public int getCurrentCredits() { return currentCredits; }
    public int getUsedCredits() { return usedCredits; }
    public int getExecutionCount() { return executionCount; }

    public List<StatisticDTO> getUserHistory()
    {
        return userHistory;
    }

    public void setUserHistory(List<StatisticDTO> userHistory)
    {
        this.userHistory = userHistory;
    }

    // Methods to update
    public void incrementProgramCount() { uploadedProgramCount++; }
    public void incrementFunctionCount() { uploadedFunctionCount++; }
    public void incrementExecutionCount() { executionCount++; }

    //returns whether client have the amount of credits to use
    public boolean useCredits(int amount)
    {
        if (this.currentCredits < amount)
        {
            this.usedCredits += this.currentCredits;
            this.currentCredits = 0;
            return false;
        }
        else
        {
            currentCredits -= amount;
            usedCredits += amount;
            return true;
        }

    }

    public void chargeCredits(int amount)
    {
        currentCredits += amount;
    }

    public void takeAllCredits()
    {
        this.usedCredits += this.currentCredits;
        this.currentCredits = 0;
    }

    // Convert to DTO
    public UserInfoDTO toDTO()
    {
        return new UserInfoDTO(userName, uploadedProgramCount, uploadedFunctionCount,
                currentCredits, usedCredits, executionCount,  userHistory);
    }


}